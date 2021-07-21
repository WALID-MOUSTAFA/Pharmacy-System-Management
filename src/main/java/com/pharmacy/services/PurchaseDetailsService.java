package com.pharmacy.services;

import com.pharmacy.DatabaseConnection;
import com.pharmacy.POGO.PurchaseDetails;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PurchaseDetailsService {
    private Connection dbConnection;

    public PurchaseDetailsService() throws SQLException {
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
    }


    public List<PurchaseDetails> getAllPurchaseDetails() {

        return null;
    }
}
