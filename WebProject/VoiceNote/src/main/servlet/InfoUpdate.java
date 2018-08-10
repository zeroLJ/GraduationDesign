package main.servlet;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import database.entity.UserT;
import database.query.UserTQuery;
import datasourse.DBUtils;
import main.ResponseParams;
import main.User;
import main.util.ObjUtils;

@WebServlet("/InfoUpdate")
public class InfoUpdate extends BaseServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public ResponseParams doSQL(Map<String, String> params, DBUtils db, User user) {
		String nickName = ObjUtils.objToStr(params.get("nickName"));
		String sex = ObjUtils.objToStr(params.get("sex"));
		Date birthday = ObjUtils.objToDate(params.get("birthday"));
		String job = ObjUtils.objToStr(params.get("job"));
		String telephone = ObjUtils.objToStr(params.get("telephone"));
		String e_mail = ObjUtils.objToStr(params.get("e_mail"));
		UserTQuery query = new UserTQuery();
		// query.Field_Password().setIs(user.getPassword());
		if (user.getUserName().endsWith("_qq")) {
			query.Field_Name_qq().setIs(user.getUserName());
		} else if (user.getUserName().endsWith("_sina")) {
			query.Field_Name_sina().setIs(user.getUserName());
		} else {
			query.Field_Name().setIs(user.getUserName());
		}
		List<UserT> list = db.queryEntity(query);
		if (list.size() <= 0) {
			return ResponseParams.failResult("用户不存在");
		}
		list.get(0).Field_Nickname().setValue(nickName);
		list.get(0).Field_E_mail().setValue(e_mail);
		list.get(0).Field_Job().setValue(job);
		list.get(0).Field_Telephone().setValue(telephone);
		list.get(0).Field_Sex().setValue(sex);
		list.get(0).Field_Birthday().setValue(birthday);
		if (db.saveToDB(list)) {
			return ResponseParams.successResult();
		}else {
			return ResponseParams.failResult("修改失败");
		}	
	}

}
