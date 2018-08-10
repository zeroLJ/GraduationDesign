package main.servlet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import database.entity.UserT;
import database.query.UserTQuery;
import datasourse.DBUtils;
import main.ResponseParams;
import main.User;
import main.util.ObjUtils;

/**
 * ��������¼�ӿ�
 * 
 * @author ljl
 *
 */
@WebServlet("/SigninOther")
public class SigninOther extends BaseServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public ResponseParams doSQL(Map<String, String> params, DBUtils db, User user) {
		UserTQuery query = new UserTQuery();
		if (user.getUserName().endsWith("_qq")) {
			query.Field_Name_qq().setIs(user.getUserName());
		}else if (user.getUserName().endsWith("_sina")) {
			query.Field_Name_sina().setIs(user.getUserName());
		}
		if (db.queryList(query).isEmpty()) {
			if (params.get("file") != null && !params.get("file").equals("")) {
				File file = new File(params.get("file"));
				System.out.println("�ļ���:" + file.getAbsolutePath());
				File toFile = new File("C:\\VoiceNote\\" + ObjUtils.objToStr(params.get("name")) + "\\icon.jpg");
				toFile.getParentFile().mkdirs();
				if (toFile.exists()) {
					toFile.delete();
				}
				file.renameTo(toFile);
				System.out.println("�ļ���:" + toFile.getAbsolutePath());
			}
			UserT userT = new UserT();
			userT.Field_Password().setValue(user.getPassword());
			userT.Field_Nickname().setValue(user.getNickName());
			if (user.getUserName().endsWith("_qq")) {
				userT.Field_Name_qq().setValue(user.getUserName());
			}else if (user.getUserName().endsWith("_sina")) {
				userT.Field_Name_sina().setValue(user.getUserName());
			}
			if (db.saveToDB(userT)) {
				Map<String, Object> map = new HashMap<>();
				map.put("newUser", "true");
//				ResponseUtil.response(response, null, map, "ע��ɹ�����", true);
				return ResponseParams.successResultMap(map);
			}else {
				return ResponseParams.failResult("��¼ʧ��");
			}
		}
//		ResponseUtil.response(response, "��¼�ɹ�����", true);
		return ResponseParams.successResult();
	}
}
