package com.lw.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.lw.config.Config;

public class DBstuscore {

	private final static DBstuscore instance = new DBstuscore();

	public static final DBstuscore getInstance() {
		return instance;
	}

	private DBstuscore() {
	}

	private static Connection connection = null;
	private static final String DB_NAME = "stu_score";

	/** 创建数据库连接 */
	private static void creatConnection() {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(Config.DB_URL + DB_NAME,
					Config.DB_USER, Config.DB_PWD);
		} catch (Exception e) {
			System.out.println("连接数据库出错：" + e.toString());
		}
	}

	/** 返回数据库连接 */
	private static Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				creatConnection();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

	public static void closecon() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}

		} catch (SQLException e) {
			System.out.println("关闭数据库连接出错：" + e.toString());
		}
	}

	/** 执行sql语句 */
	public static void execSQL(String sql) {
		try {
			getConnection().createStatement().executeUpdate(sql);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static ResultSet querySQL(String sql) {
		ResultSet rs = null;
		try {
			rs = getConnection().createStatement().executeQuery(sql);
		} catch (Exception e) {
			System.out.println("querySQL" + e.toString());
		}
		return rs;
	}

}
