package datasourse.queryField;

import java.math.BigDecimal;

import datasourse.type.FieldType;

public class NumberFieldQuery extends FieldQuery<BigDecimal>{
	public NumberFieldQuery(String fieldName) {
		super(fieldName);
		setFieldType(FieldType.NUMBER);
	}
}
