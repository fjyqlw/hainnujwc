<%@page import="com.lw.db.GetIdPwd"%>
<%@page import="com.lw.cj.GetSore"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.lw.login.*"%>
<%@page import="org.apache.http.client.CookieStore"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>成绩统计</title>
<link rel="stylesheet"
	href="../jquerymobile/jquery.mobile-1.3.2.min.css">
	<script src="../jquerymobile/jquery-1.8.3.min.js"></script>
	<script src="../jquerymobile/jquery.mobile-1.3.2.min.js"></script>
<script type="text/javascript">
showLoader();
	window.onload = function() {
		if (isWeiXin()) {
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
</script>

</head>

<body style="margin: 0px" bgcolor="#E1E9F4">

	<br />

	<%
	Map cj_map = new HashMap();
	Map cj_map1 = new HashMap();
	Map cj_map2 = new HashMap();
	Map cj_map3 = new HashMap();
	Map cj_map4 = new HashMap();
	Map user_map = new HashMap();
	GetIdPwd gIdPwd = new GetIdPwd();
	Login login=new Login(null);
	Map cjResultMap = null;
		try {
			String openID = request.getParameter("openID");
			String bgcolor[] = { "bgcolor=\"#DEF0D8\"",
					"bgcolor=\"#F2DEDF\"", "bgcolor=\"#D9EDF6\"" };

			user_map = gIdPwd.getIdPwd(openID);


			//======登录成功，开始查询成绩=========
			cj_map = login.login((String) user_map.get("id"),
					(String) user_map.get("psd"), "学生");

			if("no".equals((String)cj_map.get("login_url"))){
				out.append("<div align=\"center\">系统繁忙或暂未开放成绩查询！</div>");
			}else{
			Map cjMap = new HashMap();
			cjMap.put("Id", (String) user_map.get("id"));
			cjMap.put("isupdate", true);
			
			cjMap.put("btnType", "Button1");
			cjMap.put("btnValue", "成绩统计");
			cjMap.put("kcxz", "");
			cjMap.put("xn", "");
			cjMap.put("xq", "");
			cjMap.put("cookieStore",
					(CookieStore) cj_map.get("cookieStore"));
			cjMap.put("url_cjcx", (String) cj_map.get("login_url"));
			cjMap.put("ip", (String) cj_map.get("ip"));
			
			GetSore getSore = new GetSore(cjMap);
			cjResultMap=getSore.call();

			
			
			out.println("<div align=\"center\">"
					+ (String) cjResultMap.get("xftj") + "</div>");
			out.println("<hr/>");
			out.print("<table bgcolor=\"#CDDBEE\" width=\"100%\" border=\"0\" align=\"center\"><tr><td align=\"center\">课程<br/>性质</td><td align=\"center\">学分<br/>要求</td><td align=\"center\">获得<br/>学分</td><td align=\"center\">未通过<br/>学分</td><td align=\"center\">还需<br/>学分</td></tr>");
			cj_map1 = (Map) cjResultMap.get("1");
			int count = 1;
			while (true) {
				String key = "zcj_" + count;

				if ((Map) cj_map1.get(key) == null) {
					break;
				}
				out.print("<tr " + bgcolor[count % 3]
						+ "\"><td align=\"center\">"
						+ ((Map) ((Map) cjResultMap.get("1")).get(key)).get("0")
						+ "</td><td align=\"center\">"
						+ ((Map) ((Map) cjResultMap.get("1")).get(key)).get("1")
						+ "</td><td align=\"center\">"
						+ ((Map) ((Map) cjResultMap.get("1")).get(key)).get("2")
						+ "</td><td align=\"center\">"
						+ ((Map) ((Map) cjResultMap.get("1")).get(key)).get("3")
						+ "</td><td align=\"center\">"
						+ ((Map) ((Map) cjResultMap.get("1")).get(key)).get("4")
						+ "</td></tr>");

				count++;

			}
			out.print("</table>");

			out.print("<hr/>");
			out.print("<table bgcolor=\"#CDDBEE\" width=\"100%\" border=\"0\" align=\"center\"><tr><td align=\"center\">课程<br/>归属</td><td align=\"center\">学分<br/>要求</td><td align=\"center\">获得<br/>学分</td><td align=\"center\">未通过<br/>学分</td><td align=\"center\">还需<br/>学分</td></tr>");
			cj_map2 = (Map) cjResultMap.get("2");
			count = 1;
			while (true) {
				String key = "zcj_" + count;

				if ((Map) cj_map2.get(key) == null) {
					break;
				}
				out.print("<tr " + bgcolor[count % 3]
						+ "\"><td align=\"center\">"
						+ ((Map) ((Map) cjResultMap.get("2")).get(key)).get("0")
						+ "</td><td align=\"center\">"
						+ ((Map) ((Map) cjResultMap.get("2")).get(key)).get("1")
						+ "</td><td align=\"center\">"
						+ ((Map) ((Map) cjResultMap.get("2")).get(key)).get("2")
						+ "</td><td align=\"center\">"
						+ ((Map) ((Map) cjResultMap.get("2")).get(key)).get("3")
						+ "</td><td align=\"center\">"
						+ ((Map) ((Map) cjResultMap.get("2")).get(key)).get("4")
						+ "</td></tr>");

				count++;

			}
			out.print("</table>");
			out.print("<hr/>");
			out.print("<table bgcolor=\"#CDDBEE\" width=\"100%\" border=\"0\" align=\"center\"><tr><td align=\"center\">学年</td><td align=\"center\">学期</td><td align=\"center\">获得<br/>学分</td><td align=\"center\">未通过<br/>及学分</td><td align=\"center\">未通过<br/>学分</td></tr>");
			cj_map3 = (Map) cjResultMap.get("3");
			count = 1;
			while (true) {
				String key = "zcj_" + count;

				if ((Map) cj_map3.get(key) == null) {
					break;
				}
				out.print("<tr " + bgcolor[count % 3]
						+ "\"><td align=\"center\">"
						+ ((Map) ((Map) cjResultMap.get("3")).get(key)).get("0")
						+ "</td><td align=\"center\">"
						+ ((Map) ((Map) cjResultMap.get("3")).get(key)).get("1")
						+ "</td><td align=\"center\">"
						+ ((Map) ((Map) cjResultMap.get("3")).get(key)).get("2")
						+ "</td><td align=\"center\">"
						+ ((Map) ((Map) cjResultMap.get("3")).get(key)).get("3")
						+ "</td><td align=\"center\">"
						+ ((Map) ((Map) cjResultMap.get("3")).get(key)).get("4")
						+ "</td></tr>");

				count++;

			}
			out.print("</table>");

			cj_map4 = (Map) cjResultMap.get("4");
			if (null != cj_map4) {

				out.print("<hr/>");
				out.print("<table bgcolor=\"#CDDBEE\" width=\"100%\" border=\"0\" align=\"center\"><tr><td align=\"center\">学年</td><td align=\"center\">学期</td><td align=\"center\">创新<br/>内容</td><td align=\"center\">创新<br/>及学分</td><td align=\"center\">创新<br/>次数</td></tr>");

				count = 1;
				while (true) {
					String key = "zcj_" + count;

					if ((Map) cj_map4.get(key) == null) {
						break;
					}
					out.print("<tr "
							+ bgcolor[count % 3]
							+ "\"><td align=\"center\">"
							+ ((Map) ((Map) cjResultMap.get("4")).get(key))
									.get("0")
							+ "</td><td align=\"center\">"
							+ ((Map) ((Map) cjResultMap.get("4")).get(key))
									.get("1")
							+ "</td><td align=\"center\">"
							+ ((Map) ((Map) cjResultMap.get("4")).get(key))
									.get("2")
							+ "</td><td align=\"center\">"
							+ ((Map) ((Map) cjResultMap.get("4")).get(key))
									.get("3")
							+ "</td><td align=\"center\">"
							+ ((Map) ((Map) cjResultMap.get("4")).get(key))
									.get("4") + "</td></tr>");

					count++;

				}
				out.print("</table>");
			}
			out.print("<br/>");
			}
		} catch (Exception e) {

			out.print("成绩统计页面错误：" + e.toString());
		}finally{
			 cj_map = null;
			 cj_map1 = null;
			 cj_map2 = null;
			 cj_map3 = null;
			 cj_map4 = null;
			 user_map = null;
			 gIdPwd = null;
			 System.gc();
		}
	%>


</body>
</html>