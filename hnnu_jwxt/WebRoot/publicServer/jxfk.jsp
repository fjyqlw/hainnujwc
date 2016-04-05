<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>反馈系统</title>
<link rel="stylesheet"
	href="../jquerymobile/jquery.mobile-1.3.2.min.css">
	<script src="../jquerymobile/jquery-1.8.3.min.js"></script>
	<script src="../jquerymobile/jquery.mobile-1.3.2.min.js"></script>
	<script src="../jquerymobile/jquery.validate.min.js"></script>
	<script src="../jquerymobile/jquery.metadata.js"></script>
</head>

<body>
	<script type="text/javascript" language="javascript">
		function reinitIframe() {
			var iframe = document.getElementById("ftext");
			try {
				var bHeight = iframe.contentWindow.document.body.scrollHeight;
				var dHeight = iframe.contentWindow.document.documentElement.scrollHeight;
				var height = Math.max(bHeight, dHeight);
				iframe.height = height;
			} catch (ex) {
			}
		}

		var timer1 = window.setInterval("reinitIframe()", 500); //定时开始  

		function reinitIframeEND() {
			var iframe = document.getElementById("ftext");
			try {
				var bHeight = iframe.contentWindow.document.body.scrollHeight;
				var dHeight = iframe.contentWindow.document.documentElement.scrollHeight;
				var height = Math.max(bHeight, dHeight);
				iframe.height = height;
			} catch (ex) {
			}
			// 停止定时  
			window.clearInterval(timer1);

		}
	</script>
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

		function submitfile() {//
			var frames = window.parent.window.document.getElementById("ffile");
			frames.contentWindow.submitfrom();

		}
		function submittext(filename) {
			alert("frame-submittext-" + filename);
			var framesff1 = window.parent.window.document
					.getElementById("ftext");
			framesff1.contentWindow.submitfrom(filename);
		}

		function displayHideFFileUI() {
			var ui = document.getElementById("ffile");
			ui.style.display = "none";
		}
		function displayShowFFileUI() {
			var ui = document.getElementById("ffile");
			ui.style.display = "block";
		}

		function close_wechat() {

			WeixinJSBridge.call("closeWindow");

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
	<div data-role="page">
		<iframe id="ftext" src="jxfk2.jsp" style="width:100%" scrolling="no"
			frameborder="0" onLoad="reinitIframeEND();"></iframe>
		<!-- 
			
			<input data-corners="flase" data-mini="flase" type="button"
				id="btnSubmit" value="提交" onclick="submitfile()" >
 -->



		<iframe id="ffile" src="jxfk3.jsp" style="width:100%;height:20%"
			scrolling="yes" frameborder="0"></iframe>

		<div align="center" data-role="content"></div>
	</div>
</body>
</html>