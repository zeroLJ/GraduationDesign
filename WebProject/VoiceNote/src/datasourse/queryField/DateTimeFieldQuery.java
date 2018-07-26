package datasourse.queryField;

import java.util.Date;

import datasourse.type.FieldType;

public class DateTimeFieldQuery extends FieldQuery<Date>{
	public DateTimeFieldQuery(String fieldName) {
		super(fieldName);
		setFieldType(FieldType.DATETIME);
	}
}
