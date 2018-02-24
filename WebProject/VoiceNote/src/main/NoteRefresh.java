package main;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import jdk.nashorn.internal.ir.Flags;

@WebServlet("/NoteRefresh")
public class NoteRefresh extends BaseServlet{

	@Override
	void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params)
			throws SQLException, IOException {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = JSON.parseObject(params.get("data"),List.class);
		String s = "";
		for(Map<String, Object> note : list) {
			String addTime = ObjUtils.objToStr(note.get("addTime"));
			String editTime = ObjUtils.objToStr(note.get("editTime"));
			String title = ObjUtils.objToStr(note.get("title"));
			String message = ObjUtils.objToStr(note.get("message"));
			String audioPath = ObjUtils.objToStr(note.get("audioPath"));
			switch (ObjUtils.objToStr(note.get("flag"))) {
				case "1"://新增
					s = "insert into dbo.[note](name,title,message,addTime,audioPath) values("
							+"'"+name
							+"','"+title
							+"','"+message
							+"','"+addTime
							+"','"+audioPath
							+"')";
					System.out.println("执行sql语句:"+s);
					sql.execute(s);
					break;
				case "2"://修改
					s =  "select * from dbo.[note] where addTime='"+addTime+"' and name='"+name+"'";
					System.out.println("执行sql语句:"+s);
					ResultSet rs = stmt.executeQuery(s);
					if (!rs.next()) {
						sql.execute("insert into dbo.[note](name,title,message,addTime,editTime,audioPath) values("
								+"'"+name
								+"','"+title
								+"','"+message
								+"','"+addTime
								+"','"+editTime
								+"','"+audioPath
								+"')"); 
					}else {
						Date date1 = DateUtils.StringDateTime(editTime);
						String eString = rs.getString(rs.findColumn("editTime"));
						if (eString!=null) {
							Date date2 = DateUtils.StringDateTime(rs.getString(rs.findColumn("editTime")));
							if (date1.getTime() > date2.getTime()) { //客户端的修改时间晚于服务器端的修改时间
								s = "update dbo.[note] set "
										+ "title='"+title+"',"
										+ "message='"+message+"',"
										+ "editTime='"+editTime+"',"
										+ "audioPath='"+audioPath+"' "
										+ "where addTime='"+addTime+"' "
										+ "and name='"+name+"'";
								System.out.println("执行sql语句:"+s);
								sql.execute(s);
							}
						}else {
							s = "update dbo.[note] set "
									+ "title='"+title+"',"
									+ "message='"+message+"',"
									+ "editTime='"+editTime+"',"
									+ "audioPath='"+audioPath+"' "
									+ "where addTime='"+addTime+"' "
									+ "and name='"+name+"'";
							System.out.println("执行sql语句:"+s);
							sql.execute(s); 
						}
					}
					break;
				case "3"://删除
					s = "delete from dbo.[note] where addTime='"+addTime+"' and name='"+name+"'";
					System.out.println("执行sql语句:"+s);
					sql.execute(s); 
					break;
				default:
					break;
			}
		}
		
		s = "select * from dbo.[note] where name='"+name+"'";
		System.out.println("执行sql语句:"+s);
		ResultSet rs = sql.executeQuery(s);
		List<Map<String, Object>> mList = new ArrayList<>();
		while (rs.next()) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", null);
			map.put("name", name);
			map.put("flag", "0");
			map.put("title", rs.getString(rs.findColumn("title")));
			map.put("message", rs.getString(rs.findColumn("message")));
			String addTime = rs.getString(rs.findColumn("addTime"));
			if (addTime !=null && addTime.length() > 19) {
				addTime = addTime.substring(0, 19);
			}
			map.put("addTime", addTime);
			String editTime = rs.getString(rs.findColumn("editTime"));
			if (editTime !=null && editTime.length() > 19) {
				editTime = editTime.substring(0, 19);
			}
			map.put("editTime", editTime);
			map.put("audioPath", rs.getString(rs.findColumn("audioPath")));
			mList.add(map);
		}
		System.out.println(mList.toString());
		ResponseUtil.response(response, mList, null,"同步成功");
	}

}
