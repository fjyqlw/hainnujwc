package com.lw.login;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.*;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.*;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sun.misc.BASE64Encoder;

import com.lw.cj.GetBuKao;
import com.lw.cj.GetSore;
import com.lw.classrooms.GetClassRooms;
import com.lw.classrooms.GetClassRoomsTch;
import com.lw.config.Methods;
import com.lw.db.DB;
import com.lw.db.SevSch;
import com.lw.errorinfo.ERROR_INFO;
import com.lw.errorinfo.ERROR_KEY;
import com.lw.methods.RequestRESDelegate;
import com.lw.methods.RequestRESService;
import com.lw.teacherinfo.TeaInfo;
import com.lw.xuanke.GetXuanKe;

public class Login extends Thread implements Callable<Map> {

	String cookie = null;
	String mianUrl = null;
	int isc1 = 1, isc2 = 1;
	/** 使用登录模块的用途（使用参数：1、绑定;2、成绩统计；3、更新成绩；4、补考确认;5、选课查询;6、教师个人简历;7、空教室查询） */
	// static int loginType = 1;
	CookieStore cookieStore;
	/** 全局变量，用来存储基础数据 */
	//Map hp = new HashMap();
	/** 全局变量，查教室相关 */
	Map classrooMap = new HashMap();
	Map sev_map = new HashMap();
	SevSch sevSch = new SevSch();
	private Map loginMap;

	public Login(Map loginMap) {
		this.loginMap = loginMap;
	}

	@Override
	public void run() {// 用于后台

		// Map loginResultMap = login((String) loginMap.get("xh"),
		// (String) loginMap.get("psd"), (String) loginMap.get("usertype"));

	}

