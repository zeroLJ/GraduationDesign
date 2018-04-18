package main;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 第三方登录接口
 * @author ljl
 *
 */
@WebServlet("/SigninOther")
public class SigninOther extends BaseServlet {
//	private static final long serialVersionUID = 1L;
	@Override
    protected void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params)
    		throws SQLException, IOException {
    	name = params.get("name");
    	password = params.get("password");
        String key = "name";
        if (name.endsWith("_qq")) {
			key = "name_qq";
		}else if(name.endsWith("_sina")) {
			key = "name_sina";
		}	    
        String nickname = params.get("nickname");
        ResultSet rs = stmt.executeQuery("select * from dbo.[user] where "+ key +"='"+ name +"'");
        if (!rs.next()) {
        	 if (params.get("file") != null && !params.get("file").equals("") ) {
     			File file = new File(params.get("file"));
     			System.out.println("文件从:" + file.getAbsolutePath());  
     			File toFile = new File("C:\\VoiceNote\\" + name + "\\icon.jpg");
     			toFile.getParentFile().mkdirs();
     			if (toFile.exists()) {
     				toFile.delete();
     			}
     			file.renameTo(toFile);
     			System.out.println("文件到:" + toFile.getAbsolutePath());
     		}
			stmt.execute("insert into dbo.[user]("+ key +",password, nickname) values('"+name+"','"+password+"','" + nickname +"')"); 
			Map<String, Object> map = new HashMap<>();
			map.put("newUser", "true");
			ResponseUtil.response(response, null, map, "注册成功！！", true);
			return;
        }
        ResponseUtil.response(response, "登录成功！！", true);
	}
}
