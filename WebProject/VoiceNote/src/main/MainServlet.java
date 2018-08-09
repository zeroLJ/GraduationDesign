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

//@WebServlet("/MainServlet")//�˲�����������Ŀ�б���Ψһ����������ʧ��


/**
 * ����.doΪ��׺�����󶼻���ʴ�Servlet����ͨ�������ҵ���ӦServlet
 * ���磬����main.servlet.InfoGet����ʹ��http://localhost:8081/VoiceNote/main/servlet/InfoGet.do ��ͨ�����䣩
 * ��http://localhost:8081/VoiceNote/InfoGet(Servlet���õ�·��)
 */
//ͨ��ע������Servlet���ƺͲ���·����������web.xml����
@WebServlet(name="MainServlet",urlPatterns={"*.do"},
		//Ĭ�ϲ�����ͨ��getInitParameter()������ȡ����ע��ͨ��������ʵ�Servlet�޷���ȡ��
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
        String className = currentURL.substring(ctxPath.length()+1);//ȥ������ǰ׺
        if (className.contains(".")) {
        	className = className.substring(0,className.lastIndexOf("."));//ȥ����׺
		}
        className = className.replaceAll("/", ".");//ת����class������
        System.out.println("class:"+className);
        className = getInitParameter("path") + className;
        System.out.println("path:"+className);
        try {
			aClass = Class.forName(className);
			method = aClass.getDeclaredMethod("doSQL",  HttpServletRequest.class, 
					HttpServletResponse.class,Statement.class, Map.class);
			System.out.println("��������");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("�Ҳ���"+className);
			return;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			System.out.println("����������");
			return;
		} catch (SecurityException e) {
			e.printStackTrace();
			System.out.println("�޷�����");
			return;
		}
        Object classObj;
        try {
			classObj = aClass.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
			System.out.println(className+"��ʼ������");
			return;
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println(className+"��ʼ������");
			return;
		}
		
		String uploadFileName = ""; // �ϴ����ļ���  
        String fieldName = ""; // ���ֶ�Ԫ�ص�name����ֵ  
        
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			request.setCharacterEncoding("UTF-8"); 
			con = DriverManager.getConnection(connectionUrl);
			
			// ������Ϣ�е������Ƿ���multipart����  
	        boolean isMultipart = ServletFileUpload.isMultipartContent(request);  
	        if (isMultipart) {  
	            FileItemFactory factory = new DiskFileItemFactory();
	            ServletFileUpload upload = new ServletFileUpload(factory);  
	            try {  
	                // ����form���������ļ�  
	                @SuppressWarnings("unchecked")
					List<FileItem> items = upload.parseRequest(request);  
	                Iterator<FileItem> iter = items.iterator();  
	                while (iter.hasNext()) { // ���δ���ÿ���ļ�  
	                    FileItem item = (FileItem) iter.next();  
	                    if (item.isFormField()) { // ��ͨ���ֶ�  
	                        fieldName = item.getFieldName(); // ���ֶε�name����ֵ  
	                        params.put(fieldName, item.getString("UTF-8"));
	                        if (fieldName.equals("name")) {  
	                            // ������ֶε�ֵ  
	                            System.out.println(item.getString("UTF-8") + "�ϴ����ļ�.");  
	                        }  
	                    } else { // �ļ����ֶ�  
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
	                            params.put(strings[1], saveFile.getAbsolutePath());//�����ļ�·���Ա�ʹ��
	                            System.out.println("�ϴ����ļ�����:" + uploadFileName + " �洢·����" + saveFile.getAbsolutePath());  
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
			System.out.println("�����б�"+params.toString());
			
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
	        	System.out.println("�д��û�");
	        	params.put("nickname", rs.getString("nickname"));
	        	method.invoke(classObj,request, response, stmt, params);
			}else {
				ResponseUtil.response(response, "�û�����������󣡣�", false);
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
			// ���÷���ʧ��
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
