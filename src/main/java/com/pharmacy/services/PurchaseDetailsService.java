package com.pharmacy.services;

import com.pharmacy.DatabaseConnection;
import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.POGO.Purchase;
import com.pharmacy.POGO.PurchaseDetails;
import com.pharmacy.POGO.Treatment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDetailsService {
	private Connection dbConnection;

	public PurchaseDetailsService() throws SQLException {
		this.dbConnection= DatabaseConnection.getInstance().getConnection();
	}

	public List<PurchaseDetails> getAllRelatedPurchaseDetails(Purchase purchase) throws SQLException{
		List<PurchaseDetails> pDS= new ArrayList<>();
		String query= "SELECT purchases_details.*, treat.id as treat_id, typetreat.typename as typeName, treat.name as treat_name FROM purchases_details INNER JOIN treat ON purchases_details.treat_id=treat.id inner join typetreat on treat.typet=typetreat.id where purchases_id="+ "\"" + purchase.getId() + "\"";;
		Statement stmt= this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);
		PurchaseDetails pD;
		DetailedTreatment treatment;

		while(rs.next()) {
			pD= new PurchaseDetails();
			treatment= new DetailedTreatment();

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
			pD.setDiscount(rs.getString("discount"));
			treatment.setName(rs.getString("treat_name"));
			treatment.setId(rs.getLong("treat_id"));
			treatment.setTypeTreatName(rs.getString("typeName"));
			pD.setTreat(treatment);
			pDS.add(pD);
			pD= null;
			treatment= null;
		}

			
			
		return pDS;
	}

	public long insertPurchaseDetails(PurchaseDetails purchaseDetails)
		throws SQLException
	{
		String query= "INSERT INTO purchases_details (purchases_id, treat_id, expire_date, production_date, date_at, quantity, price_pl, total_people, total_pharmcy, price_p, discount)  VALUES (?,?,?,?,?,?,?,?,?,?,?);";
		
		PreparedStatement preparedStatement= this.dbConnection.
			prepareStatement(query);
		
		preparedStatement.setLong(1, purchaseDetails.getPurchase_id());
		preparedStatement.setLong(2, purchaseDetails.getTreat_id());
		preparedStatement.setString(3, purchaseDetails.getExpireDate());
		preparedStatement.setString(4,purchaseDetails.getProductionDate());
		preparedStatement.setString(5,new Timestamp(System.currentTimeMillis()).toString() );
		preparedStatement.setDouble(6, purchaseDetails.getQuantity());
		preparedStatement.setDouble(7, purchaseDetails.getPricePeople());
		preparedStatement.setDouble(8,purchaseDetails.getTotalPeople() );
		preparedStatement.setDouble(9,purchaseDetails.getTotalPharmacy());
		preparedStatement.setDouble(10,purchaseDetails.getPricePharmacy());
		preparedStatement.setString(11,purchaseDetails.getDiscount());

		if(preparedStatement.executeUpdate() > 0) {
			ResultSet generatedKeys= preparedStatement.getGeneratedKeys();
			if(generatedKeys.next()) {
				return generatedKeys.getLong(1);
			}
		}
		
		return 0;
	}


	public PurchaseDetails getPurchaseDetailsById(long id) throws SQLException{

		PurchaseDetails pd= new PurchaseDetails();
		DetailedTreatment treatment= new DetailedTreatment();
		String query= "SELECT purchases_details.*, treat.id as treat_id, typetreat.typename as typeName, treat.name as treat_name FROM purchases_details INNER JOIN treat ON purchases_details.treat_id=treat.id inner join typetreat on treat.typet=typetreat.id where purchases_details.id="+ "\"" + id + "\";";
		Statement stmt= this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);
		
		if(!rs.isBeforeFirst()) {
			return null;
		}

		while(rs.next()){
			pd.setId(rs.getLong("id"));
			pd.setPurchase_id(rs.getLong("purchases_id"));
			pd.setTotalPeople(rs.getDouble("total_people"));
			pd.setPricePeople(rs.getDouble("price_pl"));
			pd.setTotalPharmacy(rs.getDouble("total_pharmcy"));
			pd.setPricePharmacy(rs.getDouble("price_p"));
			pd.setExpireDate(rs.getString("expire_date"));
			pd.setProductionDate(rs.getString("production_date"));
			pd.setQuantity(rs.getDouble("quantity"));
			pd.setDateAt(rs.getString("date_at"));
			pd.setDiscount(rs.getString("discount"));
			treatment.setName(rs.getString("treat_name"));
			treatment.setId(rs.getLong("treat_id"));
			treatment.setTypeTreatName(rs.getString("typeName"));
			pd.setTreat(treatment);
		}
		
		return pd;
	}


	public boolean deletePurchaseDetailsById(long id) throws SQLException {
		String query= "DELETE FROM purchases_details WHERE id=" + id + ";";
		Statement stmt= this.dbConnection.createStatement();
		int result= stmt.executeUpdate(query);
		if(result > 0) {
			return true;
		}
		return false;
	}

	
	public boolean updatePurchaseDetails(PurchaseDetails purchaseDetails)
		throws SQLException
	{

		String query= "UPDATE purchases_details "
			+"set purchases_id=?,"
			+"treat_id=?,"
			+"expire_date=?,"
			+"production_date=?,"
			+"date_at=?,"
			+"quantity=?,"
			+"price_pl=?,"
			+"total_people=?,"
			+"total_pharmcy=?,"
			+"price_p=?, discount=? WHERE id=" + purchaseDetails.getId()+ ";";

		PreparedStatement preparedStatement=
			this.dbConnection.prepareStatement(query);
				
		preparedStatement.setLong(1, purchaseDetails.getPurchase_id());
		preparedStatement.setLong(2, purchaseDetails.getTreat_id());
		preparedStatement.setString(3, purchaseDetails.getExpireDate());
		preparedStatement.setString(4,purchaseDetails.getProductionDate());
		preparedStatement.setString(5,purchaseDetails.getDateAt());
		preparedStatement.setDouble(6, purchaseDetails.getQuantity());
		preparedStatement.setDouble(7, purchaseDetails.getPricePeople());
		preparedStatement.setDouble(8,purchaseDetails.getTotalPeople() );
		preparedStatement.setDouble(9,purchaseDetails.getTotalPharmacy());
		preparedStatement.setDouble(10,purchaseDetails.getPricePharmacy());
		preparedStatement.setString(11, purchaseDetails.getDiscount());

		if(preparedStatement.executeUpdate() > 0) {
			return true;
		}

		return false;
	}


}
