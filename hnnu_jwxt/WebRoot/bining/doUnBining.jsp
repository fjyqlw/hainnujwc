<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ page import="java.sql.*"%>
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>授权绑定</title>
<script type="text/javascript">
	/*	window.onload = function() {
	 if (!isWeiXin()) {
	 location.assign("../error/noWeiXin.html");
	 // var p = document.getElementsByTagName('p');
	 //"byshisss".innerHTML = window.navigator.userAgent;
	 }
	 }
	 function isWeiXin() {
	 var ua = window.navigator.userAgent.toLowerCase();
	 if (ua.match(/MicroMessenger/i) == 'micromessenger') {
	 return true;
	 } else {
	 return false;
	 }
	 }*/
	function close_wechat() {

		WeixinJSBridge.call("closeWindow");

	}
</script>
<base href="<%=basePath%>">


<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link rel="stylesheet" href="jquerymobile/jquery.mobile-1.3.2.min.css">
<link rel="stylesheet"
	href="jquerymobile/jquery.mobile.structure-1.2.0.min.css">
<script src="jquerymobile/jquery-1.8.3.min.js"></script>
<script src="jquerymobile/jquery.mobile-1.3.2.min.js"></script>
</head>

<body>
	<div data-role="page" id="pageone" align="center">


		<div data-role="content">
			<%
				String openID = "", Id = "", psd = "";
				openID = request.getParameter("openID");

				Connection connection = null;
				Statement stmt = null;
				ResultSet rs = null;
				try {
					String sql = "", sql3 = "";
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					connection = DriverManager.getConnection(
							"jdbc:mysql://120.24.183.211:3306/hnnu_jwc",
							DB.sql_user, DB.sql_pwd);

					sql = "delete from user where openID='" + openID + "'";//删除总表，由触发器删除相应用户
					sql3 = "select id from user where openID='" + openID + "'";//得到学号，用来构造成绩表参数

					PreparedStatement ps = connection.prepareStatement(sql3);
					rs = ps.executeQuery();
					while (rs.next()) {
						Id = rs.getString("id");
					}
					ps = null;
					ps = connection.prepareStatement(sql);
					ps.executeUpdate();
					connection.close();
					if (Id.length() > 10) {//学生

						DB db = new DB();
						db.dropTB(Id);
						db.dropKBTB(Id);
						db = null;
					}
					out.print("<h3>解除绑定成功！</h3><hr/><img onload=\"close_wechat();\" alt=\"\" src=\"bining/check.png\" width=\"128\" height=\"128\"/>");

				} catch (Exception e) {
					out.println(e.toString());
				}
			%>
			<input type="button" value="关闭页面" onclick="close_wechat();">
		</div>
	</div>
	<script>
		setTimeout(close_wechat, 2000);
	</script>
</body>
</html>
