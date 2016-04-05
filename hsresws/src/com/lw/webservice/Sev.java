package com.lw.webservice;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

import com.lw.db.DBhnnujwc;
import com.lw.db.DBstukebiao;
import com.lw.db.DBstuscore;
import com.lw.db.DBtchkebiao;

public class Sev {
	public String login(String id, String pwd) {
		JSONObject jObject = new JSONObject();
		try {
			String sql = "SELECT * FROM user WHERE id=" + "'" + id + "'";
			int role = 0;
			ResultSet rs = DBhnnujwc.getInstance().execSQL(sql);
			if (rs.next()) {
				role = rs.getInt("role");
				jObject.put("role", role);
			} else {
				jObject.put("error", 1);
				jObject.put("errorinfo", "用户名不存在！");
				role = 2;
			}
			rs.last();
			switch (role) {// 判断用户角色
			case 0:// 学生
				sql = "SELECT * FROM userbining WHERE id=" + "'" + id + "'";
				rs = DBhnnujwc.getInstance().execSQL(sql.toString());
				if (rs.next()) {

					jObject.put("user", id);
					jObject.put("password", rs.getString("psd"));
					jObject.put("name", rs.getString("name"));
					jObject.put("daytime", rs.getTimestamp("daytime")
							.toString());
					jObject.put("openID", rs.getString("openID"));
					jObject.put("error", 0);

				} else {

					jObject.put("error", 1);
					jObject.put("errorinfo", "用户名不存在！");
				}
				break;

			case 1:// 教师
				sql = "SELECT * FROM userbining_teacher WHERE id=" + "'" + id + "'";
				rs = DBhnnujwc.getInstance().execSQL(sql.toString());
				if (rs.next()) {

					jObject.put("user", id);
					jObject.put("password", rs.getString("psd"));
					jObject.put("name", rs.getString("name"));
					jObject.put("daytime", rs.getTimestamp("daytime")
							.toString());
					jObject.put("openID", rs.getString("openID"));
					jObject.put("mz", rs.getString("mz"));
					jObject.put("sex", rs.getString("sex"));
					jObject.put("xy", rs.getString("xy"));
					jObject.put("xl", rs.getString("xl"));
					jObject.put("xw", rs.getString("xw"));
					jObject.put("error", 0);

				} else {

					jObject.put("error", 1);
					jObject.put("errorinfo", "用户名不存在！");
				}
				break;

			default:
				break;
			}

			return jObject.toString();
		} catch (Exception e) {
			System.out.println("sev error:" + e.toString());
			jObject.put("error", 2);
			jObject.put("errorinfo", e.toString());
			return jObject.toString();
		}

	}

	public String getKebiaoData(String userinfoString) {
		JSONObject jObject = new JSONObject();
              jObject.put("error", -1);
              JSONObject userinfo=new JSONObject();
              System.out.println(userinfoString);
		try {
              userinfo=JSONObject.fromObject(userinfoString);
			int role = (Integer)userinfo.get("role");
			System.out.println("role"+role);
			String user = userinfo.getString("user");
			String sql = "";
			ResultSet rs = null;
				int n=1;
			switch (role) {
			case 0:
				sql = "SELECT * FROM kb" + user;
				rs = DBstukebiao.getInstance().execSQL(sql);
				while (rs.next()) {
					JSONObject jsonTmp=new JSONObject();
					jsonTmp.put("id", rs.getInt("id"));
					jsonTmp.put("cname", rs.getString("cname"));
					jsonTmp.put("week", rs.getString("week"));
					jsonTmp.put("strjie", rs.getInt("strjie"));
					jsonTmp.put("endjie", rs.getInt("endjie"));
					jsonTmp.put("dan", rs.getString("dan"));
					jsonTmp.put("weekno", rs.getString("weekno"));
					jsonTmp.put("teacher", rs.getString("teacher"));
					jsonTmp.put("addr", rs.getString("addr"));
					jsonTmp.put("beizhu", rs.getString("beizhu"));
					
					jObject.put("j"+n, jsonTmp);
					jObject.put("error", 0);
					n++;
				}
				jObject.put("n", n-1);
				break;

			case 1:
				sql = "SELECT * FROM tch" + user;
				rs = DBtchkebiao.getInstance().execSQL(sql);
				while (rs.next()) {
					JSONObject jsonTmp=new JSONObject();
					jsonTmp.put("id", rs.getInt("id"));
					jsonTmp.put("cname", rs.getString("cname"));
					jsonTmp.put("week", rs.getString("week"));
					jsonTmp.put("strjie", rs.getInt("strjie"));
					jsonTmp.put("endjie", rs.getInt("endjie"));
					jsonTmp.put("dan", rs.getString("dan"));
					jsonTmp.put("weekno", rs.getString("weekno"));
					jsonTmp.put("teacher", rs.getString("teacher"));
					jsonTmp.put("addr", rs.getString("addr"));
					jsonTmp.put("detail", rs.getString("detail"));
					jsonTmp.put("beizhu", rs.getString("beizhu"));
					
					jObject.put("j"+n, jsonTmp);
					jObject.put("error", 0);
				}
				break;

			default:
				break;
			}
          if (-1==jObject.getInt("error")) {
			jObject.put("error", 1);
			jObject.put("errorinfo", "您本学期没课！");
		}
			
			return jObject.toString();
		} catch (Exception e) {
			System.out.println("sev error:" + e.toString());
			jObject.put("error", 2);
			jObject.put("errorinfo", e.toString());
			return jObject.toString();
		}
	}
	public String getScoreData(String userinfoString) {
		JSONObject jObject = new JSONObject();
              jObject.put("error", -1);
              JSONObject userinfo=new JSONObject();
              System.out.println(userinfoString);
		try {
              userinfo=JSONObject.fromObject(userinfoString);

			String user = userinfo.getString("user");
			String sql = "";
			ResultSet rs = null;

				sql = "SELECT * FROM cj" + user;
				rs = DBstuscore.getInstance().execSQL(sql);
				int n=1;
				while (rs.next()) {
					JSONObject jsonTmp=new JSONObject();
					jsonTmp.put("ID", rs.getInt("id"));
					jsonTmp.put("xn", rs.getString("xn"));
					jsonTmp.put("xq", rs.getString("xq"));
					jsonTmp.put("kcdm", rs.getString("kcdm"));
					jsonTmp.put("kcxz", rs.getString("kcxz"));
					jsonTmp.put("kcgs", rs.getString("kcgs"));
					jsonTmp.put("xf", rs.getString("xf"));
					jsonTmp.put("jd", rs.getString("jd"));
					jsonTmp.put("fxbj", rs.getString("fxbj"));
					jsonTmp.put("bkcj", rs.getString("bkcj"));
					jsonTmp.put("cxcj", rs.getString("cxcj"));
					jsonTmp.put("kkxy", rs.getString("kkxy"));
					jsonTmp.put("bz", rs.getString("bz"));
					jsonTmp.put("cxbj", rs.getString("cxbj"));
					
					jObject.put("j"+n, jsonTmp);
					jObject.put("error", 0);
					n++;
				}
				jObject.put("n", n-1);

          if (-1==jObject.getInt("error")) {
			jObject.put("error", 1);
			jObject.put("errorinfo", "！");
		}
			
			return jObject.toString();
		} catch (Exception e) {
			System.out.println("sev error:" + e.toString());
			jObject.put("error", 2);
			jObject.put("errorinfo", e.toString());
			return jObject.toString();
		}
	}
}
