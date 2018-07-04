package main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//通过注解配置Servlet名称和部署路径，不需在web.xml配置
@WebServlet(name="MainServlet2",urlPatterns={"*.do2"},
		//默认参数，通过getInitParameter()方法获取，但注意通过反射访问的Servlet无法获取到
	initParams= {@WebInitParam(name = "path", value = "main.servlet.")})
public class MainServlet2 extends MainServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("22222");
		super.doGet(request, response);
	}
}
