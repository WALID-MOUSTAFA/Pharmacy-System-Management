package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.Supplier;
import com.pharmacy.services.SuppliersService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.ArrayList;
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

	private SuppliersController suppliersController; //for communication between parent and included fxml

	public void setSuppliersController(SuppliersController suppliersController) {
		this.suppliersController = suppliersController;
	}


    public CreateSupplierController() throws SQLException {
        this.suppliersService= new SuppliersService();
    }

    @FXML
    public void addNewSupplier() throws SQLException{
	    List<String> errors= new ArrayList<>();
	    String name;
	    String address;
	    String phone;
	    double cash;	    
	    
	    name= this.supplierName.getText();
	    address= this.supplierAddress.getText();
	    phone= this.supplierPhone.getText();
	    cash= !this.supplierCash.getText().isEmpty()?
		    Double.valueOf(supplierCash.getText())
		    : 0;
	    
	    
	    Supplier supplier= new Supplier();
	    supplier.setName(name);
	    supplier.setAddress(address);
	    supplier.setPhone(phone);
	    supplier.setCash(cash);

	    MyUtils.<Supplier>validateModel(supplier, errors);
	    if(!errors.isEmpty()) {
		    MyUtils.showValidationErrors(errors);;
		    return;
	    }
	    
	    boolean result= this.suppliersService.insertTreatment(supplier);

		if (result) {
			this.suppliersController.initializeTableView();
		}

    }
}
