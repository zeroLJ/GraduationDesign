package datasourse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.regexp.internal.recompile;

import datasourse.entityField.BooleanFieldValue;
import datasourse.entityField.DateTimeFieldValue;
import datasourse.entityField.FieldValue;
import datasourse.entityField.NumberFieldValue;
import datasourse.entityField.StringFieldValue;
import datasourse.type.FieldType;
import main.util.ObjUtils;

/**
 * 表 实体类基础类
 */
public abstract class BaseEntity extends DBOperation {
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
	 * 获取主键名列表
	 * @return
	 */
	protected abstract List<String> getKeys();

	protected abstract Map<String, Integer> getFieldPositionMap();
	//各字段值列表
	private List<FieldValue> valueList = new ArrayList<>();
	/**
	 * 获取各字段值列表
	 * @return
	 */
	public List<FieldValue> getFieldValues() {
		return valueList;
	}

	public BaseEntity() {
		super("");
		List<String> nameList = getFieldNames();
		for (int i = 0; i < nameList.size(); i++) {
			FieldType type = getFieldTypes().get(i);
			String fieldName = getTableName() + "." + nameList.get(i);
			if (type == FieldType.STRING) {
				valueList.add(new StringFieldValue(fieldName));
			} else if (type == FieldType.NUMBER) {
				valueList.add(new NumberFieldValue(fieldName));
			} else if (type == FieldType.DATETIME) {
				valueList.add(new DateTimeFieldValue(fieldName));
			} else if (type == FieldType.BOOLEAN) {
				valueList.add(new BooleanFieldValue(fieldName));
			}
		}
	}

	protected NumberFieldValue getNumberField(int position) {
		return (NumberFieldValue) valueList.get(position);
	}

	protected StringFieldValue getStringField(int position) {
		return (StringFieldValue) valueList.get(position);
	}

	protected DateTimeFieldValue getDateTimeField(int position) {
		return (DateTimeFieldValue) valueList.get(position);
	}

	protected BooleanFieldValue getBooleanField(int position) {
		return (BooleanFieldValue) valueList.get(position);
	}

	public int getFieldPosition(String fieldName) {
		if (getFieldPositionMap().containsKey(fieldName)) {
			return getFieldPositionMap().get(fieldName);
		} else {
			return -1;
		}
	}

	public String toString() {
		return toMap().toString();
	}

