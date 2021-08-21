package com.pharmacy.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.pharmacy.POGO.DetailedTreatment;
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

//	public boolean DeleteSaleDetail(long id) throws SQLException{
//		String query="DELETE FROM sales_details WHERE id="+id+";";
//		Statement stmt= this.dbConnection.createStatement();
//		//increase related balance treat by quantity
//		this.restoreBalance(id);
//		if(stmt.executeUpdate(query) > 0) {
//			return true;
//		}
//		return false;
//	}

	private void deleteSaleDetails(long id) throws SQLException {
		
	}
	
	//this method restore balance after deleting sales Details;
	public void restoreBalance(SaleDetails sd) throws SQLException {
		BalanceService balanceService= new BalanceService();
		balanceService.increaseBalance(sd.getBalanceId(), sd.getQuantity());
	}


	public List<SaleDetails> getRelatedSaleDetails(long saleId) throws SQLException {
		List<SaleDetails> saleDetailsList= new ArrayList<>();
		String query="SELECT sales_details.*, treat.name as treatName, typetreat.typeName from sales_details join treat on sales_details.treat_id=treat.id join typetreat on treat.typet=typetreat.id"
				+" where sales_id="+ saleId+";";
		Statement stmt= this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);
		if(!rs.isBeforeFirst()) {
			return null;
		}
		SaleDetails saleDetails;
		DetailedTreatment treatment;
		while(rs.next()){
			saleDetails= new SaleDetails();
			treatment= new DetailedTreatment();
			saleDetails.setTreatId(rs.getLong("treat_id"));
			saleDetails.setSaleId(rs.getLong("sales_id"));
			saleDetails.setQuantity(rs.getDouble("quantity"));
			saleDetails.setPriceOne(rs.getDouble("price_one"));
			saleDetails.setExpireDate(rs.getString("expire"));
			saleDetails.setTotal(rs.getDouble("total"));
			saleDetails.setBalanceId(rs.getLong("blance_id"));
			treatment.setId(rs.getLong("treat_id"));
			treatment.setTypeTreatName(rs.getString("typeName"));
			treatment.setName(rs.getString("treatName"));
			saleDetails.setTreat(treatment);
			saleDetailsList.add(saleDetails);
			saleDetails= null;
			treatment= null;
		}
		return saleDetailsList;
	}
}
