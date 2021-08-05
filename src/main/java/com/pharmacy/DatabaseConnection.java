package com.pharmacy;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteOpenMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {

	private static DatabaseConnection instance;
	private Connection connection;
	private String url = "jdbc:sqlite:C:\\Users\\walid\\workspace\\pharmacygui\\target\\classes\\pharmacy.db";
	
	private DatabaseConnection() throws SQLException {
		SQLiteConfig config = new SQLiteConfig();
		config.enforceForeignKeys(true);
		config.resetOpenMode(SQLiteOpenMode.CREATE); // this disable creation
		config.setEncoding(SQLiteConfig.Encoding.UTF8);
		try {
			Class.forName("org.sqlite.JDBC");
			this.connection = DriverManager.getConnection(url, config.toProperties());
		} catch (ClassNotFoundException ex) {
			System.out.println
				("Database Connection Creation Failed : " + ex.getMessage());
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public static DatabaseConnection getInstance() throws SQLException {
		if (instance == null) {
			instance = new DatabaseConnection();
		} else if (instance.getConnection().isClosed()) {
			instance = new DatabaseConnection();
		}

		return instance;
	}
}
