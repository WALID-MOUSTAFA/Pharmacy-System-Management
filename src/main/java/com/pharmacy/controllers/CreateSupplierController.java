package com.pharmacy.controllers;

import com.pharmacy.POGO.Supplier;
import com.pharmacy.services.SuppliersService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.sql.Timestamp;

public class CreateSupplierController extends  MyController{

    private SuppliersService suppliersService;

    @FXML
    private TextField supplierName;
    @FXML
    private TextField supplierPhone;
    @FXML
    private TextField supplierAddress;
    @FXML
    private TextField supplierCash;


    public CreateSupplierController() throws SQLException {
        this.suppliersService= new SuppliersService();
    }

    @FXML
    public void addNewSupplier() throws SQLException{
        System.out.println(this.stage);

        Supplier supplier= new Supplier();
        supplier.setDateAt((new Timestamp(System.currentTimeMillis())).toString());
        supplier.setName(supplierName.getText());
        supplier.setAddress(supplierAddress.getText());
        supplier.setPhone(supplierPhone.getText());
        supplier.setCash(Double.valueOf(supplierCash.getText()));

        boolean result= this.suppliersService.insertTreatment(supplier);

        if(result == true) {
            this.stage.close();
        }

    }
}