	/**
	 * 把表数据实例转成map类型，便于进行网络数据传输
	 * @return
	 */	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap();
		List<String> nameList = getFieldNames();
		for(int i = 0; i < nameList.size(); i++) {
			map.put(nameList.get(i), valueList.get(i).getValue());
		}
		return map;
	}

	public String toJson() {
		return JSON.toJSONString(toMap());
	}

	//主键数据类型列表
	private List<FieldType> keyTypes = new ArrayList<>();
	/**
	 * 获取主键数据类型列表,若没有主键，则为所有字段
	 * @return
	 */
	public List<FieldType> getKeyFieldTypes() {
		return keyTypes;
	}
	
	//主键数据值列表
	private List<Object> keyValues = new ArrayList<>();
	/**
	 * 获取主键初始数据值列表,若没有主键，则为所有字段
	 * 必须是通过查询查询生成的entity才能获取到
	 * @return
	 */
	public List<Object> getKeyFieldValues() {
		return keyValues;
	}

	/**
	 * 主键初始值，一般为查询时自动调用，不需要手动调用此方法。
	 * 若没有主键，则设置所有字段的初始值
	 * 注意，一旦调用此方法，则判定为update类型。
	 * ps:手动调用的话请注意该条数据在数据库表中存在且唯一！
	 * @param map
	 * @return 是否有主键
	 */
	public void initKeyField(Map<String, Object> map) {
		keyTypes.clear();
		keyValues.clear();
		
		for (String name : getKeys()) {
			keyTypes.add(getFieldTypes().get(getFieldPosition(name)));
//			keyValues.add(map.get(name));
			Object object = getObject(map.get(name), getFieldTypes().get(getFieldPosition(name)));
			keyValues.add(object);
		}
		if (keyTypes.isEmpty()) {
			for (String name : getFieldNames()) {
				keyTypes.add(getFieldTypes().get(getFieldPosition(name)));
//				keyValues.add(map.get(name));
				Object object = getObject(map.get(name), getFieldTypes().get(getFieldPosition(name)));
				keyValues.add(object);
			}
		}
	}
	
	/**
	 * 将所有值替换成map中的值，若没有对应key值，则为null
	 * @param map
	 * @return
	 */
	public void initData(Map<String, Object> map) {
		for(int i = 0; i < valueList.size(); i++) {
			FieldType type = getFieldTypes().get(i);
			Object object = getObject(map.get(getFieldNames().get(i)),type);
			valueList.get(i).initValue(object);
		}
	}

	@Override
	public String getSQL() {
		if (!isChange()) {
			return "";
		}
		StringBuilder sql = new StringBuilder();
		if(isInsert()) {
			sql.append("insert into ").append(getTableName()).append("(");
			StringBuilder value = new StringBuilder();
			for(String name : getFieldNames()) {
				sql.append(name).append(",");
				value.append("?,");
			}
			sql.deleteCharAt(sql.lastIndexOf(","));
			value.deleteCharAt(value.lastIndexOf(","));
			sql.append(") values(").append(value.toString()).append(")");
		}else if (isUpdate()) {
			sql.append("update ").append(getTableName()).append(" set ");
			for(FieldValue value : valueList) {
				if (value.isChange()) {
					sql.append(value.getFieldName()).append("=? ");
				}
			}
			sql.append("where ");
			for(int i = 0; i< getKeys().size(); i++) {
				if (keyValues.get(i) == null) {
					sql.append(getTableName() + "." + getKeys().get(i)).append(" is null and ");
				}else {
					sql.append(getTableName() + "." + getKeys().get(i)).append("=? and ");
				}
			}
//			for(String name : getKeys()) {
//				sql.append(getTableName() + "." + name).append("=? and ");
//			}
			sql.delete(sql.lastIndexOf("and"), sql.length());
		}
		return sql.toString();
	}
	
	/**
	 * 数据是否有变化
	 * @return
	 */
	public boolean isChange() {
		for(FieldValue value : valueList) {
			if (value.isChange()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否为新增的数据
	 * @return
	 */
	public boolean isInsert() {
		if (keyTypes.isEmpty()) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 是否为更新的数据
	 * @return
	 */
	public boolean isUpdate() {
		return !isInsert();
	}
	
	@Override
	public DBOperation toDBOperation() {
		this.sql = getSQL();
		List<Object> params = new ArrayList<>();
		List<FieldType> paramTypes = new ArrayList<>();
		if (!sql.equals("")) {
			if (isInsert()) {
				for(FieldValue value : valueList) {
					params.add(value.getValue());
					paramTypes.add(value.getFieldType());
				}
			}else {
				for(FieldValue value : valueList) {
					if (value.isChange()) {
						params.add(value.getValue());
						paramTypes.add(value.getFieldType());
					}
				}
				for(int i = 0 ; i < keyValues.size(); i++) {
					if (keyValues.get(i) != null) {
						params.add(keyValues.get(i));
						paramTypes.add(keyTypes.get(i));
					}
				}
//				for(Object object : entity.getKeyValues()) {
//					params.add(object);
//				};
//				for(FieldType type : entity.getKeyTypes()) {
//					paramTypes.add(type);
//				};
			}
		}
		this.params = params;
		this.paramTypes = paramTypes;
		return this;
	}
	
	
	private Object getObject(Object object, FieldType type) {
		if (object == null) {
			return null;
		}
		if (type == FieldType.STRING) {
			return ObjUtils.objToStr(object);
		}else if (type == FieldType.NUMBER){
			return ObjUtils.objToBigDecimal(object);
		}else if (type == FieldType.DATETIME){
			return ObjUtils.objToDate(object);
		}else if (type == FieldType.BOOLEAN){
			return ObjUtils.objToBoolean(object);
		}	
		return ObjUtils.objToStr(object);
	}
}
