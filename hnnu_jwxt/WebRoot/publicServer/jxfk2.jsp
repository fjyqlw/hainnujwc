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
			var frames2=window.parent.window.document.getElementById("ff1"); 
			frames2.contentWindow.reinitIframeEND();

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
		
		function submitfile(){
			//document.getElementById("form0").submit();
			window.parent.showLoader();
			var frames=window.parent.window.document.getElementById("ffile"); 
			frames.contentWindow.submitfrom();

		//	setTimeout(3000);
			//document.getElementById("fk").submit();
		//	var framesff1=window.parent.window.document.getElementById("ftext"); 
			//framesff1.contentWindow.submitfrom();
				}
		
		
	function submitfrom(filename) {
			var ff0=document.getElementById("fk");
			document.getElementById("attname").value=filename;
			ff0.submit();
		}
	
			
	</script>
	<div data-role="page">
		<form data-ajax="false" name="fk" id="fk" action="dojxfk.jsp" method="get" >
			<textarea data-corners="false" data-shadow="flase"
				style="height:90%;background-color:#FFF" name="content" rows="15"
				id="tmp" value="请输入您对我们教学工作的意见和建议！" placeholder="请输入您对我们教学工作的意见和建议！"
				class="{required:true,minlength:5,messages:

{required:'请输入您的建议！'}}"></textarea>

			<input data-corners="flase" data-mini="flase" type="button"
				id="btnSubmit" value="提交" onclick="submitfile()" >
				 
				<fieldset data-role="fieldcontain">
					<div style="text-align:center">
						<b>附加信息</b>
					</div>
				</fieldset>
				<fieldset data-role="fieldcontain">
					<label>选择要反馈的部门</label> <select name="department" id="department"
						data-native-menu="false" onchange="showxueyuan(this)">
						<option value="教务处">教务处</option>
						<option value="学院">学院</option>
						<option value="团委">团委</option>
						<option value="招生办公室">招生办公室</option>
						<option value="研究生处">研究生处</option>
						<option value="财务处">财务处</option>
						<option value="学生处">学生处</option>
						<option value="图书馆">图书馆</option>
						<option value="保卫处">保卫处</option>
						<option value="后勤管理处">后勤管理处</option>
						<option value="校园网络中心">校园网络中心</option>
						<option value="其它">其它</option>
					</select>

				</fieldset>


				<fieldset style="display:none" name="xueyuanselect"
					id="xueyuanselect">
					<label>选择学院</label> <select name="xueyuan"
						id="xueyuan" data-native-menu="false">
						<option value="信息学院">信息学院</option>
						<option value="教科院">教科院</option>
						<option value="团委">团委</option>
					</select>
				</fieldset>


				<fieldset data-role="fieldcontain">
					<label>您的邮箱地址（选填）</label> <input data-corners="flase"
						style="width:100%" type="email" name="email" id="email"
						placeholder="您的邮箱地址（选填）" />
				</fieldset>
				
				<input type="hidden" name="attname" id="attname" value=""/>
				</form>
			

                   
		<div align="center" data-role="content"></div>
	</div>
</body>
</html>