package com.lw.bining;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.liufeng.course.util.MessageUtil;

import com.lw.dbpool.DBPhnnujwc;

//import com.sun.org.apache.regexp.internal.recompile;

/**
 * @author 练威
 * @date 创建时间：2015年8月6日 下午10:44:30
 * @version 1.0
 * @描述 微信号与教务系统绑定
 */
public class Bining {
	/** 判断用户是否绑定(通过user表) error:0成功、1失败、2程序内部错误 */
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
				json.put("errorinfo", "您未绑定");
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
