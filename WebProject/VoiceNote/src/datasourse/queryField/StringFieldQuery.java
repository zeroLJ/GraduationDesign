package datasourse.queryField;

import datasourse.type.FieldType;

public class StringFieldQuery extends FieldQuery<String>{
	public StringFieldQuery(String fieldName) {
		super(fieldName);
		setFieldType(FieldType.STRING);
	}
}
