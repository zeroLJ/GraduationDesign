package main.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.util.ObjUtils;
import main.util.ResponseUtil;

@WebServlet("/InfoUpdate")
public class InfoUpdate extends BaseServlet{
	private static final long serialVersionUID = 1L;
	@Override
	public void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params)
			throws SQLException, IOException {
		// TODO Auto-generated method stub
		String nickName = ObjUtils.objToStr(params.get("nickName"));
		String sex = ObjUtils.objToStr(params.get("sex"));
		String birthday = ObjUtils.objToStr(params.get("birthday"));
		String job = ObjUtils.objToStr(params.get("job"));
		String telephone = ObjUtils.objToStr(params.get("telephone"));
		String e_mail = ObjUtils.objToStr(params.get("e_mail"));
		String query = "select * from dbo.[user] where " + ObjUtils.objToStr(params.get("nameKey")) + "='" + ObjUtils.objToStr(params.get("name"))+"'";
		ResultSet rs = sql.executeQuery(query);
		if (!rs.next()) {
			ResponseUtil.response(response, "用户不存在", false);
			return;
		}
		String s = "update dbo.[user] set "
				+ "nickName='"+nickName+"',"
				+ "sex='"+sex+"',"
				+ "birthday='"+birthday+"',"
				+ "job='"+job+"',"
				+ "telephone='"+telephone+"',"
				+ "e_mail='"+e_mail+"' "
				+ "where " + ObjUtils.objToStr(params.get("nameKey")) + "='"+ObjUtils.objToStr(params.get("name"))+"'";
		System.out.println("执行sql语句:"+s);
		sql.execute(s); 
		ResponseUtil.response(response, "修改成功");
	}

}
