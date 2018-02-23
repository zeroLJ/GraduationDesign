package main;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

@WebServlet("/NoteUpdate")
public class NoteUpdate extends BaseServlet{

	@Override
	void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params)
			throws SQLException, IOException {
		// TODO Auto-generated method stub
		Map<String, Object> map = JSON.parseObject(params.get("data"),Map.class);
		String title = ObjUtils.objToStr(map.get("title"));
		String message = ObjUtils.objToStr(map.get("message"));
		String addTime = ObjUtils.objToStr(map.get("addTime"));
		String editTime = ObjUtils.objToStr(map.get("editTime"));
		String audioPath = ObjUtils.objToStr(map.get("audioPath"));
		String query = "select * from dbo.[note] where addTime='"+addTime+"' and name='"+name+"'";
		ResultSet rs = stmt.executeQuery(query);
		if (!rs.next()) {
			sql.execute("insert into dbo.[note](name,title,message,addTime,editTime,audioPath) values("
					+"'"+name
					+"','"+title
					+"','"+message
					+"','"+addTime
					+"','"+editTime
					+"','"+audioPath
					+"')"); 
			ResponseUtil.response(response, "添加成功");
			return;
		}
		String s = "update dbo.[note] set "
				+ "title='"+title+"',"
				+ "message='"+message+"',"
				+ "editTime='"+editTime+"',"
				+ "audioPath='"+audioPath+"' "
				+ "where addTime='"+addTime+"' "
				+ "and name='"+name+"'";
		System.out.println("执行sql语句:"+s);
		sql.execute(s); 
		ResponseUtil.response(response, "修改成功");
	}

}
