package main.wxUtils;

import main.util.ObjUtils;

/**
 * 公众号回复工具类
 * @author ljl
 */
public class WxSolve {
	public static String getResult(WxMessage message) {
		switch (ObjUtils.objToStr(message.MsgType)) {
			case "event":
				return getEventResult(message);
			default:
				return getOtherResult(message);
		}
	}
	
	/**
	 * 获取event事件要返回的字符串
	 * @param message
	 * @return
	 */
	private static String getEventResult(WxMessage message) {
		if (message.Event!=null) {
			switch (message.Event) {
				case "subscribe"://关注
					return getSubscribeResult(message);
				case "unsubscribe"://取消关注
					
					break;
				case "CLICK":
					return getClickResult(message);
				default:
					break;
			}
		}
		return "";
	}
	
	
	/**
	 * 获取除event事件外要返回的字符串
	 * @param message
	 * @return
	 */
	private static String getOtherResult(WxMessage message) {
		StringBuilder s = new StringBuilder();
		if (message.Content!=null) {
			String result = "你也"+message.Content;
			s.append("<xml>");
			s.append(" <ToUserName><![CDATA[").append(message.FromUserName).append("]]></ToUserName>");
			s.append(" <FromUserName><![CDATA[").append(message.ToUserName).append("]]></FromUserName>");
			s.append(" <CreateTime>").append(System.currentTimeMillis()).append("</CreateTime>");
			s.append(" <MsgType><![CDATA[").append("text").append("]]></MsgType>");
			s.append(" <Content><![CDATA[").append(result).append("]]></Content>");
			s.append("</xml>");
		}

		return s.toString();
	}
	
	/**
	 * 获取关注事件需要返回的字符串
	 * @param message
	 * @return
	 */
	private static String getSubscribeResult(WxMessage message) {
		StringBuilder s = new StringBuilder();
		s.append("<xml>");
		s.append(" <ToUserName><![CDATA[").append(message.FromUserName).append("]]></ToUserName>");
		s.append(" <FromUserName><![CDATA[").append(message.ToUserName).append("]]></FromUserName>");
		s.append(" <CreateTime>").append(System.currentTimeMillis()).append("</CreateTime>");
		s.append(" <MsgType><![CDATA[").append("text").append("]]></MsgType>");
		s.append(" <Content><![CDATA[").append("感谢关注").append("]]></Content>");
		s.append("</xml>");
		return s.toString();
	}
	
	
	/**
	 * 获取点击事件需要返回的字符串
	 * @param message
	 * @return
	 */
	private static String getClickResult(WxMessage message) {
		String msg = "";
		switch (ObjUtils.objToStr(message.EventKey)) {
			case "":
				
				break;
			default:
				msg = message.EventKey;
				break;
		}
		
		StringBuilder s = new StringBuilder();
		s.append("<xml>");
		s.append(" <ToUserName><![CDATA[").append(message.FromUserName).append("]]></ToUserName>");
		s.append(" <FromUserName><![CDATA[").append(message.ToUserName).append("]]></FromUserName>");
		s.append(" <CreateTime>").append(System.currentTimeMillis()).append("</CreateTime>");
		s.append(" <MsgType><![CDATA[").append("text").append("]]></MsgType>");
		s.append(" <Content><![CDATA[").append(msg).append("]]></Content>");
		s.append("</xml>");
		return s.toString();
	}
}
