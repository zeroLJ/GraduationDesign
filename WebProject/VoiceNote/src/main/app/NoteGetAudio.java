package main.app;

import java.io.File;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import datasourse.DBUtils;
import main.ResponseParams;
import main.User;
import main.base.BaseServlet;
import main.util.DateUtils;
import main.util.ObjUtils;

@WebServlet("/NoteGetAudio")
public class NoteGetAudio extends BaseServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public ResponseParams doSQL(Map<String, String> params, DBUtils db, User user) {
		String filepath = "C:\\VoiceNote\\" + ObjUtils.objToStr(params.get("name")) + "\\"
				+ DateUtils.getFileNameByDate(DateUtils.StringDateTime(params.get("addTime"))) + "\\iat.wav";
		File file = new File(filepath);
		if (!file.exists()) {
			return ResponseParams.failResult("下载文件不存在！！");
		}
		return ResponseParams.successResultFile(file);
	}

}
