package datasourse.entityField;

import datasourse.type.FieldType;

public class BooleanFieldValue extends FieldValue<Boolean>{
	public BooleanFieldValue(String fieldName) {
		super(fieldName);
	}
	
	@Override
	public FieldType getFieldType() {
		return FieldType.BOOLEAN;
	}

	@Override
	public boolean isChange() {
		if ((valueOld == null && valueNew!=null) || (valueOld != null && valueNew==null)) {
			return true;
		}
		if (valueOld == null && valueNew == null) {
			return false;
		}
		return (valueOld != valueNew);
	}
}
