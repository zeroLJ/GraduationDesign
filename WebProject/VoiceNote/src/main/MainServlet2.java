package main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//通过默认参数配置路径，缩短请求url的长度，比如在MainServlet为 http://localhost:8081/VoiceNote/main/servlet/InfoGet.do
//在这里只需要http://localhost:8081/VoiceNote/InfoGet.do2
@WebServlet(name="MainServlet2",urlPatterns={"*.do2"},
	initParams= {@WebInitParam(name = "path", value = "main.servlet.")})//
public class MainServlet2 extends MainServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("22222");
		super.doGet(request, response);
	}
}
