package main;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

@WebServlet("/NoteAdd")
public class NoteAdd extends BaseServlet{

	@Override
	void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params)
			throws SQLException, IOException {
		// TODO Auto-generated method stub
		Map<String, Object> map = JSON.parseObject(params.get("data"),Map.class);
		String title = ObjUtils.objToStr(map.get("title"));
		String message = ObjUtils.objToStr(map.get("message"));
		String addTime = ObjUtils.objToStr(map.get("addTime"));
		String audioPath = ObjUtils.objToStr(map.get("audioPath"));
		sql.execute("insert into dbo.[note](name,title,message,addTime,audioPath) values("
				+"'"+name
				+"','"+title
				+"','"+message
				+"','"+addTime
				+"','"+audioPath
				+"')"); 
		ResponseUtil.response(response, "Ìí¼Ó³É¹¦");
	}

}
