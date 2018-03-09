package main;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.StringUtils;

import com.alibaba.fastjson.JSON;

@WebServlet("/HeadIconGet")
public class HeadIconGet extends BaseServlet{

	@Override
	void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params)
			throws SQLException, IOException {
		 String filepath = "D:\\VoiceNote\\" + name + "\\icon.jpg";
	        File file = new File(filepath);  
	        if (!file.exists()) {
	        	ResponseUtil.response(response, "下载头像不存在！！", false);
	        	response.sendError(300);
	        	return;
			}
	        
	        ResponseUtil.responseFile(response, file);
	}

}