	/**
	 * 登录模块
	 * 
	 * @param xh
	 *            学号
	 * @param psd
	 *            密码
	 * @param usertype
	 *            用户类型
	 * @param logintype
	 *            使用登录模块的用途（使用参数：1、绑定;2、成绩统计；3、更新成绩；4、补考确认;5、选课查询;6、教师个人简历;7、
	 *            更新教室个人简历;8、空教室查询）
	 */
	/**
	 * @param xh
	 * @param psd
	 * @param usertype
	 * @param txtSecretCode
	 * @param cookiein
	 * @return
	 */
	/**
	 * @param xh
	 * @param psd
	 * @param usertype
	 * @param txtSecretCode
	 * @param cookiein
	 * @return
	 */
	/**
	 * @param xh
	 * @param psd
	 * @param usertype
	 * @param txtSecretCode
	 * @param cookiein
	 * @return
	 */
	/**
	 * @param xh
	 * @param psd
	 * @param usertype
	 * @param txtSecretCode
	 * @param cookiein
	 * @return
	 */
	public JSONObject login(final String xh, final String psd, final String usertype,
			final String txtSecretCode, final DefaultHttpClient client) {
		//hp.put("loginStatus", -1);
		JSONObject outJson=new JSONObject();
		JSONObject datdaJson=new JSONObject();
        String ServerIP="",viewstate="";
	//	System.out.println("cookie=" + "ASP.NET_SessionId=" + cookiein);
		//final DefaultHttpClient client = new DefaultHttpClient();
		/** 获取教务在线入口服务器 */
		try {
			sev_map = sevSch.getSev();
			JSONObject inJsonsev=new JSONObject();
			 RequestRESService rrs=new RequestRESService();
				RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
				inJsonsev.put(ERROR_KEY.METHOD, Methods.achieveServer);
				
				JSONObject j1=new JSONObject();
				j1=JSONObject.fromObject( rrd.requestRES(inJsonsev.toString()));
				ServerIP=j1.getJSONObject(ERROR_KEY.DATA).getString("sevip");
				viewstate=j1.getJSONObject(ERROR_KEY.DATA).getString("viewstate");
		} catch (Exception e) {
			sev_map.put("ip", "210.37.0.27");
			sev_map.put("viewstate",
					"dDwyODE2NTM0OTg7Oz6HUJqmDIQtFOzZEhCpCbCAYzXb1Q==");
			ServerIP="http://210.37.0.27/";
			viewstate="dDwyODE2NTM0OTg7Oz6HUJqmDIQtFOzZEhCpCbCAYzXb1Q==";
		} finally {
			// System.out.println("当前服务器：" + (String) sev_map.get("ip"));
		//	hp.putAll(sev_map);
		}

		datdaJson.put("sevip", "http://210.37.0.28/");
		try {
			/** 登录模块开始 */
			/** 更新教务在线入口使用数量 */
			// sevSch.startSev((String) hp.get("ip"));
			System.out.println("----1----");

			
//			HttpGet get0 = new HttpGet("http://210.37.0.28/default2.aspx");
//			HttpResponse httpResponse0 = client.execute(get0);
//			EntityUtils.consume(httpResponse0.getEntity()); // 释放
		//	EntityUtils.consume(get0.get); // 释放


			System.out.println("【1】" + client.getCookieStore());
			HttpPost httpPost = new HttpPost(ServerIP+"default2.aspx"); // 建立一个Post请求，第一步的方法是Post方法嘛

			System.out.println("----2----"
					+ client.getCookieStore().getCookies());
			// 禁止重定向，由于刚刚Post的状态值是重定向，所以我们要去禁止它，不然网页会乱飞。
			httpPost.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS,
					false);
			// 设置头部信息（头部信息在刚刚的Httpwatch下面Headers标签会有，不过我感觉写多跟写少没多大区别，只是多写没有坏处吧。）
			httpPost.setHeader("User-Agent",
					"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded");

//			try {
//				// HttpGet httpget = new HttpGet("http://" + (String)
//				//
//				// hp.get("ip") + "/CheckCode.aspx");
//
//				HttpGet get = new HttpGet("http://210.37.0.28/CheckCode.aspx");
//				HttpResponse httpResponse = client.execute(get);
//
//				System.out.println("【3】" + client.getCookieStore());
//				HttpEntity entity = httpResponse.getEntity();
//
//				cookie = "ASP.NET_SessionId="
//						+ client.getCookieStore().getCookies().get(0)
//								.getValue();
//				System.out.println("----getCookies----" + cookie);
//
//				if (entity != null) { // 读取内容 String picName = "/ck.bmp";
//					InputStream instream = entity.getContent();
//
//					OutputStream outStream = new FileOutputStream("C:\\ck.bmp");
//					IOUtils.copy(instream, outStream);
//					outStream.close();
//				}
//
//				EntityUtils.consume(httpResponse.getEntity()); // 释放
//			} catch (Exception e) { // TODO: handle exception
//				System.out.println("获取验证码出错："+e.toString());
//			}
//			System.out.println(client);
//			Scanner in = new
//
//			Scanner(System.in);
//			String ck = in.next();
//			System.out.println("ck=" + ck);

			System.out.println("------3-1------");
			// 第一种模拟登陆传值
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			/**
			 * 以下三个数据就是我们的之前在POST里的数据，不用在意验证码
			 */
			params.add(new BasicNameValuePair("__VIEWSTATE",viewstate)); // xxx表示要提交的内容，需要自己获取
			params.add(new BasicNameValuePair("Button1", ""));
			params.add(new BasicNameValuePair("hidPdrs", ""));
			params.add(new BasicNameValuePair("hidsc", ""));
			params.add(new BasicNameValuePair("lbLanguage", ""));
			params.add(new BasicNameValuePair("RadioButtonList1", usertype));
			params.add(new BasicNameValuePair("TextBox2", psd));
			params.add(new BasicNameValuePair("txtSecretCode", txtSecretCode));// txtSecretCode
			params.add(new BasicNameValuePair("txtUserName", xh));
			// 传递参数的时候注意编码使用,否则乱码
			httpPost.setEntity(new UrlEncodedFormEntity(params, "GBK"));
			// 响应请求
			HttpResponse response = client.execute(httpPost);

			// 获取响应状态码
			int Status = response.getStatusLine().getStatusCode();
			System.out.println("----4----Status=" + Status);
			// 302表示重定向状态
			if (Status == 302 || Status == 301) {

				System.out.println(response.toString());
				// 获取响应的cookie值
				// cookie = response.getFirstHeader("Set-Cookie").getValue();
			//	hp.put("cookie", cookie);
				cookieStore = client.getCookieStore();
				// response.getFirstHeader("Set-Cookie").getValue();
				System.out.println("【login-cookieStore】"
						+ cookieStore.getCookies().toString());

				// 获取头部信息中Location的值
				String location = response.getFirstHeader("Location")
						.getValue();

				mianUrl = ServerIP+ location;
				//System.out.println("----5--mianUrl=" + mianUrl);
			}
			EntityUtils.consume(response.getEntity()); // 释放
			EntityUtils.consume(httpPost.getEntity()); // 释放
			HttpGet get = new HttpGet(mianUrl);
			cookie="ASP.NET_SessionId="+client.getCookieStore().getCookies().get(0).getValue();
			// 关键点,这里有个关键点就是设置头部信息中的Referer和cookie值，cookie值大家都知道，模拟登陆的时候必须带着cookie一起访问，但是Referer我无法理解，但必须要设置。
			// 也就是必须指定它的Referer必须为当前访问的URL
			get.setHeader("Referer", mianUrl);
			get.addHeader("Cookie", cookie);

			// 获取Get响应，如果状态码是200的话表示连接成功
			HttpResponse httpResponse = new DefaultHttpClient().execute(get);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {//登录成功
				HttpEntity entity = httpResponse.getEntity();
				// 获取纯净的主页HTML源码，这里大家可以将mianhtml定义在其他地方
				String mainhtml = EntityUtils.toString(entity);
			//	System.out.println(mainhtml);
				System.out.println("----666666666666----");
				
				JSONObject j1= filterHtml(mainhtml);// 各种查询URL
				
				outJson.put(ERROR_KEY.ERROR, j1.getInt(ERROR_KEY.ERROR));
				datdaJson.putAll(j1.getJSONObject(ERROR_KEY.DATA));
				//String login_url_cjcx 
				
				
				
			//	String login_user = filterHtml2(mainhtml);

			//	login_user = login_user.substring(0, login_user.length() - 2);
			//	hp.put("loginStatus", 1);
			//	hp.put("cookieStore", cookieStore);
			//	hp.put("login_user", login_user);
			//	System.out.println("登录用户:" + login_user);
				System.out.println("登录学号:" + xh);
			//	System.out.println("需要的网址:" + hp);

			}

			get.abort();// 断开连接
			get = null;
			/** 登录模块完成 */
//			datdaJson.put("client", client);
//			datdaJson.put("cookiestore", client.getCookieStore());
			datdaJson.put("xh", xh);
			outJson.put(ERROR_KEY.ERROR, ERROR_INFO.SUCCESS);
			outJson.put(ERROR_KEY.DATA, datdaJson);

		} catch (Exception e) {
			outJson.put(ERROR_KEY.ERROR, ERROR_INFO.PROGRAM_ERROR);
			outJson.put(ERROR_KEY.MSG, e.toString());
//			hp.put("loginStatus", 0);
			System.out.println("【程序出错：Java_Login出错】" + e.toString());
		} finally {
			client.close();
		//	sevSch.endSev((String) hp.get("ip"));
			System.out.println("最终返回的数据：\n"+outJson);
		}

