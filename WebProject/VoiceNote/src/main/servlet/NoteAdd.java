package main.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import main.util.DateUtils;
import main.util.ObjUtils;
import main.util.ResponseUtil;

@WebServlet("/NoteAdd")
public class NoteAdd extends BaseServlet{
	private static final long serialVersionUID = 1L;
	@Override
	public void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params)
			throws SQLException, IOException {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		Map<String, Object> map = JSON.parseObject(params.get("data"),Map.class);
		String title = ObjUtils.objToStr(map.get("title"));
		String message = ObjUtils.objToStr(map.get("message"));
		String addTime = ObjUtils.objToStr(map.get("addTime"));
		String audioPath = ObjUtils.objToStr(map.get("audioPath"));
		if (params.get("file") != null && !params.get("file").equals("") ) {
			File file = new File(params.get("file"));
			System.out.println("文件从:" + file.getAbsolutePath());  
			File toFile = new File("C:\\VoiceNote\\" + ObjUtils.objToStr(params.get("name")) + "\\" + DateUtils.getFileNameByDate(DateUtils.StringDateTime(addTime)) + "\\iat.wav");
			toFile.getParentFile().mkdirs();
			file.renameTo(toFile);
			System.out.println("文件到:" + toFile.getAbsolutePath());  
		}
		sql.execute("insert into dbo.[note](name,title,message,addTime,audioPath) values("
				+"'"+ObjUtils.objToStr(params.get("name"))
				+"','"+title
				+"','"+message
				+"','"+addTime
				+"','"+audioPath
				+"')"); 
		ResponseUtil.response(response, "添加成功");
	}

}
