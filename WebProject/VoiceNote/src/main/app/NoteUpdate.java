package main.app;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import com.alibaba.fastjson.JSON;

import database.entity.Note;
import database.query.NoteQuery;
import datasourse.DBUtils;
import main.ResponseParams;
import main.User;
import main.base.BaseServlet;
import main.util.DateUtils;
import main.util.ObjUtils;

@WebServlet("/NoteUpdate")
public class NoteUpdate extends BaseServlet{
	private static final long serialVersionUID = 1L;
	@Override
	public ResponseParams doSQL(Map<String, String> params, DBUtils db, User user) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JSON.parseObject(params.get("data"),Map.class);
		String title = ObjUtils.objToStr(map.get("title"));
		String message = ObjUtils.objToStr(map.get("message"));
		String addTime = ObjUtils.objToStr(map.get("addTime"));
		String editTime = ObjUtils.objToStr(map.get("editTime"));
		String audioPath = ObjUtils.objToStr(map.get("audioPath"));
//		String query = "select * from dbo.[note] where addTime='"+addTime+"' and name='"+ ObjUtils.objToStr(params.get("name"))+"'";
		if (params.get("file") != null && !params.get("file").equals("") ) {
			File file = new File(params.get("file"));
			System.out.println("文件从:" + file.getAbsolutePath());  
			File toFile = new File("C:\\VoiceNote\\" + ObjUtils.objToStr(params.get("name")) + "\\" + DateUtils.getFileNameByDate(DateUtils.StringDateTime(addTime)) + "\\iat.wav");
			toFile.getParentFile().mkdirs();
			if (toFile.exists()) {
				toFile.delete();
			}
			file.renameTo(toFile);
			System.out.println("文件到:" + toFile.getAbsolutePath());  
		}
		NoteQuery query = new NoteQuery();
		query.Field_Name().setIs(user.getUserName());
		query.Field_AddTime().setIs(DateUtils.StringDateTime(addTime));
		List<Note> list = db.queryEntity(query);
		if (list.isEmpty()) {
			Note note = new Note();
			note.Field_Name().setValue(user.getUserName());
			note.Field_Title().setValue(title);
			note.Field_Message().setValue(message);
			note.Field_AddTime().setValue(new Date());
			note.Field_AudioPath().setValue(audioPath);
			db.saveToDB(note);
//			ResponseUtil.response(response, "添加成功");
			return ResponseParams.successResult();
		}
		list.get(0).Field_Name().setValue(user.getUserName());
		list.get(0).Field_Title().setValue(title);
		list.get(0).Field_Message().setValue(message);
		list.get(0).Field_EditTime().setValue(new Date());
		list.get(0).Field_AudioPath().setValue(audioPath);
		db.saveToDB(list);
		return ResponseParams.successResult();
//		String s = "update dbo.[note] set "
//				+ "title='"+title+"',"
//				+ "message='"+message+"',"
//				+ "editTime='"+editTime+"',"
//				+ "audioPath='"+audioPath+"' "
//				+ "where addTime='"+addTime+"' "
//				+ "and name='"+ ObjUtils.objToStr(params.get("name")) +"'";
//		System.out.println("执行sql语句:"+s);
//		sql.execute(s); 
//		ResponseUtil.response(response, "修改成功");
//		return null;
	}

}