		return outJson;
	}

	private JSONObject filterHtml(String source) {// 获取需要的查询网址
		JSONObject outJson=new JSONObject();
		JSONObject datdaJson=new JSONObject();
		if (null == source) {

			outJson.put(ERROR_KEY.ERROR, ERROR_INFO.PROGRAM_ERROR);
			outJson.put(ERROR_KEY.MSG, "source=null");
			return outJson;
		}
		try {
			
		
		String html = source;
		Document doc = Jsoup.parse(html); // 把HTML代码加载到doc中 <span
		
		Elements links_user = doc.select("span[id=xhxm]");
		String userString=links_user.text();
		userString=userString.substring(0, userString.length()-2);
System.out.println("userString="+userString);

      datdaJson.put("user", userString);
		
		
		Elements links = doc.select("a[href]"); // 这是课程名，因为课程名的HTML标签事<td
//		hp.put("login_url", "no");
//		hp.put("login_url_xuanke", "no");
//		hp.put("login_url_teainfo", "no");
//		hp.put("login_url_classroom", "no");
		for (Element link : links) {
			// 获取所要查询的URL,这里对应地址按钮的名字叫成绩查询
			if (link.text().equals("成绩查询")) {
				// 获取所要查询的相对地址，获取相对的地址
				// 返回查询的URL,将主页地址与相对地址连接起来，同样这里的cjcxUrl可以定义在外面
//				String cjcxUrl = "http://" + (String) hp.get("ip") + "//"
//						+ link.attr("href");
				datdaJson.put("cjcx", link.attr("href"));
				//hp.put("login_url", cjcxUrl);
			}
			if (link.text().equals("专业选修课")) {
				// 获取所要查询的相对地址，获取相对的地址
				// 返回查询的URL,将主页地址与相对地址连接起来，同样这里的cjcxUrl可以定义在外面
//				String cjcxUrl = "http://" + (String) hp.get("ip") + "//"
//						+ link.attr("href");
				//hp.put("login_url_xuanke", cjcxUrl);
				datdaJson.put("zyxxk", link.attr("href"));
			}
			if (link.text().equals("个人简历")) {
				// 获取所要查询的相对地址，获取相对的地址
				// 返回查询的URL,将主页地址与相对地址连接起来，同样这里的cjcxUrl可以定义在外面
//				String cjcxUrl = "http://" + (String) hp.get("ip") + "//"
//						+ link.attr("href");
			//	hp.put("login_url_teainfo", cjcxUrl);
				datdaJson.put("grjl", link.attr("href"));
			}
			if (link.text().equals("教室查询")) {
				// 获取所要查询的相对地址，获取相对的地址
				// 返回查询的URL,将主页地址与相对地址连接起来，同样这里的cjcxUrl可以定义在外面
//				String cjcxUrl = "http://" + (String) hp.get("ip") + "//"
//						+ link.attr("href");
				//hp.put("login_url_classroom", cjcxUrl);
				datdaJson.put("jscx", link.attr("href"));
			}

		}
			outJson.put(ERROR_KEY.DATA, datdaJson);
			outJson.put(ERROR_KEY.ERROR, ERROR_INFO.SUCCESS);
		} catch (Exception e) {
			outJson.put(ERROR_KEY.ERROR, ERROR_INFO.PROGRAM_ERROR);
			outJson.put(ERROR_KEY.MSG, e.toString());
		}finally{
			System.out.println(outJson);
		}
		return outJson;// cjcxUrl
	}

