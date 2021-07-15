package com.pharmacy.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.pharmacy.DatabaseConnection;
import java.sql.Connection;

import java.io.IOException;
import java.sql.SQLException;

public class TreatmentController extends MyController{
    @FXML
    private Button backToControlPanelButton;

    private Connection dbConnection;

    @FXML
    private void initialize() throws SQLException {
        this.dbConnection= DatabaseConnection.getInstance().getConnection();

    }


    @FXML
    private void backToControlPanel() throws IOException {
        this.swapWithControlPanelScene();
    }


}
