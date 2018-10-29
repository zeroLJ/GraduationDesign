package main;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * ������������������У�������Ƿ���ȷ
 * ����ServletУ�����MainServlet�������ﲻ����У�飬ʵ��Ҳ����ֱ���ڴ�У�鲢ͨ��������ʵ���ӦServlet�ķ���
 */
//@WebFilter("/MainFilter")
//΢��URL��֤��ʱ�����ע�͵������������������أ�������֤�ɹ���ԭ����
//@WebFilter(filterName="MainFilter",urlPatterns={"/*"})//Filter���ƺ͹��˷�Χ���ã�ͨ��ע�����þͲ�����web.xml��������
public class MainFilter implements Filter {

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
     * Default constructor. 
     */
    public MainFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		// pass the request along the filter chain
		HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String currentURL=request.getRequestURI();
        String ctxPath = request.getContextPath();
		System.out.println("fliter");
        System.out.println(currentURL);
        System.out.println(ctxPath);
        chain.doFilter(request, response);

        //ServletУ�����BaseServlet����
       
        /*if (!currentURL.contains("main")) {
			 return;
		}
        Class<?> aClass;
        Method method;
        try {
			aClass = Class.forName("main.InfoGet");
			method = aClass.getDeclaredMethod("doSQL",  HttpServletRequest.class, 
					HttpServletResponse.class,Statement.class, Map.class);
			System.out.println("��������");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("������");
			return;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("����������");
			return;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("�޷�����");
			return;
		}
        
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
//					params.put(key, map.get(key)[0]);
					params.put(key,URLDecoder.decode(map.get(key)[0],"utf-8"));
				}
			} 
			System.out.println("�����б�"+params.toString());
			
		    stmt = con.createStatement();
	        name = ObjUtils.objToStr(params.get("name"));
	  
	        password = ObjUtils.objToStr(params.get("password"));
	        System.out.println("name:"+name+" password:"+password);
	        if (getClass().isAssignableFrom(Login.class) || getClass().isAssignableFrom(SigninOther.class)) {
				method.invoke(aClass.newInstance(),request, response, stmt, params);
				return;
			}
	        if (name.endsWith("_qq")) {
				nameKey = "name_qq";
			}else if(name.endsWith("_sina")) {
				nameKey = "name_sina";
			}else {
				nameKey = "name";
			}	        
	        ResultSet rs = stmt.executeQuery("select * from dbo.[user] where "+ nameKey + "='"+ name +"' and password='" + password + "'");
	        if (rs.next()) {
	        	System.out.println("�д��û�");
	        	params.put("nickname", rs.getString("nickname"));
	        	method.invoke(aClass.newInstance(),request, response, stmt, params);
			}else {
				ResponseUtil.response(response, "�û�����������󣡣�", false);
			}      
		} catch (ClassNotFoundException e) {
			ResponseUtil.response(response, e.getMessage(), false);
			e.printStackTrace();
			return;
		} catch (SQLException e) {
			ResponseUtil.response(response, e.getMessage(), false);
			e.printStackTrace();
			return;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}*/
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
