package datasourse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import datasourse.queryField.BooleanFieldQuery;
import datasourse.queryField.DateTimeFieldQuery;
import datasourse.queryField.FieldQuery;
import datasourse.queryField.NumberFieldQuery;
import datasourse.queryField.StringFieldQuery;
import datasourse.type.FieldType;
import datasourse.type.LinkType;

/**
 * 表查询基础类
 */
public abstract class BaseQuery extends DBOperation{
	public BaseQuery() {
		super("");
	}
	/**
	 * 获取各字段名
	 * @return
	 */
	public abstract List<String> getFieldNames();
	/**
	 * 获取各字段数据类型
	 * @return
	 */
	public abstract List<FieldType> getFieldTypes();
	/**
	 * 获取表名
	 * @return
	 */
	public abstract String getTableName();
	/**
	 * 获取相应的实体类
	 * @return
	 */
	public abstract Class<? extends BaseEntity> getEntityClass();
	
	protected abstract Map<String, Integer> getFieldPositionMap();
	
	private LinkType linkType = LinkType.AND;
	/**
	 * 接下来设置的where条件以and连接
	 */
	public void linkAnd() {
		this.linkType = LinkType.AND;
	}
	/**
	 * 接下来设置的where条件以or连接
	 */
	public void linkOr() {
		this.linkType = LinkType.OR;
	}
	
	private List<FieldQuery> whereList = new ArrayList<>();
	
	
	public List<FieldQuery> getWhereList() {
		return whereList;
	}
	
	protected NumberFieldQuery getNumberField(int position) {
		NumberFieldQuery field = new NumberFieldQuery(getTableName()+"."+getFieldNames().get(position));
		field.setLinkType(linkType);
		whereList.add(field);
		return field;
	}
	
	protected StringFieldQuery getStringField(int position) {
		StringFieldQuery field = new StringFieldQuery(getTableName()+"."+getFieldNames().get(position));
		field.setLinkType(linkType);
		whereList.add(field);
		return field;
	}
	
	protected DateTimeFieldQuery getDateTimeField(int position) {
		DateTimeFieldQuery field = new DateTimeFieldQuery(getTableName()+"."+getFieldNames().get(position));
		field.setLinkType(linkType);
		whereList.add(field);
		return field;
	}
	
	protected BooleanFieldQuery getBooleanField(int position) {
		BooleanFieldQuery field = new BooleanFieldQuery(getTableName()+"."+getFieldNames().get(position));
		field.setLinkType(linkType);
		whereList.add(field);
		return field;
	}
	
	public void bracketLeft() {
		FieldQuery field = new FieldQuery("");
		field.setSql("(");
		field.setLinkType(linkType);
		whereList.add(field);
	}
	
	public void bracketRight() {
		FieldQuery field = new FieldQuery("");
		field.setSql(") ");
		field.setLinkType(LinkType.NONE);
		whereList.add(field);
	}
	
	public String getWhereSQL() {
		StringBuilder builder = new StringBuilder(); //where 条件字符串
		boolean flag = true;  //下一个查询条件是否不需要添加连接符  and/or
		for (FieldQuery field : whereList) {
			if (!field.getSQL().equals("")) {
				if (flag) {
					flag = false;
				}else {
					if (field.getLinkType() == LinkType.AND) {
						builder.append("and ");
					}else if (field.getLinkType() == LinkType.OR){
						builder.append("or ");
					}
				}
				if (field.getSQL().equals("(")) {
					flag = true;
				}
				builder.append(field.getSQL());	
			}
		}
		return builder.toString();
	}
	
	
	public String getSQL() {
		Set<String> selectSet = new HashSet<>();//需要select出来的字段，若为空，则select所有字段
		for (FieldQuery field : whereList) {
			if (field.isSelected()) {
				selectSet.add(field.getFieldName());
			}
		}
		StringBuilder sql = new StringBuilder("select ");
		if (selectSet.isEmpty()) {
			sql.append("* ");
		}else {
			for(String name : selectSet) {
				sql.append(name).append(", ");
			}
			sql.deleteCharAt(sql.lastIndexOf(","));
		}
		sql.append("from dbo.").append(getTableName());
		String whereSQL = getWhereSQL();
		if (!whereSQL.toString().equals("")) {
			sql.append(" where ").append(whereSQL);
		}
		return sql.toString();
	}
	
	 /** 
     * 功能：将输入字符串的首字母改成大写 
     * @param str 
     * @return 
     */  
    private String initcap(String str) {  
          
        char[] ch = str.toCharArray();  
        if(ch[0] >= 'a' && ch[0] <= 'z'){  
            ch[0] = (char)(ch[0] - 32);  
        }  
          
        return new String(ch);  
    }  
    
    @Override
    public DBOperation toDBOperation() {
    	this.sql = getSQL();
		List<Object> params = new ArrayList<>();
		List<FieldType> paramTypes = new ArrayList<>();
		List<FieldQuery> whereList = getWhereList();
		for (FieldQuery field : whereList) {
			for (Object value : field.getValue()) {
				params.add(value);
				paramTypes.add(field.getFieldType());
			}
		}
		this.params = params;
		this.paramTypes = paramTypes;
    	return this;
    }
}
