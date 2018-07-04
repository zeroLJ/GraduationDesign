package main.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.util.ObjUtils;
import main.util.ResponseUtil;

@WebServlet("/InfoGet")
public class InfoGet extends BaseServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(request, response);
	}
	
	@Override
	public void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params)
			throws SQLException, IOException {
		String namekey = "name";
		if (params.containsKey("nameKey")) {
			namekey = params.get("nameKey");
		}
		String query = "select * from dbo.[user] where " + ObjUtils.objToStr(params.get("nameKey")) + "='"+ ObjUtils.objToStr(params.get("name"))+"'";
		ResultSet rs = sql.executeQuery(query);
		if (!rs.next()) {
			ResponseUtil.response(response, "用户不存在", false);
			return;
		}
		List<Map<String, Object>> mList = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		String nickname = ObjUtils.objToStr(rs.getString("nickname"));
		if (nickname.equals("")) {
			nickname =  ObjUtils.objToStr(params.get("name"));
		}
		map.put("nickname", nickname);
		map.put("sex", rs.getString("sex"));
		Date date = rs.getDate("birthday");
//		String birthday = "";
		if (date!=null) {
		    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			map.put("birthday", format.format(date));
		}
		map.put("job", rs.getString("job"));
		map.put("telephone", rs.getString("telephone"));
		map.put("e_mail", rs.getString("e_mail"));
        mList.add(map);
		ResponseUtil.response(response, mList);
	}

}
