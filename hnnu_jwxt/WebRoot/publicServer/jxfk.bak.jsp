<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>教学反馈</title>
<link rel="stylesheet"
	href="../jquerymobile/jquery.mobile-1.3.2.min.css">
	<script src="../jquerymobile/jquery-1.8.3.min.js"></script>
	<script src="../jquerymobile/jquery.mobile-1.3.2.min.js"></script>
	<script src="../jquerymobile/jquery.validate.min.js"></script>
	<script src="../jquerymobile/jquery.metadata.js"></script>
</head>

<body>
	<script>
		$().ready(function() {
			$("#fk").validate();
		});
	</script>


	<div data-role="page">


		<div align="center" data-role="content">
			<form data-ajax="false" id="fk" action="dojxfk.jsp" method="get">
				<input style="width:100%" type="email" name="yx" id="email"
					placeholder="您的邮箱地址（选填）"> <textarea style="height:90%"
						name="jy" rows="15" id="tmp" value="请输入您对我们教学工作的意见和建议！"
						placeholder="请输入您对我们教学工作的意见和建议！"
						class="{required:true,minlength:5,messages:{required:'请输入您的建议！'}}"></textarea>
					<input type="submit" id="btnSubmit" value="提交">
			</form>

		</div>
	</div>
</body>
</html>