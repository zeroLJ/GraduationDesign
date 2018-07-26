package datasourse.entityField;

import java.util.Date;

import datasourse.type.FieldType;

public class DateTimeFieldValue extends FieldValue<Date>{
	public DateTimeFieldValue(String fieldName) {
		super(fieldName);
	}
	
	@Override
	public FieldType getFieldType() {
		return FieldType.DATETIME;
	}

	@Override
	public boolean isChange() {
		if ((valueOld == null && valueNew!=null) || (valueOld != null && valueNew==null)) {
			return true;
		}
		if (valueOld == null && valueNew == null) {
			return false;
		}
		return (valueOld.getTime() != valueNew.getTime());
	}
}
