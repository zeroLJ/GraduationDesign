package main.servlet;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.util.DateUtils;
import main.util.ObjUtils;
import main.util.ResponseUtil;

@WebServlet("/NoteGetAudio")
public class NoteGetAudio extends BaseServlet{
	private static final long serialVersionUID = 1L;
	@Override
	public void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params)
			throws SQLException, IOException {
		// TODO Auto-generated method stub
	        // must be linux path(not \\)   
	        String filepath = "C:\\VoiceNote\\" + ObjUtils.objToStr(params.get("name")) + "\\" + DateUtils.getFileNameByDate(DateUtils.StringDateTime(params.get("addTime"))) + "\\iat.wav";
	        File file = new File(filepath);  
	        if (!file.exists()) {
	        	ResponseUtil.response(response, "下载文件不存在！！", false);
	        	response.sendError(300);
	        	return;
			}
	        
	        ResponseUtil.responseFile(response, file);
	        
	}

}
