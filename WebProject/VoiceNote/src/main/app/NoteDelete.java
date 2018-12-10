package main.app;

import java.io.File;
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

@WebServlet("/NoteDelete")
public class NoteDelete extends BaseServlet{
	private static final long serialVersionUID = 1L;
	public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

	@Override
	public ResponseParams doSQL(Map<String, String> params, DBUtils db, User user) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JSON.parseObject(params.get("data"),Map.class);
		String addTime = ObjUtils.objToStr(map.get("addTime"));
		NoteQuery query = new NoteQuery();
		query.Field_Name().setIs(user.getUserName());
		query.Field_AddTime().setIs(DateUtils.StringDateTime(addTime));
		List<Note> list = db.queryEntity(query);
		for(Note note : list) {
			note.delete();
		}
		db.saveToDB(list);
		String filepath = "C:\\VoiceNote\\" + user.getUserName() + "\\" 
		+ DateUtils.getFileNameByDate(DateUtils.StringDateTime(addTime));
		deleteFile(new File(filepath));
		return ResponseParams.successResult();
	}
}
