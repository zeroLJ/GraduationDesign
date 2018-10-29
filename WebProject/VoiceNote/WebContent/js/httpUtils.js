/**
 * 网络请求封装
 * 同时将通用的js/css文件引入，减少html文件中的代码量
 */
var head = document.getElementsByTagName("head")[0];
//css文件最好直接在head加载，这样保证页面显示的是我们想要的样式。
//以这种方式加载会在页面显示后才进行加载，页面在一定时间内为默认样式。
/*var link = document.createElement("link");
link.rel = "stylesheet";
link.href = "./../style/weui.min.css";
head.appendChild(link);*/

var script = document.createElement("script");
script.language = "javascript";
script.src = "http://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js";
head.appendChild(script);
//document.write("<script language='javascript' src='http://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js'></script>");
//document.write("<script language='javascript' src='/VoiceNote/js/jquery.js'></script>");

script = document.createElement("script");
script.language = "javascript";
script.src = "https://res.wx.qq.com/open/libs/weuijs/1.1.4/weui.min.js";
head.appendChild(script);

/*function post(url , params, callback){
	$.post(url, params, function(result, status, xhr){
		console.log("result："+result);
		console.log("status："+status);
		console.log("xhr："+xhr);
		var jsonObject=JSON.parse(decodeURIComponent(xhr.getResponseHeader("data")));
		callback(jsonObject);
	}).error(function(str){//处理
		console.log(str) 
		console.log("sdsdsd")
	});
	
	$.ajax({
        type:'POST',
        url: url,
        data: params,
        success:function(response,status,xhr){
          	console.log("result："+result);
			console.log("status："+status);
			console.log("xhr："+xhr);
			//var jsonObject=eval('('+result+')');
			//var jsonObject=JSON.parse(result);
			var jsonObject=JSON.parse(decodeURIComponent(xhr.getResponseHeader("data")));
			callback(jsonObject);
        },
		error: function(xhr){
			console.log("error");
			console.log(xhr);
		}
    })
}*/

const URL = 'http://localhost:8081/VoiceNote/'
//const URL = 'https://jhonliu.club/VoiceNote/'
var loading;
function post(mapjson){
	var showMsg = mapjson.msg
	var url = mapjson.url
	var data = mapjson.data
    if (isEmpty(url)) {
    	weui.alert('url不能为空！')
    	return
	}
	if(url.indexOf('http')!=0){
		url = URL + url
	}
	var loading
	if (!isEmpty(showMsg)) {
		loading = weui.loading(showMsg);
	}
	if(isEmpty(data)){
		data = {}
	}
	log(data)
	$.ajax({
        type:'POST',
        url: url,
        data: data,
		scriptCharset: 'utf-8',
		timeout: 5000,
        success:function(response,status,xhr){
          	log("response："+response);
			log("status："+status);
			log(xhr);
			//var jsonObject=eval('('+result+')');
			//var jsonObject=JSON.parse(result);
			var json=JSON.parse(decodeURIComponent(xhr.getResponseHeader("data")));
		    if (json.success) {
		        if(!isEmpty(mapjson.success)){
		        	mapjson.success(json)
	            }
	        } else {
	        	if (isEmpty(mapjson.error)) {
	        		weui.alert(json.msg)
	        	} else {
	        		mapjson.error(json)
	        	}
	        }			
        },
		error: function(xhr,status,error){
			logW("error")
			if (isEmpty(mapjson.fail)) {
				weui.alert('网络请求失败')
		      }else{
		        mapjson.fail(res)
		      }
		},
		complete: function(xhr,status) {
			logW("complete")
			if (!isEmpty(loading)) {
				loading.hide()
			}
		}
    })
}

function isEmpty(str){
	if (str == "" || str == undefined || str == null){
		return true;
	}else{
		return false;
	}
}

var isDebug = true
function log(s) {
	if(isDebug){
		console.log(s)
	}
}

function logW(s) {
	if(isDebug){
		console.warn(s)
	}
}