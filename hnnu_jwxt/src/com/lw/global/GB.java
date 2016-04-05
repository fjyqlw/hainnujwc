package com.lw.global;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class GB {
	DefaultHttpClient client;
	String cookies;
	public DefaultHttpClient getClient() {
		return client;
	}
	public void setClient(DefaultHttpClient client) {
		this.client = client;
	}
	public String getCookies() {
		return cookies;
	}
	public void setCookies(String cookies) {
		this.cookies = cookies;
	}
}
