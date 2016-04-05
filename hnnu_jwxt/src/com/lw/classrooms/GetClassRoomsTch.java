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
	/** 一门课程（历年成绩、学期/学年成绩通用） */
	Map cj_map = new HashMap();//
	/** 一门课程（最高成绩） */
	Map zgcj_map = new HashMap();//
	String sevIP = "210.37.0.27";
	


	public Map getClassRooms(boolean isNextPage, Map classrooMap) {

		final String Cookies, Url_cjcx, xh;
		final Map classrooMap2 = classrooMap;
		final boolean isNextPage2=isNextPage;
		final CookieStore cookieStore = (CookieStore)crMap.get("cookieStore");
		sevIP = (String)classrooMap2.get("ip");
		cj_map.clear();
		cj_map.put("cjStatus", -1);// 初始查询状态

		//System.out.println("【classrooMap】" + classrooMap2);
		Url_cjcx = (String)classrooMap2.get("login_url_classroom");
		String xh1 = "";
		char[] ch = Url_cjcx.toCharArray();
		for (int i = 36; i < 42; i++) { // 获取教师工号
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
							
								ksInfo = HttpUtil.getUrl(Url_cjcx,// "http://210.37.0.59/xsxk.aspx?xh=201224010219&xm=练威&gnmkdm=N121207",
									mHttpClient, "http://" + sevIP
											+ "//js_main.aspx?xh=" + xh);
								//System.out.println("【前】"+Url_cjcx);
								// System.out.println("【后】"+"http://" + sevIP
										//	+ "//js_main.aspx?xh=" + xh);
						
							 
						} catch (IOException e) {
							cj_map.put("cjStatus", 0);// 查询出错状态
							System.out.println("11111111" + e.toString());
						}
//System.out.println("【经过viewstate1】");
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
						//System.out.println("【经过viewstate2】");
					}
					// ViewState获取完成
					// //////////////////////////////////////////////////////////
					List<NameValuePair> paramsgra1 = new ArrayList<NameValuePair>();
//__EVENTARGUMENT: 

					paramsgra1.add(new BasicNameValuePair("__VIEWSTATE",
							__VIEWSTATE_xscj_gc));// Viewstate
													// __EVENTTARGET
					paramsgra1.add(new BasicNameValuePair("ddlDsz",
							classrooMap2.get("ddlDsz").toString()));// 单双周
