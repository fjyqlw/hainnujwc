package com.lw.login;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.*;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.*;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sun.misc.BASE64Encoder;

import com.lw.cj.GetBuKao;
import com.lw.cj.GetSore;
import com.lw.classrooms.GetClassRooms;
import com.lw.classrooms.GetClassRoomsTch;
import com.lw.config.Methods;
import com.lw.db.DB;
import com.lw.db.SevSch;
import com.lw.errorinfo.ERROR_INFO;
import com.lw.errorinfo.ERROR_KEY;
import com.lw.methods.RequestRESDelegate;
import com.lw.methods.RequestRESService;
import com.lw.teacherinfo.TeaInfo;
import com.lw.xuanke.GetXuanKe;

public class Login extends Thread implements Callable<Map> {

	String cookie = null;
	String mianUrl = null;
	int isc1 = 1, isc2 = 1;
	/** ʹ�õ�¼ģ�����;��ʹ�ò�����1����;2���ɼ�ͳ�ƣ�3�����³ɼ���4������ȷ��;5��ѡ�β�ѯ;6����ʦ���˼���;7���ս��Ҳ�ѯ�� */
	// static int loginType = 1;
	CookieStore cookieStore;
	/** ȫ�ֱ����������洢�������� */
	//Map hp = new HashMap();
	/** ȫ�ֱ������������� */
	Map classrooMap = new HashMap();
	Map sev_map = new HashMap();
	SevSch sevSch = new SevSch();
	private Map loginMap;

	public Login(Map loginMap) {
		this.loginMap = loginMap;
	}

	@Override
	public void run() {// ���ں�̨

		// Map loginResultMap = login((String) loginMap.get("xh"),
		// (String) loginMap.get("psd"), (String) loginMap.get("usertype"));

	}

