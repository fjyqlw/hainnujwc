package com.lw.test;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BasicDataSourceExample {
	public static void main(String[] args) {
		DataSource dataSource = setupDataSource();// args[0]存放访问数据库的url

		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		try {
			conn = dataSource.getConnection();

			stmt = conn.createStatement();

			rset = stmt.executeQuery("select * from menuctr");// args[1]中存放查询数据库的语句

			int numcols = rset.getMetaData().getColumnCount();
			while (rset.next()) {
				for (int i = 1; i <= numcols; i++) {
					System.out.print("\t" + rset.getString(i));
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
			} catch (Exception e) {
			}
			try {
				stmt.close();
			} catch (Exception e) {
			}
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}

	public static DataSource setupDataSource() {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");// 此处设置数据库驱动
		ds.setUsername("root");// 访问数据库用户名
		ds.setPassword("HSwxjwptLwCwz2015-6");// 访问数据库密码
		ds.setUrl("jdbc:mysql://120.24.183.211:3306/hnnu_jwc");// 访问数据库的url
		return ds;
	}

	public static void printDataSourceStats(DataSource ds) throws SQLException {
		BasicDataSource bds = (BasicDataSource) ds;
		System.out.println("NumActive: " + bds.getNumActive());
		System.out.println("NumIdle: " + bds.getNumIdle());
	}

	public static void shutdownDataSource(DataSource ds) throws SQLException {
		BasicDataSource bds = (BasicDataSource) ds;
		bds.close();
	}
}