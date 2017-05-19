<%@ page language="java"  pageEncoding="utf-8"%><%@ taglib prefix="s" uri="/struts-tags"%>

<%
response.setHeader("Pragma", "No-cache");
response.setHeader("Cache-Control", "no-cache");
response.setDateHeader("Expires", 0);
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>传通云锁管理平台</title>

<script src="<%=basePath %>/javascript/jbase64.js"></script>
<style type="text/css">
body {
	margin:0;
	padding:0;
	height:100%;
	width:100%;
	align:center;
	background-color:#91ccea	
	}
#center{
	position:absolute;
	background-image:url(<%=path %>/css/images/login/login_input_bg.png);
	background-repeat:no-repeat;
	margin:-156px 0 0 -364px; 
	height:431px;
	width:557px;
	top:40%;
	left:63%;
	
}

.text {
	font-size:18px;
	font-weight:bold;
	color:#0058e7;
	text-align:left;
	letter-spacing:0.5px;
	line-height:22px;
	width:250px;
	height:30px;
	background-color:transparent;
	border : 0px solid #cccccc;
	}
.code_text {
	font-size:18px;
	font-weight:bold;
	color:#0058e7;
	text-align:left;
	letter-spacing:0.5px;
	line-height:22px;
	width:127px;
	height:30px;
	background-color:transparent;
	border : 0px solid #cccccc;
	}

.tzCheckBox{
	background:url(<%=path %>/css/images/login/background.png) no-repeat left bottom;
	display:inline-block;
	min-width:24px;
	height:24px;
	white-space:nowrap;
	position:relative;
	cursor:pointer;
}

.tzCheckBox.checked{
	background-position:top left;
}

.tzCheckBox .tzCBContent{
	color: white;
	line-height: 20px;
	text-align: left;
}

.tzCheckBox.checked .tzCBContent{
	text-align:left;
}

.tzCBPart{
	background:url(<%=path %>/css/images/login/background.png) no-repeat left bottom;
	width:24px;
	position:absolute;
	top:212;
	left:35px;
	height:24px;
	overflow: hidden;
}

.tzCheckBox.checked .tzCBPart{
	background-position:top left;
}


.btCheckCodeClass{
	background:url(<%=path %>/css/images/login/buttonCheckCode.png)  no-repeat scroll 0 0 transparent;
	width:84px;
	height:34px;
	cursor:pointer;
	font-size:12px;
	color:#254b5e;
	border: medium none; 
}

.btCheckCodeHoverClass{
	background:url(<%=path %>/css/images/login/buttonCheckCode-click.png) no-repeat;
	border:none;
	width:84px;
	height:34px;
	cursor:pointer;
	font-size:12px;
	color:#254b5e;
}

</style>
<script type="text/javascript">
//避免在框架页面中打开
var windowsArray=[];
if(window.top==window&&!(window.parentWindow)){
	
}
else{
	 var topWindow = window;
	 while(!!(topWindow.opener)){
	 	windowsArray.push(topWindow);
	 	topWindow=topWindow.opener;
	 }
	 for(var i=0;i<windowsArray.length;i++){
	 	windowsArray[i].close();
	 }
	 if(!!(topWindow.top)){
	 	topWindow.top.location='<%=basePath%>login.jsp';
	 }
	 else{
	 	topWindow.location='<%=basePath%>login.jsp';
	 }
}
</script>

<script type="text/javascript">

function onLogin() {
	var isCheck = true;
	if ($("#userAccount").val() == '') {
		$("#ts").val("请输入用户名！");
		$("#userAccount").focus();
		isCheck = false;
		return;
	}
	if ($("#userPasswd").val() == '') {
		$("#ts").val("请输入密码！");
		$("#userPasswd").focus();
		isCheck = false;
		return;
	}
	
	if ($("#checkCode").val() == '') {
		//$("#ts").val("请输入验证码！");
		//$("#checkCode").focus();
		//isCheck = false;
		//return;
	}
	
	if(isCheck){
		$("#j_username").val(BASE64.encoder($("#userAccount").val()));
		$("#userPasswd").val(BASE64.encoder($("#userPasswd").val()));
		$("#loginForm").attr("action", "<%=basePath%>j_spring_security_check");
		$('#loginForm').submit();
	}
	
}

