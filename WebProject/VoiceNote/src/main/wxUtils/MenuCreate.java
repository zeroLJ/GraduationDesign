package main.wxUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import main.util.HttpRequest;

/**
 * 微信公众号创建自定义菜单，数据结构请看https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141013
 * @author ljl
 */
public class MenuCreate {
	
	public static void main(String[] args) {
		Map<String, List<Map<String, Object>>> map = new HashMap<>(); //最外层封装
	    List<Map<String, Object>> list = new ArrayList<>();//一级菜单列表，个数应为1~3个，   一级菜单最多4个汉字
	    List<Map<String, String>> childList = new ArrayList<>();//二级菜单列表，个数应为1~5个， 二级菜单最多7个汉字
	    
//        String access_token=HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/token", "grant_type=client_credential&appid=wxf0c5a5db0211f99d&secret=9f793217bf3047e764000d79edc63121");
//	    System.out.println("access_token" + access_token);
//	    if (true) {
//			return;
//		}

	    String access_token = WxUtils.getAccess_token();
	    
//	    String access_token = "13_hzjkAdlI_UvhWX0O0T8LJbQaAZiNxYdoyOtdqdkzQzBtfsXW4c_C-e4OqvP43tqpWiHDfK8qZFPAmMFTsXVSw0YuuPOl1aIUB40tT1STaScS_YQqEOKTVDCETGvgUtmbNDiB0GsYQw_ut3_lNMOfAEAAZB";
	   
	    map.put("button", list);
	    //第一个菜单
	    Map<String, Object> objMap = new HashMap<>();
	    objMap.put("type", "click");
	    objMap.put("name", "今日歌曲");
	    objMap.put("key", "V1001_TODAY_MUSIC");
	    list.add(objMap);
	    //未发布的小程序无法绑定
	    /*objMap.put("type", "miniprogram");
	    objMap.put("name", "VoiceNote");
	    objMap.put("url", "http://mp.weixin.qq.com");
	    objMap.put("appid", "gh_f7d2de80ebde");
	    objMap.put("pagepath", "pages/index/index");
	    list.add(objMap);*/
	    
	    
	    //第二个菜单
	    objMap = new HashMap<>();//菜单属性
	    objMap.put("name", "菜单");
	    //第二个菜单的二级菜单
	    childList = new ArrayList<>();
	    Map<String, String> childMap = new HashMap<>();
	    childMap.put("type", "view");
	    childMap.put("name", "soso");
	    childMap.put("url", "http://www.soso.com/");
	    childList.add(childMap);
	    
	    //http://193.112.132.83/WxDemo/index。jsp
	    //https://www.csdn.net/
	    childMap = new HashMap<>();
	    childMap.put("type", "view");
	    childMap.put("name", "测试网页");
	    childMap.put("url", "https://jhonliu.club/VoiceNote/view/signin.html");
	    childList.add(childMap);
	    
	    childMap = new HashMap<>();
	    childMap.put("type", "view");
	    childMap.put("name", "base");
	    childMap.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf0c5a5db0211f99d&redirect_uri=https://jhonliu.club/VoiceNote/WxGetUserInfo&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
	    childList.add(childMap);
	    
	    childMap = new HashMap<>();
	    childMap.put("type", "view");
	    childMap.put("name", "userinfo");
	    childMap.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf0c5a5db0211f99d&redirect_uri=https://jhonliu.club/VoiceNote/WxGetUserInfo&response_type=code&scope=snsapi_userinfo&state={123456}#wechat_redirect");
	    childList.add(childMap);
	    
	    
//	    childMap = new HashMap<>();
//	    childMap.put("type", "miniprogram");
//	    childMap.put("name", "wxaa");
//	    childMap.put("url", "http://mp.weixin.qq.com");
//	    childMap.put("appid", "wx286b93c14bbf93aa");
//	    childMap.put("pagepath", "pages/lunar/index");
//	    childList.add(childMap);
	    
	    childMap = new HashMap<>();
	    childMap.put("type", "view");
	    childMap.put("name", "菜单");
	    childMap.put("url", "https://mp.weixin.qq.com/mp/homepage?__biz=MzU2NzY3OTM5Mg%3D%3D&hid=1&sn=7cea03cee8b433fb6c5d4becbd44025c");
	    childList.add(childMap);
	    
//	    childMap = new HashMap<>();
//	    childMap.put("type", "click");
//	    childMap.put("name", "赞我们一下");
//	    childMap.put("key", "V1001_GOOD");
//	    childList.add(childMap);
	    
	    objMap.put("sub_button", childList);//设置第二个菜单的子菜单列表
	    list.add(objMap); 
	    
	    //第三个菜单
	    objMap = new HashMap<>();
	    objMap.put("type", "scancode_waitmsg");
	    objMap.put("name", "扫码提示");
	    objMap.put("key", "rselfmenu_0_0");
	    list.add(objMap); 
	    
	    String json = JSON.toJSONString(map);//转化成json
	    System.out.println("json:" + json);
	    String result = HttpRequest.sendPost("https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+access_token, json);
	    System.out.println(result);
		
	}
}
