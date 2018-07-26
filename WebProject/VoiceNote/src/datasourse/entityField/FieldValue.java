package datasourse.entityField;

import datasourse.type.FieldType;

/**
 * ʵ���ֶλ���
 * @param <E> �ֶ���������
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

	private boolean flag = false;//�Ƿ�Ϊ��ʼֵ
	
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
