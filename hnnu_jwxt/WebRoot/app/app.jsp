<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<title>应用</title>
<link rel="stylesheet" href="../jquerymobile/jquery.mobile-1.3.2.min.css">
<script src="../jquerymobile/jquery-1.8.3.min.js"></script>
<script src="../jquerymobile/jquery.mobile-1.3.2.min.js"></script>
<title>文件下载</title>
<style>
#weixin-tip {
	display: none;
	position: fixed;
	left: 0;
	top: 0;
	background: rgba(0,0,0,0.8);
	filter: alpha(opacity=80);
	width: 100%;
	height: 100%;
	z-index: 100;
}
#weixin-tip p {
	text-align: center;
	margin-top: 10%;
	padding: 0 5%;
	position: relative;
}
#weixin-tip .close {
	color: #fff;
	padding: 5px;
	font: bold 20px/24px simsun;
	text-shadow: 0 1px 0 #ddd;
	position: absolute;
	top: 0;
	left: 5%;
}
</style>


<script>
// var is_weixin = (function() { //判断微信UA
// 	var ua = navigator.userAgent.toLowerCase();
// 	if (ua.match(/MicroMessenger/i) == "micromessenger") {
// 		return true;
// 	} else {
// 		return false;
// 	}
// })();
var is_weixin = (function(){return navigator.userAgent.toLowerCase().indexOf('MicroMessenger') !== -1})();
window.onload = function() {
	var winHeight = typeof window.innerHeight != 'undefined' ? window.innerHeight : document.documentElement.clientHeight; //兼容IOS，不需要的可以去掉
	var btn = document.getElementById('J_weixin');
	var tip = document.getElementById('weixin-tip');
	var close = document.getElementById('close');
	if (is_weixin) {
		btn.onclick = function(e) {
			tip.style.height = winHeight + 'px'; //兼容IOS弹窗整屏
			tip.style.display = 'block';
			return false;
		}
		close.onclick = function() {
			tip.style.display = 'none';
		}
	}
}
</script>
</head>

<body>
<div data-role="page" id="pageone">
<div id="weixin-tip">
  <p><img src="img/tip.png" alt="微信扫描打开APP下载链接提示代码优化" alt="微信打开"/><span id="close" title="关闭" class="close">×</span></p>
</div>
  <ul data-role="listview" data-inset="true">
    <li data-icon="false" > <a href="#" > <img style="margin-top:6px;width:64px;height:64px" src="../app/img/ZXMicroCampus.png">
      <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="80%"><h1>掌心微校园</h1></td>
          <td rowspan="3"><a href="http://120.24.183.211/hnnu_jwxt/app/download/download_ZXMicroCampus.jsp" data-role="button" data-inline="true" data-mini="true" data-corners="false" data-shadow="false" >
            <div style="width:5px;">下&nbsp;载</div>
            </a></td>
        </tr>
        <tr>
          <td valign="bottom">学习生活 | 10.16MB</td>
        </tr>
        <tr>
          <td valign="top" >学习生活好帮手</td>
        </tr>
      </table>
      </a> </li>
    <li data-icon="false" > <a href="#" > <img style="margin-top:6px;width:64px;height:64px" src="../app/img/xyshz.png">
      <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="80%"><h1>校园拾荒站</h1></td>
          <td rowspan="3"><a href="http://mp.weixin.qq.com/mp/redirect?url=http://120.24.183.211/download/ZXMicroCampus.apk#weixin.qq.com#wechat_redirect" id="J_weixin" class="android-btn" data-role="button" data-inline="true" data-mini="true" data-corners="false" data-shadow="false" >
            <div style="width:5px; text-align" align="center">下&nbsp;载</div>
            </a></td>
        </tr>
        <tr>
          <td valign="bottom">社交通讯 | 10.5MB</td>
        </tr>
        <tr>
          <td valign="top" >校园拾荒站</td>
        </tr>
      </table>
      </a> </li>
  </ul>
  
</div>

</body>
</html>