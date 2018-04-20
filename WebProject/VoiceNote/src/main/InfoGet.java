package main;

import java.io.File;
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

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

@WebServlet("/InfoGet")
public class InfoGet extends BaseServlet{

	@Override
	void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params)
			throws SQLException, IOException {
		// TODO Auto-generated method stub
		String query = "select * from dbo.[user] where " + nameKey + "='"+name+"'";
		ResultSet rs = stmt.executeQuery(query);
		if (!rs.next()) {
			ResponseUtil.response(response, "用户不存在", false);
			return;
		}
		List<Map<String, Object>> mList = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("nickname", rs.getString("nickname"));
		map.put("sex", rs.getString("sex"));
		Date date = rs.getDate("birthday");
		String birthday = "";
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
