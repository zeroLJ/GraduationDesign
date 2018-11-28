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

//由于这种方法会导致在页面加载时无法调用jquery等方法，故最好还是直接在head加载
/*var script = document.createElement("script");
script.language = "javascript";
script.src = "http://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js";
head.appendChild(script);

script = document.createElement("script");
script.language = "javascript";
script.src = "https://res.wx.qq.com/open/libs/weuijs/1.1.4/weui.min.js";
head.appendChild(script);*/

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
 * 检测是否已经登录，若没登录，返回登录界面
 * @returns
 */
function checkSignIn(){
	if (isEmpty(getUserName())) {
		weui.confirm('', {
		    title: '您尚未登录!',
		    buttons: [ {
		        label: '登录',
		        type: 'primary',
		        onClick: function(){ 
		        	console.log('登录') 
		        	goToPage('signin.html')
	        	}
		    }]
		});
		
	}
}

/**
 * 判断对象是否为空
 * @param str
 * @returns
 */
function isEmpty(str){
	if (str == "" || str == undefined || str == null || str == 'null'){
		return true;
	}else{
		return false;
	}
}

/**
 * 对象转字符串
 * @param str
 * @returns
 */
function objToStr(str){
	if (str == "" || str == undefined || str == null || str == 'null'){
		return '';
	}else{
		return str.toString().trim();
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

function signout() {
	setUserName('')
	setPassWord('')
	goToPage('signin.html')
}

function getStringDate(date) {
    return date.getFullYear() + '-' + formatNumber(date.getMonth() + 1) + '-' + formatNumber(date.getDate())
}

function getStartDate(date) {
    return (date.getFullYear()-100) + '-' + formatNumber(date.getMonth() + 1) + '-' + formatNumber(date.getDate())
}

const formatNumber = n => {
    n = n.toString()
    return n[1] ? n : '0' + n
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