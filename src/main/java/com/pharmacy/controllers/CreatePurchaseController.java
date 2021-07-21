package com.pharmacy.controllers;



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
import java.time.LocalDate;
import java.util.List;

public class CreatePurchaseController extends MyController
{

	//services
	private SuppliersService suppliersService;
	private PurchasesService purchasesService;
	
	@FXML
	private TextField pillNum;
	
	@FXML
	private DatePicker purchaseDate;
	
	@FXML
	private TextField totalPeople;

	@FXML
	private TextField totalPharmacy;

	@FXML
	private TextField countUnit;

	@FXML
	private TextField description;

	@FXML
	private ComboBox supplierCombo;



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
	}


	@FXML
	public void addNewPurchase() throws SQLException {
		String pillNum= this.pillNum.getText();
		Timestamp datePur= Timestamp.valueOf(purchaseDate.getValue().atStartOfDay());
		String totalPeople= this.totalPeople.getText();
		String totalPharmacy= this.totalPharmacy.getText();
		String countUnit= this.countUnit.getText();
		String description= this.description.getText();
		String supplierName= this.supplierCombo
			.getSelectionModel().getSelectedItem().toString();

		Supplier supplier= this.suppliersService
			.getSupplierByName(supplierName);

		Purchase purchase= new Purchase();
		purchase.setPillNum(pillNum);
		purchase.setDatePur(datePur.toString());
		purchase.setTotalPeople(Double.valueOf(totalPeople));
		purchase.setTotalPharmacy(Double.valueOf(totalPharmacy));
		purchase.setCountUnit(Double.valueOf(countUnit));
		purchase.setDescription(description);
		purchase.setSupplier(supplier);
		
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
