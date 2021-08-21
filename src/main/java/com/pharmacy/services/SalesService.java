package com.pharmacy.services;

import com.pharmacy.DatabaseConnection;
import com.pharmacy.POGO.Customer;
import com.pharmacy.POGO.Sale;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SalesService{

	private Connection dbConnection;
	
	public SalesService() throws SQLException{
		this.dbConnection= DatabaseConnection
			.getInstance().getConnection();
	}


	public long insertSale( Sale sale) throws SQLException{
		String query= "INSERT INTO sales (customer_id, total, discount, net_total, date_in, name) VALUES (?,?,?,?,?,?)";
		
		PreparedStatement preparedStatement=
			this.dbConnection.prepareStatement(query);

		preparedStatement.setLong(1, sale.getCustomerId());
		preparedStatement.setDouble(2, sale.getTotal());
		preparedStatement.setDouble(3, sale.getDiscount());
		preparedStatement.setDouble(4, sale.getNetTotal());
		preparedStatement.setString(5, sale.getDateIn());
		preparedStatement.setString(6, sale.getName());


		if(preparedStatement.executeUpdate() > 0) {
			ResultSet rs= preparedStatement.getGeneratedKeys();
			while(rs.next()){
				return rs.getLong(1);
			}
		}

		return 0;
	}


	public boolean deleteSale(long id) throws SQLException {
		String query= "DELETE FROM sales WHERE id=" + id +";";
		Statement stmt= this.dbConnection.createStatement();
		if(stmt.executeUpdate(query) > 0){
			return true;
		}
		return false;
	}


	public List<Sale> getAllSales() throws SQLException{
		List<Sale> sales= new ArrayList<>();
		String query= "SELECT sales.*, customer.name as customerName from sales join customer on sales.customer_id=customer.id;";
		Statement stmt= this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);
		if(!rs.isBeforeFirst()) {
			return null;
		}
		Sale sale;
		Customer customer;
		while(rs.next()) {
			sale= new Sale();
			customer= new Customer();
			sale.setId(rs.getLong("id"));
			sale.setName(rs.getString("name"));
			sale.setDiscount(rs.getDouble("discount"));
			sale.setTotal(rs.getDouble("total"));
			sale.setDateIn(rs.getString("date_in"));
			sale.setCustomerId(rs.getLong("customer_id"));
			sale.setNetTotal(rs.getDouble("net_total"));
			customer.setId(rs.getLong("customer_id"));
			customer.setName(rs.getString("customerName"));
			sale.setCustomer(customer);
			sales.add(sale);
			customer= null;
			sale=null;
		}

		return sales;
	}


	

	
}
