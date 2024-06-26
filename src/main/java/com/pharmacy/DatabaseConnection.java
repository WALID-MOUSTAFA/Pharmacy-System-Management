package com.pharmacy;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteOpenMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
	
	private static DatabaseConnection instance;
	private Connection connection;
	
	private String url;
	private boolean production= false;

	private DatabaseConnection() throws SQLException {
		if(System.getProperty("os.name").startsWith(("Windows"))) {
			if (!production) {
				url = "jdbc:sqlite:C:\\Users\\walid\\Documents\\pharmacygui\\target\\classes\\pharmacy.db";
			} else {
				url = "jdbc:sqlite:.\\database.db";
			}
		} else {
			url = "jdbc:sqlite://home/walid/pharmacy.db";
		}
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
