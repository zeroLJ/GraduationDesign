package database.query;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datasourse.BaseEntity;
import datasourse.BaseQuery;
import database.entity.Note;
import datasourse.queryField.StringFieldQuery;
import datasourse.queryField.DateTimeFieldQuery;
import datasourse.queryField.NumberFieldQuery;
import datasourse.type.FieldType;
/**
 * @author ljl
 * note ≤È—Ø¿‡
 * 2018-07-26 02:18
 */ 

public class NoteQuery extends BaseQuery {
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
	@Override
	public Class<? extends BaseEntity> getEntityClass(){
		return Note.class;
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

	public StringFieldQuery Field_Name() {
		return getStringField(0);
	}

	public StringFieldQuery Field_Title() {
		return getStringField(1);
	}

	public StringFieldQuery Field_Message() {
		return getStringField(2);
	}

	public DateTimeFieldQuery Field_AddTime() {
		return getDateTimeField(3);
	}

	public DateTimeFieldQuery Field_EditTime() {
		return getDateTimeField(4);
	}

	public StringFieldQuery Field_AudioPath() {
		return getStringField(5);
	}

	public NumberFieldQuery Field_Asd() {
		return getNumberField(6);
	}
}
