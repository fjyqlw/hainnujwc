package com.lw.msgrecord;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.json.JSONObject;

import com.lw.dbpool.DBPhnnujwc;
import com.lw.errorinfo.ERROR_INFO;
import com.lw.errorinfo.ERROR_KEY;
import com.sun.xml.registry.uddi.bindings_v2_2.ErrInfo;

public class MessageRecord {

	public JSONObject recordMSG(String fromUserName, String ContenText) {
		JSONObject outJson=new JSONObject();
		StringBuffer sql = new StringBuffer();
		sql.append("insert into usermessage(openID,content) values('");
		sql.append(fromUserName);
		sql.append("','");
		sql.append(ContenText);
		sql.append("')");

		/** 记录消息 */
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = DBPhnnujwc.getPool().getConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate(sql.toString());
		} catch (Exception e) {
			System.out.println("记录消息出错" + e.toString());
			outJson.put(ERROR_KEY.ERROR, ERROR_INFO.MSG_RECORD_ERR);
		} finally {
			try {
				rs.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			try {
				stmt.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			try {
				conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			sql.delete(0, sql.length());
		}
	return outJson;
	}
}
