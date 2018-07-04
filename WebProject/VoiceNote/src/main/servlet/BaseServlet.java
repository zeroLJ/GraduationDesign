package main.servlet;

import java.io.File;
import java.io.IOException;
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import main.util.ObjUtils;
import main.util.ResponseUtil;




/**
 * Servlet implementation class BaseServlet
 */
//@WebServlet("/BaseServlet")//�˲�����������Ŀ�б���Ψһ����������ʧ��
public abstract class BaseServlet extends HttpServlet {
	//���������������
	protected static final long serialVersionUID = 1L;
	protected String connectionUrl = 
    		"jdbc:sqlserver://localhost:1433;" 
               +"databaseName=demo;"
               + "user=ljl;"
               + "password=pp123456;";   

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
		String name;
		String password;
		String nameKey = "name";
		Connection con;
		Statement stmt;
		Map<String, String> params = new HashMap<>();
		System.out.println("Served at: "+request.getContextPath() + "  Class:" + getClass().getName());
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
//	                            File saveFile = new File(System.getProperty("user.dir"), fullFile.getName()); 
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
	        
	        if (getClass().isAssignableFrom(Login.class) || getClass().isAssignableFrom(SigninOther.class)) {
				doSQL(request, response, stmt, params);
				return;
			}
	               
//	        System.out.println(nameKey);
	        ResultSet rs = stmt.executeQuery("select * from dbo.[user] where "+ nameKey + "='"+ name +"' and password='" + password + "'");
	        if (rs.next()) {
	        	System.out.println("�д��û�");
	        	params.put("nickname", rs.getString("nickname"));
	        	doSQL(request, response, stmt, params);
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
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public abstract void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params) throws  SQLException, IOException;
}
