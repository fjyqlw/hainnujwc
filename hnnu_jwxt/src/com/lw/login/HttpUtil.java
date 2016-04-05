package com.lw.login;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * @author Still
 * @创建时间�?2012-2-11 下午05:44:49
 */
/**
 * @author Still
 * @创建时间�?2012-2-14 下午11:14:57
 */
public class HttpUtil {

	/**
	 * 
	 * @param url
	 *            url地址
	 * @param httpClient
	 *            httpClient
	 * @param setHeader
	 *            setHeader
	 * @return
	 * 			String
	 * @throws IOException
	 */
	public static String getUrl(String url, DefaultHttpClient httpClient,
			String setHeader) throws IOException {
		HttpGet request = new HttpGet(url);
		request.setHeader("Referer", setHeader);
		HttpResponse response = httpClient.execute(request);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			
			return EntityUtils.toString(response.getEntity());
		} else {
			return "";
		}
	}
	
	
	/**
	 * 
	 * @param url
	 *            url地址
	 * @param httpClient
	 *            httpClient
	 * @param setHeader
	 * 			setHeader
	 * @return
	 * 			byte[] img
	 * @throws IOException
	 */
	public static byte[] getUrl_byte(String url, DefaultHttpClient httpClient,
			String setHeader) throws IOException {
		HttpGet request = new HttpGet(url);
		request.setHeader("Referer", setHeader);
		HttpResponse response = httpClient.execute(request);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			return EntityUtils.toByteArray(response.getEntity());
		} else {
			return null;
		}
	}

	/**
	 * post提交数据
	 * 
	 * @param url
	 *            提交地址
	 * @param pairs
	 *            参数
	 * @param httpClient
	 *            httpClient          
	 * @param setHeader
	 *            Header
	 * @return 
	 * 			String
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String postUrl(String url, List<BasicNameValuePair> pairs,
			DefaultHttpClient httpClient, String setHeader)
			throws ClientProtocolException, IOException {

		HttpPost request = new HttpPost(url);
		request.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
		request.setHeader("Referer", setHeader);
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 10000); // 设置请求超时时间
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				10000); // 读取超时
		HttpResponse response = httpClient.execute(request);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			System.out.println("���سɹ���״̬"+response.getStatusLine().getStatusCode());
			return EntityUtils.toString(response.getEntity());
		} else {
			System.out.println("response.getStatusLine().getStatusCode()==="+response.getStatusLine().getStatusCode());

			return null;
		}

	}

}
