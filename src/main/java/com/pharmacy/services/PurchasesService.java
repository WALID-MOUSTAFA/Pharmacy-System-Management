package com.pharmacy.services;

import com.pharmacy.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PurchasesService {

    private Connection dbConnection;

    public PurchasesService() throws SQLException {
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
    }




}
