package com.lw.wxvidio;

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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class GetVedioLink {
	/** 过期变量 */
	String[][] strcj = new String[200][10];
	CookieStore cookieStore;
	DefaultHttpClient mHttpClient = new DefaultHttpClient();
	/** 一门课程（历年成绩、学期/学年成绩通用） */
	Map cj_map = new HashMap();//
	/** 一门课程（最高成绩） */
	Map zgcj_map = new HashMap();//
	String sevIP = "210.37.0.27";

	public Map getCj() {

		new Thread() {

			public void run() {
				try {
					String __VIEWSTATE = "";
					StringTokenizer tokenizer = null;
					String ksInfo = "";
					String __VIEWSTATE_xscj_gc = "";
					HttpGet get = new HttpGet(
							"http://video.haosou.com/v?q=%E5%BE%AE%E4%BF%A1%E5%85%AC%E5%BC%80%E8%AF%BE&src=&ie=utf-8");
					// 获取Get响应，如果状态码是200的话表示连接成功
					HttpResponse httpResponse = new DefaultHttpClient()
							.execute(get);

					HttpEntity entity = httpResponse.getEntity();
					// 获取纯净的主页HTML源码，这里大家可以将mianhtml定义在其他地方
					String mainhtml = EntityUtils.toString(entity);
					System.out.println(mainhtml);
				} catch (Exception e) {

				}
			}
		}.start();

		return cj_map;
	}

	public static void main(String[] d) {
		GetVedioLink getVedioLink = new GetVedioLink();
		getVedioLink.getCj();

	}
}
