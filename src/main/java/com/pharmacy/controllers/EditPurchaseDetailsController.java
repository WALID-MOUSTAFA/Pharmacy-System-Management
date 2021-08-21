//TODO(walid): change the quantity when change the the quantity
package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.DetailedTreatment;
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
import java.util.ArrayList;
import java.util.List;

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

	
	public void initializeTreatmentCombo() throws SQLException {
		TreatmentService ts= new TreatmentService();
		List<DetailedTreatment> treatments= ts.getAllTreatments();
		for (Treatment t: treatments) {
			this.treatName.getItems().add(t.getName());
		}
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

		this.initializeTreatmentCombo();
		this.treatName.setValue(pd.getTreat().getName());
		
	}


	@FXML
	public void initialize() throws SQLException {
		this.initializeForm();
		MyUtils.setDatePickerFormat(this.expireDate);
		MyUtils.setDatePickerFormat(this.productionDate);
	}

	
	@FXML
	public void editPurchaseDetails() throws SQLException{

		List<String> errors= new ArrayList<>();
		TreatmentService treatmentService= new TreatmentService();
		PurchaseDetails purchaseDetails= this.getSpecificPurchaseDetails();


		String quantity;
		String pricePharmacy;
		String totalPharmacy;
		String pricePeople;
		String totalPeople;
		String expireDate;
		String productionDate;
		String treatName;
		Treatment treat;


		
		if(this.expireDate.getValue() == null) {
			errors.add("يجب اختيار تاريخ الصلاحية");
			MyUtils.showValidationErrors(errors);
			return;
		}

		if(this.productionDate.getValue() == null) {
			errors.add("يجب اختيار تاريخ الإنتاج");
			MyUtils.showValidationErrors(errors);
			return;
		}

		
		if(this.treatName.getValue() == null) {
			errors.add("يجب اختيار اسم المنتج");
			MyUtils.showValidationErrors(errors);
			return;
		}

		
		quantity= this.quantity.getText();
		totalPharmacy= this.totalPharmacy.getText();
		totalPeople = this.totalPeople.getText();
		pricePharmacy=this.pricePharmacy.getText();
		pricePeople= this.pricePeople.getText();
		expireDate= this.expireDate.getValue().toString();
		productionDate= this.productionDate.getValue().toString();

		String name= this.treatName
				.getSelectionModel()
				.getSelectedItem().toString();
		treat= treatmentService
			.getTreatmentByName(name.split("-")[0], name.split("-")[1]);


		purchaseDetails.setQuantity
			(!quantity.isEmpty()? Double.valueOf(quantity):0);
		purchaseDetails.setTotalPharmacy
			(!totalPharmacy.isEmpty()?Double.valueOf(totalPharmacy):0);
		purchaseDetails.setPricePharmacy
			(!pricePharmacy.isEmpty()?Double.valueOf(pricePharmacy):0);
		purchaseDetails.setTotalPeople
			(!totalPeople.isEmpty()? Double.valueOf(totalPeople):0);
		purchaseDetails.setPricePeople
			(!pricePeople.isEmpty()? Double.valueOf(pricePeople):0);
		purchaseDetails.setExpireDate
			(expireDate);
		purchaseDetails.setProductionDate
			(productionDate);

		purchaseDetails.setTreat_id(treat.getId());
		
		
		
		MyUtils.<PurchaseDetails>validateModel(purchaseDetails, errors);
		if(!errors.isEmpty()){
			MyUtils.showValidationErrors(errors);
			return;
		}

		
		if(this.purchaseDetailsService
		   .updatePurchaseDetails(purchaseDetails)) {
			this.stage.close();
		} else {
			Alert alert= new Alert(Alert.AlertType.ERROR);
			alert.setContentText("حدث خطأ ما أثناء عملية الحفظ");
			alert.show();
		}
	}
    

}
