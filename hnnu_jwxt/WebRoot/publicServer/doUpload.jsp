<%@page import="net.sf.json.JSONObject"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="com.lw.upload.*"%>
<%@ page import="java.text.*"%>
<html>
<head>
<title>upFile</title>
<script type="text/javascript">
var frames4 = window.parent.window.document.getElementById("ffile");
frames4.style.display = "none";


	function GetQueryString(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r != null)
			return unescape(r[2]);
		return null;
	}

	function submittextform(filename) {
		var frames3 = window.parent.window.document.getElementById("ftext");
		//frames.contentWindow.submittext(filename);

		frames3.contentWindow.submitfrom(filename);
	}
</script>
</head>
<body bgcolor="#ffffff">
	

	<%
	
		JSONObject json = Upload.doUpload(request);
	String filename ="-1";
		try {
			switch (json.getInt("error")) {
			case 0:
				out.print("上传成功！");
				 filename = json.getString("filename");
				out.print(filename);
			//	out.print("<script>setTimeout(submittextform(" + filename
			//			+ "),2000);</script>");
				break;
			case 1:
				out.print(json.getString("errorinfo"));
				break;
			default:
				break;
			}
		} catch (Exception e) {
			out.print(e.toString());
		}
	%>
	<script>
	var fn="<%=filename%>";
	submittextform(fn);
	
	</script>
</body>
</html>