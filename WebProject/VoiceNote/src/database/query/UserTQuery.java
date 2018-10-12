package database.query;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datasourse.BaseEntity;
import datasourse.BaseQuery;
import database.entity.UserT;
import datasourse.queryField.NumberFieldQuery;
import datasourse.queryField.StringFieldQuery;
import datasourse.queryField.DateTimeFieldQuery;
import datasourse.type.FieldType;
/**
 * @author ljl
 * userT ≤È—Ø¿‡
 * 2018-10-11 11:17
 */ 

public class UserTQuery extends BaseQuery {
	private final static List<String> NAMELIST = Collections.unmodifiableList(Arrays.asList(new String[]{"id","name","name_qq","name_sina","name_mini","password","nickname","sex","birthday","job","telephone","e_mail"}));
	private final static List<FieldType> TYPELIST = Collections.unmodifiableList(Arrays.asList(new FieldType[]{FieldType.NUMBER,FieldType.STRING,FieldType.STRING,FieldType.STRING,FieldType.STRING,FieldType.STRING,FieldType.STRING,FieldType.STRING,FieldType.DATETIME,FieldType.STRING,FieldType.STRING,FieldType.STRING}));
	private final static Map<String,Integer> FIELDPOSITION;
	private final static String tablename = "userT";
	static{
		Map<String,Integer> m = new HashMap<String,Integer>();
		for(int i=0;i<NAMELIST.size();i++){
			m.put(NAMELIST.get(i), i);
		}
		FIELDPOSITION = Collections.unmodifiableMap(m);
	}
	@Override
	public Class<? extends BaseEntity> getEntityClass(){
		return UserT.class;
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

	public NumberFieldQuery Field_Id() {
		return getNumberField(0);
	}

	public StringFieldQuery Field_Name() {
		return getStringField(1);
	}

	public StringFieldQuery Field_Name_qq() {
		return getStringField(2);
	}

	public StringFieldQuery Field_Name_sina() {
		return getStringField(3);
	}

	public StringFieldQuery Field_Name_mini() {
		return getStringField(4);
	}

	public StringFieldQuery Field_Password() {
		return getStringField(5);
	}

	public StringFieldQuery Field_Nickname() {
		return getStringField(6);
	}

	public StringFieldQuery Field_Sex() {
		return getStringField(7);
	}

	public DateTimeFieldQuery Field_Birthday() {
		return getDateTimeField(8);
	}

	public StringFieldQuery Field_Job() {
		return getStringField(9);
	}

	public StringFieldQuery Field_Telephone() {
		return getStringField(10);
	}

	public StringFieldQuery Field_E_mail() {
		return getStringField(11);
	}
}
