package main;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jdt.internal.compiler.ast.NullLiteral;

import com.alibaba.fastjson.JSON;

@WebServlet("/NoteGetAudio")
public class NoteGetAudio extends BaseServlet{

	@Override
	void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params)
			throws SQLException, IOException {
		// TODO Auto-generated method stub
	        // must be linux path(not \\)   
	        String filepath = "C:\\VoiceNote\\" + name + "\\" + DateUtils.getFileNameByDate(DateUtils.StringDateTime(params.get("addTime"))) + "\\iat.wav";
	        File file = new File(filepath);  
	        if (!file.exists()) {
	        	ResponseUtil.response(response, "下载文件不存在！！", false);
	        	response.sendError(300);
	        	return;
			}
	        
	        ResponseUtil.responseFile(response, file);
	        
	}

}
