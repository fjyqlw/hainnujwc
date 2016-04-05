package com.lw.bining;

import java.sql.*;

import net.sf.json.JSONObject;

import com.lw.dbpool.DBPhnnujwc;
import com.lw.errorinfo.ERROR_INFO;
import com.lw.errorinfo.ERROR_KEY;

/**
 * @author ����
 * @date ����ʱ�䣺2015��8��6�� ����10:44:30
 * @version 1.0
 * @���� ΢�ź������ϵͳ��
 */
public class Bining {

	/** �ж��û��Ƿ��(ͨ��user��) error:0�ɹ���1ʧ�ܡ�2�����ڲ����� */
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
				// json.put(ERROR_KEY.MSG, "��δ��");
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

	/** �û��� */
	public JSONObject userBining(JSONObject inJson) {
		JSONObject outJson = new JSONObject();
		try {
     System.out.println("��biningUser��Ρ�"+inJson);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return outJson;
	}

}
