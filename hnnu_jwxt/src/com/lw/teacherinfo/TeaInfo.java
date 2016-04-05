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
	/** ��ʦ�û���Ϣ */
	Map teainfo_map = new HashMap();//
	/** һ�ſγ̣���߳ɼ��� */
	String sevIP = "210.37.0.27";
	private Map tchMap = null;

	public TeaInfo(Map tchMap) {
		this.tchMap = tchMap;
	}

	@Override
	public void run() {
		Map tchResultMap = null;
		try {
			//System.out.println("��tchMap��"+tchMap);
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
			System.out.println("��TeaInfo-run��"+e.toString());
		}
	}

	public Map getTeaInfo(CookieStore cookies, String url_cjcx, String sevIP2) {
		sevIP = sevIP2;
		teainfo_map.put("cjStatus", -1);// ��ʼ��ѯ״̬
		final String Url_cjcx, xh;
		final CookieStore cookieStore = cookies;

		Url_cjcx = url_cjcx;
		//System.out.println("Url_cjcx=" + Url_cjcx);
		//System.out.println();
		String xh1 = "";
		char[] ch = Url_cjcx.toCharArray();
		for (int i = 37; i < 43; i++) { // ��ȡ��ʦ����
			xh1 = xh1 + ch[i];
		}
		xh = xh1;
		 System.out.println("��ʦ����=" + xh);
		teainfo_map.put("xh", xh);
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
									+ "//js_main.aspx?xh=" + xh);
					// System.out.println(ksInfo);
				} catch (IOException e) {
					teainfo_map.put("cjStatus", 0);// ��ѯ����״̬
					System.out.println("11111111" + e.toString());
				}

			}

			// ////////////////////////////////////////////////////////////////
			// �����ȡ�ķ���ҳ��
			Document cjdoc = Jsoup.parse(ksInfo);

			Map teainfo_map_1 = new HashMap();// �γ�����
			Map teainfo_map_2 = new HashMap();// �γ̹���
			Map teainfo_map_3 = new HashMap();// ����
			Map teainfo_map_4 = new HashMap();// ����ѧ��

			Elements rowElementstj3 = cjdoc.select("[id=Table1]");

			//
			// String imgurl=filterHtmlTeaInfoImg(ksInfo, Url_cjcx);
			// System.out.println(imgurl);
			// UrlResource urlResource=new UrlResource();
			// urlResource.saveUrlFile(imgurl, "D:\\1.jpg");

			Elements rowElementstj31 = rowElementstj3.get(0).select("tr");
			Elements elements1 = rowElementstj31.get(0).select("td");

			teainfo_map.put("0", elements1.get(3).text());// ����

			Elements elements2 = rowElementstj31.get(1).select("td");

			teainfo_map.put("1", elements2.get(1).select("[selected=selected]")
					.get(0).text());// �Ա�
			teainfo_map.put("2", elements2.get(3).select("[selected=selected]")
					.get(0).text());// ����

			Elements elements3 = rowElementstj31.get(3).select("td");

			teainfo_map.put("3", elements3.get(1).select("[selected=selected]")
					.get(0).text());// ѧԺ

			Elements elements4 = rowElementstj31.get(6).select("td");

			teainfo_map.put("4", elements4.get(1).select("[selected=selected]")
					.get(0).text());// ѧ��
			// teainfo_map.put("13", elements4.get(2).text());
			teainfo_map.put("5", elements4.get(3).select("[selected=selected]")
					.get(0).text());// ѧλ

			teainfo_map.put("cjStatus", 1);// ��ѯ�ɹ�״̬
		} catch (Exception e) {

			teainfo_map.put("cjStatus", 0);// ��ѯ����״̬
			System.out.println("Java_GetTeaInfo����======" + e.toString());
		}

		return teainfo_map;
	}

	private String filterHtmlTeaInfoImg(String source, String url_cjcx) {// ��ȡ��ʦ������Ƭ��ַ
		if (null == source) {
			return "";
		}
		// isc1=true;
		StringBuffer sff = new StringBuffer();
		String score[];
		int i = 0, j = 0;
		String html = source;
		Document doc = Jsoup.parse(html); // ��HTML������ص�doc�� <span
		Elements links = doc.select("img[id=Image1]"); // ���ǿγ�������Ϊ�γ�����HTML��ǩ��<td
		//System.out.println(links.size());
		for (Element link : links) {
			// ��ȡ��Ҫ��ѯ��URL,�����Ӧ��ַ��ť�����ֽгɼ���ѯ

			// ��ȡ��Ҫ��ѯ����Ե�ַ����ȡ��Եĵ�ַ
			sff.append(link.attr("src"));
		}

		String str = sff.toString();
		//System.out.println("src:" + str);
		// ���ز�ѯ��URL,����ҳ��ַ����Ե�ַ����������ͬ�������cjcxUrl���Զ���������
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
