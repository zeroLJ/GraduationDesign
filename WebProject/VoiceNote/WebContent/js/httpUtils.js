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
/**
 * 网络请求{url:请求的地址，必填	msg:请求时的提示	data:提交参数	success：function 请求成功时调用	error：function 请求成功，但服务器返回失败信息时调用		fail：function 网络请求失败时调用}
 * @param obj json格式
 */
function request(obj){
	var showMsg = obj.msg
	var url = obj.url
	var data = obj.data
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
	data.name = getUserName()
	data.password = getPassWord()
	log(data)
	$.ajax({
        type:'POST',
        url: url,
        data: data,
		scriptCharset: 'utf-8',
		timeout: 5000,
        success:function(response,status,xhr){
			log("status："+status);
//			log(xhr);
			var json=JSON.parse(response);
//			var json=JSON.parse(decodeURIComponent(xhr.getResponseHeader("data")));
		    if (json.success) {
		        if(!isEmpty(obj.success)){
		        	obj.success(json)
	            }
	        } else {
	        	if (isEmpty(obj.error)) {
	        		weui.alert(json.msg)
	        	} else {
	        		obj.error(json)
	        	}
	        }			
        },
		error: function(xhr,status,error){
			logW("error")
			if (isEmpty(obj.fail)) {
				weui.alert('网络请求失败')
			}else{
				obj.fail(res)
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

/**
 * 判断对象是否为空
 * @param str
 * @returns
 */
function isEmpty(str){
	if (str == "" || str == undefined || str == null){
		return true;
	}else{
		return false;
	}
}

/**
 * @param html 网页路径，如：login.html
 */
function goToPage(html) {
//	window.location.href= page; 
	$(location).attr('href', html);
}

const WEB_NAME = "VoiceNote"
function setUserName(name) {
	if(window.localStorage){     
		localStorage.setItem(WEB_NAME+"_name", name);
	}else{
		//不支持的情况下暂不考虑
		alert("浏览暂不支持sessionStorage") 
	}
}

function getUserName() {
	if(window.localStorage){     
		return localStorage.getItem(WEB_NAME+"_name")
	}else{
		//不支持的情况下暂不考虑
		alert("浏览暂不支持sessionStorage") 
		return ''
	}
}

function setPassWord(password) {
	if(window.localStorage){     
		localStorage.setItem(WEB_NAME+"_password", password);
	}else{
		//不支持的情况下暂不考虑
		alert("浏览暂不支持sessionStorage") 
	}
}

function getPassWord() {
	if(window.localStorage){     
		return localStorage.getItem(WEB_NAME+"_password")
	}else{
		//不支持的情况下暂不考虑
		alert("浏览暂不支持sessionStorage") 
		return ''
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