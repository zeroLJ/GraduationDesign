package main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.jaxrs.FastJsonAutoDiscoverable;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends BaseServlet {

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params)
    		throws SQLException, IOException {
    	// TODO Auto-generated method stub
    	   	name = params.get("name");
	        password = params.get("password");
	        ResultSet rs = stmt.executeQuery("select * from dbo.[user] where name='"+ name +"'");
	        if (rs.next()) {
	        	ResponseUtil.response(response, "此用户名已存在！", false);
			}else {
				stmt.execute("insert into dbo.[user](name,password) values('"+name+"','"+password+"')"); 
//				response.getWriter().println("注册成功！");
				ResponseUtil.response(response, "注册成功！！", true);
			}
    }
}
