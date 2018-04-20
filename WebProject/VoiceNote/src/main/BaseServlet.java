package main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.RequestContext;




/**
 * Servlet implementation class BaseServlet
 */
//@WebServlet("/BaseServlet")//此参数在整个项目中必须唯一，否则运行失败
public abstract class BaseServlet extends HttpServlet {
	//这个变量必须设置
	protected static final long serialVersionUID = 1L;
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

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BaseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("Served at: "+request.getContextPath() + "  Class:" + getClass().getName());
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
//	                            File saveFile = new File(System.getProperty("user.dir"), fullFile.getName()); 
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
	        name = params.get("name");
	        password = params.get("password");
	        System.out.println("name:"+name+" password:"+password);
	        if (getClass().isAssignableFrom(Login.class) || getClass().isAssignableFrom(SigninOther.class)) {
				doSQL(request, response, stmt, params);
				return;
			}
	        
	        
	        if (name.endsWith("_qq")) {
				nameKey = "name_qq";
			}else if(name.endsWith("_sina")) {
				nameKey = "name_sina";
			}	        
	        
	        ResultSet rs = stmt.executeQuery("select * from dbo.[user] where "+ nameKey + "='"+ name +"' and password='" + password + "'");
	        if (rs.next()) {
	        	System.out.println("有此用户");
	        	doSQL(request, response, stmt, params);
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
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	abstract void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params) throws  SQLException, IOException;

}
