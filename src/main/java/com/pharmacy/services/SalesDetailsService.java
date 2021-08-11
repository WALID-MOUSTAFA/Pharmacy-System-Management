package com.pharmacy.services;

import java.sql.*;
import com.pharmacy.POGO.SaleDetails;
import com.pharmacy.DatabaseConnection;
import com.pharmacy.POGO.SaleDetails;


public class SalesDetailsService{

	private Connection dbConnection;
	
	public SalesDetailsService() throws SQLException{
		this.dbConnection= DatabaseConnection
			.getInstance().getConnection();
	}



	public boolean insertSaleDetail(SaleDetails saleDetail) throws SQLException
	{
		String query= "INSERT INTO sales_details (sales_id, treat_id, quantity, blance_id, price_one, expire, date_in, total) VALUES (?,?,?,?,?,?,?,?)";

		PreparedStatement preparedStatement=
			this.dbConnection.prepareStatement(query);
		preparedStatement.setLong(1,saleDetail.getSaleId());
		preparedStatement.setLong(2,saleDetail.getTreatId());
		preparedStatement.setDouble(3,saleDetail.getQuantity());
		preparedStatement.setLong(4,saleDetail.getBalanceId());
		preparedStatement.setDouble(5,saleDetail.getPriceOne());
		preparedStatement.setString(6,saleDetail.getExpireDate());
		preparedStatement.setString(7,saleDetail.getDateIn());
		preparedStatement.setDouble(8,saleDetail.getTotal());

		if(preparedStatement.executeUpdate() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean DeleteSaleDetail() throws SQLException{

		return false;
	}



}
