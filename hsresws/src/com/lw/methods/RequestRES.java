package com.lw.methods;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.lw.bining.Bining;
import com.lw.dbpool.DBPhnnujwc;
import com.lw.dbpool.DBPstukebiao;
import com.lw.dbpool.DBPstuscore;
import com.lw.dbpool.DBPtchkebiao;
import com.lw.errorinfo.ERROR_INFO;
import com.lw.errorinfo.ERROR_KEY;
import com.lw.msgrecord.MessageRecord;
import com.lw.permission.PermissionManger;

import net.sf.json.JSONObject;

public class RequestRES {

	public String requestRES(String inJsonStr) {

		JSONObject inJson = new JSONObject();
		JSONObject outJson = new JSONObject();
		JSONObject dataJson = new JSONObject();
		int methodCode = 100;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "";
		try {
			inJson = JSONObject.fromObject(inJsonStr);
			if (null == inJson || !inJson.has("method")) {
				System.out.println("入参错误");
				outJson.put(ERROR_KEY.ERROR, ERROR_INFO.IN_PARAM_ERR);
				outJson.put(ERROR_KEY.MSG, "入参错误");
			} else {// 入参正常
				methodCode = inJson.getInt("method");

				switch (methodCode) {
				case Methods.dbExecute_hnnujwc:
					try {
						conn = DBPhnnujwc.getPool().getConnection();
						stmt = conn.createStatement();
						sql = inJson.getJSONObject(ERROR_KEY.PARAMTER)
								.getString("sql");
						System.out.println(sql);
						stmt.executeUpdate(sql);

						outJson.put(ERROR_KEY.ERROR, ERROR_INFO.SUCCESS);
					} catch (Exception e) {
						outJson.put(ERROR_KEY.ERROR, ERROR_INFO.SQL_EXEC_ERR);
						outJson.put(ERROR_KEY.MSG, e.toString());
					}
					break;

				case Methods.dbExecute_stukebiao:
					try {
						conn = DBPstukebiao.getPool().getConnection();
						stmt = conn.createStatement();
						sql = inJson.getJSONObject(ERROR_KEY.PARAMTER)
								.getString("sql");
						stmt.executeUpdate(sql);

					} catch (Exception e) {
						outJson.put(ERROR_KEY.MSG, e.toString());
					}
					break;

				case Methods.dbExecute_stuscore:
					try {
						conn = DBPstuscore.getPool().getConnection();
						stmt = conn.createStatement();
						sql = inJson.getJSONObject(ERROR_KEY.PARAMTER)
								.getString("sql");
						stmt.executeUpdate(sql);

					} catch (Exception e) {
						outJson.put(ERROR_KEY.MSG, e.toString());
					}
					break;

				case Methods.dbExecute_tchkebiao:
					try {
						conn = DBPtchkebiao.getPool().getConnection();
						stmt = conn.createStatement();
						sql = inJson.getJSONObject(ERROR_KEY.PARAMTER)
								.getString("sql");
						stmt.executeUpdate(sql);

					} catch (Exception e) {
						outJson.put(ERROR_KEY.MSG, e.toString());
					}
					break;

				case Methods.dbExecute_zxmicrocampus:
					try {
						conn = DBPtchkebiao.getPool().getConnection();
						stmt = conn.createStatement();
						sql = inJson.getJSONObject(ERROR_KEY.PARAMTER)
								.getString("sql");
						stmt.executeUpdate(sql);

					} catch (Exception e) {
						outJson.put(ERROR_KEY.MSG, e.toString());
					}
					break;

				/** 消息记录 hnnu_jwc */
				case Methods.msgRecord:

					MessageRecord mr = new MessageRecord();
					try {
						JSONObject j1 = mr.recordMSG(
								inJson.getJSONObject(ERROR_KEY.PARAMTER)
										.getString("openID"), inJson
										.getJSONObject(ERROR_KEY.PARAMTER)
										.getString("content"));
						if (j1.getInt(ERROR_KEY.ERROR) == 0) {
							outJson.put(ERROR_KEY.ERROR, ERROR_INFO.SUCCESS);
						} else {
							outJson.put(ERROR_KEY.ERROR,
									j1.getInt(ERROR_KEY.ERROR));
							outJson.put(ERROR_KEY.MSG,
									j1.getString(ERROR_KEY.MSG));

						}
					} catch (Exception e) {
						outJson.put(ERROR_KEY.ERROR, ERROR_INFO.PROGRAM_ERROR);
						outJson.put(ERROR_KEY.MSG, e.toString());
					} finally {
						mr = null;
					}

				case Methods.biningInfo:
					Bining bining = new Bining();
					try {
						JSONObject j1 = bining.getBiningInfo(inJson
								.getJSONObject(ERROR_KEY.PARAMTER).getString(
										"openID"));
						if (j1.getInt(ERROR_KEY.ERROR) == 0) {
							outJson.put(ERROR_KEY.ERROR, ERROR_INFO.SUCCESS);
							outJson.put(ERROR_KEY.DATA,
									j1.getJSONObject(ERROR_KEY.DATA));
						} else {
							outJson.put(ERROR_KEY.ERROR,
									j1.getInt(ERROR_KEY.ERROR));
							outJson.put(ERROR_KEY.MSG,
									j1.getString(ERROR_KEY.MSG));

						}
					} catch (Exception e) {
						outJson.put(ERROR_KEY.ERROR, ERROR_INFO.PROGRAM_ERROR);
						outJson.put(ERROR_KEY.MSG, e.toString());
					} finally {
						bining=null;
					}
					break;
					/**获取权限信息*/
				case Methods.permissionInfo:
					PermissionManger pm = new PermissionManger();
					try {
						JSONObject j1 = pm.getPermissionInfo(inJson
								.getJSONObject(ERROR_KEY.PARAMTER).getString(
										"eventKey"),inJson
										.getJSONObject(ERROR_KEY.PARAMTER).getString(
												"openID"));
						if (j1.getInt(ERROR_KEY.ERROR) == 0) {
							outJson.put(ERROR_KEY.ERROR, ERROR_INFO.SUCCESS);
							outJson.put(ERROR_KEY.DATA,
									j1.getJSONObject(ERROR_KEY.DATA));
						} else {
							outJson.put(ERROR_KEY.ERROR,
									j1.getInt(ERROR_KEY.ERROR));
							outJson.put(ERROR_KEY.DATA,
									j1.getJSONObject(ERROR_KEY.DATA));

						}
					} catch (Exception e) {
						outJson.put(ERROR_KEY.ERROR, ERROR_INFO.PROGRAM_ERROR);
						outJson.put(ERROR_KEY.MSG, e.toString());
					} finally {
						pm=null;
					}
					break;
					
				case Methods.biningUser:
					try {
                        bining=new Bining();
						JSONObject j1= bining.userBining(inJson);
						if (j1.getInt(ERROR_KEY.ERROR) == 0) {
							outJson.put(ERROR_KEY.ERROR, ERROR_INFO.SUCCESS);
							outJson.put(ERROR_KEY.DATA,
									j1.getJSONObject(ERROR_KEY.DATA));
						} else {
							outJson.put(ERROR_KEY.ERROR,
									j1.getInt(ERROR_KEY.ERROR));
							outJson.put(ERROR_KEY.DATA,
									j1.getJSONObject(ERROR_KEY.DATA));

						}
						outJson.put(ERROR_KEY.ERROR, ERROR_INFO.SUCCESS);
					} catch (Exception e) {
						outJson.put(ERROR_KEY.ERROR, ERROR_INFO.SQL_EXEC_ERR);
						outJson.put(ERROR_KEY.MSG, e.toString());
					}
					break;
					/**获取权限信息*/
				case Methods.achieveServer:
					PermissionManger pm = new PermissionManger();
					try {
						JSONObject j1 = pm.getPermissionInfo(inJson
								.getJSONObject(ERROR_KEY.PARAMTER).getString(
										"eventKey"),inJson
										.getJSONObject(ERROR_KEY.PARAMTER).getString(
												"openID"));
						if (j1.getInt(ERROR_KEY.ERROR) == 0) {
							outJson.put(ERROR_KEY.ERROR, ERROR_INFO.SUCCESS);
							outJson.put(ERROR_KEY.DATA,
									j1.getJSONObject(ERROR_KEY.DATA));
						} else {
							outJson.put(ERROR_KEY.ERROR,
									j1.getInt(ERROR_KEY.ERROR));
							outJson.put(ERROR_KEY.DATA,
									j1.getJSONObject(ERROR_KEY.DATA));

						}
					} catch (Exception e) {
						outJson.put(ERROR_KEY.ERROR, ERROR_INFO.PROGRAM_ERROR);
						outJson.put(ERROR_KEY.MSG, e.toString());
					} finally {
						pm=null;
					}
					break;
					
	//-------------------------------------				
				default:
					break;
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
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

		System.out.println(outJson);
		return outJson.toString();
	}
}
