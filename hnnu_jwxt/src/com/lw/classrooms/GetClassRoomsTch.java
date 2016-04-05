package com.lw.classrooms;

import java.io.IOException;
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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.lw.cj.StringUtil;
import com.lw.login.Login;
import com.lw.xuanke.HttpUtil;

public class GetClassRoomsTch extends Thread implements Callable<Map>{
	public GetClassRoomsTch(Map crMap) {
		this.crMap=crMap;
	}

	private Map crMap = new HashMap();
	// CookieStore cookieStore;
	public DefaultHttpClient mHttpClient = new DefaultHttpClient();
	/** һ�ſγ̣�����ɼ���ѧ��/ѧ��ɼ�ͨ�ã� */
	Map cj_map = new HashMap();//
	/** һ�ſγ̣���߳ɼ��� */
	Map zgcj_map = new HashMap();//
	String sevIP = "210.37.0.27";
	


	public Map getClassRooms(boolean isNextPage, Map classrooMap) {

		final String Cookies, Url_cjcx, xh;
		final Map classrooMap2 = classrooMap;
		final boolean isNextPage2=isNextPage;
		final CookieStore cookieStore = (CookieStore)crMap.get("cookieStore");
		sevIP = (String)classrooMap2.get("ip");
		cj_map.clear();
		cj_map.put("cjStatus", -1);// ��ʼ��ѯ״̬

		//System.out.println("��classrooMap��" + classrooMap2);
		Url_cjcx = (String)classrooMap2.get("login_url_classroom");
		String xh1 = "";
		char[] ch = Url_cjcx.toCharArray();
		for (int i = 36; i < 42; i++) { // ��ȡ��ʦ����
			xh1 = xh1 + ch[i];
		}
		xh = xh1;
		cj_map.put("xh", xh);
//		new Thread() {
//
//			public void run() {
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
								//System.out.println("��ǰ��"+Url_cjcx);
								// System.out.println("����"+"http://" + sevIP
										//	+ "//js_main.aspx?xh=" + xh);
						
							 
						} catch (IOException e) {
							cj_map.put("cjStatus", 0);// ��ѯ����״̬
							System.out.println("11111111" + e.toString());
						}
//System.out.println("������viewstate1��");
					}
					if (ksInfo != "") {
						tokenizer = new StringTokenizer(ksInfo);
						while (tokenizer.hasMoreTokens()) {
							String valueToken = tokenizer.nextToken();
							if (StringUtil.isValue(valueToken, "value")
									&& valueToken.length() > 100) {
								if (StringUtil.getValue(valueToken, "value",
										"\"", 7).length() > 100) {
									__VIEWSTATE = StringUtil.getValue(
											valueToken, "value", "\"", 7);// value
									__VIEWSTATE_xscj_gc = __VIEWSTATE;
								}
							}
						}
						//System.out.println("������viewstate2��");
					}
					// ViewState��ȡ���
					// //////////////////////////////////////////////////////////
					List<NameValuePair> paramsgra1 = new ArrayList<NameValuePair>();
//__EVENTARGUMENT: 

					paramsgra1.add(new BasicNameValuePair("__VIEWSTATE",
							__VIEWSTATE_xscj_gc));// Viewstate
													// __EVENTTARGET
					paramsgra1.add(new BasicNameValuePair("ddlDsz",
							classrooMap2.get("ddlDsz").toString()));// ��˫��
