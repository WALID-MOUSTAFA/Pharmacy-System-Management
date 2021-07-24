package com.pharmacy.services;

import com.pharmacy.DatabaseConnection;
import com.pharmacy.POGO.Purchase;
import com.pharmacy.POGO.PurchaseDetails;
import com.pharmacy.POGO.Treatment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDetailsService {
	private Connection dbConnection;

	public PurchaseDetailsService() throws SQLException {
		this.dbConnection= DatabaseConnection.getInstance().getConnection();
	}

	public List<PurchaseDetails> getAllRelatedPurchaseDetails(Purchase purchase) throws SQLException{
		List<PurchaseDetails> pDS= new ArrayList<>();
		String query= "SELECT purchases_details.*, treat.id as treat_id, treat.name as treat_name FROM purchases_details INNER JOIN treat ON purchases_details.treat_id=treat.id where purchases_id="+ "\"" + purchase.getId() + "\"";;
		Statement stmt= this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);
		PurchaseDetails pD;
		Treatment treatment;

		while(rs.next()) {
			pD= new PurchaseDetails();
			treatment= new Treatment();

			pD.setDateAt(rs.getString("date_at"));
			pD.setId(rs.getLong("id"));
			pD.setPurchase_id(rs.getLong("purchases_id"));
			pD.setTotalPeople(rs.getDouble("total_people"));
			pD.setPricePeople(rs.getDouble("price_pl"));
			pD.setTotalPharmacy(rs.getDouble("total_pharmcy"));
			pD.setPricePharmacy(rs.getDouble("price_p"));
			pD.setExpireDate(rs.getString("expire_date"));
			pD.setProductionDate(rs.getString("production_date"));
			pD.setQuantity(rs.getDouble("quantity"));

			treatment.setName(rs.getString("treat_name"));
			treatment.setId(rs.getLong("treat_id"));

			pD.setTreat(treatment);
			pDS.add(pD);
			pD= null;
			treatment= null;
		}

			
			
		return pDS;
	}

	public boolean isnertPurchaseDetails(PurchaseDetails purchaseDetails)
		throws SQLException
	{
		String query= "INSERT INTO purchases_details (purchaes_id, treat_id, expire_date, production_date, date_at, quantity, price_pl, total_pl, total_pharmcy, price_p)  VALUES (?,?,?,?,?,?,?,?,?,?);";
		
		PreparedStatement preparedStatement= this.dbConnection.
			prepareStatement(query);
		
		preparedStatement.setLong(1, purchaseDetails.getPurchase_id());
		preparedStatement.setLong(2, purchaseDetails.getTreat_id);
		preparedStatement.setString(3, purchaseDetails.getExpireDate());
		preparedStatement.setString(4,purchaseDetails.getProductionDate());
		preparedStatement.setString(5,new Timestamp(System.currentTimeMillis().toString()) );
		preparedStatement.setString(6, purchaseDetails.getQuantity());
		preparedStatement.setString(7, purchaseDetails.getPricePeople());
		preparedStatement.setString(8,purchaseDetails.getTotalPeople() );
		preparedStatement.setString(9,purchaseDetails.getTotalPharmacy());
		preparedStatement.setString(10,purchaseDetails.getPricePharmacy());

		if(preparedStatement.executeUpdate() > 0) {
			return true;
		}
		
		return false;
	}
}