	/**
	 * ��¼ģ��
	 * 
	 * @param xh
	 *            ѧ��
	 * @param psd
	 *            ����
	 * @param usertype
	 *            �û�����
	 * @param logintype
	 *            ʹ�õ�¼ģ�����;��ʹ�ò�����1����;2���ɼ�ͳ�ƣ�3�����³ɼ���4������ȷ��;5��ѡ�β�ѯ;6����ʦ���˼���;7��
	 *            ���½��Ҹ��˼���;8���ս��Ҳ�ѯ��
	 */
	/**
	 * @param xh
	 * @param psd
	 * @param usertype
	 * @param txtSecretCode
	 * @param cookiein
	 * @return
	 */
	/**
	 * @param xh
	 * @param psd
	 * @param usertype
	 * @param txtSecretCode
	 * @param cookiein
	 * @return
	 */
	/**
	 * @param xh
	 * @param psd
	 * @param usertype
	 * @param txtSecretCode
	 * @param cookiein
	 * @return
	 */
	/**
	 * @param xh
	 * @param psd
	 * @param usertype
	 * @param txtSecretCode
	 * @param cookiein
	 * @return
	 */
	public JSONObject login(final String xh, final String psd, final String usertype,
			final String txtSecretCode, final DefaultHttpClient client) {
		//hp.put("loginStatus", -1);
		JSONObject outJson=new JSONObject();
		JSONObject datdaJson=new JSONObject();
        String ServerIP="",viewstate="";
	//	System.out.println("cookie=" + "ASP.NET_SessionId=" + cookiein);
		//final DefaultHttpClient client = new DefaultHttpClient();
		/** ��ȡ����������ڷ����� */
		try {
			sev_map = sevSch.getSev();
			JSONObject inJsonsev=new JSONObject();
			 RequestRESService rrs=new RequestRESService();
				RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
				inJsonsev.put(ERROR_KEY.METHOD, Methods.achieveServer);
				
				JSONObject j1=new JSONObject();
				j1=JSONObject.fromObject( rrd.requestRES(inJsonsev.toString()));
				ServerIP=j1.getJSONObject(ERROR_KEY.DATA).getString("sevip");
				viewstate=j1.getJSONObject(ERROR_KEY.DATA).getString("viewstate");
		} catch (Exception e) {
			sev_map.put("ip", "210.37.0.27");
			sev_map.put("viewstate",
					"dDwyODE2NTM0OTg7Oz6HUJqmDIQtFOzZEhCpCbCAYzXb1Q==");
			ServerIP="http://210.37.0.27/";
			viewstate="dDwyODE2NTM0OTg7Oz6HUJqmDIQtFOzZEhCpCbCAYzXb1Q==";
		} finally {
			// System.out.println("��ǰ��������" + (String) sev_map.get("ip"));
		//	hp.putAll(sev_map);
		}

		datdaJson.put("sevip", "http://210.37.0.28/");
		try {
			/** ��¼ģ�鿪ʼ */
			/** ���½����������ʹ������ */
			// sevSch.startSev((String) hp.get("ip"));
			System.out.println("----1----");

			
//			HttpGet get0 = new HttpGet("http://210.37.0.28/default2.aspx");
//			HttpResponse httpResponse0 = client.execute(get0);
//			EntityUtils.consume(httpResponse0.getEntity()); // �ͷ�
		//	EntityUtils.consume(get0.get); // �ͷ�


			System.out.println("��1��" + client.getCookieStore());
			HttpPost httpPost = new HttpPost(ServerIP+"default2.aspx"); // ����һ��Post���󣬵�һ���ķ�����Post������

			System.out.println("----2----"
					+ client.getCookieStore().getCookies());
			// ��ֹ�ض������ڸո�Post��״ֵ̬���ض�����������Ҫȥ��ֹ������Ȼ��ҳ���ҷɡ�
			httpPost.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS,
					false);
			// ����ͷ����Ϣ��ͷ����Ϣ�ڸոյ�Httpwatch����Headers��ǩ���У������Ҹо�д���д��û�������ֻ�Ƕ�дû�л����ɡ���
			httpPost.setHeader("User-Agent",
					"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded");

//			try {
//				// HttpGet httpget = new HttpGet("http://" + (String)
//				//
//				// hp.get("ip") + "/CheckCode.aspx");
//
//				HttpGet get = new HttpGet("http://210.37.0.28/CheckCode.aspx");
//				HttpResponse httpResponse = client.execute(get);
//
//				System.out.println("��3��" + client.getCookieStore());
//				HttpEntity entity = httpResponse.getEntity();
//
//				cookie = "ASP.NET_SessionId="
//						+ client.getCookieStore().getCookies().get(0)
//								.getValue();
//				System.out.println("----getCookies----" + cookie);
//
//				if (entity != null) { // ��ȡ���� String picName = "/ck.bmp";
//					InputStream instream = entity.getContent();
//
//					OutputStream outStream = new FileOutputStream("C:\\ck.bmp");
//					IOUtils.copy(instream, outStream);
//					outStream.close();
//				}
//
//				EntityUtils.consume(httpResponse.getEntity()); // �ͷ�
//			} catch (Exception e) { // TODO: handle exception
//				System.out.println("��ȡ��֤�����"+e.toString());
//			}
//			System.out.println(client);
//			Scanner in = new
//
//			Scanner(System.in);
//			String ck = in.next();
//			System.out.println("ck=" + ck);

			System.out.println("------3-1------");
			// ��һ��ģ���½��ֵ
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			/**
			 * �����������ݾ������ǵ�֮ǰ��POST������ݣ�����������֤��
			 */
			params.add(new BasicNameValuePair("__VIEWSTATE",viewstate)); // xxx��ʾҪ�ύ�����ݣ���Ҫ�Լ���ȡ
			params.add(new BasicNameValuePair("Button1", ""));
			params.add(new BasicNameValuePair("hidPdrs", ""));
			params.add(new BasicNameValuePair("hidsc", ""));
			params.add(new BasicNameValuePair("lbLanguage", ""));
			params.add(new BasicNameValuePair("RadioButtonList1", usertype));
			params.add(new BasicNameValuePair("TextBox2", psd));
			params.add(new BasicNameValuePair("txtSecretCode", txtSecretCode));// txtSecretCode
			params.add(new BasicNameValuePair("txtUserName", xh));
			// ���ݲ�����ʱ��ע�����ʹ��,��������
			httpPost.setEntity(new UrlEncodedFormEntity(params, "GBK"));
			// ��Ӧ����
			HttpResponse response = client.execute(httpPost);

			// ��ȡ��Ӧ״̬��
			int Status = response.getStatusLine().getStatusCode();
			System.out.println("----4----Status=" + Status);
			// 302��ʾ�ض���״̬
			if (Status == 302 || Status == 301) {

				System.out.println(response.toString());
				// ��ȡ��Ӧ��cookieֵ
				// cookie = response.getFirstHeader("Set-Cookie").getValue();
			//	hp.put("cookie", cookie);
				cookieStore = client.getCookieStore();
				// response.getFirstHeader("Set-Cookie").getValue();
				System.out.println("��login-cookieStore��"
						+ cookieStore.getCookies().toString());

				// ��ȡͷ����Ϣ��Location��ֵ
				String location = response.getFirstHeader("Location")
						.getValue();

				mianUrl = ServerIP+ location;
				//System.out.println("----5--mianUrl=" + mianUrl);
			}
			EntityUtils.consume(response.getEntity()); // �ͷ�
			EntityUtils.consume(httpPost.getEntity()); // �ͷ�
			HttpGet get = new HttpGet(mianUrl);
			cookie="ASP.NET_SessionId="+client.getCookieStore().getCookies().get(0).getValue();
			// �ؼ���,�����и��ؼ����������ͷ����Ϣ�е�Referer��cookieֵ��cookieֵ��Ҷ�֪����ģ���½��ʱ��������cookieһ����ʣ�����Referer���޷���⣬������Ҫ���á�
			// Ҳ���Ǳ���ָ������Referer����Ϊ��ǰ���ʵ�URL
			get.setHeader("Referer", mianUrl);
			get.addHeader("Cookie", cookie);

			// ��ȡGet��Ӧ�����״̬����200�Ļ���ʾ���ӳɹ�
			HttpResponse httpResponse = new DefaultHttpClient().execute(get);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {//��¼�ɹ�
				HttpEntity entity = httpResponse.getEntity();
				// ��ȡ��������ҳHTMLԴ�룬�����ҿ��Խ�mianhtml�����������ط�
				String mainhtml = EntityUtils.toString(entity);
			//	System.out.println(mainhtml);
				System.out.println("----666666666666----");
				
				JSONObject j1= filterHtml(mainhtml);// ���ֲ�ѯURL
				
				outJson.put(ERROR_KEY.ERROR, j1.getInt(ERROR_KEY.ERROR));
				datdaJson.putAll(j1.getJSONObject(ERROR_KEY.DATA));
				//String login_url_cjcx 
				
				
				
			//	String login_user = filterHtml2(mainhtml);

			//	login_user = login_user.substring(0, login_user.length() - 2);
			//	hp.put("loginStatus", 1);
			//	hp.put("cookieStore", cookieStore);
			//	hp.put("login_user", login_user);
			//	System.out.println("��¼�û�:" + login_user);
				System.out.println("��¼ѧ��:" + xh);
			//	System.out.println("��Ҫ����ַ:" + hp);

			}

			get.abort();// �Ͽ�����
			get = null;
			/** ��¼ģ����� */
//			datdaJson.put("client", client);
//			datdaJson.put("cookiestore", client.getCookieStore());
			datdaJson.put("xh", xh);
			outJson.put(ERROR_KEY.ERROR, ERROR_INFO.SUCCESS);
			outJson.put(ERROR_KEY.DATA, datdaJson);

		} catch (Exception e) {
			outJson.put(ERROR_KEY.ERROR, ERROR_INFO.PROGRAM_ERROR);
			outJson.put(ERROR_KEY.MSG, e.toString());
//			hp.put("loginStatus", 0);
			System.out.println("���������Java_Login����" + e.toString());
		} finally {
			client.close();
		//	sevSch.endSev((String) hp.get("ip"));
			System.out.println("���շ��ص����ݣ�\n"+outJson);
		}

