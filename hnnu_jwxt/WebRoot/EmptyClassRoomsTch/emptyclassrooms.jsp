<%@page import="com.lw.db.GetIdPwd"%>
<%@page import="com.lw.classrooms.*"%>
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
<title>可用教室</title>
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
					<td align="left" style="padding-left:20px"><strong>教室名称</strong></td>
					<td align="right"><strong>教室类别</strong></td>
				</tr>
			</table>
			<%
				try {

					request.setCharacterEncoding("UTF-8");
					String openID = request.getParameter("openID");
					String xiaoq = "", jslb = "", kssj = "", jssj = "", xqj = "", ddlDsz = "", sjd = "", min_zws = "", max_zws = "";

					xiaoq = request.getParameter("xiaoq");
					jslb = request.getParameter("jslb");
					kssj = request.getParameter("kssj");
					jssj = request.getParameter("jssj");
					xqj = request.getParameter("xqj");
					ddlDsz = request.getParameter("ddlDsz");
					sjd = request.getParameter("sjd");
					min_zws = request.getParameter("min_zws");
					max_zws = request.getParameter("max_zws");
					if ("all".equals(xiaoq)) {
						xiaoq = "";
					}
					if ("all".equals(jslb)) {
						jslb = "";
					}
					if ("all".equals(ddlDsz)) {
						ddlDsz = "";
					}
					Map cj_map = new HashMap();

					Login login = new Login(null);
					Map crMap = new HashMap();
					Map user_map = new HashMap();
					GetIdPwd gIdPwd = new GetIdPwd();
					user_map = gIdPwd.getIdPwd(openID);
					Map crResultMap = null;

					crMap.put("xiaoq", xiaoq);
					crMap.put("jslb", jslb);
					crMap.put("kssj", kssj);
					crMap.put("jssj", jssj);
					crMap.put("xqj", xqj);
					crMap.put("ddlDsz", ddlDsz);
					crMap.put("sjd", sjd);
					crMap.put("min_zws", min_zws);
					crMap.put("max_zws", max_zws);

					crMap.put("choosepage", "1");

					//======登录成功，开始查询空教室=========

					cj_map = login.login((String) user_map.get("id"),
							(String) user_map.get("psd"), "教师");
					crMap.put("login_url_classroom",
							(String) cj_map.get("login_url_classroom"));
					crMap.put("ip", (String) cj_map.get("ip"));
					crMap.put("cookieStore",
							(CookieStore) cj_map.get("cookieStore"));
					GetClassRooms getClassRooms = new GetClassRooms(crMap);
					crResultMap = getClassRooms.call();
					//===========================================================			

					int count = 1;
					while (true) {
						Map zcj_info_map = new HashMap();//课程具体成绩
						String key = "zcj_" + count;
						if (crResultMap.get(key) == null) {
							break;
						}

						zcj_info_map = (Map) crResultMap.get(key);
						//==================================================

						StringBuffer s = new StringBuffer();

						s.append("<div style=\"margin:0\" data-role=\"collapsible\"data-collapsed-icon=\"arrow-r\" data-expanded-icon=\"arrow-d\"data-inset=\"false\"><h1><table width=\"100%\" border=\"0\" cellpadding=\"0\"><tr><td align=\"left\"><strong>");
						s.append("<div>" + zcj_info_map.get("1") + "</div>");
						s.append("	</strong></td><td align=\"right\" style=\"padding-right:15px\"><strong>");

						if ("停用".equals(zcj_info_map.get("2").toString())) {
							s.append("<font color=\"#FF0000\">");
							s.append(zcj_info_map.get("2"));
							s.append("</font>");
						} else {
							s.append("<font color=\"#000000\">");
							s.append(zcj_info_map.get("2"));
							s.append("</font>");
						}
						s.append("</strong></td></tr></table></h1>");
						s.append("<table style=\"font-size:12px\" width=\"100%\" border=\"0\"cellpadding=\"1\" cellspacing=\"1\"><tr>");
						s.append("<td width=\"25%\" align=\"right\">教&nbsp;室&nbsp;编&nbsp;号:</td><td>");
						s.append(zcj_info_map.get("0"));
						s.append("</td></tr><tr><td align=\"right\">校&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;区:</td><td>");
						s.append(zcj_info_map.get("3"));
						s.append("</td></tr><tr><td align=\"right\">上课座位数:</td><td>");
						s.append(zcj_info_map.get("4"));
						s.append("</td></tr><tr><td align=\"right\">考试座位数:</td><td>");
						s.append(zcj_info_map.get("6"));
						s.append("</td></tr></table></div>");

						out.print(s.toString());

						zcj_info_map = null;
						count++;
					}
					int totalroom = (Integer) crResultMap.get("totalpage");
					if (totalroom > 40) {
						out.println("<a href=\"#\" data-role=\"button\" onclick=\"updatecj();\">下一页</a>");
					}
				} catch (Exception e) {

					out.print(e.toString());
				}
			%>
		
</body>
</html>