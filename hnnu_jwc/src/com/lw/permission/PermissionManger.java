package com.lw.permission;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.json.JSONObject;

import com.lw.bining.Bining;
import com.lw.dbpool.DBPhnnujwc;

public class PermissionManger {
	public static JSONObject getPermissionInfo(String eventKey,
			String fromUserName) {
		JSONObject json = new JSONObject();
		String permission = "1000";
		int state = 0;
		String message = "";
		int role = -1;
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			/** ��ù���Ȩ����Ϣ */

			conn = DBPhnnujwc.getPool().getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM menuctr WHERE eventKey="
					+ eventKey);

			System.out.println(rs.isClosed());
			if (rs.next()) {
				permission = rs.getString("permission");
				state = rs.getInt("state");
				message = rs.getString("message");
				System.out.println(state + "\t" + message + "\t" + permission);
			} else {// ϵͳ����
				state = 1003;
				message = "�������ڲ�����";
			}

			System.out.println("state=" + state);
			/** �жϲ˵����Ƿ����� */
			switch (state) {
			case 0:// ����
				try {
					/** ��ȡ�û�����Ϣ */
					System.out.println("-------- 14 1-------------");
					JSONObject json2 = Bining.getBiningInfo(fromUserName);

					System.out
							.println("-------- 15 ---------json2----" + json2);
					/** �ж��û���״̬ */
					switch (json2.getInt("error")) {
					case 0:// �Ѱ�
						role = json2.getInt("role");

						int pm[] = stringToInts(permission);
						switch (role) {
						case -1:// ��ͨ�û�
							if (pm[0] == 1) {
								json.put("error", 0);
							} else {
								json.put("error", 1002);
								json.put("errorinfo", "û��Ȩ��");
							}
							break;
						case 0:// ѧ��
							if (pm[1] == 1) {
								json.put("error", 0);
							} else {
								json.put("error", 1);
								json.put("errorinfo", "û��Ȩ��");
							}
							break;
						case 1:// ��ʦ
							if (pm[2] == 1) {
								json.put("error", 0);
							} else {
								json.put("error", 1);
								json.put("errorinfo", "û��Ȩ��");
							}
							break;
						case 2:// ����
							if (pm[3] == 1) {
								json.put("error", 0);
							} else {
								json.put("error", 1);
								json.put("errorinfo", "û��Ȩ��");
							}
							break;

						default:
							// isallow=false;
							break;
						}
						break;
					case 2:// ϵͳ����
						json.put("error", 2);
						json.put("errorinfo", json2.getString("errorinfo"));
						break;
					case 1001:// δ��
						json.put("error", 1001);
						json.put("errorinfo", json2.getString("errorinfo"));
						break;

					default:
						break;
					}

				} catch (Exception e) {

					System.out.println("-------- 16 -------------");
					json.put("error", -2);
					json.put("errorinfo", "PermissionManger:isAllow:case 0 ==>"
							+ e.toString());
					System.out.println("PermissionManger:isAllow:case 0 ==>"
							+ e.toString());
				}

				break;
			case 1:// �˵��ر�
				json.put("error", 1000);
				json.put("errorinfo", message);
				break;
			case -2:// ���ݿ�û�иü�¼
				json.put("error", 1003);
				json.put("errorinfo", message);
				break;

			default:
				break;
			}

		} catch (Exception e) {
			System.out.println("-------- 17 -------------");
			json.put("error", -2);
			json.put("errorinfo", "PermissionManger:isAllow ==>" + e.toString());
			System.out.println("PermissionManger:isAllow ==>" + e.toString());
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

	private static int[] stringToInts(String s) {
		int[] n = new int[s.length()];
		for (int i = 0; i < s.length(); i++) {
			n[i] = Integer.parseInt(s.substring(i, i + 1));
		}
		return n;
	}
}
