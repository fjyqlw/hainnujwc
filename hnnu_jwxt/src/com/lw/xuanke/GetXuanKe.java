package com.lw.xuanke;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.lw.login.Login;

public class GetXuanKe implements Callable<Map> {
	CookieStore cookieStore;
	DefaultHttpClient mHttpClient = new DefaultHttpClient();
	/** 一门课程（历年成绩、学期/学年成绩通用） */
	Map cj_map = new HashMap();//
	/** 一门课程（最高成绩） */
	Map zgcj_map = new HashMap();//
	String sevIP = "210.37.0.27";
	private Map xuankeMap = null;

	public GetXuanKe(Map xuankeMap) {
		this.xuankeMap = xuankeMap;
	}

	public Map getXuanKe(Map xuankeMap) {
		sevIP = (String) xuankeMap.get("ip");
		cj_map.put("cjStatus", -1);// 初始查询状态

		final String Cookies, Url_cjcx, xh;
		final CookieStore cookieStore = (CookieStore) xuankeMap
				.get("cookieStore");
		
		Url_cjcx = (String) xuankeMap.get("login_url_xuanke");
		String xh1 = "";
		char[] ch = Url_cjcx.toCharArray();
		for (int i = 34; i < 47; i++) { // 获取学号
			xh1 = xh1 + ch[i];
		}
		xh = xh1;
		cj_map.put("xh", xh);
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
									+ "//xs_main.aspx?xh=" + xh);
					// System.out.println(ksInfo);
				} catch (IOException e) {
					cj_map.put("cjStatus", 0);// 查询出错状态
					System.out.println("11111111" + e.toString());
				}

			}

			// ////////////////////////////////////////////////////////////////
			// 处理获取的分数页面
			Document cjdoc = Jsoup.parse(ksInfo);

			// 获取到每行数据的选择器,这里的选择器你们可以观察下HTML代码，这里就不多说了。
			String rowRegex = "div.main_box div.mid_box span.formbox table#Datagrid1.datelist tbody tr";
			// 每行的数据元素
			Elements rowElements = cjdoc.select(rowRegex);

			// 获取到每行数据的选择器,这里的选择器你们可以观察下HTML代码，这里就不多说了。
			String rowRegex2 = "[class=datelist][id=Datagrid3]";
			// 每行的数据元素
			Elements rowElements2 = cjdoc.select(rowRegex2);
			Elements rowElements3 = rowElements2.select("tr");

			Map cj_map_1 = new HashMap();// 课程性质
			Map cj_map_2 = new HashMap();// 课程归属
			Map cj_map_3 = new HashMap();// 其他
			Map cj_map_4 = new HashMap();// 创新学分

			Elements rowElementstj3 = cjdoc.select("[class=datelist]");

			Elements rowElementstj31 = rowElementstj3.get(0).select("tr");
			for (int i = 0; i < rowElementstj31.size() - 1; i++) {
				Elements elements = rowElementstj31.get(i).select("td");

				Map zcj_info_map = new HashMap();// 课程具体成绩
				zcj_info_map.put("0", elements.get(0).text());// 加入三级级HashMap
				zcj_info_map.put("1", elements.get(1).text());
				zcj_info_map.put("2", elements.get(2).text());
				zcj_info_map.put("4", elements.get(4).text());
				zcj_info_map.put("5", elements.get(5).text());
				zcj_info_map.put("6", elements.get(6).text());
				zcj_info_map.put("8", elements.get(8).text());
				zcj_info_map.put("9", elements.get(9).text());

				cj_map.put("zcj_" + i, zcj_info_map);// 加入二级HashMap
				zcj_info_map = null;
			}

			cj_map.put("cjStatus", 1);// 查询成功状态
			return cj_map;
		} catch (Exception e) {

			cj_map.put("cjStatus", 0);// 查询出错状态
			System.out.println("Java_GetXuanKe出错======" + e.toString());
		} finally {

		}

		return cj_map;
	}

	@Override
	public Map call() {
		Map xuankeResultMap = null;
		try {
			xuankeResultMap = getXuanKe(xuankeMap);
			return xuankeResultMap;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return xuankeResultMap;
	}

}
