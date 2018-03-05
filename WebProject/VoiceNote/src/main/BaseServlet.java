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
//@WebServlet("/BaseServlet")//�˲�����������Ŀ�б���Ψһ����������ʧ��
public abstract class BaseServlet extends HttpServlet {
	//���������������
	protected static final long serialVersionUID = 1L;
	protected String connectionUrl = 
    		"jdbc:sqlserver://localhost:1433;" 
//    		"jdbc:sqlserver://192.168.0.188:1433;"
               +"databaseName=Demo;"
               + "user=940034240;"
               + "password=pp123456;";   
	protected String name;
	protected String password;
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
	                            File file = new File("D:\\VoiceNote\\"+strings[0]);
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
	            }  
	        }else {
	        	Map<String, String[]> map =  request.getParameterMap();
				for(String key : map.keySet()) {
					params.put(key, map.get(key)[0]);
				}
			} 
			System.out.println("�����б�"+params.toString());
			
		    stmt = con.createStatement();
	        name = params.get("name");
	        password = params.get("password");
	        System.out.println("name:"+name+" password:"+password);
	        if (getClass().isAssignableFrom(Login.class)) {
				doSQL(request, response, stmt, params);
				return;
			}
	        ResultSet rs = stmt.executeQuery("select * from dbo.[user] where name='"+ name +"' and password='" + password + "'");
	        if (rs.next()) {
	        	System.out.println("�д��û�");
	        	doSQL(request, response, stmt, params);
			}else {
				ResponseUtil.response(response, "�û�����������󣡣�", false);
			}      
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	abstract void doSQL(HttpServletRequest request, HttpServletResponse response, Statement sql, Map<String, String> params) throws  SQLException, IOException;

	
	
	
	/**

     * �ֽ������е�indexOf��������String���е�indexOf����

     * b Ҫ�������ֽ�����

     * s Ҫ�ҵ��ַ���

     * start ��������ʼλ��

     * ����ҵ�������s�ĵ�һ���ֽ���b�е��±꣬û���򷵻�-1

     */

    private static int byteIndexOf(byte[] b, String s, int start) {

            return byteIndexOf(b, s.getBytes(), start);

    }

    /**

     * �ֽ������е�indexOf��������String���е�indexOf����

     * b Ҫ�������ֽ�����

     * s Ҫ�ҵ��ֽ�����

     * start ��������ʼλ��

     * ����ҵ�������s�ĵ�һ���ֽ���b�е��±꣬û���򷵻�-1

     */

    private static int byteIndexOf(byte[] b, byte[] s, int start) {

            int i;

            if (s.length == 0) {

                    return 0;

            }

            int max = b.length - s.length;

            if (max < 0) {

                    return -1;

            }

            if (start > max) {

                    return -1;

            }

            if (start < 0) {

                    start = 0;

            }

            // ��b���ҵ�s�ĵ�һ��Ԫ��

    search:

            for (i = start; i <= max; i++) {

                    if (b[i] == s[0]) {

                            // �ҵ���s�еĵ�һ��Ԫ�غ󣬱Ƚ�ʣ��Ĳ����Ƿ����

                            int k = 1;

                            while (k < s.length) {

                                    if (b[k + i] != s[k]) {

                                            continue search;

                                    }

                                    k++;

                            }

                            return i;

                    }

            }

            return -1;

    }

    /**

     * ���ڴ�һ���ֽ���������ȡһ���ֽ�����

     * ������String ���substring()

     */

    private static byte[] subBytes(byte[] b, int from ,int end) {

            byte[] result = new byte[end - from];

            System.arraycopy(b, from, result, 0, end - from);

            return result;

    }

    

    /**

     * ���ڴ�һ���ֽ���������ȡһ���ַ���

     * ������String���substring()

     */

    private static String subBytesString(byte[] b, int from, int end) {

            return new String(subBytes(b, from, end));

    }
}
