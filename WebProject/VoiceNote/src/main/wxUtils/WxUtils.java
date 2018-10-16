package main.wxUtils;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import main.util.HttpRequest;
import main.util.Logs;
import main.util.ObjUtils;

public class WxUtils {
	private static String appID = "wxf0c5a5db0211f99d";
	private static String appsecret = "9f793217bf3047e764000d79edc63121";
	
	
	/**
	 * 获取公众号Access_token
	 * @return
	 */
	public static String getAccess_token() {
		Map<String, String> params = new HashMap<>();
		params.put("grant_type", "client_credential");
		params.put("appid", appID);
		params.put("secret", appsecret);
	    String access_token_json=HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/token", params);
	    System.out.println("access_token_json:" + access_token_json);
	    Map<String, Object> access_token_map = JSON.parseObject(access_token_json, Map.class);
	    String access_token = String.valueOf(access_token_map.get("access_token"));
	    return access_token;
	}
	
	/**
	 * 获取用户Access_token
	 * @param code  用户同意授权后传回来的code
	 * @return
	 */
	public static WxUserToken getUserAccess_token(String code) {
		Map<String, String> params = new HashMap<>();
		params.put("appid", appID);
		params.put("secret", appsecret);
		params.put("code", code);
		params.put("grant_type", "authorization_code");
	    String json=HttpRequest.sendGet("https://api.weixin.qq.com/sns/oauth2/access_token", params);
	    Logs.d("user_json:" + json);
	    WxUserToken token = new WxUserToken(json);
	    return token;
	}
	
	/**
	 * 刷新用户Access_token
	 * @param oldToken  通过getUserAccess_token获取的token
	 * @return
	 */
	public static WxUserToken refreshUserAccess_token(WxUserToken oldToken) {
		Map<String, String> params = new HashMap<>();
		params.put("appid", appID);
		params.put("grant_type", "refresh_token");
		params.put("refresh_token", oldToken.refresh_token);
	    String json=HttpRequest.sendGet("https://api.weixin.qq.com/sns/oauth2/refresh_token", params);
	    Logs.d("refresh_json:" + json);
	    WxUserToken token = new WxUserToken(json);
	    return token;
	}
	
	/**
	 * 检验用户授权凭证（access_token）是否有效
	 * @param wxUserToken
	 * @return
	 */
	public static boolean checkUserAccess_token(WxUserToken wxUserToken) {
		Map<String, String> params = new HashMap<>();
		params.put("access_token", wxUserToken.access_token);
		params.put("openid", wxUserToken.openid);
	    String json=HttpRequest.sendGet("https://api.weixin.qq.com/sns/auth", params);
	    Logs.d("check_json:" + json);
	    Map<String, Object> resultMap = JSON.parseObject(json, Map.class);
	    if (ObjUtils.objToInt(resultMap.get("errcode"))==0) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 拉取用户信息，(需scope为 snsapi_userinfo)
	 * @param token
	 * @return
	 */
	public static WxUserInfo getWxUserInfo(WxUserToken token) {
		Map<String, String> params = new HashMap<>();
		params.put("access_token", token.access_token);
		params.put("openid", token.openid);
		params.put("lang", "zh_CN"); //	返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
	    String json=HttpRequest.sendGet("https://api.weixin.qq.com/sns/userinfo", params);
	    Logs.d("userinfo_json:" + json);
	    WxUserInfo info = new WxUserInfo(json);
	    return info;
	}
	
	/**
	 * 接口调用次数清0 ，每个帐号每月共10次清零操作机会，清零生效一次即用掉一次机会（10次包括了平台上的清零和调用接口API的清零）。
	 * @return
	 */
	public static boolean clear_quota() {
		Map<String, String> map = new HashMap<>();
		map.put("appid", appID);
	    String result = HttpRequest.sendPost("https://api.weixin.qq.com/cgi-bin/clear_quota?access_token=" + getAccess_token(), JSON.toJSONString(map));
	    Logs.d("result:" + result);
	    Map<String, Object> resultMap = JSON.parseObject(result, Map.class);
	    if (ObjUtils.objToInt(resultMap.get("errcode"))==0) {
			return true;
		}else {
			return false;
		}
	}
}
