package com.pharmacy.services;

import com.pharmacy.DatabaseConnection;
import com.pharmacy.POGO.Supplier;
import com.pharmacy.POGO.SupplierPayment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierPaymentService {

    private Connection dbConnection;

    public SupplierPaymentService() throws SQLException {
        this.dbConnection= DatabaseConnection
                .getInstance().getConnection();
    }

    public boolean insertSupplierPayment(SupplierPayment supplierPayment) throws SQLException {
        String query= "INSERT INTO paysuplier (suplier_get, suplier_get_date, status, notes, suplier_id) VALUES(?,?,?,?,?)";
        PreparedStatement preparedStatement= this.dbConnection.prepareStatement(query);
        preparedStatement.setDouble(1, supplierPayment.getSupplierGet());
        preparedStatement.setString(2, supplierPayment.getSupplierGetDate());
        preparedStatement.setShort(3, supplierPayment.getStatus());
        preparedStatement.setString(4, supplierPayment.getNotes());
        preparedStatement.setLong(5, supplierPayment.getSupplierId());
        if(preparedStatement.executeUpdate() > 0) {
            return true;
        }
        return false;
    }


    public boolean deleteSupplier(long id) throws SQLException {
        String query= "DELETE FROM paysuplier WHERE id=" + id + ";";
        Statement stmt= this.dbConnection.createStatement();
        if(stmt.executeUpdate(query) > 0) {
            return true;
        }
        return false;
    }

    public List<SupplierPayment> getAllSupplierPayment(Supplier supplier) throws SQLException {
        List<SupplierPayment> supplierPayments= new ArrayList<>();
        String query= "SELECT * FROM paysuplier WHERE suplier_id="+ supplier.getId()+";";
        Statement stmt= this.dbConnection.createStatement();
        ResultSet rs= stmt .executeQuery(query);

        SupplierPayment supplierPayment;
        while(rs.next()) {
            supplierPayment= new SupplierPayment();
            supplierPayment.setId(rs.getLong("id"));
            supplierPayment.setSupplierId(rs.getLong("suplier_id"));
            supplierPayment.setSupplierGet(rs.getDouble("suplier_get"));
            supplierPayment.setSupplierGetDate(rs.getString("suplier_get_date"));
            supplierPayment.setNotes(rs.getString("notes"));
            supplierPayment.setStatus(rs.getShort("status"));
            supplierPayments.add(supplierPayment);
            supplierPayment= null;
        }
        return supplierPayments;
    }

    public boolean updateSupplierPayment(SupplierPayment supplierPayment) throws SQLException {
        String query="UPDATE paysuplier SET suplier_get=?, suplier_get_date=?, status=?, notes=? where id=" + supplierPayment.getId()+ ";";
        PreparedStatement preparedStatement= this.dbConnection.prepareStatement(query);

        preparedStatement.setDouble(1, supplierPayment.getSupplierGet());
        preparedStatement.setString(2, supplierPayment.getSupplierGetDate());
        preparedStatement.setShort(3, supplierPayment.getStatus());
        preparedStatement.setString(4, supplierPayment.getNotes());

        if(preparedStatement.executeUpdate() > 0) {
            return true;
        }
        return false;
    }
}
