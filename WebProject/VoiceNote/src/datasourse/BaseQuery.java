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
 * ���ѯ������
 */
public abstract class BaseQuery extends DBOperation{
	public BaseQuery() {
		super("");
	}
	/**
	 * ��ȡ���ֶ���
	 * @return
	 */
	public abstract List<String> getFieldNames();
	/**
	 * ��ȡ���ֶ���������
	 * @return
	 */
	public abstract List<FieldType> getFieldTypes();
	/**
	 * ��ȡ����
	 * @return
	 */
	public abstract String getTableName();
	/**
	 * ��ȡ��Ӧ��ʵ����
	 * @return
	 */
	public abstract Class<? extends BaseEntity> getEntityClass();
	
	protected abstract Map<String, Integer> getFieldPositionMap();
	
	private LinkType linkType = LinkType.AND;
	/**
	 * ���������õ�where������and����
	 */
	public void linkAnd() {
		this.linkType = LinkType.AND;
	}
	/**
	 * ���������õ�where������or����
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
	
	
	public String getSQL() {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		Set<String> selectSet = new HashSet<>();//��Ҫselect�������ֶΣ���Ϊ�գ���select�����ֶ�
		for (FieldQuery field : whereList) {
			if (field.isSelected()) {
				selectSet.add(field.getFieldName());
			}
			if (!field.getSQL().equals("")) {
				if (first) {
					builder.append(field.getSQL());
					first = false;
				}else {
					if (field.getLinkType() == LinkType.AND) {
						builder.append("and ");
					}else {
						builder.append("or ");
					}
					builder.append(field.getSQL());	
				}	
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
		if (!builder.toString().equals("")) {
			sql.append(" where ").append(builder.toString());
		}
		return sql.toString();
	}
	
	 /** 
     * ���ܣ��������ַ���������ĸ�ĳɴ�д 
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
