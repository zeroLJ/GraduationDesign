package main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BaseServlet
 */
//@WebServlet("/BaseServlet")//此参数在整个项目中必须唯一，否则运行失败
public abstract class BaseServlet extends HttpServlet {
	//这个变量必须设置
	protected static final long serialVersionUID = 1L;
	protected String connectionUrl = 
    		"jdbc:sqlserver://localhost:1433;" 
//    		"jdbc:sqlserver://192.168.0.188:1433;"
               +"databaseName=Demo;"
               + "user=940034240;"
               + "password=pp123456;";   
	protected String name;
	protected String password;
	protected Connection con;
	protected Statement stmt;
	protected Map<String, String> params = new HashMap<>();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BaseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("Served at: "+request.getContextPath() + "  Class:" + getClass().getName());
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(connectionUrl);			
			Map<String, String[]> map =  request.getParameterMap();
			for(String key : map.keySet()) {
				params.put(key, map.get(key)[1]);
			}
			System.out.println("参数列表："+params.toString());
		    stmt = con.createStatement();
	        name = params.get("name");
	        password = params.get("password");
	        System.out.println("name:"+name+" password:"+password);
	        if (getClass().isAssignableFrom(Login.class)) {
				doSQL(request, response, stmt, params);
				return;
			}
	        ResultSet rs = stmt.executeQuery("select * from dbo.[user] where name='"+ name +"' and password='" + password + "'");
	        if (rs.next()) {
	        	System.out.println("有此用户");
	        	doSQL(request, response, stmt, params);
			}else {
				ResponseUtil.response(response, "用户名或密码错误！！", false);
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
		doGet(request, response);
	}
	
	abstract void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params) throws  SQLException, IOException;

}
