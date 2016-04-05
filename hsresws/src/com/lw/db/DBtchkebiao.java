package com.lw.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.Connection;

public class DBtchkebiao {
	private final static DBtchkebiao instance = new DBtchkebiao();
	private static Connection connection = null;

	private DBtchkebiao() {

	}

	public static DBtchkebiao getInstance() {
		return instance;
	}

	/** 返回数据库连接 */
	public static Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				creatConnection();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("back success!");
		return connection;
	}

	/** 创建数据库连接 */
	private static void creatConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			connection = (Connection) DriverManager.getConnection(
					"jdbc:mysql://120.24.183.211:3306/tch_kebiao", "root",
					"HSwxjwptLwCwz2015-6");
			System.out.println("success");
		} catch (Exception e) {
			System.out.println("数据库连接创建出错" + e.toString());
		}
	}

	/** 关闭数据库连接 */
	public static void closconnection() {
		try {
			if (connection == null || !connection.isClosed()) {
				connection.close();
			}
		} catch (Exception e) {
			System.out.println("数据库连接关闭出错" + e.toString());
		}
	}

	/** 执行查询语句 */
	public static ResultSet execSQL(String sql) {
		ResultSet rs = null;
		Statement stm = null;
		try {
			stm = getConnection().createStatement();
			rs = stm.executeQuery(sql);
			return rs;
		} catch (Exception e) {
			System.out.println("execSQL(String sql)出错==>" + e.toString());
			return null;
		}

	}
}