//	private String filterHtml2(String source) {
//		if (null == source) {
//			return "";
//		}
//		// isc2=true;
//		StringBuffer sff = new StringBuffer();
//		String score[];
//		int i = 0, j = 0;
//		String html = source;
//		Document doc = Jsoup.parse(html); // 把HTML代码加载到doc中 <span
//											// id="xhxm"><span id="Label3">
//		Elements links_user = doc.select("span[id=xhxm]");
//		for (Element link : links_user) {
//			sff.append(link.text()); // .append(" : ").append("\n")
//			j = j + 2; // 这里之所以+2是因为分数的标签是<td width=5%
//		}
//		html = sff.toString();
//		return html;
//	}

	public static void main(String[] a) {

	}

	@Override
	public Map call() {
		Map loginResultMap = null;
		// System.out.println(loginMap);
		try {

			// loginResultMap = login((String) loginMap.get("xh"),
			// (String) loginMap.get("psd"),
			// (String) loginMap.get("usertype"));
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return loginResultMap;
	}

	public String ioToBase64() throws IOException {
		String fileName = "d:/gril.gif"; // 源文件
		String strBase64 = null;
		try {
			InputStream in = new FileInputStream(fileName);
			// in.available()返回文件的字节长度
			byte[] bytes = new byte[in.available()];
			// 将文件中的内容读入到数组中
			in.read(bytes);
			strBase64 = new BASE64Encoder().encode(bytes); // 将字节流数组转换为字符串
			in.close();
		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return strBase64;
	}
}
