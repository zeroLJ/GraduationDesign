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
 * 过滤器，可以在这里校验请求是否正确
 * 由于Servlet校验放在MainServlet处理，这里不进行校验，实际也可以直接在此校验并通过反射访问到对应Servlet的方法
 */
//@WebFilter("/MainFilter")
//微信URL验证的时候必须注释掉（不能有拦截器拦截）才能验证成功，原因不明
//@WebFilter(filterName="MainFilter",urlPatterns={"/*"})//Filter名称和过滤范围设置，通过注解设置就不用在web.xml进行配置
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

        //Servlet校验放在BaseServlet处理
       
        /*if (!currentURL.contains("main")) {
			 return;
		}
        Class<?> aClass;
        Method method;
        try {
			aClass = Class.forName("main.InfoGet");
			method = aClass.getDeclaredMethod("doSQL",  HttpServletRequest.class, 
					HttpServletResponse.class,Statement.class, Map.class);
			System.out.println("方法存在");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("不存在");
			return;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("方法不存在");
			return;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("无法访问");
			return;
		}
        
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
//					params.put(key, map.get(key)[0]);
					params.put(key,URLDecoder.decode(map.get(key)[0],"utf-8"));
				}
			} 
			System.out.println("参数列表："+params.toString());
			
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
	        	System.out.println("有此用户");
	        	params.put("nickname", rs.getString("nickname"));
	        	method.invoke(aClass.newInstance(),request, response, stmt, params);
			}else {
				ResponseUtil.response(response, "用户名或密码错误！！", false);
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
