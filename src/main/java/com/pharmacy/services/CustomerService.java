package com.pharmacy.services;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.pharmacy.POGO.Customer;
import com.pharmacy.DatabaseConnection;
import com.pharmacy.POGO.Sale;


public class CustomerService{

	private Connection dbConnection;
	
	public CustomerService() throws SQLException{
		this.dbConnection= DatabaseConnection
			.getInstance().getConnection();
	}



	public List<Customer> getAllCustomers() throws SQLException{
		List<Customer> customers= new ArrayList<>();
		String query= "SELECT * FROM customer";
		Statement stmt=  this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);
		if(!rs.isBeforeFirst()) {
			return null;
		}
		Customer customer;
		while(rs.next()){
			customer= new Customer();
			customer.setId(rs.getLong("id"));
			customer.setCash(rs.getDouble("cash"));
			customer.setName(rs.getString("name"));
			customer.setAddress(rs.getString("address"));
			customers.add(customer);
			customer=null;
		}
		return customers;
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


	public boolean updateCustomer(Customer customer) throws SQLException {
		String query= "UPDATE customer SET name=?, address=?, cash=? WHERE id=" + customer.getId() + ";";
		PreparedStatement preparedStatement=
			this.dbConnection.prepareStatement(query);
		preparedStatement.setString(1, customer.getName());
		preparedStatement.setString(2, customer.getAddress());
		preparedStatement.setDouble(3, customer.getCash());

		if(preparedStatement.executeUpdate() > 0){
			return true;
		}
		return false;
	}
	
	public boolean deleteCustomer(long id) throws SQLException {
		String query= "DELETE FROM customer WHERE id="+id+";";
		Statement stmt= this.dbConnection.createStatement();
		if(stmt.executeUpdate(query) > 0){
			return true;
		}
		return false;
	}

	public Customer getCustomerByName(String name) throws SQLException {
		Customer customer;
		String query= "SELECT * FROM customer WHERE name=\""+name+"\";";
		Statement stmt= this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);

		if(!rs.isBeforeFirst()) {
			return null;
		}
		
		customer = new Customer();
		while(rs.next()){
			customer.setId(rs.getLong("id"));
			customer.setName(rs.getString("name"));
		}
		
		return customer;
	}


}
