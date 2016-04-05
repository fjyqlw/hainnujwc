package com.lw.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.enterprise.inject.New;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.sf.json.JSONObject;

import com.lw.dbpool.DBPhnnujwc;
import com.lw.methods.RequestRES;
import com.lw.methods.RequestRESDelegate;
import com.lw.methods.RequestRESService;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			RequestRESService rss=new RequestRESService();
			RequestRESDelegate rsd=rss.getPort(RequestRESDelegate.class);
			
			JSONObject inJson=new JSONObject();
			inJson.put("method", 101);
			JSONObject j=new JSONObject();
			j.put("sql", "update menuctr set state=0 where eventKey=31");
			inJson.put("paramter", j);
			
			String outJsonStr=rsd.requestRES(inJson.toString());
			System.out.println(outJsonStr);
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}
