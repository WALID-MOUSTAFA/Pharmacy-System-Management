package com.pharmacy.services;

import com.pharmacy.DatabaseConnection;
import com.pharmacy.POGO.Sale;

import java.sql.*;


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


	public boolean DeleteSale() {return false;}



}
