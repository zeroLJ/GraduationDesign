package main;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Signin")
public class Signin extends BaseServlet {
//	private static final long serialVersionUID = 1L;
	@Override
    protected void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params)
    		throws SQLException, IOException {
    	// TODO Auto-generated method stub
    	ResponseUtil.response(response, "µÇÂ¼³É¹¦£¡£¡", true);
	}
}
