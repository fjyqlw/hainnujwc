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
import java.util.concurrent.Callable;

import net.sf.json.JSONObject;

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

import com.lw.config.Methods;
import com.lw.db.DB;
import com.lw.errorinfo.ERROR_INFO;
import com.lw.errorinfo.ERROR_KEY;
import com.lw.login.Login;
import com.lw.methods.RequestRES;
import com.lw.methods.RequestRESDelegate;
import com.lw.methods.RequestRESService;

public class GetSore extends Thread implements Callable<Map> {
	//private CookieStore cookieStore;
	//private DefaultHttpClient mHttpClient = new DefaultHttpClient();
	/** һ�ſγ̣�����ɼ���ѧ��/ѧ��ɼ�ͨ�ã� */
	private Map cj_map = new HashMap();//
	/** һ�ſγ̣���߳ɼ��� */
	private Map zgcj_map = new HashMap();//
	//private String sevIP = "210.37.0.27";
	private Map cjMap = null;
	

	private JSONObject inJsonrun=null;
	private CookieStore cookieStorerun;

	public GetSore( JSONObject inJson,  CookieStore cookieStore) {
		this.inJsonrun = inJson;
		this.cookieStorerun=cookieStore;
	}

	@Override
	public void run() {// ���ǵ���ɼ�
		Map cjResultMap = null;
			DB db = new DB();
				String openID=inJsonrun.getJSONObject(ERROR_KEY.PARAMTER).getString("openID");
			    String xh=inJsonrun.getJSONObject(ERROR_KEY.PARAMTER).getString("xh");
			    String psd=inJsonrun.getJSONObject(ERROR_KEY.PARAMTER).getString("psd");
			    String user=inJsonrun.getJSONObject(ERROR_KEY.PARAMTER).getString("user");
			/**��*/
//			try {
//			    int role=inJsonrun.getJSONObject(ERROR_KEY.PARAMTER).getInt("role");
//			    
//			    JSONObject inJson=new JSONObject();
//			    JSONObject dataJson=new JSONObject();
//			    
//			    inJson.put(ERROR_KEY.METHOD, Methods.dbExecute_hnnujwc);
////			    dataJson.put("sql", openID);
////			    dataJson.put("xh", xh);
////			    dataJson.put("psd", psd);
////			    dataJson.put("user", user);
////			    dataJson.put("role", role);
//			    
//			    StringBuffer sql = new StringBuffer();
//	             sql.append("insert into user set openID='");
//	             sql.append(openID);
//	             sql.append("',id='");
//	             sql.append(xh);
//	             sql.append("',role=");
//	             sql.append(role);
//	             sql.append("',name='");
//	             sql.append(user);
//	             sql.append("',psd='");
//	             sql.append(psd);
//	             sql.append("'");
//				    dataJson.put("sql", sql.toString());
//			    
//			    
//			    
//			    inJson.put(ERROR_KEY.PARAMTER, dataJson);
//			    
//			    RequestRESService rrs=new RequestRESService();
//				RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
//				rrd.requestRES(inJson.toString());
//				
//			} catch (Exception e) {
//				System.out.println("���󶨳���"+e.toString());
//			}
			
		try {
			if (!inJsonrun.getJSONObject(ERROR_KEY.PARAMTER).has("cjcx")) {//δ���ųɼ���ѯ
				System.out.println("��δ���ųɼ���ѯ��");
			}else {
			JSONObject inJson=new JSONObject();
			
				JSONObject outJson= getCj(inJsonrun,cookieStorerun); 
				
				RequestRESService rrs=new RequestRESService();
				RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
				
			JSONObject dataJson=outJson.getJSONObject(ERROR_KEY.DATA);
//			dataJson
				inJson.put(ERROR_KEY.METHOD, Methods.importScore);
				inJson.put(ERROR_KEY.PARAMTER,dataJson );
				
				rrd.requestRES(inJson.toString());
				
//			cjResultMap = getCj((String) cjMap.get("btnType"),
//					(String) cjMap.get("btnValue"), (String) cjMap.get("kcxz"),
//					(String) cjMap.get("xn"), (String) cjMap.get("xq"),
//					(CookieStore) cjMap.get("cookieStore"),
//					(String) cjMap.get("url_cjcx"), (String) cjMap.get("ip"));
			
//			cjResultMap.put("Id", (String) cjMap.get("Id"));
			

			StringBuffer sql = new StringBuffer();
             sql.append("insert ignore into cj");
             sql.append(xh);
             sql.append("(xn,xq,kcdm,kcmc,kcxz,kcgs,xf,jd,cj,fxbj,bkcj,cxcj,kkxy,bz,cxbj) values(");
//			sql = ""
//					+ xh
//					+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";//
//			setSql_db("stu_score");
			
			
			
			
			
			
			
			
			
		//	db.importCj(cjResultMap);// ����ɼ�	
			}
		} catch (Exception e) {
			System.out.println("����ɼ�����"+e.toString());
		}finally{
//			if (!(Boolean) cjMap.get("isupdate")) {// ��ʱ����Ҫ����û�
//				db.insertUser((String) cjMap.get("openID"),
//						(String) cjMap.get("name"), (String) cjMap.get("Id"),
//						(String) cjMap.get("psd"), null);
//			}
//			db=null;
		}

	}

