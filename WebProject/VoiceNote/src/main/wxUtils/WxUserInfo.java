package main.wxUtils;

import java.util.Map;

import com.alibaba.fastjson.JSON;

import main.util.Logs;
import main.util.ObjUtils;
import main.util.StringUtils;

/**
 * 获取用户个人信息 ， 由于有可能获取失败，使用的时候要调用isSuccess()方法判断是否获取成功
 * @author ljl
 */
public class WxUserInfo {
	public String openid;//用户的唯一标识
	public String nickname;//用户昵称
	public String sex;//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	public String province;//用户个人资料填写的省份
	public String city;//普通用户个人资料填写的城市
	public String country;//国家，如中国为CN
	public String headimgurl;//用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
	public String privilege;//用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
	public String unionid;//只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
	public String errcode;//获取成功的时候该值为空
	public WxUserInfo(String json) {
		Map<String, Object> map = JSON.parseObject(json, Map.class);
		Logs.d("userinfo_map:" + map);
		openid = ObjUtils.objToStr(map.get("openid"));
		nickname = ObjUtils.objToStr(map.get("nickname"));
		sex = ObjUtils.objToStr(map.get("sex"));
		province = ObjUtils.objToStr(map.get("province"));
		city = ObjUtils.objToStr(map.get("city"));
		country = ObjUtils.objToStr(map.get("country"));
		headimgurl = ObjUtils.objToStr(map.get("headimgurl"));
		privilege = ObjUtils.objToStr(map.get("privilege"));
		unionid = ObjUtils.objToStr(map.get("unionid"));
		errcode = ObjUtils.objToStr(map.get("errcode"));
	}
	
	
	/**
	 * 请求用户数据是否成功
	 * @return
	 */
	public boolean isSuccess() {
		if (StringUtils.isEmpty(errcode)) {
			return true;
		}
		return false;
	}
	
}
