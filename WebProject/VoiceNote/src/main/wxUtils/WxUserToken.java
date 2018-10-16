package main.wxUtils;

import java.util.Map;

import com.alibaba.fastjson.JSON;

import main.util.ObjUtils;

/**
 * 公众号用户授权成功后获取的该用户的信息 ， 由于有可能获取失败，使用的时候要先判断expires_in是否大于0;若为0则为获取失败
 * @author ljl
 */
public class WxUserToken {
	public String access_token;//网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	public int expires_in;//access_token接口调用凭证超时时间，单位（秒）
	public String refresh_token;//用户刷新access_token
	public String openid;//用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
	public String scope;//用户授权的作用域，使用逗号（,）分隔        snsapi_base/snsapi_userinfo
	
	public WxUserToken(String json) {
		Map<String, Object> map = JSON.parseObject(json, Map.class);
		access_token = ObjUtils.objToStr(map.get("access_token"));
		expires_in = ObjUtils.objToInt(map.get("expires_in"));
		refresh_token = ObjUtils.objToStr(map.get("refresh_token"));
		openid = ObjUtils.objToStr(map.get("openid"));
		scope = ObjUtils.objToStr(map.get("scope"));
	}
	
//	public WxUserToken(Map<String, Object> map) {
//		access_token = ObjUtils.objToStr(map.get("access_token"));
//		expires_in = ObjUtils.objToInt(map.get("expires_in"));
//		refresh_token = ObjUtils.objToStr(map.get("refresh_token"));
//		openid = ObjUtils.objToStr(map.get("openid"));
//		scope = ObjUtils.objToStr(map.get("scope"));
//	}
}
