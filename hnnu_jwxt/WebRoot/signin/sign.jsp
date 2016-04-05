<%@page import="com.sun.org.apache.regexp.internal.recompile"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.lw.weixinjs.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'signin.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<%
	//微信JSSDK的AccessToken请求URL地址
	String weixin_jssdk_acceToken_url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx0442dc2fb1a7841e&secret=5fbf334424c28e63dcedb1e5e3e44283";
	// 微信JSSDK的ticket请求URL地址 
	String weixin_jssdk_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=fjyqlw&type=jsapi"; 
	
	Sign sign = new Sign();
	Map<String, String> ret = new HashMap<String, String>();
	ret=sign.getConfigMap();
	
	out.print(ret);
%>



<script type="text/javascript"
	src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
	function scanQRCode_wechat() {
		alert('点击扫一扫');
		wx.scanQRCode({
			needResult : 0, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
			scanType : [ "qrCode", "barCode" ], // 可以指定扫二维码还是一维码，默认二者都有
			success : function(res) {
				var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
			}
		});

	}
	function getLocation(){
		wx.getLocation({
		    type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
		    success: function (res) {
		        var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
		        var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
		        var speed = res.speed; // 速度，以米/每秒计
		        var accuracy = res.accuracy; // 位置精度
		        
		    }
		});	
	}
	function openLocation(){
		
		wx.openLocation({
		    latitude: 0, // 纬度，浮点数，范围为90 ~ -90
		    longitude: 0, // 经度，浮点数，范围为180 ~ -180。
		    name: '', // 位置名
		    address: '', // 地址详情说明
		    scale: 1, // 地图缩放级别,整形值,范围从1~28。默认为最大
		    infoUrl: '' // 在查看位置界面底部显示的超链接,可点击跳转
		});		

	}
</script>
<%

		out.print("<script type=\"text/javascript\"> wx.config({debug: true,appId: 'wx0442dc2fb1a7841e',timestamp: "+ret.get("timestamp")+",nonceStr: '"+ret.get("nonceStr")+"',signature: '"+ret.get("signature").toString()+"',jsApiList: ['scanQRCode','getLocation','openLocation']});wx.ready(function(){   });wx.error(function(res){  });</script>");
	//out.print("wx.config({debug: true,appId: 'wx0442dc2fb1a7841e',timestamp: "+ret.get("timestamp")+",nonceStr: '"+ret.get("nonceStr")+"',signature: '"+ret.get("signature").toString()+"',jsApiList: ['scanQRCode']});");
%>



<script type="text/javascript">
	var timestamp = $("#timestamp").val();//时间戳
	var nonceStr = $("#nonceStr").val();//随机串
	var signature = $("#signature").val();//签名
</script>





</head>

<body>

	This is my JSP page.
	<br>
	<input type="text" id="timestamp" value="${timestamp}" />

	<input type="text" id="nonceStr" value="${nonceStr}" />

	<input type="text" id="signature" value="${signature}" />


	<input type="button" onclick="scanQRCode_wechat()" value="扫一扫">
	<input type="button" onclick="getLocation()" value="位置">
	<input type="button" onclick="openLocation()" value="打开位置">
</body>
</html>
