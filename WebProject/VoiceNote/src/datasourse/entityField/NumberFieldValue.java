package datasourse.entityField;

import java.math.BigDecimal;

import datasourse.type.FieldType;

public class NumberFieldValue extends FieldValue<BigDecimal>{
	public NumberFieldValue(String fieldName) {
		super(fieldName);
	}
	
	@Override
	public FieldType getFieldType() {
		return FieldType.NUMBER;
	}

	@Override
	public boolean isChange() {
		if ((valueOld == null && valueNew!=null) || (valueOld != null && valueNew==null)) {
			return true;
		}
		if (valueOld == null && valueNew == null) {
			return false;
		}
		if (valueOld.compareTo(valueNew) == 0) {
			return false;
		}else {
			return true;
		}
	}

	
}
