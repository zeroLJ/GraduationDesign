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
@WebServlet("/LoginCopy")
public class LoginCopy extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 static String connectionUrl = 
	    		"jdbc:sqlserver://localhost:1433;" 
//	    		"jdbc:sqlserver://192.168.0.188:1433;"
	               +"databaseName=Demo;"
	               + "user=940034240;"
	               + "password=pp123456;";  
	
	 private String name="" , password = "";
	 
	 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginCopy() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8"); 
		  try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				 Connection con = DriverManager.getConnection(connectionUrl);
			        
			        Statement stmt = con.createStatement();
			        //从 “S”表中查询 “Sno”列和“Ssex”列
//			        ResultSet rs = stmt.executeQuery("select * from dbo.[user] where id='2' and name='zero'");
//			        ResultSet rs = stmt.executeQuery("select * from dbo.[user] where name='"+ name +"' and password='" + password + "'");
//			        while(rs.next()){
//			            System.out.println(rs.getString("id")+","+rs.getString("name"));
//			            response.getWriter().println(rs.getString("id")+","+rs.getString("name"));
//			        }
			        
			        name = request.getParameter("name");
			        password = request.getParameter("password");
			        ResultSet rs = stmt.executeQuery("select * from dbo.[user] where name='"+ name +"'");
			        if (rs.next()) {
//			        	 response.getWriter().println("此用户名已存在！");
//			        	 System.out.println("此用户名已存在！");
			        	ResponseUtil.response(response, "此用户名已存在！", false);
			        	Map<String, Object> map = new HashMap<>();
			        	map.put("A", "A");
			        	map.put("C", 1);
			        	List<Map<String, Object>> list = new ArrayList<>();
			        	list.add(map);
			        	map = new HashMap<>();
			        	map.put("B", "B");
			        	map.put("C", 2);
			        	list.add(map);
//			        	ResponseUtil.response(response, list, map, "此用户名已存在！", false);
					}else {
//						stmt.execute("insert into dbo.[user](name,password) values('"+name+"','"+password+"')"); 
//						response.getWriter().println("注册成功！");
						
						ResponseUtil.response(response, "注册成功！！", true);
						
//						if (a) {
//							response.getWriter().println("注册成功！");
//							 System.out.println("注册成功！");
//						}else {
//							response.getWriter().println("注册失败！");
//							 System.out.println("注册失败！");
//						}
					}
			     
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
