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
import main.util.Logs;
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
        Logs.d("class:"+className);
        className = getInitParameter("path") + className;
        Logs.d("path:"+className);
        try {
			aClass = Class.forName(className);
			Object classObj = aClass.newInstance();
			while (!aClass.getName().equals(BaseServlet.class.getName()) && aClass.getSuperclass()!=null) {
				Logs.d(aClass.getName());
				aClass = aClass.getSuperclass();
			}
			method = aClass.getDeclaredMethod("doGet",  HttpServletRequest.class, 
			HttpServletResponse.class);
//			method = aClass.getSuperclass().getDeclaredMethod("doGet",  HttpServletRequest.class, 
//					HttpServletResponse.class);//对应父类，即BaseServlet的doGet方法
			method.setAccessible(true);
			method.invoke(classObj,request, response);
			Logs.d("方法存在");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Logs.d("找不到"+className);
			return;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			Logs.d("方法不存在");
			return;
		} catch (SecurityException e) {
			e.printStackTrace();
			Logs.d("无法访问");
			return;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(className+"初始化错误");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(className+"初始化错误！");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
	
	}
	
	@Override
	public ResponseParams doSQL(Map<String, String> params, DBUtils db, User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
