package datasourse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import datasourse.type.FieldType;

/**
 * ���ݿ����������
 * ʹ�õ�ʱ��ע��params �� paramTypes�Ĵ�СҪһ�¡�
 */
public class DBOperation{
	protected String sql;   //Ҫִ�е�sql���
	protected List<Object> params;   //�����б������滻sql�еġ�?����
	protected List<FieldType> paramTypes;  	//���������б�
	
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