//					paramsgra1.add(new BasicNameValuePair("ddlSyXn",
//							"2015-2016"));
					paramsgra1.add(new BasicNameValuePair("ddlSyxq", "1"));
				if (isNextPage2) {//�״ε�����ս��Ҳ�ѯ����ť
					paramsgra1.add(new BasicNameValuePair("__EVENTARGUMENT",""));// __EVENTARGUMENT
					paramsgra1.add(new BasicNameValuePair("__EVENTTARGET", "dpDataGrid1:txtChoosePage"));
						paramsgra1.add(new BasicNameValuePair(
								"dpDataGrid1%3AtxtChoosePage", classrooMap2
										.get("choosepage").toString()));// �ڼ�ҳ
						paramsgra1.add(new BasicNameValuePair(
								"dpDataGrid1%3AbtnNextPage", "��һҳ"));// �ڼ�ҳ
						paramsgra1.add(new BasicNameValuePair(
								"dpDataGrid1%3AtxtPageSize", "80"));// ÿҳ��ʾ�����
				}else{//�������һҳ��
					paramsgra1.add(new BasicNameValuePair("__EVENTARGUMENT",""));
					paramsgra1.add(new BasicNameValuePair("__EVENTTARGET", ""));
						paramsgra1.add(new BasicNameValuePair("Button2",
								"�ս��Ҳ�ѯ"));
				}

					paramsgra1.add(new BasicNameValuePair("jslb", classrooMap2
							.get("jslb").toString()));// �������
					paramsgra1.add(new BasicNameValuePair("jssj", classrooMap2
							.get("jssj").toString()));// ����ʱ��
					paramsgra1.add(new BasicNameValuePair("kssj", classrooMap2
							.get("kssj").toString()));// ��ʼʱ��
					paramsgra1.add(new BasicNameValuePair("max_zws",
							classrooMap2.get("max_zws").toString()));// �����λ��
					paramsgra1.add(new BasicNameValuePair("min_zws",
							classrooMap2.get("min_zws").toString()));// ��С��λ��
					paramsgra1.add(new BasicNameValuePair("sjd", classrooMap2
							.get("sjd").toString()));// �ڼ���
					paramsgra1.add(new BasicNameValuePair("xiaoq", classrooMap2
							.get("xiaoq").toString()));// У��
					//paramsgra1.add(new BasicNameValuePair("xn", "2015-2016"));
					//paramsgra1.add(new BasicNameValuePair("xq", "1"));//
					paramsgra1.add(new BasicNameValuePair("xqj", classrooMap2
							.get("xqj").toString()));// ���ڼ�
                     // System.out.println("��1��:"+Url_cjcx);
					HttpPost cjpost = new HttpPost(Url_cjcx); // http://210.37.0.27//xscjcx.aspx?xh=201224010219&xm=����&gnmkdm=N121605
					// ��һ���ǹؼ������ո�һ����������Ҫ��cookie��Referer������һ�£������Ļ���������Ӧ��Ҳûʲô�£����Ƕ�����HttpWatch��Postͷ��Ϣ�У������Լ�ȥ������
					
					if (!isNextPage2) {//�״�
						cjpost.setHeader("Referer", "http://" + sevIP
								+ "/default2.aspx");///default2.aspx	 //
					}else {
						cjpost.setHeader("Referer", "http://" + sevIP
								+ "/");
					}

					//cjpost.addHeader("Cookie", cookie);
					cjpost.setHeader("User-Agent",
							"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/8.0; .NET4.0C; .NET4.0E)");//Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko
					cjpost.setHeader("Content-Type",
							"application/x-www-form-urlencoded");
					cjpost.setHeader("Accept",
							"text/html, application/xhtml+xml, */*");
					cjpost.setHeader("Connection", "Keep-Alive");

					// Post�����ȡ�ɼ���Html�����ոյĳɼ���ѯ��List�ù��������ñ��롣
					cjpost.setEntity(new UrlEncodedFormEntity(paramsgra1,
							"gb2312"));
                   // System.out.println("��2��");
						HttpResponse response1 = mHttpClient.execute(cjpost);
					
					
                   // System.out.println("��3��");

					//Login.hp.put("cookieStore", mHttpClient.getCookieStore());
					HttpEntity entity = response1.getEntity();
					// ��ȡ�ɼ���HTMLԴ��
					String cjHtml = EntityUtils.toString(entity);
					// System.out.println(cjHtml);
					// �ر�����
					cjpost.abort();
					EntityUtils.consume(response1.getEntity()); //�ͷ�
					EntityUtils.consume(cjpost.getEntity()); //�ͷ�
					EntityUtils.consume(entity); //�ͷ�
			
					// ////////////////////////////////////////////////////////////////
                     //System.out.println(cjHtml);
					// ////////////////////////////////////////////////////////////////
					// �����ȡ�ķ���ҳ��
					Document cjdoc = Jsoup.parse(cjHtml);
               // System.out.println(cjHtml);
					Elements pageinfo = cjdoc.select("[id=dpDataGrid1_divDataPager]");
					//System.out.println(pageinfo.text());
						String rowRegex2 = "[class=datelist][id=Datagrid1]";
						// ÿ�е�����Ԫ��
						Elements rowElements2 = cjdoc.select(rowRegex2);
						Elements rowElements3 = rowElements2.select("tr");

						// System.out.println(rowElementstj31);
						System.out.println("���Ҹ�����" + (rowElements3.size() - 1));
						int totalroom = rowElements3.size() - 1;
						try {

							cj_map.put("totalpage", (totalroom / 41 + 1));
						} catch (Exception e) {
							System.out.println("��ҳ����" + e.toString());
						}

						for (int i = 0; i < rowElements3.size(); i++) {
							Elements elements = rowElements3.get(i)
									.select("td");

							Map zcj_info_map = new HashMap();// �γ̾���ɼ�
							zcj_info_map.put("0", elements.get(0).text());// ����������HashMap
							zcj_info_map.put("1", elements.get(1).text());
							zcj_info_map.put("2", elements.get(2).text());
							zcj_info_map.put("3", elements.get(3).text());
							zcj_info_map.put("4", elements.get(4).text());
							zcj_info_map.put("6", elements.get(6).text());

							cj_map.put("zcj_" + i, zcj_info_map);// �������HashMap
							zcj_info_map = null;
						}
						// cj_map.put("1", cj_map_1);// ����һ��HashMap

						cj_map.put("cjStatus", 1);// ��ѯ�ɹ�״̬
					//	System.out.println(cj_map);
					

				} catch (Exception e) {

					cj_map.put("cjStatus", 0);// ��ѯ����״̬
					System.out.println("Java_GetClassRooms����======"
							+ e.toString());
				}finally{
				//	mHttpClient.close();
				}

		//	}
	//	}.start();

		return cj_map;
	}



	@Override
	public Map call(){
		Map classroomResulrMap = null;
		try {
			classroomResulrMap = getClassRooms(false, crMap);
			return classroomResulrMap;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return classroomResulrMap;
	}

}
