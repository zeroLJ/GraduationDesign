package main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//ͨ��ע������Servlet���ƺͲ���·����������web.xml����
@WebServlet(name="MainServlet2",urlPatterns={"*.do2"},
		//Ĭ�ϲ�����ͨ��getInitParameter()������ȡ����ע��ͨ��������ʵ�Servlet�޷���ȡ��
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
