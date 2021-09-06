package com.pharmacy.services;

import com.pharmacy.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
	private Connection dbConnection;

	public UserService() throws SQLException {
		this.dbConnection= DatabaseConnection.getInstance().getConnection();
	}
	
	public boolean logIn(String username, String password) throws SQLException {
		String query= "SELECT * FROM user WHERE name=? AND password=?";
		PreparedStatement preparedStatement= this.dbConnection.prepareStatement(query);
		preparedStatement.setString(1, username);
		preparedStatement.setString(2, password);
		ResultSet rs= preparedStatement.executeQuery();
		if(rs.next()) {
			return true;
		}
		return false;
	}
}
