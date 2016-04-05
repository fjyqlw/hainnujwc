<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript">
window.onload = function(){
    if(!isWeiXin()){
    location.assign("error/noWeiXin.html");
       // var p = document.getElementsByTagName('p');
        //"byshisss".innerHTML = window.navigator.userAgent;
    }
}
function isWeiXin(){
    var ua = window.navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i) == 'micromessenger'){
        return true;
    }else{
        return false;
    }
}

</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学生补考名单确认</title>

<script src="SpryAssets/SpryTabbedPanels.js" type="text/javascript"></script>
<link href="SpryAssets/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css" />
</head>

<body marginwidth="0" marginheight="0">

	<div id="TabbedPanels1" class="TabbedPanels">
		<ul class="TabbedPanelsTabGroup">
			<li class="TabbedPanelsTab" tabindex="0">需报名确定的补考课程</li>
			<li class="TabbedPanelsTab" tabindex="0">已报名确定的补考课程</li>
		</ul>
		<div class="TabbedPanelsContentGroup">
			<div class="TabbedPanelsContent">
				<table width="100%" border="1" bordercolor="#969B9E"  align="center" cellspacing="0">
					<tr>
						<td width="25%" align="center" valign="middle" nowrap="nowrap">课程代码</td>
						<td width="75%">&nbsp;</td>
					</tr>
					<tr>
						<td align="center" valign="middle" nowrap="nowrap">课程名称</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td align="center" valign="middle" nowrap="nowrap">学分</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td align="center" valign="middle" nowrap="nowrap">课程性质</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td align="center" valign="middle" nowrap="nowrap">总评成绩</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td align="center" valign="middle" nowrap="nowrap">补考学年</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td align="center" valign="middle" nowrap="nowrap">补考学期</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td align="center" valign="middle" nowrap="nowrap">选课课号</td>
						<td>&nbsp;</td>
					</tr>

				</table>
			</div>
			<div class="TabbedPanelsContent">
				<table width="100%" border="1" bordercolor="#969B9E"  align="center" cellspacing="0">
					<tr>
						<td width="25%" align="center" valign="middle" nowrap="nowrap">课程代码</td>
						<td width="75%">01002018</td>
					</tr>
					<tr>
						<td align="center" valign="middle" nowrap="nowrap">课程名称</td>
						<td>大众理财</td>
					</tr>
					<tr>
						<td align="center" valign="middle" nowrap="nowrap">学分</td>
						<td>1.0</td>
					</tr>
					<tr>
						<td align="center" valign="middle" nowrap="nowrap">课程性质</td>
						<td>公选(人文)</td>
					</tr>
					<tr>
						<td align="center" valign="middle" nowrap="nowrap">总评成绩</td>
						<td>12</td>
					</tr>
					<tr>
						<td align="center" valign="middle" nowrap="nowrap">补考学年</td>
						<td>2015-2016</td>
					</tr>
					<tr>
						<td align="center" valign="middle" nowrap="nowrap">补考学期</td>
						<td>1</td>
					</tr>
					<tr>
						<td align="center" valign="middle" nowrap="nowrap">选课课号</td>
						<td>(2014-2015-2)-01002018-01058-1</td>
					</tr>

				</table>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1", {
			defaultTab : 1
		});
	</script>
</body>
</html>