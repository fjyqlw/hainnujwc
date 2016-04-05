package com.lw.cj;

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

public class GetBuKao {
	/** 过期变量 */
	String[][] strcj = new String[200][10];
	CookieStore cookieStore;
	DefaultHttpClient mHttpClient = new DefaultHttpClient();
	/** 一门课程（历年成绩、学期/学年成绩通用） */
	Map cj_map = new HashMap();//
	/** 一门课程（最高成绩） */
	Map zgcj_map = new HashMap();//
	String sevIP = "210.37.0.27";

	public Map getBuKao( CookieStore cookies, String url_cjcx, String sevIP2) {
		sevIP = sevIP2;
		cj_map.put("cjStatus", -1);// 初始查询状态

		final String BtnType, BtnValue, Kcxz, Xn, Xq, Cookies, Url_cjcx, xh;
		final CookieStore cookieStore = cookies;
		

		Url_cjcx = url_cjcx;
		String xh1 = "";
		char[] ch = Url_cjcx.toCharArray();
		for (int i = 34; i < 47; i++) { // 获取学号
			xh1 = xh1 + ch[i];
		}
		xh = xh1;
		cj_map.put("xh", xh);
		new Thread() {

			public void run() {
				try {
					String __VIEWSTATE = "";
					StringTokenizer tokenizer = null;
					String ksInfo = "";
					String __VIEWSTATE_xscj_gc = "";

					mHttpClient.setCookieStore(cookieStore);
					if (__VIEWSTATE_xscj_gc == "") {

						try {
							ksInfo = HttpUtil.getUrl(Url_cjcx,// http://210.37.0.59/xs_main.aspx?xh=201224010219
									mHttpClient, "http://" + sevIP
											+ "//xs_bkmdqr.aspx?xh=" + xh
											+ "&xm=" + "练威" + "&gnmkdm=N121305");
						} catch (IOException e) {
							cj_map.put("cjStatus", 0);// 查询出错状态
							System.out.println("11111111" + e.toString());
						}
						System.out.println(ksInfo);

					}

				} catch (Exception e) {

					cj_map.put("cjStatus", 0);// 查询出错状态
					System.out.println("GetSore出错================="
							+ e.toString());
				}

			}
		}.start();

		return cj_map;
	}

	public static void main(String[] sd) {
//Login login=new Login();
//login.login("201224010219", "fjyqlw7235@@", "学生", 4);
		
		
	}

}
