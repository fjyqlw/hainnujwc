package com.lw.bining;

import java.sql.*;

import net.sf.json.JSONObject;

import com.lw.dbpool.DBPhnnujwc;
import com.lw.errorinfo.ERROR_INFO;
import com.lw.errorinfo.ERROR_KEY;

/**
 * @author 练威
 * @date 创建时间：2015年8月6日 下午10:44:30
 * @version 1.0
 * @描述 微信号与教务系统绑定
 */
public class Bining {

	/** 判断用户是否绑定(通过user表) error:0成功、1失败、2程序内部错误 */
	public JSONObject getBiningInfo(String openID) {
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;

		JSONObject outJson = new JSONObject();
		JSONObject dataJson = new JSONObject();
		boolean isb = false;
		try {
			String sql = "";

			sql = "select * from user where openID='" + openID + "'";
			conn = DBPhnnujwc.getPool().getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				dataJson.put("role", rs.getInt("role"));
				isb = true;
			}

			if (isb) {
				outJson.put(ERROR_KEY.ERROR, ERROR_INFO.SUCCESS);
				outJson.put(ERROR_KEY.DATA, dataJson);
			} else {
				outJson.put(ERROR_KEY.ERROR, ERROR_INFO.USER_UNBINING);
				// json.put(ERROR_KEY.MSG, "您未绑定");
			}
		} catch (Exception e) {
			outJson.put(ERROR_KEY.ERROR, ERROR_INFO.PROGRAM_ERROR);
			outJson.put(ERROR_KEY.MSG, e.toString());
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
		return outJson;
	}

	/** 用户绑定 */
	public JSONObject userBining(JSONObject inJson) {
		JSONObject outJson = new JSONObject();
		try {
     System.out.println("【biningUser入参】"+inJson);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return outJson;
	}

}
