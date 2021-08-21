package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.Customer;
import com.pharmacy.services.CustomerService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class EditCustomerController extends MyController{

	private CustomerService customerService;
	private Customer customer;
	
	@FXML
	private TextField customerName;
	@FXML private TextField customerAddress;
	@FXML private TextField customerCash;
	

	public EditCustomerController() throws SQLException{
		this.customerService= new CustomerService();
	}


	private void initializeForm() {
		this.customerCash.setText(String.valueOf(this.customer.getCash()));
		this.customerAddress.setText( this.customer.getAddress());
		this.customerName.setText(this.customer.getName());
	}

	@FXML
	private void initialize() {
		this.initializeForm();
	}
	
	@FXML
	private void updateCustomer() throws SQLException {
		List<String> errors= new ArrayList<>();
		String customerName;
		String customerAddress;
		double customerCash;
		

		customerName= this.customerName.getText();
		customerAddress= this.customerAddress.getText();
		customerCash=!this.customerCash.getText().isEmpty()?
			Double.valueOf(this.customerCash.getText()): 0;


		customer.setName(customerName);
		customer.setAddress(customerAddress);
		customer.setCash(customerCash);


		MyUtils.<Customer>validateModel(customer, errors);
		if(!errors.isEmpty()){
			MyUtils.showValidationErrors(errors);
			return;
		}

		if(this.customerService.updateCustomer(this.customer)){
			this.stage.close();

		} else {
			//TODO(walid): handle errors;
		}
	}



	public void setCustomer(Customer customer) {
		this.customer= customer;
	}
}
