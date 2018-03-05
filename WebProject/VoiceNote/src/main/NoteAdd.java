package main;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.StringUtils;

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
		if (params.get("file") != null && !params.get("file").equals("") ) {
			File file = new File(params.get("file"));
			System.out.println("文件从:" + file.getAbsolutePath());  
			File toFile = new File("D:\\VoiceNote\\" + name + "\\" + DateUtils.getFileNameByDate(DateUtils.StringDateTime(addTime)) + "\\iat.wav");
			toFile.getParentFile().mkdirs();
			file.renameTo(toFile);
			System.out.println("文件到:" + toFile.getAbsolutePath());  
		}
		sql.execute("insert into dbo.[note](name,title,message,addTime,audioPath) values("
				+"'"+name
				+"','"+title
				+"','"+message
				+"','"+addTime
				+"','"+audioPath
				+"')"); 
		ResponseUtil.response(response, "添加成功");
	}

}
