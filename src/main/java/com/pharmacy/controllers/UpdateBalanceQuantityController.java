package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.BalanceTreat;
import com.pharmacy.services.BalanceService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class UpdateBalanceQuantityController extends MyController {

    private BalanceTreat balanceTreat;
    private BalanceService balanceService;
    @FXML private TextField quantity;

    public UpdateBalanceQuantityController() throws SQLException {
        this.balanceService= new BalanceService();
    }

    @FXML
    private  void initalize() {
        this.quantity.setText(String.valueOf(this.balanceTreat.getQuantity()));
    }

    @FXML
    private void doUpdate() throws SQLException {
        double newQuantity;
        try {
            newQuantity= Double.parseDouble(this.quantity.getText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            MyUtils.ALERT_ERROR("خطا في تعديل الكمية! تأكد من المدخلات");
            return;
        }
        if(this.balanceService.updateQuantity(this.balanceTreat.getId(), newQuantity)) {
            this.stage.close();
        }
    }

    public void setBalance(BalanceTreat balanceTreat) {
        this.balanceTreat=balanceTreat;
    }
}
