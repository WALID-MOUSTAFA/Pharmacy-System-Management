package com.pharmacy.controllers;

import com.pharmacy.POGO.Purchase;
import com.pharmacy.POGO.PurchaseDetails;
import com.pharmacy.POGO.Supplier;
import com.pharmacy.POGO.Treatment;
import com.pharmacy.services.PurchaseDetailsService;
import com.pharmacy.services.PurchasesService;
import com.pharmacy.services.SuppliersService;
import com.pharmacy.services.TreatmentService;
import com.pharmacy.MyUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EditPurchaseController extends MyController{
		
	
	//services
	private SuppliersService suppliersService;
	private PurchasesService purchasesService;
	@FXML
	private TextField pillNum;
	@FXML
	private TextField totalPeople;
	@FXML
	private TextField totalPharmacy;
	@FXML
	private TextField countUnit;
	@FXML
	private TextField description;
	@FXML
	private DatePicker purchaseDate;
	@FXML
	private ComboBox supplierCombo;
	private long id;

	public EditPurchaseController(long id) throws SQLException {
		this.id= id;
		this.suppliersService= new SuppliersService();
		this.purchasesService= new PurchasesService();
	}

	
	public Purchase getSpecificPurchase() throws SQLException
	{
		Purchase purchase=
			this.purchasesService
			.getPurchaseById(this.id);
		return purchase;
	}

	
	public void initializeSuppliersCombo() throws SQLException{
		List<Supplier> suppliers=  this.suppliersService.getAllSuppliers();
		for (Supplier s: suppliers) {
			this.supplierCombo.getItems().add(s.getName());
		}
	}

	
	public void initializeForm() throws SQLException{
		Purchase p= this.getSpecificPurchase();

		this.pillNum.setText(p.getPillNum());
		this.totalPeople.setText(String.valueOf(p.getTotalPeople()));
		this.totalPharmacy.setText(String.valueOf(p.getTotalPharmacy()));
		this.countUnit.setText(String.valueOf(p.getCountUnit()));
		this.description.setText(p.getDescription());
		this.purchaseDate.setValue(LocalDate.parse(p.getDatePur()));

		this.supplierCombo.setValue(p.getSupplier().getName());
		this.initializeSuppliersCombo();
	}




	@FXML
	public void initialize() throws SQLException {
		this.initializeForm();
		MyUtils.setDatePickerFormat(this.purchaseDate);
	}

	
	@FXML
	public void editPurchase() throws SQLException{
		Purchase purchase= this.getSpecificPurchase();
		Supplier supplier= this.suppliersService
			.getSupplierByName
		       (this.supplierCombo.getSelectionModel().getSelectedItem()
			.toString());
		purchase.setPillNum(this.pillNum.getText());
		purchase.setTotalPeople
			(Double.valueOf(this.totalPeople.getText()));
		purchase.setTotalPharmacy
			(Double.valueOf(this.totalPharmacy.getText()));
		purchase.setDescription(this.description.getText());
		purchase.setDatePur(this.purchaseDate.getValue().toString());
		purchase.setSupplier(supplier);

		if(this.purchasesService.updatePurchase(purchase)) {
			this.stage.close();
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.show();
		}
		
		
	}
    

}
