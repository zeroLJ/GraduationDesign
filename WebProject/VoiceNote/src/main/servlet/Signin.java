package main.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import datasourse.DBUtils;
import main.ResponseParams;
import main.User;
import main.util.ObjUtils;

@WebServlet("/Signin")
public class Signin extends BaseServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public ResponseParams doSQL(Map<String, String> params, DBUtils db, User user) {
		Map<String, Object> map = new HashMap<>();
		String nickname = ObjUtils.objToStr(user.getNickName());
		map.put("nickname",nickname);
		return ResponseParams.successResultMap(map);
	}
}
