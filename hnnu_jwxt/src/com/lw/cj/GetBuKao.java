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
	/** ���ڱ��� */
	String[][] strcj = new String[200][10];
	CookieStore cookieStore;
	DefaultHttpClient mHttpClient = new DefaultHttpClient();
	/** һ�ſγ̣�����ɼ���ѧ��/ѧ��ɼ�ͨ�ã� */
	Map cj_map = new HashMap();//
	/** һ�ſγ̣���߳ɼ��� */
	Map zgcj_map = new HashMap();//
	String sevIP = "210.37.0.27";

	public Map getBuKao( CookieStore cookies, String url_cjcx, String sevIP2) {
		sevIP = sevIP2;
		cj_map.put("cjStatus", -1);// ��ʼ��ѯ״̬

		final String BtnType, BtnValue, Kcxz, Xn, Xq, Cookies, Url_cjcx, xh;
		final CookieStore cookieStore = cookies;
		

		Url_cjcx = url_cjcx;
		String xh1 = "";
		char[] ch = Url_cjcx.toCharArray();
		for (int i = 34; i < 47; i++) { // ��ȡѧ��
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
											+ "&xm=" + "����" + "&gnmkdm=N121305");
						} catch (IOException e) {
							cj_map.put("cjStatus", 0);// ��ѯ����״̬
							System.out.println("11111111" + e.toString());
						}
						System.out.println(ksInfo);

					}

				} catch (Exception e) {

					cj_map.put("cjStatus", 0);// ��ѯ����״̬
					System.out.println("GetSore����================="
							+ e.toString());
				}

			}
		}.start();

		return cj_map;
	}

	public static void main(String[] sd) {
//Login login=new Login();
//login.login("201224010219", "fjyqlw7235@@", "ѧ��", 4);
		
		
	}

}
