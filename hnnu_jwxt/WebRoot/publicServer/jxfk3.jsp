<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-

mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>反馈系统(开发中...)</title>
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
		function showxueyuan(obj) {
			//var input=document.getElementById( "deparment"); 
			var selatt = $(obj).find("option:selected").text();
			if ("学院" == selatt) {
				displayShowUI();
			} else {
				displayHideUI();
			}

		}
		//
		function displayHideUI() {
			var ui = document.getElementById("xueyuanselect");
			ui.style.display = "none";
		}
		function displayShowUI() {
			var ui = document.getElementById("xueyuanselect");
			ui.style.display = "block";
		}
		
		function submitfrom() {
			
			var ff0=document.getElementById("form0");
			//ff0.action="doUpload.jsp?timestamp="+timestamp;
			ff0.submit();
		}
		
	</script>
	<div data-role="page">

		<fieldset data-role="fieldcontain">
			<label>选择选择附件（选填,大小在5MB以内）</label>
			<form data-ajax="false" name="form0" id="form0" action="doUpload.jsp"
				method="post" enctype="multipart/form-data">
				<input style="width:100%;;" type="file" name="file0" id="file0"
					size="50" />
				<br> <img src="" style="width:100%" id="img0">
			</form>
		</fieldset>
		<script>
			$("#file0").change(function() {
				var objUrl = getObjectURL(this.files[0]);
				console.log("objUrl = " + objUrl);
				if (objUrl) {
					$("#img0").attr("src", objUrl);
				}
			});

			function getObjectURL(file) {
				var url = null;
				if (window.createObjectURL != undefined) { // basic
					url = window.createObjectURL(file);
				} else if (window.URL != undefined) { // mozilla(firefox)
					url = window.URL.createObjectURL(file);
				} else if (window.webkitURL != undefined) { // webkit or chrome
					url = window.webkitURL.createObjectURL(file);
				}
				return url;
			}
		</script>

		<div align="center" data-role="content"></div>
	</div>
</body>
</html>