package com.pharmacy.services;

import com.pharmacy.DatabaseConnection;
import com.pharmacy.POGO.BalanceTreat;
import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.POGO.Treatment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
		preparedStatement.setString
			(7, new Timestamp(System.currentTimeMillis()).toString());

		preparedStatement.setDouble(8, balanceTreat.getTotal());
		preparedStatement.setLong(9, balanceTreat.getPurchaseDetailsId());

		if(preparedStatement.executeUpdate() > 0) {
			return true;
		}

		return false;
	}

	

	public List<BalanceTreat> getAllBalanceTreat(long treatId)
		throws SQLException
	{
		TreatmentService treatmentService= new TreatmentService();
		List<BalanceTreat> balances= new ArrayList<>();
		String query= "SELECT  * FROM blance_treat WHERE treat_id="
			+treatId+" AND quantity not null;";
		Statement stmt= this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);
		
		if(!rs.isBeforeFirst()){
			return balances;
		}
		
		BalanceTreat balanceTreat;
		DetailedTreatment treatment;
		while(rs.next()){
			balanceTreat= new BalanceTreat();

			if(rs.getLong("treat_id") != 0) {
				treatment=  treatmentService.getDetailedTreatmentById(rs.getLong("treat_id"));
				balanceTreat.setTreat(treatment);
				treatment=null;
			}
				balanceTreat.setId(rs.getLong("id"));
			balanceTreat.setTreatId(rs.getLong("treat_id"));
			balanceTreat.setPurchaseId(rs.getLong("purchases_id"));
			balanceTreat.setQuantity(rs.getLong("quantity"));
			balanceTreat.setDateIn(rs.getString("date_in"));
			balanceTreat.setPrice(rs.getLong("price"));
			balanceTreat.setExpireDate(rs.getString("expire"));

			balances.add(balanceTreat);
			balanceTreat= null;
		}
		
		return balances;
	}
	

	public List<BalanceTreat> getBalanceTreatbyId(long id) throws SQLException {

		List<BalanceTreat> balances= new ArrayList<>();

		String query= "SELECT  blance_treat.*, treat.name as treatName, typetreat.typename as typeName FROM blance_treat  join treat on blance_treat.treat_id = treat.id left join typetreat on treat.typet= typetreat.id WHERE blance_treat.treat_id="+id+" AND blance_treat.quantity not null;\n";
		Statement stmt= this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);
		
		if(!rs.isBeforeFirst()){
			return null;
		}
		
		BalanceTreat balanceTreat;
		DetailedTreatment treatment;
		while(rs.next()){
			balanceTreat= new BalanceTreat();
			treatment= new DetailedTreatment();
			balanceTreat.setId(rs.getLong("id"));
			balanceTreat.setTreatId(rs.getLong("treat_id"));
			balanceTreat.setPurchaseId(rs.getLong("purchases_id"));
			balanceTreat.setQuantity(rs.getLong("quantity"));
			balanceTreat.setDateIn(rs.getString("date_in"));
			balanceTreat.setPrice(rs.getLong("price"));
			balanceTreat.setExpireDate(rs.getString("expire"));
			treatment.setName(rs.getString("treatName"));
			treatment.setTypeTreatName(rs.getString("typeName"));
			balanceTreat.setTreat(treatment);
			balances.add(balanceTreat);
			balanceTreat= null;
			treatment= null;
		}
		
		return balances;

	}
	

	public List<BalanceTreat> searchBalanceForTreatments(String treatName, String treatType) throws SQLException
	{
		TreatmentService treatmentService= new TreatmentService();
		List<BalanceTreat> balances= new ArrayList<>();

		String query="select blance_treat.*, treat.name as treatName, typetreat.typename as typeName from treat INNER join typetreat on typetreat.id=treat.typet INNER join blance_treat on treat.id=blance_treat.treat_id where blance_treat.quantity != 0 and typetreat.typename=\"" + treatType + "\" and treat.name=\"" + treatName+ "\";";
		System.out.println(query);
		Statement stmt= this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);

		if(!rs.isBeforeFirst()){
			return null;
		}

		BalanceTreat balanceTreat;
		DetailedTreatment treatment;
		while(rs.next()){
			balanceTreat= new BalanceTreat();

			if(rs.getLong("treat_id") != 0) {
				treatment=  treatmentService.getDetailedTreatmentById(rs.getLong("treat_id"));
				balanceTreat.setTreat(treatment);
				treatment=null;
			}
			balanceTreat.setId(rs.getLong("id"));
			balanceTreat.setTreatId(rs.getLong("treat_id"));
			balanceTreat.setPurchaseId(rs.getLong("purchases_id"));
			balanceTreat.setQuantity(rs.getLong("quantity"));
			balanceTreat.setDateIn(rs.getString("date_in"));
			balanceTreat.setPrice(rs.getLong("price"));
			balanceTreat.setExpireDate(rs.getString("expire"));
			balances.add(balanceTreat);
			balanceTreat= null;
		}


		return balances;
	}

	
	public void getBalanceTreatByTreatId() throws SQLException {

	}


	public boolean decreaseQuantity(long id, double quantity)
			throws SQLException {
		String query= "UPDATE blance_treat SET quantity = quantity-" + quantity + ";";
		Statement stmt= this.dbConnection.createStatement();
		if(stmt.executeUpdate(query) > 0 ){
			return true;
		}
		return false;
	}


	public boolean increaseBalance(long id, double quantity) throws SQLException {
		String query= "UPDATE blance_treat SET quantity = quantity+" + quantity + ";";
		Statement stmt= this.dbConnection.createStatement();
		if(stmt.executeUpdate(query)>0){
			return true;
		}
		return false;
	}

	public BalanceTreat getBalanceTreatbyPurchaseDetailsId(long id) throws SQLException {


		String query= "SELECT  blance_treat.*, treat.name as treatName, typetreat.typename as typeName FROM blance_treat  join treat on blance_treat.treat_id = treat.id left join typetreat on treat.typet= typetreat.id WHERE blance_treat.details_pur="+id+" AND blance_treat.quantity not null;\n";
		Statement stmt= this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);

		if(!rs.isBeforeFirst()) return null;

		BalanceTreat balanceTreat= new BalanceTreat();
		DetailedTreatment treatment= new DetailedTreatment();
		while(rs.next()){
			balanceTreat.setId(rs.getLong("id"));
			balanceTreat.setTreatId(rs.getLong("treat_id"));
			balanceTreat.setPurchaseId(rs.getLong("purchases_id"));
			balanceTreat.setQuantity(rs.getLong("quantity"));
			balanceTreat.setDateIn(rs.getString("date_in"));
			balanceTreat.setPrice(rs.getLong("price"));
			balanceTreat.setExpireDate(rs.getString("expire"));
			treatment.setName(rs.getString("treatName"));
			treatment.setTypeTreatName(rs.getString("typeName"));
			balanceTreat.setTreat(treatment);
		}

		return balanceTreat;

	}


}
