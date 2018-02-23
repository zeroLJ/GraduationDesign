package main;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.xpath.internal.operations.And;

@WebServlet("/NoteDelete")
public class NoteDelete extends BaseServlet{

	@Override
	void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params)
			throws SQLException, IOException {
		// TODO Auto-generated method stub
		Map<String, Object> map = JSON.parseObject(params.get("data"),Map.class);
		String addTime = ObjUtils.objToStr(map.get("addTime"));
		String s = "delete from dbo.[note] where addTime='"+addTime+"' and name='"+name+"'";
		System.out.println("Ö´ÐÐsqlÓï¾ä:"+s);
		sql.execute(s); 
		ResponseUtil.response(response, "É¾³ý³É¹¦");
	}

}
