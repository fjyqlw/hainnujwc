package com.lw.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.lw.config.Config;

public class DBhnnujwc {

	private final static DBhnnujwc instance = new DBhnnujwc();

	public static final DBhnnujwc getInstance() {
		return instance;
	}

	private DBhnnujwc() {
	}

	private static Connection connection = null;
	private static final String DB_NAME = "hnnu_jwc";

	/** �������ݿ����� */
	private static void creatConnection() {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(Config.DB_URL + DB_NAME,
					Config.DB_USER, Config.DB_PWD);
		} catch (Exception e) {
			System.out.println("�������ݿ����" + e.toString());
		}
	}

	/** �������ݿ����� */
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
			System.out.println("�ر����ݿ����ӳ���" + e.toString());
		}
	}

	/** ִ��sql��� */
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
