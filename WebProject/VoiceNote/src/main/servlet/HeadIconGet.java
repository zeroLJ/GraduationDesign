package main.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.util.ObjUtils;
import main.util.ResponseUtil;

@WebServlet("/HeadIconGet")
public class HeadIconGet extends BaseServlet{
	private static final long serialVersionUID = 1L;
	@Override
	public void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params)
			throws SQLException, IOException {
		 	String filepath = "C:\\VoiceNote\\" +  ObjUtils.objToStr(params.get("name")) + "\\icon.jpg";
	        File file = new File(filepath); 
	        boolean hasFile = true;
	        if (!file.exists()) {
	        	file = null;
	        	hasFile = false;
			}
	        String nickname = ObjUtils.objToStr(params.get("nickname"));
			if (nickname.equals("")) {
				nickname = ObjUtils.objToStr(params.get("name"));
			}
			System.out.println("nickname:"+nickname);
			
			Map<String, Object> map = new HashMap<>();
			map.put("nickname", nickname);
			map.put("hasFile", hasFile);
	        ResponseUtil.responseFile(response, null, map, nickname, file);
//	        ResponseUtil.responseFile(response,file);
	}

}
