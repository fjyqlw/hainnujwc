<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage="" import="java.util.*"
	import="com.lw.db.*"%>
<%@ page import="com.lw.upload.*" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />

<title>教学反馈</title>
<script type="text/javascript">
	
	function close_wechat_frame() {
		 window.parent.close_wechat();
		//WeixinJSBridge.call("closeWindow");

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
				request.setCharacterEncoding("utf-8");
				//out.println(yx + jy);
				Connection connection = null;
				try {
					String email = request.getParameter("email");
					String content = request.getParameter("content");
					String department=request.getParameter("department");
					String xueyuan="";
					String attname=request.getParameter("attname");
					if("学院".equals(department)){
						xueyuan=request.getParameter("xueyuan");
						//out.println("xueyuan="+xueyuan);
						department=xueyuan;
					}
					if(null==attname){
						attname="-1";
					}
					//department=xueyuan+department;
					
					String sql = "";
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					connection = DriverManager.getConnection(
							"jdbc:mysql://120.24.183.211:3306/hnnu_jwc",
							DB.sql_user, DB.sql_pwd);

					sql = "insert jxfk(email,content,department,attname)  values(?,?,?,?)";//
					PreparedStatement ps = connection.prepareStatement(sql);
					ps.setString(1, email);
					ps.setString(2, content);
					ps.setString(3, department);
					ps.setString(4, attname);
					ps.executeUpdate();
					connection.close();

					out.print("<h3>感谢您的反馈，我们会尽快处理！</h3><hr/><img alt=\"\" src=\"publicServer/check.png\" width=\"128\" height=\"128\"/>");

				} catch (Exception e) {

					out.print("<h3>反馈失败</h3>请检查您的网络状态<hr/><img alt=\"\" src=\"publicServer/cha.png\" width=\"128\" height=\"128\"/><a href=\"#\" data-role=\"button\" data-rel=\"back\">返回上一页</a>");

					out.println(e.toString());
				}
			%>

			<input type="button" value="关闭页面" onclick="close_wechat_frame()">
		</div>
	</div>
	<script>
	window.parent.hideLoader();
		setTimeout(close_wechat_frame, 2000);
	</script>
</body>
</html>