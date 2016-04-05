<%@page import="com.lw.teacherinfo.TeaInfo"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ page import="com.lw.db.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.apache.http.client.CookieStore"%>
<%@page import="java.util.Map"%>
<%@page import="com.lw.login.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>个人信息</title>
<link rel="stylesheet"
	href="../jquerymobile/jquery.mobile-1.3.2.min.css">
<link rel="stylesheet"
	href="../jquerymobile/jquery.mobile.structure-1.2.0.min.css">
<script src="../jquerymobile/jquery-1.8.3.min.js"></script>
<script src="../jquerymobile/jquery.mobile-1.3.2.min.js"></script>

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
	}

	//隐藏加载器.for jQuery Mobile 1.2.0
	function hideLoader() {
		//隐藏加载器
		$.mobile.loading('hide');
	}
	function updateuserinfo() {
		showLoader();
		var update = GetQueryString("update");
		var openID = GetQueryString("openID");
		var url = "";
		if ("1" == update) {
			url = "0";
		} else {
			url = "1";
		}
		window
				.setTimeout(
						"window.location='http://www.hainnujwc.com/hnnu_jwxt/teacher/teacherinfo.jsp?openID="
								+ openID + "&update=" + url + "'", 1000);
	}

	function GetQueryString(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r != null)
			return unescape(r[2]);
		return null;
	}
</script>
</head>

<body>
	<div data-role="page" id="pageone">
		<div data-role="content">

			<ul data-role="listview" data-inset="true" data-icon="none">
				<%
					try {

						String openID = request.getParameter("openID");
						String isupdate = request.getParameter("update");
						if ("1".equals(isupdate)) {
							/**获取用户名密码*/
							Map user_map = new HashMap();
							GetIdPwd gIdPwd = new GetIdPwd();
							user_map = gIdPwd.getIdPwd(openID);
							/**登录教务在线*/
							Map loginMap = null;
							Login login = new Login(null);
							loginMap = login.login((String) user_map.get("id"),
									(String) user_map.get("psd"), "教师");

							/**更新个人简历*/
							Map tchMap = new HashMap();
							tchMap.put("cookieStore",
									(CookieStore) loginMap.get("cookieStore"));
							tchMap.put("login_url_teainfo",
									(String) loginMap.get("login_url_teainfo"));

							tchMap.put("ip", (String) loginMap.get("ip"));
							tchMap.put("isupdate", true);
							TeaInfo teaInfo = new TeaInfo(tchMap);
							teaInfo.start();
						}
						//===========================================================			
						Map zcj_map = new HashMap();
						DB db = new DB();
						zcj_map = (Map) db.getUserInfo(openID, 2);
						if (-2 == (Integer) zcj_map.get("cjStatus")) {
							response.sendRedirect(basePath
									+ "bining/bining.jsp?openID=" + openID);
						}
						//==================================================

						StringBuffer s = new StringBuffer();

						s.append("<li><table width=\"100%\" border=\"0\">");
						s.append("<tr><td width=\"40%\">" + "职工号：" + "</td><td>"
								+ zcj_map.get("1") + "</td>");
						s.append("<tr><td width=\"40%\">" + "姓    名：" + "</td><td>"
								+ zcj_map.get("2") + "</td>");
						s.append("<tr><td width=\"40%\">" + "性    别：" + "</td><td>"
								+ zcj_map.get("3") + "</td>");
						s.append("<tr><td width=\"40%\">" + "民    族：" + "</td><td>"
								+ zcj_map.get("4") + "</td>");
						s.append("<tr><td width=\"40%\">" + "学    院：" + "</td><td>"
								+ zcj_map.get("5") + "</td>");
						s.append("<tr><td width=\"40%\">" + "学    历：" + "</td><td>"
								+ zcj_map.get("6") + "</td>");
						s.append("<tr><td width=\"40%\">" + "学    位：" + "</td><td>"
								+ zcj_map.get("7") + "</td>");
						s.append("</table></li>");

						out.print(s.toString());

					} catch (Exception e) {

						out.print(e.toString());
					}
				%>


			</ul>
		</div>
		<a href="#" data-role="button" onclick="updateuserinfo();">重新载入个人信息</a>
		<br />
	</div>
</body>
</html>