		return outJson;
	}

	private JSONObject filterHtml(String source) {// ��ȡ��Ҫ�Ĳ�ѯ��ַ
		JSONObject outJson=new JSONObject();
		JSONObject datdaJson=new JSONObject();
		if (null == source) {

			outJson.put(ERROR_KEY.ERROR, ERROR_INFO.PROGRAM_ERROR);
			outJson.put(ERROR_KEY.MSG, "source=null");
			return outJson;
		}
		try {
			
		
		String html = source;
		Document doc = Jsoup.parse(html); // ��HTML������ص�doc�� <span
		
		Elements links_user = doc.select("span[id=xhxm]");
		String userString=links_user.text();
		userString=userString.substring(0, userString.length()-2);
System.out.println("userString="+userString);

      datdaJson.put("user", userString);
		
		
		Elements links = doc.select("a[href]"); // ���ǿγ�������Ϊ�γ�����HTML��ǩ��<td
//		hp.put("login_url", "no");
//		hp.put("login_url_xuanke", "no");
//		hp.put("login_url_teainfo", "no");
//		hp.put("login_url_classroom", "no");
		for (Element link : links) {
			// ��ȡ��Ҫ��ѯ��URL,�����Ӧ��ַ��ť�����ֽгɼ���ѯ
			if (link.text().equals("�ɼ���ѯ")) {
				// ��ȡ��Ҫ��ѯ����Ե�ַ����ȡ��Եĵ�ַ
				// ���ز�ѯ��URL,����ҳ��ַ����Ե�ַ����������ͬ�������cjcxUrl���Զ���������
//				String cjcxUrl = "http://" + (String) hp.get("ip") + "//"
//						+ link.attr("href");
				datdaJson.put("cjcx", link.attr("href"));
				//hp.put("login_url", cjcxUrl);
			}
			if (link.text().equals("רҵѡ�޿�")) {
				// ��ȡ��Ҫ��ѯ����Ե�ַ����ȡ��Եĵ�ַ
				// ���ز�ѯ��URL,����ҳ��ַ����Ե�ַ����������ͬ�������cjcxUrl���Զ���������
//				String cjcxUrl = "http://" + (String) hp.get("ip") + "//"
//						+ link.attr("href");
				//hp.put("login_url_xuanke", cjcxUrl);
				datdaJson.put("zyxxk", link.attr("href"));
			}
			if (link.text().equals("���˼���")) {
				// ��ȡ��Ҫ��ѯ����Ե�ַ����ȡ��Եĵ�ַ
				// ���ز�ѯ��URL,����ҳ��ַ����Ե�ַ����������ͬ�������cjcxUrl���Զ���������
//				String cjcxUrl = "http://" + (String) hp.get("ip") + "//"
//						+ link.attr("href");
			//	hp.put("login_url_teainfo", cjcxUrl);
				datdaJson.put("grjl", link.attr("href"));
			}
			if (link.text().equals("���Ҳ�ѯ")) {
				// ��ȡ��Ҫ��ѯ����Ե�ַ����ȡ��Եĵ�ַ
				// ���ز�ѯ��URL,����ҳ��ַ����Ե�ַ����������ͬ�������cjcxUrl���Զ���������
//				String cjcxUrl = "http://" + (String) hp.get("ip") + "//"
//						+ link.attr("href");
				//hp.put("login_url_classroom", cjcxUrl);
				datdaJson.put("jscx", link.attr("href"));
			}

		}
			outJson.put(ERROR_KEY.DATA, datdaJson);
			outJson.put(ERROR_KEY.ERROR, ERROR_INFO.SUCCESS);
		} catch (Exception e) {
			outJson.put(ERROR_KEY.ERROR, ERROR_INFO.PROGRAM_ERROR);
			outJson.put(ERROR_KEY.MSG, e.toString());
		}finally{
			System.out.println(outJson);
		}
		return outJson;// cjcxUrl
	}

