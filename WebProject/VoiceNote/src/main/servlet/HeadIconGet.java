package main.servlet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import datasourse.DBUtils;
import main.ResponseParams;
import main.User;
import main.util.ObjUtils;

@WebServlet("/HeadIconGet")
public class HeadIconGet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public ResponseParams doSQL(Map<String, String> params, DBUtils db, User user) {
		String filepath = "C:\\VoiceNote\\" + ObjUtils.objToStr(params.get("name")) + "\\icon.jpg";
		File file = new File(filepath);
		boolean hasFile = true;
		if (!file.exists()) {
			file = null;
			hasFile = false;
		}
		String nickname = ObjUtils.objToStr(params.get("nickname"));
		if (nickname.equals("")) {
			nickname = ObjUtils.objToStr(params.get("name"));
		}
		System.out.println("nickname:" + nickname);

		Map<String, Object> map = new HashMap<>();
		map.put("nickname", nickname);
		map.put("hasFile", hasFile);
		// ResponseUtil.responseFile(response, null, map, nickname, file);
		ResponseParams responseParams = new ResponseParams();
		responseParams.file = file;
		responseParams.resultMap = map;
		responseParams.msg = nickname;
		return responseParams;
	}

}
