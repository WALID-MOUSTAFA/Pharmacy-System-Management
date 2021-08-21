package com.pharmacy.controllers;



import com.pharmacy.MyUtils;
import com.pharmacy.POGO.Purchase;
import com.pharmacy.POGO.Supplier;
import com.pharmacy.services.PurchasesService;
import javafx.fxml.FXML;
import com.pharmacy.services.SuppliersService;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class CreatePurchaseController extends MyController
{

	//services
	private SuppliersService suppliersService;
	private PurchasesService purchasesService;
	
	@FXML
	private TextField pillNum;
	
	@FXML private DatePicker purchaseDate;
	@FXML private TextField totalPeople;
	@FXML private TextField totalPharmacy;
	@FXML private TextField countUnit;
	@FXML private TextField description;
	@FXML private ComboBox supplierCombo;



	public CreatePurchaseController() throws SQLException{
		this.suppliersService= new SuppliersService();
		this.purchasesService= new PurchasesService();
	}

	public void initializeSuppliersCombo() throws SQLException{
		List<Supplier> suppliers=  this.suppliersService.getAllSuppliers();
		for (Supplier s: suppliers) {
			this.supplierCombo.getItems().add(s.getName());
		}
	}

	@FXML
	private void initialize() throws SQLException{
		this.initializeSuppliersCombo();
		MyUtils.setDatePickerFormat(this.purchaseDate);
	}


	@FXML
	public void addNewPurchase() throws SQLException {
		List<String> errors= new ArrayList<>();
		Timestamp datePur= null;
		Supplier supplier=new Supplier();
		String totalPeople="";
		String totalPharmacy="";
		String countUnit="";
		String description="";
		String pillNum="";
		String supplierName="";
		Purchase purchase;

		//checking here to prevent nullpointerexception;
		if(purchaseDate.getValue() == null) {
			errors.add("يجب أن تختار التاريخ");
			MyUtils.showValidationErrors(errors);
			return;
		}

		if(this.supplierCombo.getSelectionModel().isEmpty()) {
			errors.add("يجب أن تختار المورد");
			MyUtils.showValidationErrors(errors);
			return;
		}

		pillNum = this.pillNum.getText();
		datePur= Timestamp.valueOf(purchaseDate.getValue().atStartOfDay());
		totalPeople= this.totalPeople.getText();
		totalPharmacy= this.totalPharmacy.getText();
		countUnit= this.countUnit.getText();
		description= this.description.getText();
		supplierName= this.supplierCombo
			.getSelectionModel().getSelectedItem().toString();
		supplier = this.suppliersService
				.getSupplierByName(supplierName);

		purchase= new Purchase();
		purchase.setPillNum(pillNum);
		purchase.setDatePur(datePur.toString());
		purchase.setTotalPeople
			(!totalPeople.isEmpty()? Double.valueOf(totalPeople) : 0);
		purchase.setTotalPharmacy
			(!totalPharmacy.isEmpty()? Double.valueOf(totalPharmacy):0);
		purchase.setCountUnit
			(!countUnit.isEmpty()? Double.valueOf(countUnit):0);
		purchase.setDescription(description);
		purchase.setSupplier(supplier);

		MyUtils.<Purchase>validateModel(purchase, errors);
		if(!errors.isEmpty()){
			MyUtils.showValidationErrors(errors);
			return;
		}

		if(purchasesService.insertPurchase(purchase)) {

			this.stage.close();
		} else {
			Alert alert= new Alert(Alert.AlertType.ERROR);
			alert.setTitle("خطأ!!!");
			alert.setHeaderText("something went wrong");
			alert.setContentText("something went worng");
			alert.show();
		}
		
	}

}
