package datasourse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import datasourse.type.FieldType;

/**
 * 数据库操作基础类
 * 使用的时候注意params 与 paramTypes的大小要一致。
 */
public class DBOperation{
	protected String sql;   //要执行的sql语句
	protected List<Object> params;   //参数列表，用于替换sql中的“?”号
	protected List<FieldType> paramTypes;  	//参数类型列表
	
	public DBOperation(String sql){
		this.params = new ArrayList<>();
		this.paramTypes = new ArrayList<>();
		this.sql = sql;
	}
	public DBOperation(String sql, List<Object> params){
		this.params = params;
		this.paramTypes = listType(params);
		this.sql = sql;
	}
	public DBOperation(String sql, List<Object> params, List<FieldType> paramTypes){
		this.params = params;
		this.paramTypes = paramTypes;
		this.sql = sql;
	}
	
	public List<Object> getParams() {
		return params;
	}
	
	public List<FieldType> getParamTypes() {
		return paramTypes;
	}
	
	
	private List<FieldType> listType(List<Object> param){
		List<FieldType> ls=new ArrayList<FieldType>();
		for(Object item:param){
			if(item instanceof String){
				ls.add(FieldType.STRING);
			}else if(item instanceof Number){
				ls.add(FieldType.NUMBER);
			}else if(item instanceof Boolean){
				ls.add(FieldType.BOOLEAN);
			}else if(item instanceof Date){
				ls.add(FieldType.DATETIME);
			}else{
				ls.add(FieldType.STRING);
			}
		}
		return ls;
	}

	public String getSQL() {
		return sql;
	}
	
	public DBOperation toDBOperation() {
		return this;
	}
}
