package datasourse.queryField;

import datasourse.type.FieldType;

public class BooleanFieldQuery extends FieldQuery<Boolean>{
	public BooleanFieldQuery(String fieldName) {
		super(fieldName);
		setFieldType(FieldType.BOOLEAN);
	}
}
