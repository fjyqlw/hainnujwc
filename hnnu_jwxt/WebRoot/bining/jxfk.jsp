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
						<link rel="stylesheet"
							href="jquerymobile/jquery.mobile-1.3.2.min.css">
							<link rel="stylesheet"
								href="jquerymobile/jquery.mobile.structure-1.2.0.min.css">
								<script src="jquerymobile/jquery-1.8.3.min.js"></script>
								<script src="jquerymobile/jquery.mobile-1.3.2.min.js"></script>
								<script src="../jquerymobile/jquery.validate.min.js"></script>
								<script src="../jquerymobile/jquery.metadata.js"></script>


								<style>
#footer {
	position: absolute;
	bottom: 0;
	padding: 10px 0;
	width: 100%;
}
</style>
</head>

<body>
	<div data-role="page" id="pageone" align="center">
		<div align="center">
			<img src="./bining/jwc.png" width="100%" alt="" />
		</div>
		<%
			String openID = request.getParameter("openID");
			String sql = "", Id = "", name = "";
			int role = 0;
			DB db = new DB();
			try {
				db.setSql_db("hnnu_jwc");
				sql = "select * from user where openID='" + openID + "'";
				ResultSet rs = db.ExexQuery(sql);
				boolean isbining = false;
				while (rs.next()) {
					Id = rs.getString("id");
					role = rs.getInt("role");
					isbining = true;
				}
				if (isbining) {
					if (0 == role) {//学生
						sql = "select * from userbining where id='" + Id + "'";
					} else {//教师
						sql = "select * from userbining_teacher where id='"
								+ Id + "'";
					}
					rs = db.ExexQuery(sql);
					while (rs.next()) {
						name = rs.getString("name");
					}
					out.append("<div align=\"center\">您好！");
					out.append(name + "</div>");
				} else {
					out.println("<div align=\"center\">您还未绑定，无法解绑！</div>");
					out.print("<script>setTimeout(close_wechat, 3000);</script>");
				}
			} catch (Exception e) {

			} finally {
				db.closecon();
				db = null;
			}
		%>




 <br />

		<div data-role="content"></div>
		<font color="#555C60" size="-1">
		<table width="100%" border="0" cellpadding="0">
			<tr>
				<td colspan="2">&nbsp;解除绑定说明</td>
				<tr>
					<td align="right">&nbsp;&nbsp;&nbsp;*&nbsp;</td>
					<td align="left">解除绑定后我们将把您的个人信息，包括用户名密码及个人自定义课表等从系统移除，请谨慎解绑</td>
				</tr>
				<tr>
					<td>&nbsp;&nbsp;&nbsp;*&nbsp;</td>
					<td align="left">解除绑定后您将不能使用【学生专区】和【教师专区】的所有功能，【公共服务】的所有功能还可以正常使用</td>
				</tr>
				<tr>
					<td>&nbsp;&nbsp;&nbsp;*&nbsp;</td>
					<td align="left">如对我们的产品有意见或建议，请在【公共服务】-【教学反馈】中写上您宝贵的意见。感谢您的使用！</td>
				</tr>
		</table>
		</font> <br />
		<br />
		<br />
		<form action="<%=basePath%>bining/doUnBining.jsp" method="get">
			<input type="hidden" name="openID"
				value="<%=request.getParameter("openID").toString()%>" />
			<input style="width: 90%" type="submit" value="确定解除绑定" />

		</form>


		<div id="footer">
			<hr color="#e1e1e1" style="width:85%" />
			<p align="center" style="color: #a1a1a1">
				备案号：<a style="color: #a1a1a1"
					href="http://www.miitbeian.gov.cn/state/outPortal/loginPortal.action">琼ICP备15003084号</a>
			</p>
		</div>
	</div>

</body>
</html>
