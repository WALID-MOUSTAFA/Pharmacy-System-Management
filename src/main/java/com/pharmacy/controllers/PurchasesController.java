package com.pharmacy.controllers;

import javafx.fxml.FXML;

import java.io.IOException;

public class PurchasesController extends MyController{

    @FXML
    private void backToControlPanel() throws IOException {
        this.swapWithControlPanelScene();
    }
}
