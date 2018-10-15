package main.app;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import com.alibaba.fastjson.JSON;

import database.entity.Note;
import database.query.NoteQuery;
import datasourse.DBUtils;
import main.ResponseParams;
import main.User;
import main.base.BaseServlet;
import main.util.DateUtils;
import main.util.Logs;
import main.util.ObjUtils;

@WebServlet("/NoteRefresh")
public class NoteRefresh extends BaseServlet{
	private static final long serialVersionUID = 1L;
	@Override
	public ResponseParams doSQL(Map<String, String> params, DBUtils db, User user) {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = JSON.parseObject(params.get("data"),List.class);
		for(Map<String, Object> note : list) {
			String addTime = ObjUtils.objToStr(note.get("addTime"));
			String title = ObjUtils.objToStr(note.get("title"));
			String message = ObjUtils.objToStr(note.get("message"));
			String audioPath = ObjUtils.objToStr(note.get("audioPath"));
			switch (ObjUtils.objToStr(note.get("flag"))) {
				case "1"://新增
					Note entity = new Note();
					entity.Field_Name().setValue(user.getUserName());
					entity.Field_Title().setValue(title);
					entity.Field_Message().setValue(message);
					entity.Field_AddTime().setValue(DateUtils.StringDateTime(addTime));
					entity.Field_AudioPath().setValue(audioPath);
					db.saveToDB(entity);
				case "2"://修改
					NoteQuery query = new NoteQuery();
					query.Field_Name().setIs(user.getUserName());
					query.Field_AddTime().setIs(DateUtils.StringDateTime(addTime));
					List<Note> list2 = db.queryEntity(query);
					if (list.isEmpty()) {
						Note note2 = new Note();
						note2.Field_Name().setValue(user.getUserName());
						note2.Field_Title().setValue(title);
						note2.Field_Message().setValue(message);
						note2.Field_AddTime().setValue(DateUtils.StringDateTime(addTime));
						note2.Field_AudioPath().setValue(audioPath);
						db.saveToDB(note2);
//						ResponseUtil.response(response, "添加成功");
						return ResponseParams.successResult();
					}
					list2.get(0).Field_Name().setValue(user.getUserName());
					list2.get(0).Field_Title().setValue(title);
					list2.get(0).Field_Message().setValue(message);
					list2.get(0).Field_EditTime().setValue(new Date());
					list2.get(0).Field_AudioPath().setValue(audioPath);
					db.saveToDB(list2);
					
					
//					s =  "select * from dbo.[note] where addTime='"+addTime+"' and name='"+ ObjUtils.objToStr(params.get("name"))+"'";
//					System.out.println("执行sql语句:"+s);
//					ResultSet rs = sql.executeQuery(s);
//					if (!rs.next()) {
//						sql.execute("insert into dbo.[note](name,title,message,addTime,editTime,audioPath) values("
//								+"'"+ ObjUtils.objToStr(params.get("name"))
//								+"','"+title
//								+"','"+message
//								+"','"+addTime
//								+"','"+editTime
//								+"','"+audioPath
//								+"')"); 
//					}else {
//						Date date1 = DateUtils.StringDateTime(editTime);
//						String eString = rs.getString(rs.findColumn("editTime"));
//						if (eString!=null) {
//							Date date2 = DateUtils.StringDateTime(rs.getString(rs.findColumn("editTime")));
//							if (date1.getTime() > date2.getTime()) { //客户端的修改时间晚于服务器端的修改时间
//								s = "update dbo.[note] set "
//										+ "title='"+title+"',"
//										+ "message='"+message+"',"
//										+ "editTime='"+editTime+"',"
//										+ "audioPath='"+audioPath+"' "
//										+ "where addTime='"+addTime+"' "
//										+ "and name='"+ObjUtils.objToStr(params.get("name"))+"'";
//								System.out.println("执行sql语句:"+s);
//								sql.execute(s);
//							}
//						}else {
//							s = "update dbo.[note] set "
//									+ "title='"+title+"',"
//									+ "message='"+message+"',"
//									+ "editTime='"+editTime+"',"
//									+ "audioPath='"+audioPath+"' "
//									+ "where addTime='"+addTime+"' "
//									+ "and name='"+ObjUtils.objToStr(params.get("name"))+"'";
//							System.out.println("执行sql语句:"+s);
//							sql.execute(s); 
//						}
//					}
//					break;
				case "3"://删除
//					s = "delete from dbo.[note] where addTime='"+addTime+"' and name='"+ObjUtils.objToStr(params.get("name"))+"'";
//					System.out.println("执行sql语句:"+s);
//					sql.execute(s); 
					NoteQuery noteQuery = new NoteQuery();
					noteQuery.Field_Name().setIs(user.getUserName());
					noteQuery.Field_AddTime().setIs(DateUtils.StringDateTime(addTime));
					List<Note> notes = db.queryEntity(noteQuery);
					for(Note note2 : notes) {
						note2.delete();
					}
					db.saveToDB(notes);
					String filepath = "C:\\VoiceNote\\" + user.getUserName() + "\\" 
					+ DateUtils.getFileNameByDate(DateUtils.StringDateTime(addTime));
					NoteDelete.deleteFile(new File(filepath));
					break;
				default:
					break;
			}
		}
		
		NoteQuery query = new NoteQuery();
		query.Field_Name().setIs(user.getUserName());
		List<Map<String, Object>> resultList = db.queryList(query);
		for(Map<String, Object> map : resultList) {
			if (map.get("addTime")!=null) {
				map.put("addTime", DateUtils.getDateString(ObjUtils.objToDate(map.get("addTime"))));
			}
			if (map.get("editTime")!=null) {
				map.put("editTime", DateUtils.getDateString(ObjUtils.objToDate(map.get("editTime"))));
			}
			map.put("flag", "0");
		}
		Logs.d("list:"+resultList);
		return ResponseParams.successResultList(resultList);
//		s = "select * from dbo.[note] where name='"+ObjUtils.objToStr(params.get("name"))+"'";
//		System.out.println("执行sql语句:"+s);
//		ResultSet rs = sql.executeQuery(s);
//		List<Map<String, Object>> mList = new ArrayList<>();
//		while (rs.next()) {
//			Map<String, Object> map = new HashMap<>();
//			map.put("id", null);
//			map.put("name", ObjUtils.objToStr(params.get("name")));
//			map.put("flag", "0");
//			map.put("title", rs.getString(rs.findColumn("title")));
//			map.put("message", rs.getString(rs.findColumn("message")));
//			String addTime = rs.getString(rs.findColumn("addTime"));
//			if (addTime !=null && addTime.length() > 19) {
//				addTime = addTime.substring(0, 19);
//			}
//			map.put("addTime", addTime);
//			String editTime = rs.getString(rs.findColumn("editTime"));
//			if (editTime !=null && editTime.length() > 19) {
//				editTime = editTime.substring(0, 19);
//			}
//			map.put("editTime", editTime);
//			map.put("audioPath", rs.getString(rs.findColumn("audioPath")));
//			mList.add(map);
//		}
//		System.out.println(mList.toString());
//		ResponseUtil.response(response, mList, null,"同步成功");
//		return null;
	}

}
