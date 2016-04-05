<%@page import="com.lw.db.GetIdPwd"%>
<%@page import="com.lw.cj.GetSore"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.lw.login.*"%>
<%@page import="com.lw.db.*"%>
<%@page import="org.apache.http.client.CookieStore"%>
<%@page import="java.lang.*"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
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
<title>历年成绩</title>

<link rel="stylesheet"
	href="../jquerymobile/jquery.mobile-1.3.2.min.css">
	<script src="../jquerymobile/jquery-1.8.3.min.js"></script>
	<script src="../jquerymobile/jquery.mobile-1.3.2.min.js"></script>
<script type="text/javascript">
/*	window.onload = function() {
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
	}*/
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
	function updatecj() {
		showLoader();
		var update = GetQueryString("update");
		var openID = GetQueryString("openID");
		var url = "";
		if ("1" == update) {
			url = "0";
		} else {
			url = "1";
		}
		window.setTimeout(
				"window.location='http://www.hainnujwc.com/hnnu_jwxt/cj/lncj.jsp?openID="
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


		<div data-role="content" text-shadow="none">
			<table width="100%" border="0" cellpadding="0">
				<tr>
					<td align="left" style="padding-left:20px"><strong>课程名称</strong></td>
					<td align="right"><strong>成绩</strong></td>
				</tr>
			</table>
			<%
				try {

					String openID = request.getParameter("openID");
					String isupdate = request.getParameter("update");
					if ("1".equals(isupdate)) {
						/**取得用户名密码*/
						Map user_map = new HashMap();
						GetIdPwd gIdPwd = new GetIdPwd();
						user_map = gIdPwd.getIdPwd(openID);
						gIdPwd = null;
						Map loginMap = null;
						/**登录教务在线*/
						Login login = new Login(null);
						loginMap = login.login((String) user_map.get("id"),
								(String) user_map.get("psd"), "学生");
						/**查询成绩，更新*/
						Map cjMap = new HashMap();
						cjMap.put("Id", (String) user_map.get("id"));
						cjMap.put("isupdate", true);

						cjMap.put("btnType", "btn_zcj");
						cjMap.put("btnValue", "历年成绩");
						cjMap.put("kcxz", "");
						cjMap.put("xn", "");
						cjMap.put("xq", "");
						cjMap.put("cookieStore",
								(CookieStore) loginMap.get("cookieStore"));
						cjMap.put("url_cjcx", (String) loginMap.get("login_url"));
						cjMap.put("ip", (String) loginMap.get("ip"));

						GetSore getSore = new GetSore(cjMap);
						getSore.start();

					}
					//=================================================		
					Map zcj_map = new HashMap();
					DB db = new DB();
					zcj_map = (Map) db.getLNCJ(openID);
					db = null;
					if ("-2".equals(zcj_map.get("cjStatus").toString())) {//未绑定
						response.sendRedirect(basePath
								+ "bining/bining.jsp?openID=" + openID);
					}
					if("-3".equals(zcj_map.get("cjStatus").toString())){
						out.append("<div align=\"center\">系统繁忙或暂未开放成绩查询！</div>");
					}else{
					int count = 1;
					while (true) {
						Map zcj_info_map = new HashMap();//课程具体成绩
						String key = "zcj_" + count;
						if (zcj_map.get(key) == null) {
							break;
						}

						zcj_info_map = (Map) zcj_map.get(key);
						//==================================================

						StringBuffer s = new StringBuffer();

						s.append("<div style=\"margin:0\" data-role=\"collapsible\"data-collapsed-icon=\"arrow-r\" data-expanded-icon=\"arrow-d\"data-inset=\"false\"><h1><table width=\"100%\" border=\"0\" cellpadding=\"0\"><tr><td align=\"left\"><strong>");
						s.append("<div>" + zcj_info_map.get("5") + "</div>");
						s.append("	</strong></td><td align=\"right\" style=\"padding-right:15px\"><strong>");
						//s.append(zcj_info_map.get("10"));
						float fs = Float.parseFloat(zcj_info_map.get("10")
								.toString());
						if (fs < 60) {
							s.append("<font color=\"#FF0000\">");
							s.append(zcj_info_map.get("10"));
							s.append("</font>");
						} else {
							s.append("<font color=\"#000000\">");
							s.append(zcj_info_map.get("10"));
							s.append("</font>");
						}

						s.append("</strong></td></tr></table></h1>");
						s.append("<table style=\"font-size:12px\" width=\"100%\" border=\"0\"cellpadding=\"1\" cellspacing=\"1\"><tr>");
						s.append("<td width=\"15%\" align=\"right\">学年:</td><td>");
						s.append(zcj_info_map.get("2"));
						s.append("</td></tr><tr><td align=\"right\">学期:</td><td>");
						s.append(zcj_info_map.get("3"));
						s.append("</td></tr><tr><td align=\"right\">性质:</td><td>");
						s.append(zcj_info_map.get("6"));
						s.append("</td></tr><tr><td align=\"right\">学分:</td><td>");
						s.append(zcj_info_map.get("8"));
						s.append("</td></tr><tr><td align=\"right\">绩点:</td><td>");
						s.append(zcj_info_map.get("9"));
						s.append("</td></tr><tr><td align=\"right\">成绩:</td><td align=\"left\">");

						if (fs < 60) {
							s.append("<font color=\"#FF0000\">");
							s.append(zcj_info_map.get("10"));
							s.append("</font>");
						} else {
							s.append("<font color=\"#000000\">");
							s.append(zcj_info_map.get("10"));
							s.append("</font>");
						}
						s.append("</td></tr></table></div>");
						out.print(s.toString());

						zcj_info_map = null;
						s.delete(0, s.length());
						s = null;
						count++;
					}
					out.append("</div><a href=\"#\" data-role=\"button\" onclick=\"updatecj();\">重新载入成绩</a> <br /></div>");
					}
					
				} catch (Exception e) {

					out.print(e.toString());
				}
			%>


		

		
	
</body>
</html>