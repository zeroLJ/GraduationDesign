package main.wx;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.util.Logs;
import main.wxUtils.WxUserToken;
import main.wxUtils.WxUtils;



/**
 * 微信公众号授权后跳转
 */
@WebServlet("/WxGetUserInfo")
public class WxGetUserInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WxGetUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		Map<String, String> params = new HashMap<>();
		Map<String, String[]> map = request.getParameterMap();
		for (String key : map.keySet()) {
			params.put(key, map.get(key)[0]);
		}
		String code = params.get("code");
		String state = params.get("state");
		System.out.println("\n\n2222");
		System.out.println("code:"+code);
		System.out.println("state:"+state);
		WxUserToken token = WxUtils.getUserAccess_token(code);
		token = WxUtils.refreshUserAccess_token(token);
		WxUtils.getWxUserInfo(token);
		Logs.d("isWork:"+ WxUtils.checkUserAccess_token(token));
//		response.sendRedirect("http://193.112.132.83/WxDemo/NewFile.jsp");
		String result = "你好啊";
		try {
			OutputStream os = response.getOutputStream();
			response.setHeader("Content-Type", "text/html; charset=UTF-8");
			os.write(result.getBytes("UTF-8"));
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
