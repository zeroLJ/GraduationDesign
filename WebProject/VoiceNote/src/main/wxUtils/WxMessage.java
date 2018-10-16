package main.wxUtils;


/**
 * 微信公众号传过来的数据格式
 * @author ljl
 */
public class WxMessage {
	/**
	 * 公众号返回数据的格式https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141016
	 * <xml>
	 *  <ToUserName><![CDATA[toUser]]></ToUserName>
	 *	<FromUserName><![CDATA[FromUser]]></FromUserName>
	 *	<CreateTime>123456789</CreateTime>
	 *	<MsgType><![CDATA[event]]></MsgType>
	 *	<Event><![CDATA[CLICK]]></Event>
	 *	<EventKey><![CDATA[EVENTKEY]]></EventKey>
	 * </xml>
	 */
	public String ToUserName; //开发者 微信号
	public String FromUserName;//发送方帐号（一个OpenID）
	public String CreateTime;//消息创建时间 （整型）
	public String MsgType;//消息类型，event
	public String Event;//事件类型，CLICK(点击)/VIEW(跳转链接)/scancode_push/scancode_waitmsg(扫描返回)/subscribe(订阅)/unsubscribe(取消订阅)
	public String EventKey;//事件KEY值，与自定义菜单接口中KEY值对应 或 跳转的URL
	public String MenuId;//指菜单ID，如果是个性化菜单，则可以通过这个字段，知道是哪个规则的菜单被点击了。
	public boolean hasScanCodeInfo = false;//扫描信息
	public String ScanType;//扫描类型，一般是qrcode
	public String ScanResult;//扫描结果，即二维码对应的字符串信息
	public String Content;//文本消息内容
	public long MsgId;//消息id，64位整型
	public WxMessage(String xml) {
		int keyL,keyR;
		String key = "<ToUserName><![CDATA[";
		keyL = xml.indexOf(key);
		keyR = xml.indexOf("]]></ToUserName>");
		if (keyL!=-1 && keyR !=-1 && keyL < keyR) {
			ToUserName =xml.substring(keyL + key.length(), keyR);
		}
		
		key = "<FromUserName><![CDATA[";
		keyL = xml.indexOf(key);
		keyR = xml.indexOf("]]></FromUserName>");
		if (keyL!=-1 && keyR !=-1 && keyL < keyR) {
			FromUserName =xml.substring(keyL + key.length(), keyR);
		}
		
		key = "<CreateTime>";
		keyL = xml.indexOf(key);
		keyR = xml.indexOf("</CreateTime>");
		if (keyL!=-1 && keyR !=-1 && keyL < keyR) {
			CreateTime =xml.substring(keyL + key.length(), keyR);
		}
		
		key = "<MsgType><![CDATA[";
		keyL = xml.indexOf(key);
		keyR = xml.indexOf("]]></MsgType>");
		if (keyL!=-1 && keyR !=-1 && keyL < keyR) {
			MsgType =xml.substring(keyL + key.length(), keyR);
		}
		
		key = "<Event><![CDATA[";
		keyL = xml.indexOf(key);
		keyR = xml.indexOf("]]></Event>");
		if (keyL!=-1 && keyR !=-1 && keyL < keyR) {
			Event =xml.substring(keyL + key.length(), keyR);
		}
		
		key = "<EventKey><![CDATA[";
		keyL = xml.indexOf(key);
		keyR = xml.indexOf("]]></EventKey>");
		if (keyL!=-1 && keyR !=-1 && keyL < keyR) {
			EventKey =xml.substring(keyL + key.length(), keyR);
		}
		
		key = "<MenuId>";
		keyL = xml.indexOf(key);
		keyR = xml.indexOf("</MenuId>");
		if (keyL!=-1 && keyR !=-1 && keyL < keyR) {
			CreateTime =xml.substring(keyL + key.length(), keyR);
		}
		
		key = "<ScanCodeInfo>";
		keyL = xml.indexOf(key);
		keyR = xml.indexOf("</ScanCodeInfo>");
		if (keyL!=-1 && keyR !=-1 && keyL < keyR) {
			hasScanCodeInfo = true;
		}
		
		if (hasScanCodeInfo) {
			key = "<ScanType><![CDATA[";
			keyL = xml.indexOf(key);
			keyR = xml.indexOf("]]></ScanType>");
			if (keyL!=-1 && keyR !=-1 && keyL < keyR) {
				ScanType =xml.substring(keyL + key.length(), keyR);
			}
			
			key = "<ScanResult><![CDATA[";
			keyL = xml.indexOf(key);
			keyR = xml.indexOf("]]></ScanResult>");
			if (keyL!=-1 && keyR !=-1 && keyL < keyR) {
				ScanResult =xml.substring(keyL + key.length(), keyR);
			}
		}
		
		
		key = "<Content><![CDATA[";
		keyL = xml.indexOf(key);
		keyR = xml.indexOf("]]></Content>");
		if (keyL!=-1 && keyR !=-1 && keyL < keyR) {
			Content =xml.substring(keyL + key.length(), keyR);
		}
		
		key = "<MsgId>";
		keyL = xml.indexOf(key);
		keyR = xml.indexOf("</MsgId>");
		if (keyL!=-1 && keyR !=-1 && keyL < keyR) {
			MsgId = Long.valueOf(xml.substring(keyL + key.length(), keyR));
		}
	}
	
	public void print() {
		System.out.println("ToUserName:"+ToUserName);
		System.out.println("FromUserName:"+FromUserName);
		System.out.println("CreateTime:"+CreateTime);
		System.out.println("MsgType:"+MsgType);
		System.out.println("Event:"+Event);
		System.out.println("EventKey:"+EventKey);
		System.out.println("MenuId:"+MenuId);
		System.out.println("hasScanCodeInfo:"+hasScanCodeInfo);
		System.out.println("ScanType:"+ScanType);
		System.out.println("ScanResult:"+ScanResult);
		System.out.println("Content:"+Content);
		System.out.println("MsgId:"+MsgId);
	}
}
