package main.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.util.ObjUtils;
import main.util.ResponseUtil;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends BaseServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
	public void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params)
    		throws SQLException, IOException {
    	// TODO Auto-generated method stub
	        ResultSet rs = sql.executeQuery("select * from dbo.[user] where name='"+ ObjUtils.objToStr(params.get("name")) +"'");
	        if (rs.next()) {
	        	ResponseUtil.response(response, "此用户名已存在！", false);
			}else {
				sql.execute("insert into dbo.[user](name,password) values('"+ObjUtils.objToStr(params.get("name"))+"','"
						+ObjUtils.objToStr(params.get("password"))+"')"); 
//				response.getWriter().println("注册成功！");
				ResponseUtil.response(response, "注册成功！！", true);
			}
    }
}
