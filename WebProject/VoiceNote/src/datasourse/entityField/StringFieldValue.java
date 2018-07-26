package datasourse.entityField;

import datasourse.type.FieldType;

public class StringFieldValue extends FieldValue<String>{
	public StringFieldValue(String fieldName) {
		super(fieldName);
	}
	
	@Override
	public FieldType getFieldType() {
		return FieldType.STRING;
	}

	@Override
	public boolean isChange() {
		if ((valueOld == null && valueNew!=null) || (valueOld != null && valueNew==null)) {
			return true;
		}
		if (valueOld == null && valueNew == null) {
			return false;
		}
		return !valueOld.equals(valueNew);
	}
}
