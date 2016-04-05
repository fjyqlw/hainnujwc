package com.lw.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.Connection;

public class DBhnnujwc {
	private final static DBhnnujwc instance = new DBhnnujwc();
	private static Connection connection = null;

	private DBhnnujwc() {

	}

	public static DBhnnujwc getInstance() {
		return instance;
	}

	/** �������ݿ����� */
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

	/** �������ݿ����� */
	private static void creatConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			connection = (Connection) DriverManager.getConnection(
					"jdbc:mysql://120.24.183.211:3306/hnnu_jwc", "root",
					"HSwxjwptLwCwz2015-6");
			System.out.println("success");
		} catch (Exception e) {
			System.out.println("���ݿ����Ӵ�������" + e.toString());
		}
	}

	/** �ر����ݿ����� */
	public static void closconnection() {
		try {
			if (connection == null || !connection.isClosed()) {
				connection.close();
			}
		} catch (Exception e) {
			System.out.println("���ݿ����ӹرճ���" + e.toString());
		}
	}

	/** ִ�в�ѯ��� */
	public static ResultSet execSQL(String sql) {
		ResultSet rs = null;
		Statement stm = null;
		try {
			stm = getConnection().createStatement();
			rs = stm.executeQuery(sql);
			return rs;
		} catch (Exception e) {
			System.out.println("execSQL(String sql)����==>" + e.toString());
			return null;
		}

	}
}
