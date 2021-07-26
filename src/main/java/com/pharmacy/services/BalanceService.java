package com.pharmacy.services;

import com.pharmacy.DatabaseConnection;
import com.pharmacy.POGO.BalanceTreat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BalanceService {

	private Connection dbConnection;
	
	public BalanceService() throws SQLException{
		this.dbConnection= DatabaseConnection
			.getInstance().getConnection();
	}
	
	public boolean insertBalanceTreat(BalanceTreat balanceTreat)
		throws SQLException
	{
		String query= "INSERT INTO blance_treat (treat_id, expire, purchases_id, quantity, type, price, date_in, total, details_pur) VALUES (?,?,?,?,?,?,?,?,? )";
		PreparedStatement preparedStatement= this.dbConnection
			.prepareStatement(query);

		preparedStatement.setLong(1, balanceTreat.getTreatId());
		preparedStatement.setString(2, balanceTreat.getExpireDate());
		preparedStatement.setLong(3, balanceTreat.getPurchaseId());
		preparedStatement.setDouble(4, balanceTreat.getQuantity());
		//TODO(walid): fix the zero;
		preparedStatement.setLong(5, 0);
		preparedStatement.setDouble(6, balanceTreat.getPrice());
		preparedStatement.setString(7, new Timestamp(System.currentTimeMillis()).toString());

		preparedStatement.setDouble(8, balanceTreat.getTotal());
		preparedStatement.setLong(9, balanceTreat.getPurchaseDetailsId());

		if(preparedStatement.executeUpdate() > 0) {
			return true;
		}

		return false;
	}



	public void getAllBalanceTreat() throws SQLException{}


}
