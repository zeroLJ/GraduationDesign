/**
 * 网络请求封装
 */
document.write("<script language='javascript' src='js/jquery.js'></script>");
function post(url , params, callback){
	$.post(url, params, function(result){
		console.log("result："+result);
		//var jsonObject=eval('('+result+')');
		var jsonObject=JSON.parse(result);
		callback(jsonObject);
	})
}