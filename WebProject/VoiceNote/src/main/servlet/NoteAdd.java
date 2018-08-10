package main.servlet;

import java.io.File;
import java.util.Date;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import com.alibaba.fastjson.JSON;

import database.entity.Note;
import datasourse.DBUtils;
import main.ResponseParams;
import main.User;
import main.util.DateUtils;
import main.util.ObjUtils;

@WebServlet("/NoteAdd")
public class NoteAdd extends BaseServlet{
	private static final long serialVersionUID = 1L;
	@Override
	public ResponseParams doSQL(Map<String, String> params, DBUtils db, User user) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JSON.parseObject(params.get("data"),Map.class);
		String title = ObjUtils.objToStr(map.get("title"));
		String message = ObjUtils.objToStr(map.get("message"));
		String addTime = ObjUtils.objToStr(map.get("addTime"));
		String audioPath = ObjUtils.objToStr(map.get("audioPath"));
		if (!db.getUploadFiles().isEmpty()) {
			File file = db.getUploadFiles().get(0);
			System.out.println("文件从:" + file.getAbsolutePath());  
			File toFile = new File("C:\\VoiceNote\\" + ObjUtils.objToStr(params.get("name")) + "\\" + DateUtils.getFileNameByDate(DateUtils.StringDateTime(addTime)) + "\\iat.wav");
			toFile.getParentFile().mkdirs();
			file.renameTo(toFile);
			System.out.println("文件到:" + toFile.getAbsolutePath());  
		}
		Note note = new Note();
		note.Field_Name().setValue(user.getUserName());
		note.Field_Title().setValue(title);
		note.Field_Message().setValue(message);
		note.Field_AddTime().setValue(new Date());
		note.Field_AudioPath().setValue(audioPath);
		if (db.saveToDB(note)) {
			return ResponseParams.successResult();
		}else {
			return ResponseParams.successResult();
		}
	}

}
