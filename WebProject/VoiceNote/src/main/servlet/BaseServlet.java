package main.servlet;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import database.query.UserTQuery;
import datasourse.DBUtils;
import main.ResponseParams;
import main.User;
import main.util.Logs;
import main.util.ObjUtils;
import main.util.ResponseUtil;




/**
 * Servlet implementation class BaseServlet
 */
//@WebServlet("/BaseServlet")//此参数在整个项目中必须唯一，否则运行失败
public abstract class BaseServlet extends HttpServlet {
	//这个变量必须设置
	protected static final long serialVersionUID = 1L;

    public BaseServlet() {
        super();
    }
    
    private ResponseParams exec(HttpServletRequest request, HttpServletResponse response, DBUtils dbUtils) throws IOException{
    	String name;
		String password;
		String nameKey = "name";
		Map<String, String> params = new HashMap<>();
		System.out.println("Served at: "+request.getContextPath() + "  Class:" + getClass().getName());
        String uploadFileName = ""; // 上传的文件名  
        String fieldName = ""; // 表单字段元素的name属性值  
        List<File> files = new ArrayList<>();//用户上传的文件列表
        request.setCharacterEncoding("UTF-8"); 
        
		// 请求信息中的内容是否是multipart类型  
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);  
        if (isMultipart) {  
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);  
            try {  
                // 解析form表单中所有文件  
                @SuppressWarnings("unchecked")
				List<FileItem> items = upload.parseRequest(request);  
                Iterator<FileItem> iter = items.iterator();  
                while (iter.hasNext()) { // 依次处理每个文件  
                    FileItem item = (FileItem) iter.next();  
                    if (item.isFormField()) { // 普通表单字段  
                        fieldName = item.getFieldName(); // 表单字段的name属性值  
                        params.put(fieldName, item.getString("UTF-8"));
                        if (fieldName.equals("name")) {  
                            // 输出表单字段的值  
                            System.out.println(item.getString("UTF-8") + "上传了文件.");  
                        }  
                    } else { // 文件表单字段  
                    	fieldName = item.getFieldName();
                    	uploadFileName = item.getName();  
                        if (uploadFileName != null && !uploadFileName.equals("")) {  
                            File file = new File("C:\\" + request.getContextPath() + "\\temp\\"+System.currentTimeMillis());
                            if (!file.exists()) {
                    			file.mkdirs();
                    		}
                            File saveFile = new File(file.getAbsolutePath(), uploadFileName);  
                            item.write(saveFile);  
                            files.add(saveFile);
//                            params.put(fieldName, saveFile.getAbsolutePath());//保存文件路径以备使用
                            System.out.println("上传文件:" + uploadFileName + " 存储路径：" + saveFile.getAbsolutePath());  
                        }  
                    }  
                }  
                dbUtils.setUploadFiles(files);
            } catch (Exception e) {  
                e.printStackTrace();  
                return ResponseParams.failResult(e.getMessage());
            }  
        }else {
        	Map<String, String[]> map =  request.getParameterMap();
			for(String key : map.keySet()) {
//				params.put(key, map.get(key)[0]);
				params.put(key,URLDecoder.decode(map.get(key)[0],"utf-8"));
			}
		} 
		System.out.println("参数列表："+params.toString());
		
        name = ObjUtils.objToStr(params.get("name"));
        password = ObjUtils.objToStr(params.get("password"));
        System.out.println("name:"+name+" password:"+password);
        if (name.endsWith("_qq")) {
			nameKey = "name_qq";
		}else if(name.endsWith("_sina")) {
			nameKey = "name_sina";
		}else {
			nameKey = "name";
		}	
    	params.put("nameKey", nameKey);
    	
        if (getClass().isAssignableFrom(Login.class) || getClass().isAssignableFrom(SigninOther.class)) {
			return doSQL(params, dbUtils, new User(name, "", password));
		}
               
        UserTQuery query = new UserTQuery();
        query.Field_Password().setIs(password);
        if (name.endsWith("_qq")) {
			query.Field_Name_qq().setIs(name);
		}else if(name.endsWith("_sina")) {
			query.Field_Name_sina().setIs(name);
		}else {
			query.Field_Name().setIs(name);
		}	
        List<Map<String, Object>> list = dbUtils.queryList(query);
        if (list.size() > 0) {
        	System.out.println("有此用户");
        	User user = new User(name, ObjUtils.objToStr(list.get(0).get("nickname")), password);
        	return doSQL(params, dbUtils, user);
		}else {
			return ResponseParams.failResult("用户名或密码错误！！");
		}  
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBUtils dbUtils = new DBUtils();
		try {
			ResponseParams responseParams = exec(request, response, dbUtils);
			ResponseUtil.response(response, responseParams.resultList, responseParams.resultMap, responseParams.msg, responseParams.file , responseParams.success); 
		} catch (RuntimeException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			ResponseUtil.response(response, null, null, e.getMessage(), null, false);
		}finally {
			dbUtils.destory();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public abstract ResponseParams doSQL(Map<String, String> params, DBUtils db, User user);
}
