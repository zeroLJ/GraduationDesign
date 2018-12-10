package datasourse.entityField;

import datasourse.type.FieldType;

/**
 * 实体字段基类
 * @param <E> 字段数据类型
 */
public abstract class FieldValue<E> {
	protected String fieldName;
	protected E valueOld,valueNew;
	public abstract boolean isChange();
	public abstract FieldType getFieldType();
	public FieldValue(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public E getValue() {
		return valueNew;
	}
	public String getFieldName() {
		return fieldName;
	}

	private boolean flag = false;//是否为初始值
	
	public void setValue(E value) {
		this.valueNew = value;
		flag = true;
	}
	
	public void initValue(E value) {
		if (!flag) {
			this.valueOld = value;
		}
		this.valueNew = value;
		flag = true;
	}
}
