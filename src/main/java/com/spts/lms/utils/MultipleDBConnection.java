package com.spts.lms.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class MultipleDBConnection {

	protected final Log logger = LogFactory.getLog(getClass());
	public static Connection createConnection(String url, String username,
			String password, String dbName) {
		Connection connection = null;

		try {

			connection = DriverManager.getConnection(url + dbName, username,
					password);

			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			return connection;

		}

	}

	public static DriverManagerDataSource createConnectionByDS(String url,
			String username, String password, String dbName) {
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl(url + dbName);
		dataSource.setUsername(username);
		dataSource.setPassword(password);

		return dataSource;

	}

	public  DriverManagerDataSource createDefaultConnectionByDS(String url,String username,String password) {
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		logger.info("url--->"+url+"username--->"+username+"password---->"+password);
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);

		return dataSource;

	}
}
