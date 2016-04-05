package com.lw.bining;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.liufeng.course.util.MessageUtil;

import com.lw.dbpool.DBPhnnujwc;

//import com.sun.org.apache.regexp.internal.recompile;

/**
 * @author ����
 * @date ����ʱ�䣺2015��8��6�� ����10:44:30
 * @version 1.0
 * @���� ΢�ź������ϵͳ��
 */
public class Bining {
	/** �ж��û��Ƿ��(ͨ��user��) error:0�ɹ���1ʧ�ܡ�2�����ڲ����� */
	public static JSONObject getBiningInfo(String openID) {
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;

		JSONObject json = new JSONObject();
		json.put("error", 2);
		boolean isb = false;
		try {
			String sql = "";

			sql = "select * from user where openID='" + openID + "'";
			// rs = DBhnnujwc.querySQL(sql);
			conn = DBPhnnujwc.getPool().getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				json.put("role", rs.getInt("role"));
				isb = true;
			}

			if (isb) {
				json.put("error", 0);
			} else {
				json.put("error", 1001);
				json.put("errorinfo", "��δ��");
			}
		} catch (Exception e) {
			json.put("error", 2);
			json.put("errorinfo", e.toString());
			System.out.println(e.toString());
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
		}
		return json;
	}

}
