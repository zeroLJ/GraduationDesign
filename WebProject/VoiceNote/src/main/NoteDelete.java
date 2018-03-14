package main;

import java.io.File;
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
		System.out.println("ִ��sql���:"+s);
		sql.execute(s); 
		String filepath = "C:\\VoiceNote\\" + name + "\\" 
		+ DateUtils.getFileNameByDate(DateUtils.StringDateTime(addTime));
		
		ResponseUtil.response(response, "ɾ���ɹ�");
	}

	private void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();//��Ҫ�����ļ��У�ֻɾ���ļ�����ע������
        } else if (file.exists()) {
            file.delete();
        }
    }
}
