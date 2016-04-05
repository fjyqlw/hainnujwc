<%@ page contentType="application/x-msdownload" language="java"
	import="java.sql.*" import="java.io.*" import="java.net.*"
	pageEncoding="gb2312"%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<title>掌心微校园下载</title>

</head>

<body>
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
<a id="J_weixin" class="android-btn" href="#"><img src="android-btn.png" alt="微信扫描打开APP下载链接提示代码优化" alt="安卓版下载" /></a>
<div id="weixin-tip">
  <p><img src="../img/tip.png" alt="微信扫描打开APP下载链接提示代码优化" alt="微信打开"/><span id="close" title="关闭" class="close">×</span></p>
</div>
	<%
	  response.reset();//可以加也可以不加
    response.setContentType("application/x-download");
    String filedownload = "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.0\\webapps\\download\\ZXMicroCampus.apk";
    String filedisplay = "ZXMicroCampus.apk";
    filedisplay = URLEncoder.encode(filedisplay,"UTF-8");
    response.addHeader("Content-Disposition","attachment;filename=" + filedisplay);

    OutputStream outp = null;
    FileInputStream in = null;
    try
    {
        outp = response.getOutputStream();
        in = new FileInputStream(filedownload);

        byte[] b = new byte[1024];
        int i = 0;

        while((i = in.read(b)) > 0)
        {
            outp.write(b, 0, i);
        }
        outp.flush();
    }
    catch(Exception e)
    {
        System.out.println("Error!");
        e.printStackTrace();
    }
    finally
    {
        if(in != null)
        {
            in.close();
            in = null;
        }
        if(outp != null)
        {
            outp.close();
            outp = null;
        }
    }
	%>
</body>
</html>