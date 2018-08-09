package main.servlet;

import java.io.File;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import datasourse.DBUtils;
import main.ResponseParams;
import main.User;
import main.util.ObjUtils;

@WebServlet("/HeadIconChange")
public class HeadIconChange extends BaseServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public ResponseParams doSQL(Map<String, String> params, DBUtils db, User user) {
		// TODO Auto-generated method stub
		if (params.get("file") != null && !params.get("file").equals("")) {
			File file = new File(params.get("file"));
			System.out.println("文件从:" + file.getAbsolutePath());
			File toFile = new File("C:\\VoiceNote\\" + ObjUtils.objToStr(params.get("name")) + "\\icon.jpg");
			toFile.getParentFile().mkdirs();
			if (toFile.exists()) {
				toFile.delete();
			}
			file.renameTo(toFile);
			System.out.println("文件到:" + toFile.getAbsolutePath());
			return ResponseParams.successResult();
		} else {
			return ResponseParams.failResult("头像更改失败");
			// ResponseUtil.response(response, "头像更改失败",false);
		}
	}

}
