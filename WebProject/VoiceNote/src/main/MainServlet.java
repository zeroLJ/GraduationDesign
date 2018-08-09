package main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import datasourse.DBUtils;
import main.servlet.BaseServlet;
import main.servlet.Login;
import main.servlet.SigninOther;
import main.util.ObjUtils;
import main.util.ResponseUtil;

//@WebServlet("/MainServlet")//此参数在整个项目中必须唯一，否则运行失败


/**
 * 所有.do为后缀的请求都会访问此Servlet，再通过反射找到对应Servlet
 * 比如，访问main.servlet.InfoGet可以使用http://localhost:8081/VoiceNote/main/servlet/InfoGet.do （通过反射）
 * 或http://localhost:8081/VoiceNote/InfoGet(Servlet配置的路径)
 */
//通过注解配置Servlet名称和部署路径，不需在web.xml配置
@WebServlet(name="MainServlet",urlPatterns={"*.do"},
		//默认参数，通过getInitParameter()方法获取，但注意通过反射访问的Servlet无法获取到
		initParams= {@WebInitParam(name = "path", value = "")})
public class MainServlet extends BaseServlet{
	private static final long serialVersionUID = 1L;
	protected String connectionUrl = 
    		"jdbc:sqlserver://localhost:1433;" 
               +"databaseName=demo;"
               + "user=ljl;"
               + "password=pp123456;";   
	protected String name;
	protected String password;
	protected String nameKey = "name";
	protected Connection con;
	protected Statement stmt;
	protected Map<String, String> params = new HashMap<>();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Class<?> aClass;
        Method method;
        String currentURL=request.getRequestURI();
        String ctxPath = request.getContextPath();
        String className = currentURL.substring(ctxPath.length()+1);//去掉无用前缀
        if (className.contains(".")) {
        	className = className.substring(0,className.lastIndexOf("."));//去掉后缀
		}
        className = className.replaceAll("/", ".");//转化成class的名称
        System.out.println("class:"+className);
        className = getInitParameter("path") + className;
        System.out.println("path:"+className);
        try {
			aClass = Class.forName(className);
			method = aClass.getDeclaredMethod("doSQL",  HttpServletRequest.class, 
					HttpServletResponse.class,Statement.class, Map.class);
			System.out.println("方法存在");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("找不到"+className);
			return;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			System.out.println("方法不存在");
			return;
		} catch (SecurityException e) {
			e.printStackTrace();
			System.out.println("无法访问");
			return;
		}
        Object classObj;
        try {
			classObj = aClass.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
			System.out.println(className+"初始化错误");
			return;
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println(className+"初始化错误！");
			return;
		}
		
		String uploadFileName = ""; // 上传的文件名  
        String fieldName = ""; // 表单字段元素的name属性值  
        
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			request.setCharacterEncoding("UTF-8"); 
			con = DriverManager.getConnection(connectionUrl);
			
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
	                        String fileName = item.getName();  
	                        if (fileName != null && !fileName.equals("")) {  
	                            File fullFile = new File(item.getName());  
//		                            File saveFile = new File(System.getProperty("user.dir"), fullFile.getName()); 
	                            String[] strings = item.getFieldName().split("_0_");// userName + _0_ + key
	                            File file = new File("C:\\VoiceNote\\"+strings[0]);
	                            if (!file.exists()) {
									file.mkdirs();
								}
	                            File saveFile = new File(file.getAbsolutePath(), fileName);  
	                            item.write(saveFile);  
	                            uploadFileName = fullFile.getName();  
	                            params.put(strings[1], saveFile.getAbsolutePath());//保存文件路径以备使用
	                            System.out.println("上传的文件名是:" + uploadFileName + " 存储路径：" + saveFile.getAbsolutePath());  
	                        }  
	                    }  
	                }  
	            } catch (Exception e) {  
	                e.printStackTrace();  
	                ResponseUtil.response(response, e.getMessage(), false);
	                return;
	            }  
	        }else {
	        	Map<String, String[]> map =  request.getParameterMap();
				for(String key : map.keySet()) {
					params.put(key, map.get(key)[0]);
				}
			} 
			System.out.println("参数列表："+params.toString());
			
		    stmt = con.createStatement();
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
	        if (aClass.isAssignableFrom(Login.class) || aClass.isAssignableFrom(SigninOther.class)) {
	        	method.invoke(classObj,request, response, stmt, params);
				return;
			}
	        ResultSet rs = stmt.executeQuery("select * from dbo.[user] where "+ nameKey + "='"+ name +"' and password='" + password + "'");
	        if (rs.next()) {
	        	System.out.println("有此用户");
	        	params.put("nickname", rs.getString("nickname"));
	        	method.invoke(classObj,request, response, stmt, params);
			}else {
				ResponseUtil.response(response, "用户名或密码错误！！", false);
			}      
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			ResponseUtil.response(response, e.getMessage(), false);
			e.printStackTrace();
			return;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			ResponseUtil.response(response, e.getMessage(), false);
			e.printStackTrace();
			return;
		} catch (IllegalAccessException e) {
			// 调用方法失败
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	@Override
	public ResponseParams doSQL(Map<String, String> params, DBUtils db, User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
