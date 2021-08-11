package com.pharmacy.services;


import java.sql.*;
import com.pharmacy.POGO.Customer;
import com.pharmacy.DatabaseConnection;


public class CustomerService{

	private Connection dbConnection;
	
	public CustomerService() throws SQLException{
		this.dbConnection= DatabaseConnection
			.getInstance().getConnection();
	}



	public boolean insertCustomer(Customer customer) throws SQLException {
		String query= "INSERT INTO customer (name, address, cash, date_at) VALUES (?,?,?,?)";
		
		PreparedStatement preparedStatement=
			this.dbConnection.prepareStatement(query);

		preparedStatement.setString(1, customer.getName());
		preparedStatement.setString(2, customer.getAddress());
		preparedStatement.setDouble(3, customer.getCash());
		preparedStatement.setString(4, customer.getDateAt());


		if(preparedStatement.executeUpdate() > 0) {
			return true;
		} else {
			return false;
		}

	}
	
	public boolean deleteCustomer() {

		return true;
	}



}
