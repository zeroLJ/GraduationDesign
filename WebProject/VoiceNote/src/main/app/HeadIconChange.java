package main.app;

import java.io.File;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import datasourse.DBUtils;
import main.ResponseParams;
import main.User;
import main.base.BaseServlet;
import main.util.ObjUtils;

@WebServlet("/HeadIconChange")
public class HeadIconChange extends BaseServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public ResponseParams doSQL(Map<String, String> params, DBUtils db, User user) {
		if (!db.getUploadFiles().isEmpty()) {
			File file = db.getUploadFiles().get(0);
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
