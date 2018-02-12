package demo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ServletDemo
 */
@WebServlet("/ServletDemo")
public class ServletDemo extends HttpServlet {
	private static final long serialVersionUID = 1L;
    static String connectionUrl = 
    		"jdbc:sqlserver://localhost:1433;" 
//    		"jdbc:sqlserver://192.168.0.188:1433;"
               +"databaseName=Demo;"
               + "user=940034240;"
               + "password=pp123456;";  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletDemo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("sssss");
		 //注册驱动程序所需语句
        try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			 Connection con = DriverManager.getConnection(connectionUrl);
		        
		        Statement stmt = con.createStatement();
		        //从 “S”表中查询 “Sno”列和“Ssex”列
		        ResultSet rs = stmt.executeQuery("select id,name from dbo.[user]");
		        
		        //如果查到有数据，全部输出
		        while(rs.next()){
		            System.out.println(rs.getString("id")+","+rs.getString("name"));
		            response.getWriter().println(rs.getString("id")+","+rs.getString("name"));
		        }
//		        response.getWriter().append("Served at: ").append(request.getContextPath());
		        
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//       response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
