<%@page import="com.lw.db.GetIdPwd"%>
<%@page import="com.lw.xuanke.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.lw.login.*"%>
<%@page import="com.lw.db.*"%>
<%@page import="org.apache.http.client.CookieStore"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>专业选修选课查询</title>
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
				"window.location='http://fjyqlw.link/hnnu_jwxt/cj/lncj.jsp?openID="
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
<link rel="stylesheet"
	href="../jquerymobile/jquery.mobile-1.3.2.min.css">
	<script src="../jquerymobile/jquery-1.8.3.min.js"></script>
	<script src="../jquerymobile/jquery.mobile-1.3.2.min.js"></script>
</head>

<body>

	<div data-role="page" id="pageone">


		<div data-role="content">
			<table width="100%" border="0" cellpadding="0">
				<tr>
					<td align="left" style="padding-left:20px"><strong>课程名称</strong></td>
					<td align="right"><strong>课程性质</strong></td>
				</tr>
			</table>
			<%
				try {

					String openID = request.getParameter("openID");
					Map cj_map = new HashMap();

					Login login = new Login(null);
					Map user_map = new HashMap();
					GetIdPwd gIdPwd = new GetIdPwd();
					Map xuankeMap = new HashMap();
					Map xuankeResultMap = null;
					user_map = gIdPwd.getIdPwd(openID);

					//======登录成功，开始查询成绩=========
					cj_map = login.login((String) user_map.get("id"),
							(String) user_map.get("psd"), "学生");
					xuankeMap.put("cookieStore",
							(CookieStore) cj_map.get("cookieStore"));
					xuankeMap.put("login_url_xuanke",
							(String) cj_map.get("login_url_xuanke"));
					xuankeMap.put("ip", (String) cj_map.get("ip"));
					GetXuanKe getXuanKe = new GetXuanKe(xuankeMap);
					xuankeResultMap = getXuanKe.call();
					//===========================================================			

					int count = 1;
					while (true) {
						Map zcj_info_map = new HashMap();//课程具体成绩
						String key = "zcj_" + count;
						if (xuankeResultMap.get(key) == null) {
							break;
						}

						zcj_info_map = (Map) xuankeResultMap.get(key);
						//==================================================

						StringBuffer s = new StringBuffer();

						s.append("<div style=\"margin:0\" data-role=\"collapsible\"data-collapsed-icon=\"arrow-r\" data-expanded-icon=\"arrow-d\"data-inset=\"false\"><h1><table width=\"100%\" border=\"0\" cellpadding=\"0\"><tr><td align=\"left\"><strong>");
						s.append("<div>" + zcj_info_map.get("1") + "</div>");
						s.append("	</strong></td><td align=\"right\" style=\"padding-right:15px\"><strong>"
								+ zcj_info_map.get("2")
								+ "</strong></td></tr></table></h1>");
						s.append("<table style=\"font-size:12px\" width=\"100%\" border=\"0\"cellpadding=\"1\" cellspacing=\"1\"><tr>");
						s.append("<td width=\"25%\" align=\"right\">课程代码:</td><td>"
								+ zcj_info_map.get("0")
								+ "</td></tr><tr><td align=\"right\">课程学分:</td><td>"
								+ zcj_info_map.get("4")
								+ "</td></tr><tr><td align=\"right\">周 学 时:</td><td>"
								+ zcj_info_map.get("5")
								+ "</td></tr><tr><td align=\"right\">考试时间:</td><td>"
								+ zcj_info_map.get("6")
								+ "</td></tr><tr><td align=\"right\">是否选课:</td><td>"
								+ zcj_info_map.get("8")
								+ "</td></tr><tr><td align=\"right\">课程余量:</td><td align=\"left\">"
								+ zcj_info_map.get("9")
								+ "</td></tr></table></div>");

						out.print(s.toString());

						zcj_info_map = null;
						count++;
					}

				} catch (Exception e) {

					out.print(e.toString());
				}
			%>
		
</body>
</html>