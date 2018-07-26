package database.entity;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datasourse.BaseEntity;
import datasourse.entityField.StringFieldValue;
import datasourse.entityField.DateTimeFieldValue;
import datasourse.entityField.NumberFieldValue;
import datasourse.type.FieldType;
/**
 * @author ljl
 * note  µÃÂ¿‡
 * 2018-07-26 02:18
 */ 

public class Note extends BaseEntity {
	private final static List<String> NAMELIST = Collections.unmodifiableList(Arrays.asList(new String[]{"name","title","message","addTime","editTime","audioPath","asd"}));
	private final static List<FieldType> TYPELIST = Collections.unmodifiableList(Arrays.asList(new FieldType[]{FieldType.STRING,FieldType.STRING,FieldType.STRING,FieldType.DATETIME,FieldType.DATETIME,FieldType.STRING,FieldType.NUMBER}));
	private final static Map<String,Integer> FIELDPOSITION;
	private final static String tablename = "note";
	static{
		Map<String,Integer> m = new HashMap<String,Integer>();
		for(int i=0;i<NAMELIST.size();i++){
			m.put(NAMELIST.get(i), i);
		}
		FIELDPOSITION = Collections.unmodifiableMap(m);
	}
	private final static List<String> KEYLIST = Collections.unmodifiableList(Arrays.asList(new String[]{"name","addTime"}));
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

	public StringFieldValue Field_Name() {
		return getStringField(0);
	}

	public StringFieldValue Field_Title() {
		return getStringField(1);
	}

	public StringFieldValue Field_Message() {
		return getStringField(2);
	}

	public DateTimeFieldValue Field_AddTime() {
		return getDateTimeField(3);
	}

	public DateTimeFieldValue Field_EditTime() {
		return getDateTimeField(4);
	}

	public StringFieldValue Field_AudioPath() {
		return getStringField(5);
	}

	public NumberFieldValue Field_Asd() {
		return getNumberField(6);
	}
}
