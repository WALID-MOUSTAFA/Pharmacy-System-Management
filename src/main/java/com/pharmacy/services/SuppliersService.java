package com.pharmacy.services;

import com.pharmacy.DatabaseConnection;
import com.pharmacy.POGO.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SuppliersService {

    private Connection dbConnection;

    public SuppliersService() throws SQLException {
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
    }

    public List<Supplier> getAllSuppliers() throws SQLException{
        List<Supplier> suppliers= new ArrayList<>();
        String query= "SELECT * FROM supliers";
        Statement stmt = this.dbConnection.createStatement();
        ResultSet rs= stmt.executeQuery(query);
        Supplier supplier;
        while(rs.next()) {
            supplier= new Supplier();
            supplier.setId(rs.getLong("id"));
            supplier.setName(rs.getString("name"));
            supplier.setAddress(rs.getString("address"));
            supplier.setCash(rs.getDouble("cash"));
            supplier.setDateAt(rs.getString("date_at"));
            suppliers.add(supplier);
            supplier= null;
        }

        return suppliers;
    }

    public boolean insertTreatment(Supplier supplier) throws SQLException{
        String query= "INSERT INTO supliers (name, phone, address, cash, date_at) VALUES (?,?,?,?,?)";
        PreparedStatement preparedStatement= this.dbConnection.prepareStatement(query);
        preparedStatement.setString(1, supplier.getName());
        preparedStatement.setString(2, supplier.getPhone());
        preparedStatement.setString(3, supplier.getAddress());
        preparedStatement.setDouble(4, supplier.getCash());
        preparedStatement.setString(5 , supplier.getDateAt());

        if (preparedStatement.executeUpdate() > 0){
            return true;
        }
        return false;
    }

	
	public Supplier getSupplierByName(String supplierName) throws SQLException{
		String query= "SELECT * FROM supliers where name=\"" + supplierName + "\";";
		Statement stmt= this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);
		Supplier supplier= new Supplier();
		while(rs.next()) {
			supplier.setName(rs.getString("name"));
			supplier.setId(rs.getLong("id"));
		}
		return supplier;
	}



}
