package com.pharmacy.controllers;

import com.pharmacy.POGO.Purchase;
import com.pharmacy.POGO.PurchaseDetails;
import com.pharmacy.services.PurchaseDetailsService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class PurchaseDetailsController extends MyController {

	private PurchaseDetailsService purchaseDetailsService;

	private long currentPurchaseId;


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
	
	@FXML
	private TableView purchasesDetailsTableView;
	

	public PurchaseDetailsController() throws SQLException {
		this.purchaseDetailsService= new PurchaseDetailsService();
	}


	private void initalizeTableview() throws SQLException{
		Purchase purchase= new Purchase();
		purchase.setId(this.currentPurchaseId);

		ObservableList<PurchaseDetails> pDS=
			FXCollections
			.observableArrayList(this
			.purchaseDetailsService
			.getAllRelatedPurchaseDetails(purchase));

		TableColumn<PurchaseDetails, String> expireDate= new TableColumn<>("تاريخ الصلاحية");
		expireDate.setCellValueFactory(new PropertyValueFactory<>("expireDate"));
		
		TableColumn<PurchaseDetails, String> productionDate= new TableColumn<>("تاريخ الانتاج");
		productionDate.setCellValueFactory(new PropertyValueFactory<>("productionDate"));

		TableColumn<PurchaseDetails, String> dateAt= new TableColumn<>("تاريخ الانشاء");
		dateAt.setCellValueFactory(new PropertyValueFactory<>("dateAt"));

		TableColumn<PurchaseDetails, String> quantity= new TableColumn<>("الكمية");
		quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		TableColumn<PurchaseDetails, String> pricePeople= new TableColumn<>("السعر للناس");
		pricePeople.setCellValueFactory(new PropertyValueFactory<>("pricePeople"));

		TableColumn<PurchaseDetails, String> pricePharmacy= new TableColumn<>("السعر للصيدلية");
		pricePharmacy.setCellValueFactory(new PropertyValueFactory<>("pricePharmacy"));

		TableColumn<PurchaseDetails, String> totalPeople= new TableColumn<>("اجمالي الناس");
		totalPeople.setCellValueFactory(new PropertyValueFactory<>("totalPeople"));

		TableColumn<PurchaseDetails, String> totalPharmacy= new TableColumn<>("اجمالي الصيدلية");
		totalPharmacy.setCellValueFactory(new PropertyValueFactory<>("totalPharmacy"));

		
		TableColumn<PurchaseDetails, String> treatName= new TableColumn<>("اسم الدواء");
		treatName.setCellValueFactory(tf-> new SimpleStringProperty(tf.getValue().getTreat().getName()));

		this.purchasesDetailsTableView.getColumns().addAll(treatName,
								   expireDate,
								   productionDate,
								   quantity,
								   pricePharmacy,
								   pricePeople,
								   totalPharmacy,
								   totalPeople,
								   dateAt);
		this.purchasesDetailsTableView.setItems(pDS);
	}


	public initializeTreatmentCombo() throws SQLException {
		TreatmentService ts= new TreatmentService();
		List<Treatment> treatments= ts.getAllTreatments();
		for (Treatments t: treatments) {
			this.treatName.getItems().add(t.getName());
		}
	}
	
	@FXML
	private void initialize()  throws SQLException{
		this.initalizeTableview();
		this.initializeTreatmentCombo();
	}
	

	@FXML
	public void insertPurchaseDetails() throws SQLException {
		TreatmentService treatmentService= new TreatmentService();
		PurchaseDetails purchaseDetails= new PurchaseDetails();
		String quantity= this.quantity.getText();
		String pricePharmacy= this.pricePharmacy.getText();
		String totalPharmacy= this.totalPharmacy.getText();
		String pricePeople= this.pricePeople.getText();
		String totalPeople= this.totalPeople.getText();
		String expireDate= Timestamp
			.valueOf(this.expireDate.getValue().atStartOfDay());
		String productionDate= Timestamp
			.valueOf(this.productionDate.getValue().atStartOfDay());
		String treatName= this.treatName.
			getSelectionModel().getSelectedItem().toString();

		Treatments treatment= treatmentService.getTreatmentByName(treatName);


		//setting values to the pogo;
		//TODO(walid): finish the insert;
		purchaseDetails.setPurchase_id(this.currentPurchaseId);
		
	}

	public void setSelectedPurchaseId(long currentSelectedItemId) {
		this.currentPurchaseId= currentSelectedItemId;
	}

	

	
	
}
