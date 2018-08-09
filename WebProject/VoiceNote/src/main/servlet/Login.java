package main.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.entity.UserT;
import database.query.UserTQuery;
import datasourse.DBUtils;
import main.ResponseParams;
import main.User;
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
	public ResponseParams doSQL(Map<String, String> params, DBUtils db, User user) {
		UserTQuery query = new UserTQuery();
		query.Field_Name().setIs(user.getUserName());
		if (!db.queryEntity(query).isEmpty()) {
			return ResponseParams.failResult("此用户名已存在！");
		}else {
			UserT entity = new UserT();
			entity.Field_Name().setValue(user.getUserName());
			entity.Field_Password().setValue(user.getPassword());
			if (db.saveToDB(entity)) {
				return ResponseParams.successResult();
			}else {
				return ResponseParams.failResult("注册失败");
			}
		}
	}
}
