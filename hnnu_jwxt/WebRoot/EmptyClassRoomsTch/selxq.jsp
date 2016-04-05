<%@page import="com.lw.date.MyDate"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@page import=" java.util.Calendar"%><%@page
	import="com.lw.db.GetIdPwd"%>
<%@page import="com.lw.cj.GetSore"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.lw.login.*"%>
<%@page import="com.lw.db.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>空教室查询</title>
<script type="text/javascript">
	window.onload = function() {
		if (!isWeiXin()) {
			location.assign("../error/noWeiXin.html");
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
	}
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
		setTimeout(mysubmit, 2000);
	}

	//隐藏加载器.for jQuery Mobile 1.2.0
	function hideLoader() {
		//隐藏加载器
		$.mobile.loading('hide');
	}
	//===========================
	function mysubmit() {
		document.lwform.submit();
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

			<%
				request.setCharacterEncoding("UTF-8");
				String openID = request.getParameter("openID");
			%>
			<form name="lwform" id="lwform" data-ajax="false" method="post"
				action="<%=basePath%>EmptyClassRoomsTch/emptyclassrooms.jsp?openID=<%=openID%>">
				<div data-role="fieldcontain">
					<fieldset data-role="controlgroup">

						<select name="xiaoq" id="xiaoq" data-native-menu="false">
							<option value="all">校区：不限</option>
							<option value="1">校区：南校</option>
							<option value="2">校区：桂林洋</option>
						</select> <select name="jslb" id="jslb" data-native-menu="false">
							<option selected="selected" value="all">教室类别不限</option>
							<option value="大学外语自主学习中心">大学外语自主学习中心</option>
							<option value="地理专用">地理专用</option>
							<option value="电子商务专业实验室">电子商务专业实验室</option>
							<option value="多功能教室、报告厅">多功能教室、报告厅</option>
							<option value="多功能实验室">多功能实验室</option>
							<option value="多媒体">多媒体</option>
							<option value="公共课机房">公共课机房</option>
							<option value="化学实验">化学实验</option>
							<option value="会计模拟实验室">会计模拟实验室</option>
							<option value="机房">机房</option>
							<option value="计算机应用实验室">计算机应用实验室</option>
							<option value="计算机专业实验室">计算机专业实验室</option>
							<option value="简易多媒体">简易多媒体</option>
							<option value="健身馆">健身馆</option>
							<option value="教师教育实训中心实验室">教师教育实训中心实验室</option>
							<option value="教学技能训练实验室">教学技能训练实验室</option>
							<option value="教育技术实验室">教育技术实验室</option>
							<option value="篮球场">篮球场</option>
							<option value="美术专用">美术专用</option>
							<option value="排球场">排球场</option>
							<option value="乒乓球馆">乒乓球馆</option>
							<option value="普通教室">普通教室</option>
							<option value="琴房">琴房</option>
							<option value="球场">球场</option>
							<option value="日语专用">日语专用</option>
							<option value="实验楼">实验楼</option>
							<option value="实验室">实验室</option>
							<option value="数学机房">数学机房</option>
							<option value="体操场">体操场</option>
							<option value="体育教室">体育教室</option>
							<option value="体育专用">体育专用</option>
							<option value="田径场">田径场</option>
							<option value="停用">停用</option>
							<option value="外语机房">外语机房</option>
							<option value="外语专用">外语专用</option>
							<option value="网络课件室">网络课件室</option>
							<option value="微格教室">微格教室</option>
							<option value="微机实验">微机实验</option>
							<option value="武术馆">武术馆</option>
							<option value="物理机房">物理机房</option>
							<option value="物理实验">物理实验</option>
							<option value="物理专用">物理专用</option>
							<option value="新闻实验室">新闻实验室</option>
							<option value="心理实验">心理实验</option>
							<option value="虚拟教室">虚拟教室</option>
							<option value="学生公寓">学生公寓</option>
							<option value="艺术专用">艺术专用</option>
							<option value="音乐专用">音乐专用</option>
							<option value="英语语音">英语语音</option>
							<option value="游泳池">游泳池</option>
							<option value="语音教室">语音教室</option>
							<option value="羽毛球场">羽毛球场</option>
							<option value="园林画室">园林画室</option>
							<option value="园林绘图室">园林绘图室</option>
							<option value="园林专业机房">园林专业机房</option>
							<option value="中教法教室">中教法教室</option>
							<option value="中心机房">中心机房</option>
							<option value="足球场">足球场</option>
						</select> </select> <select name="kssj" id="kssj" data-native-menu="false">

							<%
								for (int i = 0; i < 7; i++) {

									out.println("<option value=\"" + (i + 1) + "18\">"
											+ MyDate.getDate(i) + "</option>");
								}
							%>

						</select> </select> <select name="jssj" id="jssj" data-native-menu="false">

							<%
								for (int i = 0; i < 7; i++) {

									out.println("<option value=\"" + (i + 1) + "18\">"
											+ MyDate.getDate(i) + "</option>");
								}
								for (int i = 7; i < 14; i++) {

									out.println("<option value=\"" + (i + 1 - 7) + "19\">"
											+ MyDate.getDate(i) + "</option>");
								}
								for (int i = 14; i < 20; i++) {

									out.println("<option value=\"" + (i + 1 - 14) + "20\">"
											+ MyDate.getDate(i) + "</option>");
								}
							%>
						</select> </select> <select name="xqj" id="xqj" data-native-menu="false">
							<option value="1">星期一</option>
							<option value="2">星期二</option>
							<option selected="selected" value="3">星期三</option>
							<option value="4">星期四</option>
							<option value="5">星期五</option>
							<option value="6">星期六</option>
							<option value="7">星期天</option>
						</select> </select> <select name="ddlDsz" id="ddlDsz" data-native-menu="false">
							<option selected="selected" value="all">单双周</option>
							<option value="单">单</option>
							<option value="双">双</option>
						</select> <select name="sjd" id="sjd" data-native-menu="false">
							<option value="'1'|'1','0','0','0','0','0','0','0','0'">
								第1,2节</option>
							<option selected="selected"
								value="'2'|'0','3','0','0','0','0','0','0','0'">第3,4节</option>
							<option value="'3'|'0','0','5','0','0','0','0','0','0'">
								第5,6节</option>
							<option value="'4'|'0','0','0','7','0','0','0','0','0'">
								第7,8节</option>
							<option value="'5'|'0','0','0','0','9','0','0','0','0'">
								第9,10,11节</option>
							<option value="'6'|'1','3','0','0','0','0','0','0','0'">
								上午</option>
							<option value="'7'|'0','0','5','7','0','0','0','0','0'">
								下午</option>
							<option value="'8'|'0','0','0','0','9','0','0','0','0'">
								晚上</option>
							<option value="'9'|'1','3','5','7','0','0','0','0','0'">
								白天</option>
							<option value="'10'|'1','3','5','7','9','0','0','0','0'">
								整天</option>
						</select> <input name="min_zws" placeholder="最小座位数" /><input
							name="max_zws" placeholder="最大座位数" /> <br />
					</fieldset>
				</div>
				<a href="#" data-role="button" onclick="showLoader()">查&nbsp;&nbsp;&nbsp;&nbsp;询</a>
			</form>
		</div>
	</div>
</body>
</html>