<%@page import="com.lw.config.Methods"%>
<%@page import="com.lw.methods.RequestRESDelegate"%>
<%@page import="com.lw.methods.RequestRESService"%>
<%@page import="com.lw.errorinfo.ERROR_INFO"%>
<%@page import="com.lw.errorinfo.ERROR_KEY"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="org.apache.http.impl.client.DefaultHttpClient"%>
<%@page import="com.lw.teacherinfo.TeaInfo"%>
<%@page import="com.lw.cj.GetSore"%>
<%@page import="com.lw.db.DB"%>
<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ page import="java.sql.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.apache.http.client.CookieStore"%>
<%@page import="java.util.Map"%>
<%@ page import="com.lw.jiami.*" import="com.lw.login.*"%>
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
</head>

<body>
	<div data-role="page" id="pageone" align="center">


		<div data-role="content">
			<%
				int loginStatus = -1;
				String openID = "", Id = "", psd = "", name = "",cookie="",txtSecretCode="";
				openID = request.getParameter("openID");
				Id = request.getParameter("Id");
				psd = request.getParameter("psd");
				cookie=request.getParameter("cookie");
				txtSecretCode=request.getParameter("txtSecretCode");

				DefaultHttpClient client =(DefaultHttpClient)session.getAttribute("client");
				//----------------------------	

				Map bining_map = new HashMap();
				JSONObject outJson=null;
				JSONObject dataJson=null;
				Login login = new Login(null);
				int role=0;
				if(Id.length() > 10){
					role=0;
				}else{
					role=1;
				}
				try {

					switch(role) {
					case 0:
						outJson = login.login(Id, psd, "学生",txtSecretCode,client);
					//	out.print(outJson);
						switch(outJson.getInt(ERROR_KEY.ERROR)){
						  case ERROR_INFO.SUCCESS:
							  JSONObject inJson1=new JSONObject();
							    JSONObject dataJson2=new JSONObject();
							    name=outJson.getJSONObject(ERROR_KEY.DATA).getString("user");
							    inJson1.put(ERROR_KEY.METHOD, Methods.dbExecute_hnnujwc);
							    
							   psd= CryptAES.AES_Encrypt("LW#CWZ@HS_jwc&@@", psd);
							    
							  StringBuffer sql = new StringBuffer();
					             sql.append("insert into user(openID,id,role,name,psd) values('");
					             sql.append(openID);
					             sql.append("','");
					             sql.append(Id);
					             sql.append("',");
					             sql.append(role);
					             sql.append(",'");
					             sql.append(name);
					             sql.append("','");
					             sql.append(psd);
					             sql.append("')");
								    dataJson2.put("sql", sql.toString());
							    
							    inJson1.put(ERROR_KEY.PARAMTER, dataJson2);
							  RequestRESService rrs=new RequestRESService();
								RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
								JSONObject j1=new JSONObject();
								j1=JSONObject.fromObject( rrd.requestRES(inJson1.toString()));
								
								switch(j1.getInt(ERROR_KEY.ERROR)){
								case ERROR_INFO.SUCCESS:

									out.print("绑定成功");
									break;
								case ERROR_INFO.PROGRAM_ERROR:
									out.print(j1.getString(ERROR_KEY.MSG));
									break;
								}
							  
							  
							  JSONObject inJson=new JSONObject();
							  JSONObject dataJsonin=outJson.getJSONObject(ERROR_KEY.DATA);
							  inJson.put(ERROR_KEY.METHOD, "");
							  dataJsonin.put("btntype", "btn_zcj");
							  dataJsonin.put("btn", "成绩查询");
							  dataJsonin.put("kcxz", "");
							  dataJsonin.put("xn", "");
							  dataJsonin.put("xq", "");
							  dataJsonin.put("openID", openID);
							  dataJsonin.put("psd", psd);
							  dataJsonin.put("role", 0);
							  dataJsonin.put("openID", openID);
							  inJson.put(ERROR_KEY.PARAMTER, dataJsonin);
							  outJson.clear();

							  GetSore gSore=new GetSore(inJson,client.getCookieStore());
							  gSore.start();
							// outJson= gSore.getCj(inJson,client.getCookieStore()); 
							// out.print(outJson);
							  break;
						  case ERROR_INFO.PROGRAM_ERROR:
							  
							  break;
						}
						
						
					//	loginStatus = (Integer) bining_map.get("loginStatus");
					//	name = (String) bining_map.get("login_user");

						//-------------------------------------------	

						if (1 == loginStatus) {
						/*	Map cjMap = new HashMap();
							cjMap.put("openID", openID);
							cjMap.put("name", name);
							cjMap.put("Id", Id);
							cjMap.put("psd", psd);
							cjMap.put("isupdate", false);

							cjMap.put("btnType", "btn_zcj");
							cjMap.put("btnValue", "历年成绩");
							cjMap.put("kcxz", "");
							cjMap.put("xn", "");
							cjMap.put("xq", "");
							cjMap.put("cookieStore",
									(CookieStore) bining_map.get("cookieStore"));
							cjMap.put("url_cjcx",
									(String) bining_map.get("login_url"));
							cjMap.put("ip", (String) bining_map.get("ip"));
							Map cjMapResultMap = null;

							GetSore getSore = new GetSore(cjMap);
							getSore.start();
							DB db = new DB();
							db.createTb(Id);//建立成绩表
							db=null;
							*/
							out.print("<h3>绑定成功</h3>欢迎您！"
									+ name
									+ "<hr/><img alt=\"\" src=\"bining/check.png\" width=\"128\" height=\"128\"/>");
                                
						} else {
							out.print("<h3>绑定失败</h3>请检查您的用户名密码，或者您已经绑定过了<hr/><img alt=\"\" src=\"bining/cha.png\" width=\"128\" height=\"128\"/><a href=\"#\" data-role=\"button\" data-rel=\"back\">返回上一页</a>");

						}
						break;
					case 1:
					/*	bining_map = login.login(Id, psd, "教师");
						loginStatus = (Integer) bining_map.get("loginStatus");
						name = (String) bining_map.get("login_user");

						if (1 == loginStatus) {
							Map tchMap = new HashMap();
							tchMap.put("cookieStore",
									(CookieStore) bining_map.get("cookieStore"));
							tchMap.put("login_url_teainfo",
									(String) bining_map.get("login_url_teainfo"));
							tchMap.put("ip", (String) bining_map.get("ip"));
							tchMap.put("openID", openID);
							tchMap.put("name", name);
							tchMap.put("Id", Id);
							tchMap.put("psd", psd);
							tchMap.put("isupdate", false);

							TeaInfo teaInfo = new TeaInfo(tchMap);
							teaInfo.start();
							out.print("<h3>绑定成功</h3>欢迎您！"
									+ name
									+ "<hr/><img alt=\"\" src=\"bining/check.png\" width=\"128\" height=\"128\"/>");

						} else {
							out.print("<h3>绑定失败</h3>请检查您的用户名密码，或者您已经绑定过了<hr/><img alt=\"\" src=\"bining/cha.png\" width=\"128\" height=\"128\"/><a href=\"#\" data-role=\"button\" data-rel=\"back\">返回上一页</a>");

						}
*/
break;
					}

				} catch (Exception e) {
					out.println("JSP_ERR_1:" + e.toString());
				} finally {
					bining_map.clear();
					bining_map = null;
					login=null;
					System.gc();
				}
			%>

			<input type="button" value="关闭页面" onclick="close_wechat();">
		</div>
	</div>
	<script>
		setTimeout(close_wechat, 1500);
	</script>
</body>
</html>
