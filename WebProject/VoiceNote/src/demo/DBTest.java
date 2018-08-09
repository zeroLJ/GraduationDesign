package demo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.entity.Note;
import database.query.NoteQuery;
import datasourse.DBUtils;


@WebServlet("/DBTest")
public class DBTest extends HttpServlet{
	protected static final long serialVersionUID = 1L;
	protected String connectionUrl = 
    		"jdbc:sqlserver://localhost:1433;" 
               +"databaseName=demo;"
               + "user=ljl;"
               + "password=pp123456;";   
	private void main(HttpServletRequest request, HttpServletResponse response){
		DBUtils dbUtils = null;
	
			dbUtils = new DBUtils();
//			List<DBOperation> list = new ArrayList<>();
//			DBOperation dbOperation = new DBOperation("update dbo.[user] set password = '222' where name= 'zero'");
//			list.add(dbOperation);
//			
//			DBOperation dbOperation2 = new DBOperation("update dbo.[user] set nickname = '333' where name= zero");
//			list.add(dbOperation2);
//			dbUtils.saveToDB(list);
			
			
			NoteQuery query2= new NoteQuery();
			query2.Field_Name().setIsNot("1B281739B5BDC2D8118D27964688CB78_qq");
			List<Note> list = dbUtils.queryEntity(query2);
			System.out.println("list:"+list);
			list.get(0).Field_Message().setValue("doub");
			dbUtils.saveToDB(list.get(0));
			
			Note note = new Note();
			note.Field_Name().setValue("sssss");
			note.Field_Message().setValue("¶º±È");
			note.Field_AddTime().setValue(new Date());
			dbUtils.saveToDB(note);
	
			if (dbUtils!=null) {
				dbUtils.destory();
			}
		
	
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		main(request, response);
		
//		Connection con = null;
//		try {
//			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//			request.setCharacterEncoding("UTF-8"); 
//			con = DriverManager.getConnection(connectionUrl);
//		    Statement stmt = con.createStatement();
//		    main(request, response, null, null);
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			ResponseUtil.response(response, e.getMessage(), false);
//			e.printStackTrace();
//			return;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			ResponseUtil.response(response, e.getMessage(), false);
//			e.printStackTrace();
//			return;
//		}finally {
//			if (con!=null) {
//				try {
//					con.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		
//			
//		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
