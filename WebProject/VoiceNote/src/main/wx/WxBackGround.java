package main.wx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import main.wxUtils.WxMessage;
import main.wxUtils.WxSolve;

@WebServlet("/WxBackGround")
public class WxBackGround extends HttpServlet{
	private static final long serialVersionUID = 1L;
	static String connectionUrl = "jdbc:sqlserver://localhost:1433;"
			// "jdbc:sqlserver://192.168.0.188:1433;"
			+ "databaseName=Demo;" + "user=940034240;" + "password=pp123456;";

	public WxBackGround() {
		super();
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
		String signature = params.get("signature");
		String timestamp = params.get("timestamp");
		String nonce = params.get("nonce");
		String echostr = params.get("echostr");
		String token = "token";
		List<String> list = new ArrayList<>();
		list.add(token);
		list.add(timestamp);
		list.add(nonce);
		System.out.println("1111");
		System.out.println("signature:"+signature);
		System.out.println("timestamp:"+timestamp);
		System.out.println("nonce:"+nonce);
		System.out.println("echostr:"+echostr);
		
//		 String access_token=HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/token", "grant_type=client_credential&appid=wx6f0a4417cb3850f4&secret=fab18c81b82699eb1fe288f8d68dc095");
//	     System.out.println("access_token" + access_token);
		
		String result = "";
		if (echostr!=null && echostr.length()>0) { //只有端口验证的时候有用
			result = echostr;
			System.out.println("xml2:" + "\n");
		}else {
			/** 读取接收到的xml消息 */
			StringBuffer sb = new StringBuffer();
			InputStream is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			String xml = sb.toString();	//次即为接收到微信端发送过来的xml数据
			System.out.println("xml:"+xml + "\n");
			
			WxMessage message = new WxMessage(xml);
			message.print();
			result = WxSolve.getResult(message);
//			Document dom=DocumentHelper.parseText(xml);
//			  Element root=dom.getRootElement();
//			  String weighTime=root.element("weighTime").getText();
//			  String cardNum=root.element("cardNum").getText();
//			  String cfid=root.element("cfid").getText();
//			  System.out.println(weighTime);
//			  System.out.println(cardNum);
//			  System.out.println(cfid);	
		}
		try {
			OutputStream os = response.getOutputStream();
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
