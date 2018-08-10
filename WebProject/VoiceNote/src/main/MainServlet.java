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
//					HttpServletResponse.class);//��Ӧ���࣬��BaseServlet��doGet����
			method.setAccessible(true);
			method.invoke(classObj,request, response);
			Logs.d("��������");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Logs.d("�Ҳ���"+className);
			return;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			Logs.d("����������");
			return;
		} catch (SecurityException e) {
			e.printStackTrace();
			Logs.d("�޷�����");
			return;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(className+"��ʼ������");
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(className+"��ʼ������");
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
