package com.lw.permission;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.json.JSONObject;

import com.lw.bining.Bining;
import com.lw.dbpool.DBPhnnujwc;
import com.lw.errorinfo.ERROR_INFO;
import com.lw.errorinfo.ERROR_KEY;

public class PermissionManger {
	public  JSONObject getPermissionInfo(String eventKey,
			String fromUserName) {
		JSONObject outJson = new JSONObject();
		JSONObject dataJson = new JSONObject();
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

			if (rs.next()) {
				permission = rs.getString("permission");
				state = rs.getInt("state");
				message = rs.getString("message");
				
				dataJson.put("permission", permission);
			} else {// ϵͳ����
				message = "�������ڲ�����";
				outJson.put(ERROR_KEY.ERROR, ERROR_INFO.NO_EVENTKEY_ERR);
				throw new Exception();
			}

			/** �жϲ˵����Ƿ����� */
			switch (state) {
			case 0:// ����
					Bining bining=new Bining();
				try {
					/** ��ȡ�û�����Ϣ */
					JSONObject json2 = bining.getBiningInfo(fromUserName);

					/** �ж��û���״̬ */
					switch (json2.getInt(ERROR_KEY.ERROR)) {
					case ERROR_INFO.SUCCESS:// �Ѱ�
						role = json2.getJSONObject(ERROR_KEY.DATA).getInt("role");

						int pm[] = stringToInts(permission);
						switch (role) {
						case -1:// ��ͨ�û�
							if (pm[0] == 1) {
								dataJson.put("role", role);
								outJson.put(ERROR_KEY.ERROR, ERROR_INFO.SUCCESS);
							} else {
								outJson.put(ERROR_KEY.ERROR, ERROR_INFO.NO_PERMISSION);
								//outJson.put("errorinfo", "û��Ȩ��");
							}
							break;
						case 0:// ѧ��
							if (pm[1] == 1) {
								dataJson.put("role", role);
								outJson.put(ERROR_KEY.ERROR, ERROR_INFO.SUCCESS);
							} else {
								outJson.put(ERROR_KEY.ERROR, ERROR_INFO.NO_PERMISSION);
								//outJson.put("errorinfo", "û��Ȩ��");
							}
							break;
						case 1:// ��ʦ
							if (pm[2] == 1) {
								dataJson.put("role", role);
								outJson.put(ERROR_KEY.ERROR, ERROR_INFO.SUCCESS);
							} else {
								outJson.put(ERROR_KEY.ERROR, ERROR_INFO.NO_PERMISSION);
								//outJson.put("errorinfo", "û��Ȩ��");
							}
							break;
						case 2:// ����
							if (pm[3] == 1) {
								dataJson.put("role", role);
								outJson.put(ERROR_KEY.ERROR, ERROR_INFO.SUCCESS);
							} else {
								outJson.put(ERROR_KEY.ERROR, ERROR_INFO.NO_PERMISSION);
								//outJson.put("errorinfo", "û��Ȩ��");
							}
							break;

						default:
							// isallow=false;
							break;
						}
						break;
					case ERROR_INFO.PROGRAM_ERROR:// ϵͳ����
						outJson.put(ERROR_KEY.ERROR, ERROR_INFO.PROGRAM_ERROR);
						//json.put("errorinfo", json2.getString("errorinfo"));
						break;
					case ERROR_INFO.USER_UNBINING:// δ��
						outJson.put(ERROR_KEY.ERROR, ERROR_INFO.USER_UNBINING);
						//json.put("errorinfo", json2.getString("errorinfo"));
						break;

					default:
						break;
					}

				} catch (Exception e) {

					outJson.put(ERROR_KEY.ERROR, ERROR_INFO.PROGRAM_ERROR);
					outJson.put(ERROR_KEY.MSG, "PermissionManger:isAllow:case 0 ==>"
							+ e.toString());
					System.out.println("PermissionManger:isAllow:case 0 ==>"
							+ e.toString());
				}finally{
					bining=null;
					outJson.put(ERROR_KEY.DATA, dataJson);
				}

				break;
			case 1:// �˵��ر�
				outJson.put(ERROR_KEY.ERROR, ERROR_INFO.MENU_CLOSE);
				dataJson.put("message", message);
				outJson.put(ERROR_KEY.DATA, dataJson);
				//json.put("errorinfo", message);
				break;
//			case -2:// ���ݿ�û�иü�¼
//				json.put("error", 1003);
//				json.put("errorinfo", message);
//				break;

			default:
				outJson.put(ERROR_KEY.ERROR, ERROR_INFO.MENU_CLOSE);
				dataJson.put("message", message);
				outJson.put(ERROR_KEY.DATA, dataJson);
				//json.put("errorinfo", message);
				break;
			}

		} catch (Exception e) {
			outJson.put(ERROR_KEY.ERROR, ERROR_INFO.PROGRAM_ERROR);
			outJson.put(ERROR_KEY.MSG, "PermissionManger:isAllow ==>" + e.toString()+outJson.getString(ERROR_KEY.MSG));
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

		return outJson;
	}

	private static int[] stringToInts(String s) {
		int[] n = new int[s.length()];
		for (int i = 0; i < s.length(); i++) {
			n[i] = Integer.parseInt(s.substring(i, i + 1));
		}
		return n;
	}
}