function createCode(){
	$("#captchaImg").attr("src",'<%=basePath%>captcha.jpg?'+Math.random());
} 

//回车时，默认是登陆
function enterSubmit(){
 if(window.event.keyCode == 13){
	 onLogin();
 }
}


function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
var lessThenIE9 = function () {
    var UA = navigator.userAgent,
        isIE = UA.indexOf('MSIE') > -1,
        v = isIE ? /\d+/.exec(UA.split(';')[1]) : 'no ie';
    return v < 9;
}();

function mouseLoginOver(){
	var isIE = !!window.ActiveXObject;  
	if(isIE && !lessThenIE9){ 
		//$("#userAccount").css({"margin-top":"16.5%","margin-left":"0%"});
	}
	document.getElementById('Image6').src ="<%=path%>/css/images/login/login_.png";
}
function mouseLoginOut(){
	document.getElementById('Image6').src ="<%=path %>/css/images/login/login.png";
}
function mouseLogOutOver(){
	document.getElementById('Image7').src ="<%=path%>/css/images/login/exit_.png";
}
function mouseLogOutOut(){
	document.getElementById('Image7').src ="<%=path %>/css/images/login/exit.png";
}

//-->
</script>
</head>
<script type="text/javascript" src="<%=basePath%>javascript/libs/jquery-1.9.1.min.js" charset="utf-8"></script>
<script src="<%=basePath%>javascript/libs/script.js"></script>
<body  onkeyPress="javascript:enterSubmit();" >
<input type="hidden" id="userId" name="userId"/>
<div style="width:100%;height:100%;align:center;background-image:url(<%=path %>/css/images/login/login_body.jpg);background-repeat:repeat;">
<div align="center" id="center" >
<form method="POST" name="loginForm" id="loginForm">
    <div align="center">
    	<div style="margin-top:12%;">
	    	<input type="text" class="text" style="color: red !important;" id="ts" name="ts" size="20" value=""/>
    	</div>
    	<div style="margin-top:1%;margin-left:-16%;">
    	<input type="text"  class="text" id="userAccount"  name="username" size="20" value="admin"/>
    	<input type="hidden" id="j_username"  name="j_username" size="20" value=""/>
    	</div>
    	<div style="margin-top:4%;margin-left:-16%;">
    		<input name="j_password" type="password" class="text" id="userPasswd" size="18" value="admin123"/>
			<input type="hidden" name="p.gotoUrl"/>
		</div>
		<div style="margin-top:3%;margin-left:5%;">
    		<input name="j_captcha" type="text" class="code_text" id="checkCode"  size="5" value=""  style="margin-left:-13%"/>
            <img id="imageF" src="<%=path%>/css/images/login/code_null.png" width="75px"/>
            <img id="captchaImg" src="<%=basePath%>captcha.jpg" onclick="createCode()"/>
            <input type="hidden" id="checktype" name="checktype" size="20" value="" />
        </div>
		<div style="margin-top:4.5%;margin-left:-20%;" id="butDiv">
			<img src="<%=path%>/css/images/login/login.png" name="Image6" border="0" id="Image6" onclick="javascript:onLogin();" onmouseover="mouseLoginOver()" onmouseout="mouseLoginOut()"/>
		  	<img src="<%=path%>/css/images/login/exit.png" name="Image7" border="0" id="Image7" onclick="javascript:window.close();" onmouseover="mouseLogOutOver()" onmouseout="mouseLogOutOut()"/>
		</div>
    </div>
</form>
</div>
</div>
</body>
</html>
<script type="text/javascript">
document.getElementById("userAccount").focus();

if (top.location != self.location){       
	top.location=self.location;       
} 

var rqUrl = window.location.href;
var paramStr = rqUrl.substring(rqUrl.indexOf('?')+1,rqUrl.length);
if(paramStr == "fail=true"){
	createCode();
	$("#ts").val("用户名或密码错误！");
}else if(paramStr == "errorCode=1"){
	createCode();
	$("#ts").val("验证码出错！");
}
</script>