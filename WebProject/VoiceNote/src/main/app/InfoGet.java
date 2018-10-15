package main.app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import database.entity.UserT;
import database.query.UserTQuery;
import datasourse.DBUtils;
import main.ResponseParams;
import main.User;
import main.base.BaseServlet;

@WebServlet("/InfoGet")
public class InfoGet extends BaseServlet{
	private static final long serialVersionUID = 1L;

	@Override
	public ResponseParams doSQL(Map<String, String> params, DBUtils db, User user) {
		String namekey = "name";
		if (params.containsKey("nameKey")) {
			namekey = params.get("nameKey");
		}
		UserTQuery query = new UserTQuery();
//        query.Field_Password().setIs(user.getPassword());
        if (user.getUserName().endsWith("_qq")) {
			query.Field_Name_qq().setIs(user.getUserName());
		}else if(user.getUserName().endsWith("_sina")) {
			query.Field_Name_sina().setIs(user.getUserName());
		}else if(user.getUserName().endsWith("_mini")) {
			query.Field_Name_mini().setIs(user.getUserName());
		}else {
			query.Field_Name().setIs(user.getUserName());
		}	
        List<UserT> list =db.queryEntity(query);
		if (list.size() <= 0) {
			return ResponseParams.failResult("用户不存在");
		}
		List<Map<String, Object>> mList = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
//		String nickname = ObjUtils.objToStr(rs.getString("nickname"));
//		if (nickname.equals("")) {
//			nickname =  ObjUtils.objToStr(params.get("name"));
//		}
		map.put("nickname", user.getNickName());
		map.put("sex", list.get(0).Field_Sex().getValue());
		Date date = list.get(0).Field_Birthday().getValue();
//		String birthday = "";
		if (date!=null) {
		    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			map.put("birthday", format.format(date));
		}
		map.put("job", list.get(0).Field_Job().getValue());
		map.put("telephone", list.get(0).Field_Telephone().getValue());
		map.put("e_mail",  list.get(0).Field_E_mail().getValue());
        mList.add(map);
		return ResponseParams.successResultList(mList);
	}

}
