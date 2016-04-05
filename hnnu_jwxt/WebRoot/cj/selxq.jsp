<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@page import=" java.util.Calendar"%><%@page import="com.lw.db.GetIdPwd"%>
<%@page import="com.lw.cj.GetSore"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.lw.login.*"%>
<%@page import="com.lw.db.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学期/学年成绩</title>
<script type="text/javascript">
	/*
	window.onload = function() {
	if (!isWeiXin()) {
	location.assign("../error/noWeiXin.html");
	// var p = document.getElementsByTagName('p');
	//"byshisss".innerHTML = window.navigator.userAgent;
	}
	document.addEventListener('WeixinJSBridgeReady',
	function onBridgeReady() {
	WeixinJSBridge.call('hideToolbar');
	WeixinJSBridge.call('hideOptionMenu')
	});
	}
	function isWeiXin() {
	var ua = window.navigator.userAgent.toLowerCase();
	if (ua.match(/MicroMessenger/i) == 'micromessenger') {
	return true;
	} else {
	return false;
	}
	}*/
	//显示加载器
//显示加载器
	function showLoader() {
		//显示加载器.for jQuery Mobile 1.2.0
		$.mobile.loading('show', {
			text : '加载中...', //加载器中显示的文字
			textVisible : true, //是否显示文字
			theme : 'a', //加载器主题样式a-e
			textonly : false, //是否只显示文字
			html : "" //要显示的html内容，如图片等
		});
	}

</script>
<link rel="stylesheet"
	href="../jquerymobile/jquery.mobile-1.3.2.min.css">
<link rel="stylesheet"
	href="../jquerymobile/jquery.mobile.structure-1.2.0.min.css">
	<script src="../jquerymobile/jquery-1.8.3.min.js"></script>
	<script src="../jquerymobile/jquery.mobile-1.3.2.min.js"></script>
	
</head>

<body>
	<div data-role="page" id="pageone">


		<div data-role="content" align="center">
			<p>成绩查询</p>
			<%
				String openID = request.getParameter("openID");

			String isupdate = request.getParameter("update");
				
				
				Calendar a = Calendar.getInstance();
						int xn2 = a.get(Calendar.YEAR);
						int xn1 = xn2 + 1;
						String xn = xn1 + "-" + xn2;
			%>
			<form  data-ajax="false" method="post" action="<%=basePath %>cj/xqcj.jsp?openID=<%=openID%>&isupdate=<%=isupdate%>">
				<div data-role="fieldcontain">
					<fieldset data-role="controlgroup">

						<select name="xn" id="xn"
							data-native-menu="false">
							<%for(int i=0;i<4;i++){
							out.print("<option value=\""+(xn2-i)+"-"+(xn1-i)+"\">"+(xn2-i)+"-"+(xn1-i)+"</option>");
							} %>
							
						</select> <select name="xq" id="xq" data-native-menu="false">
							<option value="1">第一学期</option>
							<option value="2">第二学期</option>
							<option value="3">第三学期</option>
						</select> <select name="kcxz" id="kcxz"
							data-native-menu="false">
							<option value="all">所有性质</option>
							<option value="专业必修">专业必修</option>
							<option value="公共必修">公共必修</option>
							<option value="教师教育必修">教师教育必修</option>
							<option value="公选(自然)">公选(自然)</option>
							<option value="公选(人文)">公选(人文)</option>
							<option value="公选(教育)">公选(教育)</option>
							<option value="自然/体育">自然/体育</option>
							<option value="大学体育选项">大学体育选项</option>
							<option value="教师教育选修">教师教育选修</option>
							<option value="方向专修">方向专修</option>
							<option value="通知教育">通知教育</option>
						</select>
					</fieldset>
				</div>
				<input type="submit" value="查&nbsp;&nbsp;&nbsp;&nbsp;询" onclick="showLoader()">
			</form>
		</div>
	</div>
</body>
</html>