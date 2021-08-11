package com.pharmacy.services;

import com.pharmacy.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import com.pharmacy.POGO.Purchase;
import com.pharmacy.POGO.Supplier;
import com.pharmacy.POGO.TypeTreat;
import java.sql.Timestamp;
import java.sql.PreparedStatement;


public class PurchasesService {

	private Connection dbConnection;

	public PurchasesService() throws SQLException {
		this.dbConnection= DatabaseConnection.getInstance().getConnection();
	}

	public List<Purchase>getAllPurchases() throws SQLException {

		List<Purchase> purchases = new ArrayList<Purchase>();
		String query= "SELECT  purchases.*, supliers.name as supplierName from purchases left JOIN supliers on purchases.suplier_id = supliers.id or purchases.suplier_id=NULL;";
		Statement stmt = this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);
		Purchase purchase;
		Supplier supplier;
		
		while (rs.next()) {
			purchase= new Purchase();
			supplier= new Supplier();
			purchase.setId(rs.getLong("id"));
			purchase.setDatePur(rs.getString("date_pur"));
			purchase.setPillNum(rs.getString("pill_num"));
			purchase.setTotalPeople(rs.getDouble("total_people"));
			purchase.setTotalPharmacy(rs.getDouble("total_phermcy"));
			purchase.setCountUnit(rs.getDouble("count_unit"));
			purchase.setDiscount(rs.getDouble("dicount"));
			purchase.setProfit(rs.getDouble("profit"));
			purchase.setDescription(rs.getString("description"));
			purchase.setDateAt(rs.getString("date_at"));
			purchase.setSupplierID(rs.getLong("suplier_id"));
			supplier.setName(rs.getString("supplierName"));
			purchase.setSupplier(supplier);
			purchases.add(purchase);
		}
		
		return purchases;
	}


	public Purchase getPurchaseById(long id) throws SQLException {
		
		String query= "SELECT  purchases.*, supliers.name as supplierName from purchases LEFT JOIN supliers on purchases.suplier_id = supliers.id OR purchases.suplier_id=NULL WHERE purchases.id=" + id +";";
		Statement stmt = this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);

		Purchase purchase= new Purchase();
		Supplier supplier= new Supplier();
		
		while (rs.next()) {
			purchase.setId(rs.getLong("id"));
			purchase.setDatePur(rs.getString("date_pur"));
			purchase.setPillNum(rs.getString("pill_num"));
			purchase.setTotalPeople(rs.getDouble("total_people"));
			purchase.setTotalPharmacy(rs.getDouble("total_phermcy"));
			purchase.setCountUnit(rs.getDouble("count_unit"));
			purchase.setDiscount(rs.getDouble("dicount"));
			purchase.setProfit(rs.getDouble("profit"));
			purchase.setDescription(rs.getString("description"));
			purchase.setDateAt(rs.getString("date_at"));
			purchase.setSupplierID(rs.getLong("suplier_id"));
			supplier.setName(rs.getString("supplierName"));
			purchase.setSupplier(supplier);
		}
		
		return purchase;
		
	}
	
	public boolean insertPurchase(Purchase purchase) throws SQLException {
		SuppliersService suppliersService= new SuppliersService();
		Supplier supplier= suppliersService
			.getSupplierByName(purchase.getSupplier().getName());
		String query= "INSERT INTO purchases (date_pur, pill_num, total_people, total_phermcy, count_unit, dicount, profit, description, date_at, suplier_id) VALUES(?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement= this.dbConnection.prepareStatement(query);
		preparedStatement.setString(1, purchase.getDatePur());
		preparedStatement.setString(2, purchase.getPillNum());
		preparedStatement.setDouble(3, purchase.getTotalPeople());
		preparedStatement.setDouble(4, purchase.getTotalPharmacy());
		preparedStatement.setDouble(5, purchase.getCountUnit());
		//TODO(walid): replace the zero value with the actuall;
		preparedStatement.setDouble(6, 0);
		//TODO(walid): replace the zero value with the actuall;
		preparedStatement.setDouble(7, 0);
		preparedStatement.setString(8, purchase.getDescription());
		preparedStatement.setString(9, (new Timestamp(System.currentTimeMillis()).toString()));
		preparedStatement.setLong(10, supplier.getId());

		if(preparedStatement.executeUpdate() > 0){
			return true;
		}

		return false;
	}



	public boolean deletePurchase(long id) throws SQLException {
		String query= "DELETE FROM purchases WHERE id=" + id + ";";
		Statement stmt= this.dbConnection.createStatement();
		int result= stmt.executeUpdate(query);
		if(result > 0) {
			return true;
		}
		return false;
	}

	public boolean updatePurchase(Purchase purchase) throws SQLException {
		SuppliersService suppliersService= new SuppliersService();

		Supplier supplier= suppliersService
				.getSupplierByName(purchase.getSupplier().getName());
		String query= "UPDATE purchases "
			+"set "
			+"date_pur=?,"
			+"pill_num=?,"
			+"count_unit=?,"
			+"dicount=?,"
			+"profit=?,"
			+"description=?,"
			+"date_at=?,"
			+"suplier_id=?,"
			+"total_people=?,"
			+"total_phermcy=? WHERE id=" +purchase.getId()+ ";";


		PreparedStatement preparedStatement=
			this.dbConnection.prepareStatement(query);

		preparedStatement.setString(1, purchase.getDatePur());
		preparedStatement.setString(2, purchase.getPillNum());
		preparedStatement.setDouble(3, purchase.getCountUnit());
		//TODO(walid): replace the zero value with the actuall;
		preparedStatement.setDouble(4, 0);
		//TODO(walid): replace the zero value with the actuall;
		preparedStatement.setDouble(5, 0);
		preparedStatement.setString(6, purchase.getDescription());
		preparedStatement.setString(7, (new Timestamp
						(System.currentTimeMillis())
						.toString()));
		preparedStatement.setLong(8, supplier.getId());
		preparedStatement.setDouble(9, purchase.getTotalPeople());
		
		preparedStatement.setDouble(10, purchase.getTotalPharmacy());


		
		if(preparedStatement.executeUpdate() > 0){
			return true;
		}

		return false;

	}
}
