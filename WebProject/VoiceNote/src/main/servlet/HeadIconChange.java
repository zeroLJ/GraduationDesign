package main.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.util.ObjUtils;
import main.util.ResponseUtil;

@WebServlet("/HeadIconChange")
public class HeadIconChange extends BaseServlet{
	private static final long serialVersionUID = 1L;

	@Override
	public void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params)
			throws SQLException, IOException {
		
		if (params.get("file") != null && !params.get("file").equals("") ) {
			File file = new File(params.get("file"));
			System.out.println("文件从:" + file.getAbsolutePath());  
			File toFile = new File("C:\\VoiceNote\\" +  ObjUtils.objToStr(params.get("name")) + "\\icon.jpg");
			toFile.getParentFile().mkdirs();
			if (toFile.exists()) {
				toFile.delete();
			}
			file.renameTo(toFile);
			System.out.println("文件到:" + toFile.getAbsolutePath());  
			ResponseUtil.response(response, "头像更改成功");
		}else {
			ResponseUtil.response(response, "头像更改失败",false);
		}
	}

}
