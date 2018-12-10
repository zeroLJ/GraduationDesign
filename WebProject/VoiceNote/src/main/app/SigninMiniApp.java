package main.app;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import com.alibaba.fastjson.JSON;

import database.entity.UserT;
import database.query.UserTQuery;
import datasourse.DBUtils;
import main.ResponseParams;
import main.User;
import main.base.BaseNoSigninServlet;
import main.util.HttpRequest;
import main.util.ObjUtils;
import main.util.StringUtils;

/**
 * 微信小程序专用登录接口
 * @author ljl
 *
 */
@WebServlet("/SigninMiniApp")
public class SigninMiniApp extends BaseNoSigninServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public ResponseParams doSQL(Map<String, String> params, DBUtils db, User user) {
		String code = ObjUtils.objToStr(params.get("code"));
		if (StringUtils.isEmpty(code)) {
			return ResponseParams.failResult("参数code不能为空");
		}
		Map<String, String> map = new HashMap<>();
		map.put("appid", "wx8c8a0f220cd8fc1c");
		map.put("secret", "3eca0497e8580a950070a1913bc12fa7");
		map.put("js_code", code);
		map.put("grant_type", "authorization_code");
		String result = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", map);
		System.out.println("result:"+ result);
		Map<String, Object> rMap;
		try {
			rMap = JSON.parseObject(result,Map.class);
		} catch (Exception e) {
			return ResponseParams.failResult("jscode2session返回结果解析失败");
		}
		System.out.println(rMap.toString());
		String userName = ObjUtils.objToStr(rMap.get("openid")) + "_mini";
		UserTQuery query = new UserTQuery();
		query.Field_Name_mini().setIs(userName);
		List<Map<String, Object>> list = db.queryList(query);
		if (list.isEmpty()) {
			if (StringUtils.isNotEmpty(params.get("iconUrl"))) {
				File file = HttpRequest.downloadFile(params.get("iconUrl"), new File("C:\\VoiceNote\\" + userName + "\\icon_temp.jpg"));
				if (file!=null) {
					System.out.println("文件从:" + file.getAbsolutePath());
					File toFile = new File("C:\\VoiceNote\\" + userName + "\\icon.jpg");
					toFile.getParentFile().mkdirs();
					if (toFile.exists()) {
						toFile.delete();
					}
					file.renameTo(toFile);
					System.out.println("文件到:" + toFile.getAbsolutePath());
				}
			}
			UserT userT = new UserT();
			userT.Field_Password().setValue(userName);
			userT.Field_Nickname().setValue(user.getNickName());
			userT.Field_Name_mini().setValue(userName);
			int sex = ObjUtils.objToInt(params.get("gender"));
			if (sex == 1) {
				userT.Field_Sex().setValue("男");
			}else if(sex==2){
				userT.Field_Sex().setValue("女");
			}
			if (db.saveToDB(userT)) {
				rMap.put("newUser", "true");
				rMap.put("nickname", user.getNickName());
				return ResponseParams.successResultMap(rMap);
			}else {
				return ResponseParams.failResult("登录失败");
			}
		}
		rMap.put("nickname", ObjUtils.objToStr(list.get(0).get("nickname")));
		return ResponseParams.successResultMap(rMap);
	}
}
