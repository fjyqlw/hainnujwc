<%@page import="sun.misc.BASE64Encoder"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.apache.http.HttpEntity"%>
<%@page import="org.apache.http.HttpResponse"%>
<%@page import="org.apache.http.client.methods.HttpGet"%>
<%@page import="org.apache.http.impl.client.DefaultHttpClient"%>
<%@page import="com.lw.dbpool.DBPhnnujwc"%>
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
			
			 DefaultHttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet("http://210.37.0.28/CheckCode.aspx");
			HttpResponse httpResponse = client.execute(get);
			
			HttpEntity entity = httpResponse.getEntity();
			
			String s=entity.getContent().toString();
			InputStream instream = entity.getContent();
			 byte[] bytes = new byte[instream.available()];
			 // 将文件中的内容读入到数组中
			 instream.read(bytes);
			 String strtype="data:text/html;base64,";
			 String imgstr="";
			 String strBase64 = new BASE64Encoder().encode(bytes);      //将字节流数组转换为字符串
			 instream.close(); 
			 imgstr=strtype+strBase64;
			 String cookie=client.getCookieStore().getCookies().get(0).getValue();
			session.setAttribute("client",client);
			 
		%>
		<div align="center">请输入您的教务系统用户名和密码</div>
		<div align="center" data-role="content">
			<form data-ajax="false" action="<%=basePath%>bining/doBining.jsp"
				method="get">
				<input type="hidden" name="openID"
					value="<%=request.getParameter("openID").toString()%>">
					<div data-role="controlgroup" data-type="vertical">



						<table id="t1" cellpadding="0" cellspacing="0" valign="top" border="0"
							style="width:100%;padding:0;">
							<tr>
								<td colspan="2"><input
									style="width:100%;height:100%;margin:0;" name="Id"
									placeholder="用户名" required="true" message="fff"
									value="201224010219" /></td>
							</tr>
							<tr>
								<td colspan="2"><input style="width:100%;height:100%"
									type="password" name="psd" placeholder="密码" required 
									value="fjyqlw7235@@" /></td>
							</tr>

							<tr>
								<td><input required="true" placeholder="验证码" valign="top" style="width:90%;"
									name="txtSecretCode" value="" /></td>
								<td><img id="yzmimg" style="width:100%;" src="<%=imgstr%>"
									alt="看不清，换一张" onclick="document.location.reload()"/></td>

							</tr>
						</table>
						<input type="hidden" name="cookie" value="<%=cookie%>" />
					</div> <br /> <font color="#555C60" size="-1">
					<div align="left">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="#5f5f5f"> <b>绑定说明</b></font>
						<ul>

							<li>每个微信号只能绑定一个教务在线账号
								<li>绑定后可方便查询成绩、课表等信息
									<li>若您在教务在线更改了密码或者在使用过程中出现异常，请先解绑后重新绑定方能正常使用功能
										<li>如需解绑定请到主界面点击【公共服务】-【更多功能】-【取消绑定】
						</ul>
					</div>
				</font>  <%
 	String openID = request.getParameter("openID");
 	String sql = "", Id = "", name = "";
 	int role = 0;
 	Connection conn = null;
 	Statement stmt = null;
 	ResultSet rs = null;
 	try {
 		sql = "select * from user where openID='" + openID + "'";
 		// rs=DBhnnujwc.getInstance().querySQL(sql);
 		conn = DBPhnnujwc.getPool().getConnection();
 		stmt = conn.createStatement();
 		rs = stmt.executeQuery(sql);

 		if (rs.next()) {//已绑定
 			out.append("<input type=\"submit\"  disabled value=\"您已绑定,如需重新绑定请先解绑!\">");
 			out.append("<script>setTimeout(close_wechat, 3000);</script>");
 		} else {//未绑定
 			out.append("<input type=\"submit\" value=\"一键绑定\">");
 		}
 	} catch (Exception e) {

 	} finally {
 		try {
 			rs.close();
 		} catch (Exception e) {

 		}
 		try {
 			stmt.close();
 		} catch (Exception e) {

 		}
 		try {
 			conn.close();
 		} catch (Exception e) {

 		}
 	}
 %>
				
			</form>

		</div>

<!-- 
		<div id="footer">
			<hr color="#e1e1e1" style="width:85%" />
			<p align="center" style="color: #a1a1a1">
				备案号：<a style="color: #a1a1a1"
					href="http://www.miitbeian.gov.cn/state/outPortal/loginPortal.action">琼ICP备15003084号</a>
			</p>
		</div>
		 -->
	</div>

</body>
</html>
