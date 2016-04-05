package com.lw.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.lw.jiami.CryptAES;

public class DB {

	/** 数据库地址（不含数据库名称）/数据库用户名/数据库密码/数据库名称 */
	public static String sql_url, sql_user = "root", sql_pwd = "HSwxjwptLwCwz2015-6",
			sql_db;

	public String getSql_db() {
		return sql_db;
	}

	public void setSql_db(String sql_db) {
		this.sql_db = sql_db;
	}

	public String getSql_url() {
		return sql_url;
	}

	public void setSql_url(String sql_url) {
		this.sql_url = sql_url;
	}

	public String getSql_user() {
		return sql_user;
	}

	public void setSql_user(String sql_user) {
		this.sql_user = sql_user;
	}

	public String getSql_pwd() {
		return sql_pwd;
	}

	public void setSql_pwd(String sql_pwd) {
		this.sql_pwd = sql_pwd;
	}

	public DB() {
		setSql_url("jdbc:mysql://120.24.183.211:3306/");
		setSql_db("stu_score");
		setSql_user(DB.sql_user);
		setSql_pwd(DB.sql_pwd);
	}

	public Connection connection = null;

	/** 返回数据库连接 */
	public Connection getConnection() {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(
					getSql_url() + getSql_db(), getSql_user(), getSql_pwd());
		} catch (Exception e) {
			System.out.println("连接数据库出错：" + e.toString());
		}
		return connection;
	}

	public void closecon() {
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("关闭数据库连接出错：" + e.toString());
		}
	}

	public ResultSet ExexQuery(String sql) {
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = getConnection().createStatement();
			rs = stmt.executeQuery(sql);
		} catch (Exception e) {
			System.out.println("【执行SQL语句出错】" + e.toString());
		}

		return rs;
	}

	/**
	 * 删除成绩表*
	 * 
	 * @param xh
	 *            学号
	 */
	public void dropTB(String xh) {

		Statement stmt = null;
		try {

			stmt = getConnection().createStatement();
			stmt.executeUpdate("drop table if exists cj" + xh);
			closecon();

		} catch (Exception e) {
			System.out.println("error=dropTB===" + e.toString());
		}

	}

	/**
	 * 删除课表*
	 * 
	 * @param xh
	 *            学号
	 */
	public void dropKBTB(String xh) {

		Statement stmt = null;
		try {
			setSql_db("stu_kebiao");
			stmt = getConnection().createStatement();
			stmt.executeUpdate("drop table if exists kb" + xh);

			closecon();

		} catch (Exception e) {
			System.out.println("error=dropKBTB===" + e.toString());
		}

	}

	public Map createTb(String xh) {
		Map db_map = new HashMap();
		System.out.println("建成绩表");
		db_map.put("userStatus", false);
		CryptAES cAes = new CryptAES();

		Statement stmt = null;
		PreparedStatement pstmt = null;
		try {
			String sql = "", sql2 = "";

			stmt = getConnection().createStatement();
			stmt.executeUpdate("drop table if exists cj" + xh);
			sql = "create table if not exists cj"
					+ xh
					+ "(ID bigint(5)NOT NULL AUTO_INCREMENT primary key,xn varchar(9) NOT NULL COMMENT '学年',xq varchar(5) NOT NULL COMMENT '学期',kcdm varchar(15) NOT NULL COMMENT '课程代码',kcmc varchar(60) NOT NULL COMMENT '课程名称',kcxz varchar(15) NOT NULL COMMENT '课程性质',kcgs varchar(15) COMMENT '课程归属',xf varchar(5) NOT NULL COMMENT '学分',jd varchar(5) NOT NULL COMMENT '绩点',cj varchar(5) NOT NULL COMMENT '成绩',fxbj varchar(8) NOT NULL COMMENT '辅修标记',bkcj varchar(5) COMMENT '补考成绩',cxcj varchar(5) COMMENT '重修成绩',kkxy varchar(80) NOT NULL COMMENT '开课学院',bz varchar(50) COMMENT '备注',cxbj varchar(8) COMMENT '重修标记', UNIQUE (kcdm,cj,cxbj));";//
			sql2 = "alter table cj" + xh + " add UNIQUE (kcdm,cj,cxbj)";
			// System.out.println(sql);
			stmt.executeUpdate(sql);
			stmt.executeUpdate(sql2);

			closecon();
			db_map.put("userStatus", true);
			return db_map;
		} catch (Exception e) {
			db_map.put("userStatus", false);
			System.out.println("error=createTb===" + e.toString());
		} finally {
			cAes = null;
			db_map = null;
		}

		return db_map;
	}

	/**
	 * @param zcj_map
	 *            需要包含学号
	 */
	public void insertUser(String openID, String name, String Id, String psd,
			Map userinfo_map) {

		CryptAES cAes = new CryptAES();

		String psdString = cAes.AES_Encrypt("LW#CWZ@HS_jwc&@@", psd);

		try {
			String sql = "";
			setSql_db("hnnu_jwc");
			PreparedStatement ps = null;
			if (Id.length() > 10) {
				sql = "insert userbining(openID,name,id,psd)  values(?,?,?,?)";//

				ps = getConnection().prepareStatement(sql);
				ps.setString(1, openID);
				ps.setString(2, name);
				ps.setString(3, Id);
				ps.setString(4, psdString);
			} else {
				sql = "insert userbining_teacher(openID,name,id,psd,mz,sex,xy,xl,xw)  values(?,?,?,?,?,?,?,?,?)";//

				ps = getConnection().prepareStatement(sql);
				ps.setString(1, openID);
				ps.setString(2, (String) userinfo_map.get("0"));
				ps.setString(3, Id);
				ps.setString(4, psdString);
				ps.setString(5, (String) userinfo_map.get("2"));
				ps.setString(6, (String) userinfo_map.get("1"));
				ps.setString(7, (String) userinfo_map.get("3"));
				ps.setString(8, (String) userinfo_map.get("4"));
				if ("null".equals((String) userinfo_map.get("5"))
						|| null == (String) userinfo_map.get("5")) {
					ps.setString(9, "");
				} else {
					ps.setString(9, (String) userinfo_map.get("5"));
				}

			}
			ps.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("Java_DB_insertUser_ERR_2:" + e.toString());
		} finally {
			cAes = null;
			closecon();
		}

	}

	// =========================
	/**
	 * 教师用
	 * 
	 * @param zcj_map
	 *            需要包含工号
	 */
	public void updateUserInfo(Map zcj_map) {

		Map db_map = zcj_map;// new HashMap();

		db_map.put("userStatus", false);
		CryptAES cAes = new CryptAES();
		setSql_db("hnnu_jwc");
		String xh = (String) db_map.get("xh");
		// Statement stmt = null;
		Statement pStmt = null;
		try {
			String sql = "";
          if ("null".equals((String) zcj_map.get("5"))
					|| null == (String) zcj_map.get("5")) {
        	  zcj_map.put("5", "");
			}
			sql = "update userbining_teacher set name='"
					+ (String) zcj_map.get("0") + "',sex='"
					+ (String) zcj_map.get("1") + "',mz='"
					+ (String) zcj_map.get("2") + "',xy='"
					+ (String) zcj_map.get("3") + "',xl='"
					+ (String) zcj_map.get("4") + "',xw='"
					+ (String) zcj_map.get("5") + "' where id='" + xh + "'";
			
			pStmt = getConnection().createStatement();
			pStmt.executeUpdate(sql);

			// Thread.sleep(50);

		} catch (Exception e) {
			db_map.put("userStatus", false);
			
			System.out.println("error=updateUserInfo===" + e.toString());
		}finally{
			closecon();
		}

		// return db_map;

	}

	// ========================
	/**
	 * 获取历年成绩
	 * 
	 * @param openID
	 *            openID
	 */
	public Map getUserInfo(String openID, int Type) {
		Map zcj_map = new HashMap();
		Statement stmt = null;
		ResultSet rs = null;
		String xh = "";
		try {
			setSql_db("hnnu_jwc");
			stmt = getConnection().createStatement();
			rs = stmt
					.executeQuery("select * from userbining_teacher where openID='"
							+ openID + "'");
			boolean isbining = false;
			while (rs.next()) {
				isbining = true;
				xh = rs.getString("id");
				zcj_map.put("1", rs.getString("id"));
				zcj_map.put("2", rs.getString("name"));
				zcj_map.put("3", rs.getString("sex"));
				zcj_map.put("4", rs.getString("mz"));
				zcj_map.put("5", rs.getString("xy"));
				zcj_map.put("6", rs.getString("xl"));
				zcj_map.put("7", rs.getString("xw"));
			}
			if (!isbining) {
				// 用户未绑定，直接退出
				zcj_map.put("cjStatus", -2);
				return zcj_map;
			}

			zcj_map.put("cjStatus", 1);
			return zcj_map;
		} catch (Exception e) {
			zcj_map.put("cjStatus", 0);
			System.out.println("db.getUserInfo**错误=" + e.toString());
		} finally {
			closecon();
		}

		return zcj_map;
	}

	// ===================================

	/**
	 * @param zcj_map
	 *            需要包含学号
	 */
	public void importCj(Map zcj_map) {

		Map db_map = zcj_map;// new HashMap();

		db_map.put("userStatus", false);
		CryptAES cAes = new CryptAES();

		String xh = (String) db_map.get("Id");
		PreparedStatement pStmt = null;
		try {
			String sql = "";

			sql = "insert ignore into cj"
					+ xh
					+ "(xn,xq,kcdm,kcmc,kcxz,kcgs,xf,jd,cj,fxbj,bkcj,cxcj,kkxy,bz,cxbj) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";//
			setSql_db("stu_score");
			pStmt = getConnection().prepareStatement(sql);
			int count = 1;
			while (true) {
				Map zcj_info_map = new HashMap();// 课程具体成绩
				String key = "zcj_" + count;
				if (db_map.get(key) == null) {
					break;
				}
				zcj_info_map = (Map) db_map.get(key);
				pStmt.setString(1, (String) zcj_info_map.get("0"));
				pStmt.setString(2, (String) zcj_info_map.get("1"));
				pStmt.setString(3, (String) zcj_info_map.get("2"));
				pStmt.setString(4, (String) zcj_info_map.get("3"));
				pStmt.setString(5, (String) zcj_info_map.get("4"));
				pStmt.setString(6, (String) zcj_info_map.get("5"));
				pStmt.setString(7, (String) zcj_info_map.get("6"));
				pStmt.setString(8, (String) zcj_info_map.get("7"));
				pStmt.setString(9, (String) zcj_info_map.get("8"));
				pStmt.setString(10, (String) zcj_info_map.get("9"));
				pStmt.setString(11, (String) zcj_info_map.get("10"));
				pStmt.setString(12, (String) zcj_info_map.get("11"));
				pStmt.setString(13, (String) zcj_info_map.get("12"));
				pStmt.setString(14, (String) zcj_info_map.get("13"));
				pStmt.setString(15, (String) zcj_info_map.get("14"));
				int r = pStmt.executeUpdate();
				count = count + 1;
				zcj_info_map.clear();
				zcj_info_map = null;
			}

		} catch (Exception e) {
			db_map.put("userStatus", false);
			System.out.println("error=importCj===" + e.toString());
		} finally {
			cAes = null;
			closecon();
		}

		// return db_map;

	}

	/**
	 * @param zcj_map
	 *            需要包含学号
	 */
	public void updateCj(Map zcj_map) {

		Map db_map = zcj_map;// new HashMap();

		db_map.put("userStatus", false);
		CryptAES cAes = new CryptAES();

		String xh = (String) db_map.get("xh");
		// Statement stmt = null;
		PreparedStatement pStmt = null;
		try {
			String sql = "";

			sql = "insert into cj"
					+ xh
					+ "(xn,xq,kcdm,kcmc,kcxz,kcgs,xf,jd,cj,fxbj,bkcj,cxcj,kkxy,bz,cxbj) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";//

			pStmt = getConnection().prepareStatement(sql);
			int count = 1;
			while (true) {
				Map zcj_info_map = new HashMap();// 课程具体成绩
				String key = "zcj_" + count;
				if (db_map.get(key) == null) {
					break;
				}
				zcj_info_map = (Map) db_map.get(key);
				pStmt.setString(1, (String) zcj_info_map.get("0"));
				pStmt.setString(2, (String) zcj_info_map.get("1"));
				pStmt.setString(3, (String) zcj_info_map.get("2"));
				pStmt.setString(4, (String) zcj_info_map.get("3"));
				pStmt.setString(5, (String) zcj_info_map.get("4"));
				pStmt.setString(6, (String) zcj_info_map.get("5"));
				pStmt.setString(7, (String) zcj_info_map.get("6"));
				pStmt.setString(8, (String) zcj_info_map.get("7"));
				pStmt.setString(9, (String) zcj_info_map.get("8"));
				pStmt.setString(10, (String) zcj_info_map.get("9"));
				pStmt.setString(11, (String) zcj_info_map.get("10"));
				pStmt.setString(12, (String) zcj_info_map.get("11"));
				pStmt.setString(13, (String) zcj_info_map.get("12"));
				pStmt.setString(14, (String) zcj_info_map.get("13"));
				pStmt.setString(15, (String) zcj_info_map.get("14"));
				int r = pStmt.executeUpdate();
				// Thread.sleep(50);
				count = count + 1;
			}
			closecon();

		} catch (Exception e) {
			db_map.put("userStatus", false);
			System.out.println("error=updateCj===" + e.toString());
		}

		// return db_map;

	}

	// ========================
	/**
	 * 获取历年成绩
	 * 
	 * @param openID
	 *            openID
	 */
	public Map getLNCJ(String openID) {
		Map zcj_map = new HashMap();
		Statement stmt = null;
		ResultSet rs = null;
		String xh = "";
		try {
			setSql_db("hnnu_jwc");
			stmt = getConnection().createStatement();
			rs = stmt.executeQuery("select id from userbining where openID='"
					+ openID + "'");
			if (rs.next()) {
				xh = rs.getString("id");
			} else { // 用户未绑定，直接退出
				zcj_map.put("cjStatus", -2);
				return zcj_map;
			}
			stmt = null;
			rs = null;

			setSql_db("stu_score");
			stmt = getConnection().createStatement();
			rs = stmt.executeQuery("select * from cj" + xh);
			int count = 1;
			while (rs.next()) {
				Map zcj_infoMap = new HashMap();
				zcj_infoMap.put("2", rs.getString(2));
				zcj_infoMap.put("3", rs.getString(3));
				zcj_infoMap.put("5", rs.getString(5));
				zcj_infoMap.put("6", rs.getString(6));
				zcj_infoMap.put("8", rs.getString(8));
				zcj_infoMap.put("9", rs.getString(9));
				zcj_infoMap.put("10", rs.getString(10));

				zcj_map.put("zcj_" + count, zcj_infoMap);
				count++;
				zcj_infoMap = null;
			}

			zcj_map.put("cjStatus", 1);
			if (1==count) {
				zcj_map.put("cjStatus", -3);
			}
			return zcj_map;
		} catch (Exception e) {
			zcj_map.put("cjStatus", 0);
			System.out.println("db.getLNCJ**错误=" + e.toString());
		} finally {
			closecon();
		}

		return zcj_map;
	}

	// ========================
	/**
	 * 获取最高成绩
	 * 
	 * @param openID
	 *            openID
	 */
	public Map getZGCJ(String openID) {
		Map zcj_map = new HashMap();
		Statement stmt = null;
		ResultSet rs = null;
		String xh = "";
		try {
			setSql_db("hnnu_jwc");
			stmt = getConnection().createStatement();
			rs = stmt.executeQuery("select id from userbining where openID='"
					+ openID + "'");
			if (rs.next()) {
				xh = rs.getString("id");
			} else { // 用户未绑定，直接退出
				zcj_map.put("cjStatus", -2);
				return zcj_map;
			}
			stmt = null;
			rs = null;

			setSql_db("stu_score");
			stmt = getConnection().createStatement();// select * from
														// cj201224010219
			// where cj not in (select
			// Max(cj) from cj201224010219
			// group by kcdm)
			rs = stmt.executeQuery("select * from cj" + xh
					+ " where cj in (select Max(cj) from cj" + xh
					+ " group by kcdm)");
			int count = 1;
			while (rs.next()) {
				Map zcj_infoMap = new HashMap();
				zcj_infoMap.put("2", rs.getString(2));
				zcj_infoMap.put("3", rs.getString(3));
				zcj_infoMap.put("5", rs.getString(5));
				zcj_infoMap.put("6", rs.getString(6));
				zcj_infoMap.put("8", rs.getString(8));
				zcj_infoMap.put("9", rs.getString(9));
				zcj_infoMap.put("10", rs.getString(10));

				zcj_map.put("zcj_" + count, zcj_infoMap);
				count++;
			}
			zcj_map.put("cjStatus", 1);
			return zcj_map;
		} catch (Exception e) {
			zcj_map.put("cjStatus", 0);
			System.out.println("db.getLNCJ**错误=" + e.toString());
		} finally {

			closecon();
		}

		return zcj_map;
	}

	// ========================
	/**
	 * 获取未通过成绩
	 * 
	 * @param openID
	 *            openID
	 */
	public Map getWTGCJ(String openID) {
		Map zcj_map = new HashMap();
		Statement stmt = null;
		ResultSet rs = null;
		String xh = "";
		try {
			setSql_db("hnnu_jwc");
			stmt = getConnection().createStatement();
			rs = stmt.executeQuery("select id from userbining where openID='"
					+ openID + "'");
			if (rs.next()) {
				xh = rs.getString("id");
			} else { // 用户未绑定，直接退出
				zcj_map.put("cjStatus", -2);
				return zcj_map;
			}
			stmt = null;
			rs = null;

			setSql_db("stu_score");
			stmt = getConnection().createStatement();
			rs = stmt
					.executeQuery("select * FROM (SELECT * from cj"
							+ xh
							+ " WHERE cj<60 ) as wtgtab  where cj in (select Max(cj) from cj"
							+ xh + " group by kcdm)");
			int count = 1;
			while (rs.next()) {
				
				try {
					Double bkcj=Double.parseDouble(rs.getString("bkcj"));
					System.out.println(bkcj);
					;
					continue;
				} catch (Exception e) {
					//System.out.println(e.toString());
				}
				
				Map zcj_infoMap = new HashMap();
				zcj_infoMap.put("2", rs.getString(2));
				zcj_infoMap.put("3", rs.getString(3));
				zcj_infoMap.put("5", rs.getString(5));
				zcj_infoMap.put("6", rs.getString(6));
				zcj_infoMap.put("8", rs.getString(8));
				zcj_infoMap.put("9", rs.getString(9));
				zcj_infoMap.put("10", rs.getString(10));

				zcj_map.put("zcj_" + count, zcj_infoMap);
				count++;
			}
			zcj_map.put("cjStatus", 1);
			
			return zcj_map;
		} catch (Exception e) {
			zcj_map.put("cjStatus", 0);
			System.out.println("db.getLNCJ**错误=" + e.toString());
		} finally {
			closecon();
		}

		return zcj_map;
	}

	// ====================================
	/**
	 * 按条件查询成绩
	 * 
	 * @param openID
	 *            openID
	 * @param xn
	 *            学年
	 * @param xq
	 *            学期
	 * @param kcxz
	 *            课程性质
	 */
	public Map getQueryCJ(String openID, String xn, String xq, String kcxz) {
		Map zcj_map = new HashMap();
		Statement stmt = null;
		ResultSet rs = null;
		String xh = "";
		try {

			setSql_db("hnnu_jwc");
			stmt = getConnection().createStatement();
			rs = stmt.executeQuery("select id from userbining where openID='"
					+ openID + "'");
			if (rs.next()) {
				xh = rs.getString("id");
			} else { // 用户未绑定，直接退出
				zcj_map.put("cjStatus", -2);
				return zcj_map;
			}
			stmt = null;
			rs = null;

			setSql_db("stu_score");
			stmt = getConnection().createStatement();
			String sql = "", xn2 = "", xq2 = "", kcxz2 = "";

			if ("all".equals(kcxz)) {
				rs = stmt.executeQuery("select * from cj" + xh
						+ " where xn like '" + xn + "' and xq like '" + xq
						+ "'");
			} else {
				rs = stmt.executeQuery("select * from cj" + xh
						+ " where xn like '" + xn + "' and xq like '" + xq
						+ "' and kcxz like '" + kcxz + "'");
			}
			int count = 1;
			while (rs.next()) {
				Map zcj_infoMap = new HashMap();
				zcj_infoMap.put("2", rs.getString(2));
				zcj_infoMap.put("3", rs.getString(3));
				zcj_infoMap.put("5", rs.getString(5));
				zcj_infoMap.put("6", rs.getString(6));
				zcj_infoMap.put("8", rs.getString(8));
				zcj_infoMap.put("9", rs.getString(9));
				zcj_infoMap.put("10", rs.getString(10));

				zcj_map.put("zcj_" + count, zcj_infoMap);
				count++;
			}
			zcj_map.put("cjStatus", 1);
			if (1==count) {
				zcj_map.put("cjStatus", -3);
			}
			return zcj_map;
		} catch (Exception e) {
			zcj_map.put("cjStatus", 0);
			System.out.println("db.getLNCJ**错误=" + e.toString());
		} finally {

			closecon();
		}

		return zcj_map;

	}

}
