package com.lw.teacherinfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lw.db.DB;
import com.lw.login.HttpUtil;
import com.lw.login.Login;
import com.lw.urlresource.UrlResource;

public class TeaInfo extends Thread implements Callable<Map> {
	CookieStore cookieStore;
	DefaultHttpClient mHttpClient = new DefaultHttpClient();
	/** 教师用户信息 */
	Map teainfo_map = new HashMap();//
	/** 一门课程（最高成绩） */
	String sevIP = "210.37.0.27";
	private Map tchMap = null;

	public TeaInfo(Map tchMap) {
		this.tchMap = tchMap;
	}

	@Override
	public void run() {
		Map tchResultMap = null;
		try {
			//System.out.println("【tchMap】"+tchMap);
			tchResultMap = getTeaInfo((CookieStore) tchMap.get("cookieStore"),
					(String) tchMap.get("login_url_teainfo"), (String) tchMap.get("ip"));
			//System.out.println(tchResultMap);
			//System.out.println(tchResultMap);
			DB db = new DB();
			if (!(Boolean)tchMap.get("isupdate")) {
				db.insertUser((String) tchMap.get("openID"),
					(String) tchMap.get("name"), (String) tchMap.get("Id"),
					(String) tchMap.get("psd"), tchResultMap);
			}else {
				db.updateUserInfo(tchResultMap);
			}
			
			db = null;

		} catch (Exception e) {
			System.out.println("【TeaInfo-run】"+e.toString());
		}
	}

	public Map getTeaInfo(CookieStore cookies, String url_cjcx, String sevIP2) {
		sevIP = sevIP2;
		teainfo_map.put("cjStatus", -1);// 初始查询状态
		final String Url_cjcx, xh;
		final CookieStore cookieStore = cookies;

		Url_cjcx = url_cjcx;
		//System.out.println("Url_cjcx=" + Url_cjcx);
		//System.out.println();
		String xh1 = "";
		char[] ch = Url_cjcx.toCharArray();
		for (int i = 37; i < 43; i++) { // 获取教师工号
			xh1 = xh1 + ch[i];
		}
		xh = xh1;
		 System.out.println("教师工号=" + xh);
		teainfo_map.put("xh", xh);
		try {
			String __VIEWSTATE = "";
			StringTokenizer tokenizer = null;
			String ksInfo = "";
			String __VIEWSTATE_xscj_gc = "";
			mHttpClient.setCookieStore(cookieStore);
			if (__VIEWSTATE_xscj_gc == "") {
				try {
					ksInfo = HttpUtil.getUrl(Url_cjcx,// "http://210.37.0.59/xsxk.aspx?xh=201224010219&xm=练威&gnmkdm=N121207",
							mHttpClient, "http://" + sevIP
									+ "//js_main.aspx?xh=" + xh);
					// System.out.println(ksInfo);
				} catch (IOException e) {
					teainfo_map.put("cjStatus", 0);// 查询出错状态
					System.out.println("11111111" + e.toString());
				}

			}

			// ////////////////////////////////////////////////////////////////
			// 处理获取的分数页面
			Document cjdoc = Jsoup.parse(ksInfo);

			Map teainfo_map_1 = new HashMap();// 课程性质
			Map teainfo_map_2 = new HashMap();// 课程归属
			Map teainfo_map_3 = new HashMap();// 其他
			Map teainfo_map_4 = new HashMap();// 创新学分

			Elements rowElementstj3 = cjdoc.select("[id=Table1]");

			//
			// String imgurl=filterHtmlTeaInfoImg(ksInfo, Url_cjcx);
			// System.out.println(imgurl);
			// UrlResource urlResource=new UrlResource();
			// urlResource.saveUrlFile(imgurl, "D:\\1.jpg");

			Elements rowElementstj31 = rowElementstj3.get(0).select("tr");
			Elements elements1 = rowElementstj31.get(0).select("td");

			teainfo_map.put("0", elements1.get(3).text());// 姓名

			Elements elements2 = rowElementstj31.get(1).select("td");

			teainfo_map.put("1", elements2.get(1).select("[selected=selected]")
					.get(0).text());// 性别
			teainfo_map.put("2", elements2.get(3).select("[selected=selected]")
					.get(0).text());// 民族

			Elements elements3 = rowElementstj31.get(3).select("td");

			teainfo_map.put("3", elements3.get(1).select("[selected=selected]")
					.get(0).text());// 学院

			Elements elements4 = rowElementstj31.get(6).select("td");

			teainfo_map.put("4", elements4.get(1).select("[selected=selected]")
					.get(0).text());// 学历
			// teainfo_map.put("13", elements4.get(2).text());
			teainfo_map.put("5", elements4.get(3).select("[selected=selected]")
					.get(0).text());// 学位

			teainfo_map.put("cjStatus", 1);// 查询成功状态
		} catch (Exception e) {

			teainfo_map.put("cjStatus", 0);// 查询出错状态
			System.out.println("Java_GetTeaInfo出错======" + e.toString());
		}

		return teainfo_map;
	}

	private String filterHtmlTeaInfoImg(String source, String url_cjcx) {// 获取教师个人照片网址
		if (null == source) {
			return "";
		}
		// isc1=true;
		StringBuffer sff = new StringBuffer();
		String score[];
		int i = 0, j = 0;
		String html = source;
		Document doc = Jsoup.parse(html); // 把HTML代码加载到doc中 <span
		Elements links = doc.select("img[id=Image1]"); // 这是课程名，因为课程名的HTML标签事<td
		//System.out.println(links.size());
		for (Element link : links) {
			// 获取所要查询的URL,这里对应地址按钮的名字叫成绩查询

			// 获取所要查询的相对地址，获取相对的地址
			sff.append(link.attr("src"));
		}

		String str = sff.toString();
		//System.out.println("src:" + str);
		// 返回查询的URL,将主页地址与相对地址连接起来，同样这里的cjcxUrl可以定义在外面
		String cjcxUrl = url_cjcx.substring(0, 20) + str;

		return cjcxUrl;
	}

	@Override
	public Map call() {
		Map tchResultMap = null;
		try {
			tchResultMap = getTeaInfo((CookieStore) tchMap.get("cookieStore"),
					(String) tchMap.get("url_cjcx"), (String) tchMap.get("ip"));

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}
}
