package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import com.pharmacy.POGO.Supplier;
import com.pharmacy.services.SuppliersService;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class EditSupplierController extends MyController {

	
	@FXML private TextField supplierName;
	@FXML private TextField supplierPhone;
	@FXML private TextField supplierAddress;
	@FXML private TextField supplierCash;

	private Supplier supplier;
	private SuppliersService suppliersService;
	private long id;
	
	public EditSupplierController() throws SQLException{
		this.suppliersService= new SuppliersService();

	}

	private Supplier getSpecificSupplier() throws SQLException{
		Supplier supplier= this.suppliersService.getSupplierById(this.id);
		return supplier;
	}

	private void initializeForm() throws SQLException {
		this.supplierName.setText(this.supplier.getName());
		this.supplierPhone.setText(this.supplier.getPhone());
		this.supplierAddress.setText(this.supplier.getAddress());
		this.supplierCash.setText(String.valueOf(this.supplier.getCash()));
	}
	
	@FXML
	private void initialize() throws SQLException{
		this.supplier= this.getSpecificSupplier();
		this.initializeForm();
	}

	@FXML
	private void updateSupplier() throws SQLException {
		List<String> errors= new ArrayList<>();
		String name= this.supplierName.getText();
		String address= this.supplierAddress.getText();
		String phone= this.supplierPhone.getText();
		double cash= !this.supplierCash.getText().isEmpty()?
			Double.valueOf(this.supplierCash.getText())
			: 0;
			  
		this.supplier.setName(name);
		this.supplier.setPhone(phone);
		this.supplier.setAddress(address);
		this.supplier.setCash(cash);

		MyUtils.<Supplier>validateModel(supplier, errors);
		if(!errors.isEmpty()) {
			MyUtils.showValidationErrors(errors);;
			return;
		}

		if(this.suppliersService.updateSupplier(this.supplier)) {
			this.stage.close();
		} else {
			Alert alert= new Alert(Alert.AlertType.ERROR);
			alert.setContentText("حدث خطأ أثناء التعديل");
			alert.show();
		}
	}

	
	public void setId(long id){
		this.id= id;
	}
	
	
}
