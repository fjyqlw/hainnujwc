package com.lw.test;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.client.CookieStore;

import com.lw.cj.GetSore;
import com.lw.date.MyDate;
import com.lw.login.Login;

public class Test {

	public static void main(String[] args) {
		Map loginMap=new HashMap();
		loginMap.put("xh", "201224010219");
		loginMap.put("psd", "fjyqlw7235@@");
		loginMap.put("usertype", "学生");
		loginMap.put("logintype", 1);
		
		
		Login login=new Login(null);
		login.login("", "", "", "", "");
//		Map loginResultMap=null;
//		try {
//			ExecutorService pool=Executors.newFixedThreadPool(1);
//			  Callable c1=new Login(loginMap);
//			  Future f1=pool.submit(c1);
//			  loginResultMap=(Map)f1.get();
//			  System.out.println(loginResultMap);
//			  pool.shutdown();
//		} catch (Exception e) {
//			System.out.println(e.toString());
//		}
		/*
		Map cjMap=new HashMap();
		cjMap.put("btnType", "btn_zcj");
		cjMap.put("btnValue", "历年成绩");
		cjMap.put("kcxz", "");
		cjMap.put("xn", "");
		cjMap.put("xq", "");
		cjMap.put("cookieStore", (CookieStore)loginResultMap.get("cookieStore"));
		cjMap.put("url_cjcx", (String)loginResultMap.get("login_url"));
		cjMap.put("ip", (String)loginResultMap.get("ip"));
		Map cjMapResultMap=null;
		try {
			GetSore getSore=new GetSore(cjMap);
			getSore.start();
			System.out.println("end");
			ExecutorService pool=Executors.newFixedThreadPool(1);
			  Callable c1=new GetSore(cjMap);
			  Future f1=pool.submit(c1);
			  cjMapResultMap=(Map)f1.get();
			  System.out.println(cjMapResultMap);
			  pool.shutdown();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
*/
	}

}