	/**
	 * ��ѯ�ɼ����
	 * 
	 * @param btnType
	 *            ��ť����
	 * @param btnValue
	 *            ��ťֵ
	 * @param kcxz
	 *            �γ�����
	 * @param xn
	 *            ѧ��
	 * @param xq
	 *            ѧ��
	 * @param cookies
	 *            CookieStore
	 * @param url_cjcx
	 *            �ɼ���ѯ��ַ
	 * @param sevIP2
	 *            �����������ip
	 */
	public JSONObject getCj(final JSONObject inJson,final  CookieStore cookieStore) {

       JSONObject outJson =new JSONObject();
       JSONObject dataJson =new JSONObject();
       
System.out.println("�����������"+inJson);

	
		 DefaultHttpClient client = new DefaultHttpClient();
		 try {
			
				 client.setCookieStore(cookieStore);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		System.out.println("---------1-------------");
		final String btn=inJson.getJSONObject(ERROR_KEY.PARAMTER).getString("btn");
		System.out.println("---------2-------------");
		final String kcxz=inJson.getJSONObject(ERROR_KEY.PARAMTER).getString("kcxz");
		System.out.println("----------3------------");
		final String xn=inJson.getJSONObject(ERROR_KEY.PARAMTER).getString("xn");
		System.out.println("-----------4-----------");
		final String xq=inJson.getJSONObject(ERROR_KEY.PARAMTER).getString("xq");
		System.out.println("-----------5-----------");
		final String sevip=inJson.getJSONObject(ERROR_KEY.PARAMTER).getString("sevip");
		System.out.println("-----------6-----------");
		final String cjcx=inJson.getJSONObject(ERROR_KEY.PARAMTER).getString("cjcx");
		System.out.println("-----------7-----------");
		final String cjcx_url=sevip+cjcx;
		System.out.println("------------8----------");
		final String xh=inJson.getJSONObject(ERROR_KEY.PARAMTER).getString("xh");
		System.out.println("-------------9---------");
		final String btntype=inJson.getJSONObject(ERROR_KEY.PARAMTER).getString("btntype");

System.out.println("��---��");
//		final String ;
//		final String ;
//		final String ;
//		final String ;
		
		StringTokenizer tokenizer = null;
		String __VIEWSTATE = "";
		String ksInfo = "";
		String __VIEWSTATE_xscj_gc = "";
		try {

			//client.setCookieStore(cookieStore);
			List<NameValuePair> paramsgra1 = new ArrayList<NameValuePair>();
			if (__VIEWSTATE_xscj_gc == "") {

				try {
					ksInfo = HttpUtil.getUrl(cjcx_url,// "http://210.37.0.27//xscjcx.aspx?xh=201224010117&xm=����&gnmkdm=N121605",
							client, sevip
									+ "xs_main.aspx?xh=" + xh);
					System.out.println("cjcx_url="+cjcx_url);
					System.out.println("cjcx_url2="+sevip
							+ "xs_main.aspx?xh=" + xh);
					//System.out.println(ksInfo);
				} catch (IOException e) {
					cj_map.put("cjStatus", 0);// ��ѯ����״̬
					System.out.println("11111111" + e.toString());
					outJson.put(ERROR_KEY.ERROR, ERROR_INFO.PROGRAM_ERROR);
					outJson.put(ERROR_KEY.MSG, e.toString());
				}

				if (ksInfo != "") {
					tokenizer = new StringTokenizer(ksInfo);
					while (tokenizer.hasMoreTokens()) {
						String valueToken = tokenizer.nextToken();
						if (StringUtil.isValue(valueToken, "value")
								&& valueToken.length() > 100) {
							if (StringUtil.getValue(valueToken, "value", "\"",
									7).length() > 100) {
								__VIEWSTATE = StringUtil.getValue(valueToken,
										"value", "\"", 7);// value
								__VIEWSTATE_xscj_gc = __VIEWSTATE;
							}
						}
					}
				}
			}
			// ViewState��ȡ���
			// //////////////////////////////////////////////////////////

			paramsgra1.add(new BasicNameValuePair("__EVENTTARGET", ""));
			paramsgra1.add(new BasicNameValuePair("__VIEWSTATE",
					__VIEWSTATE_xscj_gc));// Viewstate
			paramsgra1.add(new BasicNameValuePair(btntype, btn)); // ddl_kcxz
																		// __EVENTTARGET
			paramsgra1.add(new BasicNameValuePair("ddl_kcxz", inJson.getJSONObject(ERROR_KEY.PARAMTER).getString("kcxz")));
			paramsgra1.add(new BasicNameValuePair("ddlXN", inJson.getJSONObject(ERROR_KEY.PARAMTER).getString("xn")));
			paramsgra1.add(new BasicNameValuePair("ddlXQ", inJson.getJSONObject(ERROR_KEY.PARAMTER).getString("xq")));
			paramsgra1.add(new BasicNameValuePair("hidLanguage", ""));

			HttpPost cjpost = new HttpPost(cjcx_url); // http://210.37.0.27//xscjcx.aspx?xh=201224010219&xm=����&gnmkdm=N121605
			// ��һ���ǹؼ������ո�һ����������Ҫ��cookie��Referer������һ�£������Ļ���������Ӧ��Ҳûʲô�£����Ƕ�����HttpWatch��Postͷ��Ϣ�У������Լ�ȥ������
			cjpost.setHeader("Referer", sevip+"default2.aspx");
			cjpost.setHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko");
			cjpost.setHeader("Content-Type",
					"application/x-www-form-urlencoded");
			cjpost.setHeader("Accept", "text/html, application/xhtml+xml, */*");
			cjpost.setHeader("Connection", "Keep-Alive");

			// Post�����ȡ�ɼ���Html�����ոյĳɼ���ѯ��List�ù��������ñ��롣
			cjpost.setEntity(new UrlEncodedFormEntity(paramsgra1, "gb2312"));
			HttpResponse response1 = client.execute(cjpost);
			HttpEntity entity = response1.getEntity();
			// ��ȡ�ɼ���HTMLԴ��
			String cjHtml = EntityUtils.toString(entity);
			// System.out.println(cjHtml);
			// �ر�����
			cjpost.abort();
			cjpost = null;
			EntityUtils.consume(response1.getEntity()); // �ͷ�
			client.close();
			// ////////////////////////////////////////////////////////////////
			// �����ȡ�ķ���ҳ��
			Document cjdoc = Jsoup.parse(cjHtml);

			// ��ȡ��ÿ�����ݵ�ѡ����,�����ѡ�������ǿ��Թ۲���HTML���룬����Ͳ���˵�ˡ�
			String rowRegex = "div.main_box div.mid_box span.formbox table#Datagrid1.datelist tbody tr";
			// ÿ�е�����Ԫ��
			Elements rowElements = cjdoc.select(rowRegex);

			// ��ȡ��ÿ�����ݵ�ѡ����,�����ѡ�������ǿ��Թ۲���HTML���룬����Ͳ���˵�ˡ�
			String rowRegex2 = "[class=datelist][id=Datagrid3]";
			// ÿ�е�����Ԫ��
			Elements rowElements2 = cjdoc.select(rowRegex2);
			Elements rowElements3 = rowElements2.select("tr");

			if ("Button1".equals(btntype)) { // �ɼ�ͳ��
				Map cj_map_1 = new HashMap();// �γ�����
				Map cj_map_2 = new HashMap();// �γ̹���
				Map cj_map_3 = new HashMap();// ����
				Map cj_map_4 = new HashMap();// ����ѧ��

				Elements rowElementstj2 = cjdoc.select("[id=xftj]");// ѧ�ּ�Ҫ��Ϣ
				String userinfo2 = rowElementstj2.get(0).text();
				cj_map.put("xftj", userinfo2);
				Elements rowElementstj3 = cjdoc.select("[class=datelist]");

				Elements rowElementstj31 = rowElementstj3.get(0).select("tr");

				for (int i = 0; i < rowElementstj31.size(); i++) {
					Elements elements = rowElementstj31.get(i).select("td");
					Map zcj_info_map = new HashMap();// �γ̾���ɼ�
					zcj_info_map.put("0", elements.get(0).text());// ����������HashMap
					zcj_info_map.put("1", elements.get(1).text());
					zcj_info_map.put("2", elements.get(2).text());
					zcj_info_map.put("3", elements.get(3).text());
					zcj_info_map.put("4", elements.get(4).text());

					cj_map_1.put("zcj_" + i, zcj_info_map);// �������HashMap
					zcj_info_map = null;
				}
				cj_map.put("1", cj_map_1);// ����һ��HashMap

				Elements rowElementstj32 = rowElementstj3.get(1).select("tr");

				for (int i = 0; i < rowElementstj32.size(); i++) {
					Elements elements = rowElementstj32.get(i).select("td");

					Map zcj_info_map = new HashMap();// �γ̾���ɼ�
					zcj_info_map.put("0", elements.get(0).text());// ����������HashMap
					zcj_info_map.put("1", elements.get(1).text());
					zcj_info_map.put("2", elements.get(2).text());
					zcj_info_map.put("3", elements.get(3).text());
					zcj_info_map.put("4", elements.get(4).text());

					cj_map_2.put("zcj_" + i, zcj_info_map);// �������HashMap
					zcj_info_map = null;
				}
				cj_map.put("2", cj_map_2);// ����һ��HashMap

				Elements rowElementstj33 = rowElementstj3.get(2).select("tr");

				for (int i = 0; i < rowElementstj33.size(); i++) {
					Elements elements = rowElementstj33.get(i).select("td");

					Map zcj_info_map = new HashMap();// �γ̾���ɼ�
					zcj_info_map.put("0", elements.get(0).text());// ����������HashMap
					zcj_info_map.put("1", elements.get(1).text());
					zcj_info_map.put("2", elements.get(2).text());
					zcj_info_map.put("3", elements.get(3).text());
					zcj_info_map.put("4", elements.get(4).text());

					cj_map_3.put("zcj_" + i, zcj_info_map);// �������HashMap
					zcj_info_map = null;
				}
				cj_map.put("3", cj_map_3);// ����һ��HashMap

				try {

					Elements rowElementstj34 = rowElementstj3.get(3).select(
							"tr");

					for (int i = 0; i < rowElementstj34.size(); i++) {
						Elements elements = rowElementstj34.get(i).select("td");

						Map zcj_info_map = new HashMap();// �γ̾���ɼ�
						zcj_info_map.put("0", elements.get(0).text());// ����������HashMap
						zcj_info_map.put("1", elements.get(1).text());
						zcj_info_map.put("2", elements.get(2).text());
						zcj_info_map.put("3", elements.get(3).text());
						zcj_info_map.put("4", elements.get(4).text());

						cj_map_4.put("zcj_" + i, zcj_info_map);// �������HashMap

						zcj_info_map = null;
					}
					cj_map.put("4", cj_map_4);// ����һ��HashMap

				} catch (Exception e) {
					cj_map.put("4", null);// ����һ��HashMap
				} finally {
					cj_map_1 = null;
					cj_map_2 = null;
					cj_map_3 = null;
					cj_map_4 = null;
				}
				cj_map.put("xftj", userinfo2);
				cj_map.put("cjStatus", 1);// ��ѯ�ɹ�״̬
			}

			if ("btn_zcj".equals(btntype)) {// ����ɼ�
				for (int i = 1; i < rowElements.size(); i++) {
					Elements elements = rowElements.get(i).select("td");
					Map zcj_info_map = new HashMap();// �γ̾���ɼ�
					JSONObject classJson=new JSONObject();
					classJson.put("0", elements.get(0).text());
					classJson.put("1", elements.get(1).text());
					classJson.put("2", elements.get(2).text());
					classJson.put("3", elements.get(3).text());
					classJson.put("4", elements.get(4).text());
					classJson.put("5", elements.get(5).text());
					classJson.put("6", elements.get(6).text());
					classJson.put("7", elements.get(7).text());
					classJson.put("8", elements.get(8).text());
					classJson.put("9", elements.get(9).text());
					classJson.put("10", elements.get(10).text());
					classJson.put("11", elements.get(11).text());
					classJson.put("12", elements.get(12).text());
					classJson.put("13", elements.get(13).text());
					classJson.put("14", elements.get(14).text());

					dataJson.put("c_" + i, classJson);
					classJson = null;

				}

				cj_map.put("cjStatus", 1);// ��ѯ�ɹ�״̬

			}
			//System.out.println(cj_map);
			outJson.put(ERROR_KEY.ERROR, ERROR_INFO.SUCCESS);
			outJson.put(ERROR_KEY.DATA, dataJson);
		} catch (Exception e) {
			outJson.put(ERROR_KEY.ERROR, ERROR_INFO.PROGRAM_ERROR);
			outJson.put(ERROR_KEY.MSG, e.toString());

			cj_map.put("cjStatus", 0);// ��ѯ����״̬
			System.out.println("GetSore����=================" + e.toString());
		} finally {
			tokenizer = null;
			System.gc();
			System.out.println("�����շ��ص����ݡ�"+outJson);
		}

		return outJson;
	}

	@Override
	public Map call() {
		Map cjResultMap = null;
		try {
//			cjResultMap = getCj((String) cjMap.get("btnType"),
//					(String) cjMap.get("btnValue"), (String) cjMap.get("kcxz"),
//					(String) cjMap.get("xn"), (String) cjMap.get("xq"),
//					(CookieStore) cjMap.get("cookieStore"),
//					(String) cjMap.get("url_cjcx"), (String) cjMap.get("ip"));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return cjResultMap;
	}

}
