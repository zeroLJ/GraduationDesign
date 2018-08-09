package main.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import datasourse.DBUtils;
import main.ResponseParams;
import main.User;
import main.util.DateUtils;
import main.util.ObjUtils;
import main.util.ResponseUtil;

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
//			ResponseUtil.response(response, "下载文件不存在！！", false);
//			response.sendError(300);
//			return;
		}
		return ResponseParams.successResultFile(file);
//		ResponseUtil.responseFile(response, file);
	}

}