//	private String filterHtml2(String source) {
//		if (null == source) {
//			return "";
//		}
//		// isc2=true;
//		StringBuffer sff = new StringBuffer();
//		String score[];
//		int i = 0, j = 0;
//		String html = source;
//		Document doc = Jsoup.parse(html); // ��HTML������ص�doc�� <span
//											// id="xhxm"><span id="Label3">
//		Elements links_user = doc.select("span[id=xhxm]");
//		for (Element link : links_user) {
//			sff.append(link.text()); // .append(" : ").append("\n")
//			j = j + 2; // ����֮����+2����Ϊ�����ı�ǩ��<td width=5%
//		}
//		html = sff.toString();
//		return html;
//	}

	public static void main(String[] a) {

	}

	@Override
	public Map call() {
		Map loginResultMap = null;
		// System.out.println(loginMap);
		try {

			// loginResultMap = login((String) loginMap.get("xh"),
			// (String) loginMap.get("psd"),
			// (String) loginMap.get("usertype"));
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return loginResultMap;
	}

	public String ioToBase64() throws IOException {
		String fileName = "d:/gril.gif"; // Դ�ļ�
		String strBase64 = null;
		try {
			InputStream in = new FileInputStream(fileName);
			// in.available()�����ļ����ֽڳ���
			byte[] bytes = new byte[in.available()];
			// ���ļ��е����ݶ��뵽������
			in.read(bytes);
			strBase64 = new BASE64Encoder().encode(bytes); // ���ֽ�������ת��Ϊ�ַ���
			in.close();
		} catch (FileNotFoundException fe) {
			fe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return strBase64;
	}
}
