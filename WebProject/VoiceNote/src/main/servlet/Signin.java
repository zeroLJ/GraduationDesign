package main.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import datasourse.DBUtils;
import main.ResponseParams;
import main.User;
import main.util.ObjUtils;
import main.util.ResponseUtil;

@WebServlet("/Signin")
public class Signin extends BaseServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public ResponseParams doSQL(Map<String, String> params, DBUtils db, User user) {
		Map<String, Object> map = new HashMap<>();
		String nickname = ObjUtils.objToStr(user.getNickName());
		map.put("nickname",nickname);
		return ResponseParams.successResultMap(map);
//    	ResponseUtil.response(response, null, map, "µÇÂ¼³É¹¦£¡£¡");
//		return null;
	}
}
