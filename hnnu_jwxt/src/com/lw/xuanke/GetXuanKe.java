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
	/** һ�ſγ̣�����ɼ���ѧ��/ѧ��ɼ�ͨ�ã� */
	Map cj_map = new HashMap();//
	/** һ�ſγ̣���߳ɼ��� */
	Map zgcj_map = new HashMap();//
	String sevIP = "210.37.0.27";
	private Map xuankeMap = null;

	public GetXuanKe(Map xuankeMap) {
		this.xuankeMap = xuankeMap;
	}

	public Map getXuanKe(Map xuankeMap) {
		sevIP = (String) xuankeMap.get("ip");
		cj_map.put("cjStatus", -1);// ��ʼ��ѯ״̬

		final String Cookies, Url_cjcx, xh;
		final CookieStore cookieStore = (CookieStore) xuankeMap
				.get("cookieStore");
		
		Url_cjcx = (String) xuankeMap.get("login_url_xuanke");
		String xh1 = "";
		char[] ch = Url_cjcx.toCharArray();
		for (int i = 34; i < 47; i++) { // ��ȡѧ��
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
					ksInfo = HttpUtil.getUrl(Url_cjcx,// "http://210.37.0.59/xsxk.aspx?xh=201224010219&xm=����&gnmkdm=N121207",
							mHttpClient, "http://" + sevIP
									+ "//xs_main.aspx?xh=" + xh);
					// System.out.println(ksInfo);
				} catch (IOException e) {
					cj_map.put("cjStatus", 0);// ��ѯ����״̬
					System.out.println("11111111" + e.toString());
				}

			}

			// ////////////////////////////////////////////////////////////////
			// �����ȡ�ķ���ҳ��
			Document cjdoc = Jsoup.parse(ksInfo);

			// ��ȡ��ÿ�����ݵ�ѡ����,�����ѡ�������ǿ��Թ۲���HTML���룬����Ͳ���˵�ˡ�
			String rowRegex = "div.main_box div.mid_box span.formbox table#Datagrid1.datelist tbody tr";
			// ÿ�е�����Ԫ��
			Elements rowElements = cjdoc.select(rowRegex);

			// ��ȡ��ÿ�����ݵ�ѡ����,�����ѡ�������ǿ��Թ۲���HTML���룬����Ͳ���˵�ˡ�
			String rowRegex2 = "[class=datelist][id=Datagrid3]";
			// ÿ�е�����Ԫ��
			Elements rowElements2 = cjdoc.select(rowRegex2);
			Elements rowElements3 = rowElements2.select("tr");

			Map cj_map_1 = new HashMap();// �γ�����
			Map cj_map_2 = new HashMap();// �γ̹���
			Map cj_map_3 = new HashMap();// ����
			Map cj_map_4 = new HashMap();// ����ѧ��

			Elements rowElementstj3 = cjdoc.select("[class=datelist]");

			Elements rowElementstj31 = rowElementstj3.get(0).select("tr");
			for (int i = 0; i < rowElementstj31.size() - 1; i++) {
				Elements elements = rowElementstj31.get(i).select("td");

				Map zcj_info_map = new HashMap();// �γ̾���ɼ�
				zcj_info_map.put("0", elements.get(0).text());// ����������HashMap
				zcj_info_map.put("1", elements.get(1).text());
				zcj_info_map.put("2", elements.get(2).text());
				zcj_info_map.put("4", elements.get(4).text());
				zcj_info_map.put("5", elements.get(5).text());
				zcj_info_map.put("6", elements.get(6).text());
				zcj_info_map.put("8", elements.get(8).text());
				zcj_info_map.put("9", elements.get(9).text());

				cj_map.put("zcj_" + i, zcj_info_map);// �������HashMap
				zcj_info_map = null;
			}

			cj_map.put("cjStatus", 1);// ��ѯ�ɹ�״̬
			return cj_map;
		} catch (Exception e) {

			cj_map.put("cjStatus", 0);// ��ѯ����״̬
			System.out.println("Java_GetXuanKe����======" + e.toString());
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
