package com.lw.weixinjs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Sign {
	// 微信JSSDK的AccessToken请求URL地址
	public final static String weixin_jssdk_acceToken_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx0442dc2fb1a7841e&secret=5fbf334424c28e63dcedb1e5e3e44283";
	// 微信JSSDK的ticket请求URL地址
	//public final static String weixin_jssdk_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=TUBJUY6lRvISlZwFxONfXOg0Ff1ku9r9bJH6LHnm7OZ-MBDxx9XD7k1b1ouVvmHh4M0HWQ6_aFHZBvb5v_Jpew7Cx1-XBILyhQImHMKM0-YVZYjAHAKON&type=jsapi";

	public static void main(String[] args) {
		Sign sign = new Sign();
		Map<String, String> ret = new HashMap<String, String>();
		ret=sign.getConfigMap();
	}
public Map getConfigMap(){

	String jsapi_ticket = "jsapi_ticket";
	String strjson = loadJson(weixin_jssdk_acceToken_url);
	String ACCESS_TOKEN="";
	//System.out.println("json==" + strjson);
	ACCESS_TOKEN=getACCESS_TOKEN(strjson);
	
	// 微信JSSDK的ticket请求URL地址
	String weixin_jssdk_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ACCESS_TOKEN+"&type=jsapi";
jsapi_ticket=getJSTicket(loadJson(weixin_jssdk_ticket_url));
	//System.out.println("ACCESS_TOKEN="+ACCESS_TOKEN);
	//System.out.println("weixin_jssdk_ticket_url="+weixin_jssdk_ticket_url);
	// ע�� URL һ��Ҫ��̬��ȡ������ hardcode

	String url = "http://fjyqlw.link/hnnu_jwxt/signin/sign.jsp";// "http://example.com";
	Map<String, String> ret = sign(jsapi_ticket, url);
	for (Map.Entry entry : ret.entrySet()) {
		 System.out.println(entry.getKey() + ", " + entry.getValue());
	}

	
	return ret;
}
	public static Map<String, String> sign(String jsapi_ticket, String url) {
		Map<String, String> ret = new HashMap<String, String>();
		String nonce_str = create_nonce_str();
		String timestamp = create_timestamp();
		String string1;
		String signature = "";

		// ע��������������ȫ��Сд���ұ�������
		string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str
				+ "&timestamp=" + timestamp + "&url=" + url;
		System.out.println("string1: " + string1);

		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		ret.put("url", url);
		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);

		return ret;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	private static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	private static String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	// =========================
	public static String loadJson(String url) {
		StringBuilder json = new StringBuilder();
		try {
			URL urlObject = new URL(url);
			URLConnection uc = urlObject.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					uc.getInputStream()));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				json.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	/**
	 * 解析Json数据
	 * 
	 * @param jsonString
	 *            Json数据字符串
	 */
	public static String getACCESS_TOKEN(String jsonString) {
		String access_token = "";
		// 以employee为例解析，map类似
		JSONObject jb = JSONObject.fromObject(jsonString);
		// JSONArray ja = jb.getJSONArray("employee");

		Map json_map = new HashMap();
		// 循环添加Employee对象（可能有多个）

//		json_map.put("access_token", jb.getString("access_token"));
//		json_map.put("expires_in", jb.getString("expires_in"));
		access_token = jb.getString("access_token");

		return access_token;
	}
	/**
	 * 解析Json数据
	 * 
	 * @param jsonString
	 *            Json数据字符串
	 */
	public static String getJSTicket(String jsonString) {
		String api_ticket = "";
		// 以employee为例解析，map类似
		JSONObject jb = JSONObject.fromObject(jsonString);
		// JSONArray ja = jb.getJSONArray("employee");

		Map json_map = new HashMap();
		// 循环添加Employee对象（可能有多个）

//		json_map.put("access_token", jb.getString("access_token"));
//		json_map.put("expires_in", jb.getString("expires_in"));
		api_ticket = jb.getString("ticket");

		return api_ticket;
	}
}
