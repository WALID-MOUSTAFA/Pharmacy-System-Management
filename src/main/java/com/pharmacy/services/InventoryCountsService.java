package com.pharmacy.services;

import com.pharmacy.DatabaseConnection;
import com.pharmacy.POGO.BalanceTreat;
import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.POGO.InventoryCount;
import com.pharmacy.POGO.InventoryCountDetails;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;


public class InventoryCountsService {


    private Connection dbConnection;
	
    public InventoryCountsService() throws SQLException {
	this.dbConnection= DatabaseConnection
	    .getInstance().getConnection();
    }
	
    public long insertInventoryCount(InventoryCount inventoryCount)
	throws SQLException {
	String query= "INSERT INTO inventory_counts (date_in) VALUES (?)";
	PreparedStatement preparedStatement= this.dbConnection
	    .prepareStatement(query);
	preparedStatement.setString(1, inventoryCount.getDateIn());
	if(preparedStatement.executeUpdate() > 0) {
	    ResultSet keys= preparedStatement.getGeneratedKeys();
	    if(keys.next()) {
		return keys.getLong(1);
	    }
	}
	return 0;
    }

    public long insertInventoryCountDetails
	(InventoryCountDetails inventoryCountDetails) throws SQLException {
	String query= "INSERT INTO inventory_counts_details (balance_id, system, actual, status, inventory_counts_id, date_in) VALUES (?,?,?,?,?,?);)";
	PreparedStatement preparedStatement= this.dbConnection
	    .prepareStatement(query);
	preparedStatement.setLong(1, inventoryCountDetails.getBalanceId());
	preparedStatement.setInt (2, inventoryCountDetails.getSystemQuantity());
	preparedStatement.setInt (3, inventoryCountDetails.getActualQuantity());
	preparedStatement.setInt(4, inventoryCountDetails.getStatus());
	preparedStatement.setLong(5, inventoryCountDetails.getInventoryCountsId());
	preparedStatement.setString(6, inventoryCountDetails.getDateIn());

	if(preparedStatement.executeUpdate() > 0){
	    ResultSet keys= preparedStatement.getGeneratedKeys();
	    if(keys.next()) {
		return keys.getLong(1);
	    }
	}
	return 0;
    }
    


    
    public long updateInventoryCountDetails
	(InventoryCountDetails inventoryCountDetails) throws SQLException {
	String query= "UPDATE inventory_counts_details set system=?, actual=?, status=? WHERE id=" +inventoryCountDetails.getId()+ ";";

	PreparedStatement preparedStatement= this.dbConnection
	    .prepareStatement(query);
	preparedStatement.setInt (1, inventoryCountDetails.getSystemQuantity());
	preparedStatement.setInt (2, inventoryCountDetails.getActualQuantity());
	preparedStatement.setInt(3, inventoryCountDetails.getStatus());

	if(preparedStatement.executeUpdate() > 0){
	    ResultSet keys= preparedStatement.getGeneratedKeys();
	    if(keys.next()) {
		return keys.getLong(1);
	    }
	}
	return 0;
    }

    
    
    public List<InventoryCountDetails> findInventoryCountsDetails
	(long id) throws SQLException {
	List<InventoryCountDetails> icd= new ArrayList<>();
	String query= "SELECT * from inventory_counts_details WHERE inventory_counts_id="+id+";";
	Statement stmt= this.dbConnection.createStatement();
	ResultSet rs= stmt.executeQuery(query);
	InventoryCountDetails inventoryCountDetails;
	while(rs.next()){
	    inventoryCountDetails= new InventoryCountDetails();
	    inventoryCountDetails.setId(rs.getLong("id"));
	    inventoryCountDetails.setBalanceId(rs.getLong("balance_id"));
	    inventoryCountDetails.setSystemQuantity(rs.getInt("system"));
	    inventoryCountDetails.setActualQuantity(rs.getInt("actual"));
	    inventoryCountDetails.setInventoryCountsId
		(rs.getLong("inventory_counts_id"));
	    inventoryCountDetails.setDateIn(rs.getString("date_in"));
	    icd.add(inventoryCountDetails);
	    inventoryCountDetails= null;
	}
	return icd;
    }


   public List<InventoryCountDetails> findAllInventoryCountsDetails(long id)
    throws SQLException{
	  //TODO(walid): implement this and put it on the report controller;
	  //implement showing empty and not empty balances;
	  //implement search and filtering.
       List<InventoryCountDetails> icdList= new ArrayList<>();
       String query= "select treat.name as treatName, typetreat.typename as typeName, inventory_counts_details.* "
	   +"from inventory_counts_details "
	   +"join blance_treat on blance_treat.id=inventory_counts_details.balance_id "
	   +"join treat on blance_treat.treat_id=treat.id "
	   +"join typetreat on treat.typet=typetreat.id WHERE inventory_counts_details.inventory_counts_id=" + id+ ";" ;
       
       Statement stmt= this.dbConnection.createStatement();
       ResultSet rs= stmt.executeQuery(query);
       
       InventoryCountDetails inventoryCountDetails;
       DetailedTreatment detailedTreatment;
       BalanceTreat balanceTreat;
       while(rs.next()){
	   inventoryCountDetails= new InventoryCountDetails();
	   detailedTreatment= new DetailedTreatment();
	   balanceTreat= new BalanceTreat();
	   detailedTreatment.setTypeTreatName(rs.getString("typeName"));
	   detailedTreatment.setName(rs.getString("treatName"));
	   balanceTreat.setTreat(detailedTreatment);
	   inventoryCountDetails.setId(rs.getLong("id"));
	   inventoryCountDetails.setDateIn(rs.getString("date_in"));
	   inventoryCountDetails.setSystemQuantity(rs.getInt("system"));
	   inventoryCountDetails.setActualQuantity(rs.getInt("actual"));
	   inventoryCountDetails.setStatus(rs.getInt("status"));
	   inventoryCountDetails.setBalanceTreat(balanceTreat);
	   icdList.add(inventoryCountDetails);
	   inventoryCountDetails= null;
	   detailedTreatment= null;
	   balanceTreat= null;
       }
       return icdList;
   }
    
    
    //this method extract the ids from the inventoryCountDetailslist
    //and returns them as a new ArrayList;
    public List<Long> extractIDsList(List<InventoryCountDetails> icdList)
	throws SQLException {
	List<Long> ids= new ArrayList<>();
	for(InventoryCountDetails i : icdList){
	    ids.add(i.getId());
	}
	return ids;
    }
    
    public List<InventoryCount> findAllInventoryCounts() throws SQLException {
	List<InventoryCount> counts= new ArrayList<>();
	String query= "SELECT * FROM inventory_counts";
	Statement stmt= this.dbConnection.createStatement();
	ResultSet rs= stmt.executeQuery(query);


	InventoryCount inventoryCount;
	while(rs.next()) {
	    inventoryCount= new InventoryCount();
	    inventoryCount.setId(rs.getLong("id"));
	    inventoryCount.setDateIn(rs.getString("date_in"));
	    counts.add(inventoryCount);
	    inventoryCount= null;
	}
	return counts;
    }
    

	public boolean deleteInventoryCount(long id) throws SQLException {
    	String query= "delete from inventory_counts where id="+id+";";
    	Statement stmt= this.dbConnection.createStatement();
    	if(stmt.executeUpdate(query) > 0) {
    		return true;
		}
    	return false;
	}
	
}
