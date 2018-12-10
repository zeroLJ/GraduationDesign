package datasourse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import datasourse.entityField.BooleanFieldValue;
import datasourse.entityField.DateTimeFieldValue;
import datasourse.entityField.FieldValue;
import datasourse.entityField.NumberFieldValue;
import datasourse.entityField.StringFieldValue;
import datasourse.type.FieldType;
import main.util.ObjUtils;

/**
 * 锟斤拷 实锟斤拷锟斤拷锟斤拷锟斤拷锟�
 */
public abstract class BaseEntity extends DBOperation {
	/**
	 * 锟斤拷取锟斤拷锟街讹拷锟斤拷
	 * @return
	 */
	public abstract List<String> getFieldNames();
	/**
	 * 锟斤拷取锟斤拷锟街讹拷锟斤拷锟斤拷锟斤拷锟斤拷
	 * @return
	 */
	public abstract List<FieldType> getFieldTypes();
	/**
	 * 锟斤拷取锟斤拷锟斤拷
	 * @return
	 */
	public abstract String getTableName();
	/**
	 * 锟斤拷取锟斤拷锟斤拷锟斤拷锟叫憋拷
	 * @return
	 */
	protected abstract List<String> getKeys();

	protected abstract Map<String, Integer> getFieldPositionMap();
	
	//锟斤拷锟街讹拷值锟叫憋拷
	private List<FieldValue> valueList = new ArrayList<>();
	/**
	 * 锟斤拷取锟斤拷锟街讹拷值锟叫憋拷
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
	 * 锟窖憋拷锟斤拷锟斤拷实锟斤拷转锟斤拷map锟斤拷锟酵ｏ拷锟斤拷锟节斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟捷达拷锟斤拷
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

	//锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟叫憋拷
	private List<FieldType> keyTypes = new ArrayList<>();
	/**
	 * 锟斤拷取锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟叫憋拷,锟斤拷没锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷为锟斤拷锟斤拷锟街讹拷
	 * @return
	 */
	public List<FieldType> getKeyFieldTypes() {
		return keyTypes;
	}
	
	//锟斤拷锟斤拷锟斤拷锟斤拷值锟叫憋拷
	private List<Object> keyValues = new ArrayList<>();
	/**
	 * 锟斤拷取锟斤拷锟斤拷锟斤拷始锟斤拷锟斤拷值锟叫憋拷,锟斤拷没锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷为锟斤拷锟斤拷锟街讹拷
	 * 锟斤拷锟斤拷锟斤拷通锟斤拷锟斤拷询锟斤拷询锟斤拷锟缴碉拷entity锟斤拷锟杰伙拷取锟斤拷
	 * @return
	 */
	public List<Object> getKeyFieldValues() {
		return keyValues;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷始值锟斤拷一锟斤拷为锟斤拷询时锟皆讹拷锟斤拷锟矫ｏ拷锟斤拷锟斤拷要锟街讹拷锟斤拷锟矫此凤拷锟斤拷锟斤拷
	 * 锟斤拷没锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟街段的筹拷始值
	 * 注锟解，一锟斤拷锟斤拷锟矫此凤拷锟斤拷锟斤拷锟斤拷锟叫讹拷为update锟斤拷锟酵★拷
	 * ps:锟街讹拷锟斤拷锟矫的伙拷锟斤拷注锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷菘锟斤拷锟叫达拷锟斤拷锟斤拷唯一锟斤拷
	 * @param map
	 * @return 锟角凤拷锟斤拷锟斤拷锟斤拷
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
	 * 锟斤拷锟斤拷锟斤拷值锟芥换锟斤拷map锟叫碉拷值锟斤拷锟斤拷没锟叫讹拷应key值锟斤拷锟斤拷为null
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
		if (isDelete()) {
			sql.append("delete from ").append(getTableName()).append(" where ");;
			for(int i = 0; i< getKeys().size(); i++) {
				if (keyValues.get(i) == null) {
					sql.append(getTableName() + "." + getKeys().get(i)).append(" is null and ");
				}else {
					sql.append(getTableName() + "." + getKeys().get(i)).append("=? and ");
				}
			}
			sql.delete(sql.lastIndexOf("and"), sql.length());
		}else if(isInsert()) {
			sql.append("insert into ").append(getTableName()).append("(");
			StringBuilder value = new StringBuilder();
			for(int i = 0; i < getFieldNames().size(); i++) {
				String name = getFieldNames().get(i);
				if (getFieldValues().get(i).getValue()!=null) {
					sql.append(name).append(",");
					value.append("?,");
				}
			}
			sql.deleteCharAt(sql.lastIndexOf(","));
			value.deleteCharAt(value.lastIndexOf(","));
			sql.append(") values(").append(value.toString()).append(")");
		}else if (isUpdate()) {
			sql.append("update ").append(getTableName()).append(" set ");
			for(FieldValue value : valueList) {
				if (value.isChange()) {
					sql.append(value.getFieldName()).append("=?, ");
				}
			}
			sql.delete(sql.lastIndexOf(","), sql.length());
			sql.append(" where ");
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
	 * 锟斤拷锟斤拷锟角凤拷锟叫变化
	 * @return
	 */
	public boolean isChange() {
		for(FieldValue value : valueList) {
			if (value.isChange()) {
				return true;
			}
		}
		if (delete) {
			return true;
		}
		return false;
	}
	
	/**
	 * 锟角凤拷为锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	 * @return
	 */
	public boolean isInsert() {
		if (delete) {
			return false;
		}
		if (keyTypes.isEmpty()) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 锟角凤拷为锟斤拷锟铰碉拷锟斤拷锟斤拷
	 * @return
	 */
	public boolean isUpdate() {
		if (delete) {
			return false;
		}
		if (keyTypes.isEmpty()) {
			return false;
		}else {
			return true;
		}
	}
	
	private boolean delete = false;
	/**
	 * 锟角凤拷为删锟斤拷
	 * @return
	 */
	public boolean isDelete() {
		return delete;
	}
	
	public void delete() {
		this.delete = true;
	}
	
	@Override
	public DBOperation toDBOperation() {
		this.sql = getSQL();
		List<Object> params = new ArrayList<>();
		List<FieldType> paramTypes = new ArrayList<>();
		if (!sql.equals("")) {
			if (isDelete()) {
				for(int i = 0 ; i < keyValues.size(); i++) {
					if (keyValues.get(i) != null) {
						params.add(keyValues.get(i));
						paramTypes.add(keyTypes.get(i));
					}
				}
			}else if (isInsert()) {
				for(FieldValue value : valueList) {
					if (value.getValue()!=null) {
						params.add(value.getValue());
						paramTypes.add(value.getFieldType());
					}
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
