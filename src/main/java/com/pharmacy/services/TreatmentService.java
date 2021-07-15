package com.pharmacy.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.pharmacy.DatabaseConnection;
import com.pharmacy.POGO.Treatment;

public class TreatmentService {

    private Connection dbConnection;

    public List<Treatment> getAllMedicne() throws SQLException {
        this.dbConnection= DatabaseConnection.getInstance().getConnection();
        String stmt= "SELECT * FROM treat";

        return null;
    }


}