//					paramsgra1.add(new BasicNameValuePair("ddlSyXn",
//							"2015-2016"));
					paramsgra1.add(new BasicNameValuePair("ddlSyxq", "1"));
				if (isNextPage2) {//首次点击【空教室查询】按钮
					paramsgra1.add(new BasicNameValuePair("__EVENTARGUMENT",""));// __EVENTARGUMENT
					paramsgra1.add(new BasicNameValuePair("__EVENTTARGET", "dpDataGrid1:txtChoosePage"));
						paramsgra1.add(new BasicNameValuePair(
								"dpDataGrid1%3AtxtChoosePage", classrooMap2
										.get("choosepage").toString()));// 第几页
						paramsgra1.add(new BasicNameValuePair(
								"dpDataGrid1%3AbtnNextPage", "下一页"));// 第几页
						paramsgra1.add(new BasicNameValuePair(
								"dpDataGrid1%3AtxtPageSize", "80"));// 每页显示最大数
				}else{//点击【下一页】
					paramsgra1.add(new BasicNameValuePair("__EVENTARGUMENT",""));
					paramsgra1.add(new BasicNameValuePair("__EVENTTARGET", ""));
						paramsgra1.add(new BasicNameValuePair("Button2",
								"空教室查询"));
				}

					paramsgra1.add(new BasicNameValuePair("jslb", classrooMap2
							.get("jslb").toString()));// 教室类别
					paramsgra1.add(new BasicNameValuePair("jssj", classrooMap2
							.get("jssj").toString()));// 结束时间
					paramsgra1.add(new BasicNameValuePair("kssj", classrooMap2
							.get("kssj").toString()));// 开始时间
					paramsgra1.add(new BasicNameValuePair("max_zws",
							classrooMap2.get("max_zws").toString()));// 最大座位数
					paramsgra1.add(new BasicNameValuePair("min_zws",
							classrooMap2.get("min_zws").toString()));// 最小座位数
					paramsgra1.add(new BasicNameValuePair("sjd", classrooMap2
							.get("sjd").toString()));// 第几节
					paramsgra1.add(new BasicNameValuePair("xiaoq", classrooMap2
							.get("xiaoq").toString()));// 校区
					//paramsgra1.add(new BasicNameValuePair("xn", "2015-2016"));
					//paramsgra1.add(new BasicNameValuePair("xq", "1"));//
					paramsgra1.add(new BasicNameValuePair("xqj", classrooMap2
							.get("xqj").toString()));// 星期几
                     // System.out.println("【1】:"+Url_cjcx);
					HttpPost cjpost = new HttpPost(Url_cjcx); // http://210.37.0.27//xscjcx.aspx?xh=201224010219&xm=练威&gnmkdm=N121605
					// 这一步是关键，跟刚刚一样，这里需要将cookie和Referer的设置一下，其他的话，不设置应该也没什么事，他们都是在HttpWatch的Post头信息中，你们自己去看看吧
					
					if (!isNextPage2) {//首次
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

					// Post请求获取成绩的Html。将刚刚的成绩查询的List拿过来，设置编码。
					cjpost.setEntity(new UrlEncodedFormEntity(paramsgra1,
							"gb2312"));
                   // System.out.println("【2】");
						HttpResponse response1 = mHttpClient.execute(cjpost);
					
					
                   // System.out.println("【3】");

					//Login.hp.put("cookieStore", mHttpClient.getCookieStore());
					HttpEntity entity = response1.getEntity();
					// 获取成绩的HTML源码
					String cjHtml = EntityUtils.toString(entity);
					// System.out.println(cjHtml);
					// 关闭连接
					cjpost.abort();
					EntityUtils.consume(response1.getEntity()); //释放
					EntityUtils.consume(cjpost.getEntity()); //释放
					EntityUtils.consume(entity); //释放
			
					// ////////////////////////////////////////////////////////////////
                     //System.out.println(cjHtml);
					// ////////////////////////////////////////////////////////////////
					// 处理获取的分数页面
					Document cjdoc = Jsoup.parse(cjHtml);
               // System.out.println(cjHtml);
					Elements pageinfo = cjdoc.select("[id=dpDataGrid1_divDataPager]");
					//System.out.println(pageinfo.text());
						String rowRegex2 = "[class=datelist][id=Datagrid1]";
						// 每行的数据元素
						Elements rowElements2 = cjdoc.select(rowRegex2);
						Elements rowElements3 = rowElements2.select("tr");

						// System.out.println(rowElementstj31);
						System.out.println("教室个数：" + (rowElements3.size() - 1));
						int totalroom = rowElements3.size() - 1;
						try {

							cj_map.put("totalpage", (totalroom / 41 + 1));
						} catch (Exception e) {
							System.out.println("分页出错：" + e.toString());
						}

						for (int i = 0; i < rowElements3.size(); i++) {
							Elements elements = rowElements3.get(i)
									.select("td");

							Map zcj_info_map = new HashMap();// 课程具体成绩
							zcj_info_map.put("0", elements.get(0).text());// 加入三级级HashMap
							zcj_info_map.put("1", elements.get(1).text());
							zcj_info_map.put("2", elements.get(2).text());
							zcj_info_map.put("3", elements.get(3).text());
							zcj_info_map.put("4", elements.get(4).text());
							zcj_info_map.put("6", elements.get(6).text());

							cj_map.put("zcj_" + i, zcj_info_map);// 加入二级HashMap
							zcj_info_map = null;
						}
						// cj_map.put("1", cj_map_1);// 加入一级HashMap

						cj_map.put("cjStatus", 1);// 查询成功状态
					//	System.out.println(cj_map);
					

				} catch (Exception e) {

					cj_map.put("cjStatus", 0);// 查询出错状态
					System.out.println("Java_GetClassRooms出错======"
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
