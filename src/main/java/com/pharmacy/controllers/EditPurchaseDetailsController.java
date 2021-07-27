package com.pharmacy.controllers;

import com.pharmacy.POGO.PurchaseDetails;
import com.pharmacy.POGO.Treatment;
import com.pharmacy.services.PurchaseDetailsService;
import com.pharmacy.services.TreatmentService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;

public class EditPurchaseDetailsController extends MyController{
		
	
	private PurchaseDetailsService purchaseDetailsService;
	
	
	@FXML
	private TextField quantity;
	
	@FXML
	private TextField pricePharmacy ;
	
	@FXML
	private TextField totalPharmacy;

	@FXML
	private TextField pricePeople;

	@FXML
	private TextField totalPeople;

	@FXML
	private DatePicker expireDate;

	@FXML
	private DatePicker productionDate;

	@FXML
	private ComboBox treatName;

	private long id;
	
	public EditPurchaseDetailsController(long id) throws SQLException {
		this.purchaseDetailsService= new PurchaseDetailsService();
		this.id= id;
	}

	
	public PurchaseDetails getSpecificPurchaseDetails() throws SQLException
	{
		PurchaseDetails purchaseDetails=
			this.purchaseDetailsService
			.getPurchaseDetailsById(this.id);
		return purchaseDetails;
	}
	
	
	public void initializeForm() throws SQLException{
		PurchaseDetails pd= this.getSpecificPurchaseDetails();

		this.quantity.setText(String.valueOf(pd.getQuantity()));
		this.totalPharmacy.setText(String.valueOf(pd.getTotalPharmacy()));
		this.pricePharmacy.setText(String.valueOf(pd.getPricePharmacy()));
		this.totalPeople.setText(String.valueOf(pd.getTotalPeople()));
		this.pricePeople.setText(String.valueOf(pd.getPricePeople()));
		this.expireDate.setValue(LocalDate.parse
					 (pd.getExpireDate().split(" ")[0] ));
		this.productionDate
			.setValue(LocalDate
				  .parse(pd.getProductionDate().split(" ")[0]));
		this.treatName.setValue(pd.getTreat().getName());
	}


	@FXML
	public void initialize() throws SQLException {
		this.initializeForm();
	}

	
	@FXML
	public void editPurchaseDetails() throws SQLException{
		TreatmentService treatmentService= new TreatmentService();
		PurchaseDetails purchaseDetails= this.getSpecificPurchaseDetails();
		Treatment treat= treatmentService
			.getTreatmentByName(this.treatName
					    .getSelectionModel()
					    .getSelectedItem().toString());


		purchaseDetails.setQuantity
			(Double.valueOf(this.quantity.getText()));
		purchaseDetails.setTotalPharmacy
			(Double.valueOf(this.totalPharmacy.getText()));
		purchaseDetails.setPricePharmacy
			(Double.valueOf(this.pricePharmacy.getText()));
		purchaseDetails.setTotalPeople
			(Double.valueOf(this.totalPeople.getText()));
		purchaseDetails.setPricePeople
			(Double.valueOf(this.pricePeople.getText()));
		purchaseDetails.setExpireDate
			(this.expireDate.getValue().toString());
		purchaseDetails.setProductionDate
			(this.productionDate.getValue().toString());

		purchaseDetails.setTreat_id(treat.getId());


		if(this.purchaseDetailsService
		   .updatePurchaseDetails(purchaseDetails)) {
			this.stage.close();
		} else {
			Alert alert= new Alert(Alert.AlertType.ERROR);
			alert.show();
		}
	}
    

}
