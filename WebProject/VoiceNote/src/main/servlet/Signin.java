package main.servlet;

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

@WebServlet("/Signin")
public class Signin extends BaseServlet {
	private static final long serialVersionUID = 1L;
	@Override
	public void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params)
    		throws SQLException, IOException {
    	// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		String nickname = ObjUtils.objToStr(params.get("nickname"));
		if (nickname.equals("")) {
			nickname = ObjUtils.objToStr(params.get("name"));
		}
		map.put("nickname",nickname);
    	ResponseUtil.response(response, null, map, "µÇÂ¼³É¹¦£¡£¡");
	}
}
