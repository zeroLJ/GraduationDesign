package database.entity;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datasourse.BaseEntity;
import datasourse.entityField.NumberFieldValue;
import datasourse.entityField.StringFieldValue;
import datasourse.entityField.DateTimeFieldValue;
import datasourse.type.FieldType;
/**
 * @author ljl
 * userT  µÃÂ¿‡
 * 2018-07-26 04:15
 */ 

public class UserT extends BaseEntity {
	private final static List<String> NAMELIST = Collections.unmodifiableList(Arrays.asList(new String[]{"id","name","name_qq","name_sina","password","nickname","sex","birthday","job","telephone","e_mail"}));
	private final static List<FieldType> TYPELIST = Collections.unmodifiableList(Arrays.asList(new FieldType[]{FieldType.NUMBER,FieldType.STRING,FieldType.STRING,FieldType.STRING,FieldType.STRING,FieldType.STRING,FieldType.STRING,FieldType.DATETIME,FieldType.STRING,FieldType.STRING,FieldType.STRING}));
	private final static Map<String,Integer> FIELDPOSITION;
	private final static String tablename = "userT";
	static{
		Map<String,Integer> m = new HashMap<String,Integer>();
		for(int i=0;i<NAMELIST.size();i++){
			m.put(NAMELIST.get(i), i);
		}
		FIELDPOSITION = Collections.unmodifiableMap(m);
	}
	private final static List<String> KEYLIST = Collections.unmodifiableList(Arrays.asList(new String[]{"id"}));
	@Override
	protected List<String> getKeys() {
		return KEYLIST;
	}
	@Override
	public String getTableName() {
		return tablename;
	}
	@Override
	public List<String> getFieldNames() {
		return NAMELIST;
	}
	@Override
	public List<FieldType> getFieldTypes() {
		return TYPELIST;
	}
	@Override
	public Map<String, Integer> getFieldPositionMap() {
		return FIELDPOSITION;
	}

	public NumberFieldValue Field_Id() {
		return getNumberField(0);
	}

	public StringFieldValue Field_Name() {
		return getStringField(1);
	}

	public StringFieldValue Field_Name_qq() {
		return getStringField(2);
	}

	public StringFieldValue Field_Name_sina() {
		return getStringField(3);
	}

	public StringFieldValue Field_Password() {
		return getStringField(4);
	}

	public StringFieldValue Field_Nickname() {
		return getStringField(5);
	}

	public StringFieldValue Field_Sex() {
		return getStringField(6);
	}

	public DateTimeFieldValue Field_Birthday() {
		return getDateTimeField(7);
	}

	public StringFieldValue Field_Job() {
		return getStringField(8);
	}

	public StringFieldValue Field_Telephone() {
		return getStringField(9);
	}

	public StringFieldValue Field_E_mail() {
		return getStringField(10);
	}
}
