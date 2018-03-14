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

@WebServlet("/HeadIconChange")
public class HeadIconChange extends BaseServlet{

	@Override
	void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params)
			throws SQLException, IOException {
		
		if (params.get("file") != null && !params.get("file").equals("") ) {
			File file = new File(params.get("file"));
			System.out.println("�ļ���:" + file.getAbsolutePath());  
			File toFile = new File("C:\\VoiceNote\\" + name + "\\icon.jpg");
			toFile.getParentFile().mkdirs();
			if (toFile.exists()) {
				toFile.delete();
			}
			file.renameTo(toFile);
			System.out.println("�ļ���:" + toFile.getAbsolutePath());  
			ResponseUtil.response(response, "ͷ����ĳɹ�");
		}else {
			ResponseUtil.response(response, "ͷ�����ʧ��",false);
		}
	}

}
