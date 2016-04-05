package com.lw.server;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.lw.dbpool.DBPhnnujwc;


public class SevSch {
public void startSev(String sev){
	Connection connection = null;
	Statement stmt = null;
	ResultSet rs = null;
	try {
		connection = DBPhnnujwc.getPool().getConnection();

		stmt = connection.createStatement();
		stmt.executeUpdate("update server set count=count+1 where ip='"+sev+"'");
	
		connection.close();
	} catch (Exception e) {
		System.out.println("db.startSev**´íÎó=" + e.toString());
	}
	
}
public void endSev(String sev){
	Connection connection = null;
	Statement stmt = null;
	ResultSet rs = null;
	try {
		connection = DBPhnnujwc.getPool().getConnection();

		stmt = connection.createStatement();
		stmt.executeUpdate("update server set count=count-1 where ip='"+sev+"'");
	
		connection.close();
	} catch (Exception e) {
		System.out.println("db.getLNCJ**´íÎó=" + e.toString());
	}
}
	public Map getSev() {
		Map sev_map=new HashMap();
		String sev = "210.37.0.27",viewstate="";
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			
			connection = DBPhnnujwc.getPool().getConnection();

			stmt = connection.createStatement();
			rs = stmt
					.executeQuery("SELECT * FROM server WHERE isuse=1 ORDER BY count ASC ,id asc");

			while (rs.next()) {
				sev = rs.getString("ip");
				viewstate= rs.getString("viewstate");
			URL url = new URL("http://" + sev + "/default2.aspx");

			HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
			urlCon.setReadTimeout(6000);
			if (null!=urlCon.getHeaderField(0)) {
				break;
			}
			}
			
			connection.close();
			sev_map.put("ip", sev);
			sev_map.put("viewstate", viewstate);
		} catch (Exception e) {
			System.out.println("db.getSev**´íÎó=" + e.toString());
		}
System.out.println(sev_map);
		return sev_map;
	}


}
