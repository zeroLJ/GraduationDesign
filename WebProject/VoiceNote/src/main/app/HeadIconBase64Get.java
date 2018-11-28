package main.app;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import datasourse.DBUtils;
import main.ResponseParams;
import main.User;
import main.base.BaseServlet;
import main.util.ImageUtil;
import main.util.ObjUtils;

@WebServlet("/HeadIconBase64Get")
public class HeadIconBase64Get extends BaseServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public ResponseParams doSQL(Map<String, String> params, DBUtils db, User user) {
		String filepath = "C:\\VoiceNote\\" + ObjUtils.objToStr(params.get("name")) + "\\icon.jpg";
		File file = new File(filepath);
		if (!file.exists()) {
			file = new File( "C:\\VoiceNote\\icon.png");
		}
		boolean hasFile = true;
		if (!file.exists()) {
			file = null;
			hasFile = false;
		}
		String nickname = ObjUtils.objToStr(user.getNickName());
		if (nickname.equals("")) {
			nickname = ObjUtils.objToStr(params.get("name"));
		}
		System.out.println("nickname:" + nickname);

		Map<String, Object> map = new HashMap<>();
		map.put("nickname", nickname);
		map.put("hasFile", hasFile);
		map.put("file", ImageUtil.getImageStr(file.getAbsolutePath()));
		// ResponseUtil.responseFile(response, null, map, nickname, file);
		ResponseParams responseParams = new ResponseParams();
		responseParams.resultMap = map;
		responseParams.msg = nickname;
		return responseParams;
	}

}
