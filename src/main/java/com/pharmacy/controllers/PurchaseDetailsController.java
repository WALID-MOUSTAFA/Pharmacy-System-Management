//TODO(walid): there is a bug in editing a newely added purchaseDetails;
//TODO(walid): display treats with their type, and when retrive treat, retrive it with its type and name; use split;

package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.*;
import com.pharmacy.services.BalanceService;
import com.pharmacy.services.PurchaseDetailsService;
import com.pharmacy.services.TreatmentService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDetailsController extends MyController {

	private PurchaseDetailsService purchaseDetailsService;
	private BalanceService balanceService;

	//Note(walid): this is the purchase id, not the purchase details.
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
	
	@FXML
	public Button editPurchaseDetailsButton;

	@FXML
	public Button deletePurchaseDetailsButton;
	
	public PurchaseDetailsController() throws SQLException {
		this.purchaseDetailsService= new PurchaseDetailsService();
		this.balanceService= new BalanceService();
	}


	private void initalizeTableview() throws SQLException{
		Purchase purchase= new Purchase();
		purchase.setId(this.currentPurchaseId);

		ObservableList<PurchaseDetails> pDS=
			FXCollections
			.observableArrayList(this
			.purchaseDetailsService
			.getAllRelatedPurchaseDetails(purchase));

		TableColumn<PurchaseDetails, String> expireDate=
			new TableColumn<>("تاريخ الصلاحية");
		expireDate.setCellValueFactory(new PropertyValueFactory<>
					       ("expireDate"));
		
		TableColumn<PurchaseDetails, String> productionDate=
			new TableColumn<>("تاريخ الانتاج");
		productionDate.setCellValueFactory(new PropertyValueFactory<>
						   ("productionDate"));

		TableColumn<PurchaseDetails, String> dateAt=
			new TableColumn<>("تاريخ الانشاء");
		dateAt.setCellValueFactory(new PropertyValueFactory<>
					   ("dateAt"));

		TableColumn<PurchaseDetails, String> quantity=
			new TableColumn<>("الكمية");
		quantity.setCellValueFactory(new PropertyValueFactory<>
					     ("quantity"));

		TableColumn<PurchaseDetails, String> pricePeople=
			new TableColumn<>("السعر للناس");
		pricePeople.setCellValueFactory(new PropertyValueFactory<>
						("pricePeople"));

		TableColumn<PurchaseDetails, String> pricePharmacy=
			new TableColumn<>("السعر للصيدلية");
		pricePharmacy.setCellValueFactory(new PropertyValueFactory<>
						  ("pricePharmacy"));

		TableColumn<PurchaseDetails, String> totalPeople=
			new TableColumn<>("اجمالي الناس");
		totalPeople.setCellValueFactory(new PropertyValueFactory<>
						("totalPeople"));

		TableColumn<PurchaseDetails, String> totalPharmacy=
			new TableColumn<>("اجمالي الصيدلية");
		totalPharmacy.setCellValueFactory(new PropertyValueFactory<>
						  ("totalPharmacy"));

		
		TableColumn<PurchaseDetails, String> treatName=
			new TableColumn<>("اسم الدواء");
		treatName.setCellValueFactory(tf-> 
				new SimpleStringProperty
					(tf.getValue().getTreat().getName()));

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


	public void addTableViewFocusListeners() {
		
		this.purchasesDetailsTableView.focusedProperty()
			.addListener((observableVal,oldval,newval)-> {
			if(newval) {
				this.editPurchaseDetailsButton.setDisable(false);
				this.deletePurchaseDetailsButton.setDisable(false);
			} else {
				this.editPurchaseDetailsButton.setDisable(true);
				this.deletePurchaseDetailsButton.setDisable(true);
			}
		});
	}
	

	public void initializeTreatmentCombo() throws SQLException {
		TreatmentService ts= new TreatmentService();
		List<DetailedTreatment> treatments= ts.getAllTreatments();
		for (DetailedTreatment t: treatments) {
			this.treatName.getItems().add(t.getName()+"-"+t.getTypeTreatName());
		}
	}

	
	public boolean insertBalanceTreat(PurchaseDetails pd) throws SQLException
	{
		BalanceTreat balanceTreat= new BalanceTreat();
		balanceTreat.setTreatId(pd.getTreat_id());
		balanceTreat.setPurchaseId(pd.getPurchase_id());
		balanceTreat.setPurchaseDetailsId(pd.getId());
		balanceTreat.setExpireDate(pd.getExpireDate());
		balanceTreat.setPrice(pd.getPricePeople());
		balanceTreat.setTotal(pd.getTotalPeople());
		balanceTreat.setQuantity(pd.getQuantity());

		if(this.balanceService.insertBalanceTreat(balanceTreat)){
			return true;
		}

		return false;
	}

		
	@FXML
	private void initialize()  throws SQLException{
		this.initalizeTableview();
		this.initializeTreatmentCombo();
		this.addTableViewFocusListeners();
		MyUtils.setDatePickerFormat(this.expireDate);
		MyUtils.setDatePickerFormat(this.productionDate);
	}


	@FXML
	public void insertPurchaseDetails() throws SQLException {
		List<String> errors= new ArrayList<>();
		String quantity;
		String pricePharmacy;
		String totalPharmacy;
		String pricePeople;
		String totalPeople;
		String expireDate;
		String productionDate;
		String treatName;
		Treatment treatment;
		
		TreatmentService treatmentService= new TreatmentService();
		PurchaseDetails purchaseDetails= new PurchaseDetails();


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
		pricePharmacy= this.pricePharmacy.getText();
		totalPharmacy= this.totalPharmacy.getText();
		pricePeople= this.pricePeople.getText();
		totalPeople= this.totalPeople.getText();
		expireDate= Timestamp
			.valueOf(this
				 .expireDate
				 .getValue()
				 .atStartOfDay()).toString();
		productionDate= Timestamp
			.valueOf(this
				 .productionDate
				 .getValue()
				 .atStartOfDay()).toString();
		treatName= this.treatName.
			getSelectionModel().getSelectedItem().toString();

		treatment= treatmentService
			.getTreatmentByName(treatName.split("-")[0], treatName.split("-")[1]);

		purchaseDetails.setTreat(treatment);

		
		//setting values to the pogo;
		purchaseDetails.setPurchase_id(this.currentPurchaseId);
		purchaseDetails.setTreat_id(treatment.getId());
		purchaseDetails.setExpireDate(expireDate);
		purchaseDetails.setProductionDate(productionDate);
		purchaseDetails.setQuantity
			(!quantity.isEmpty()? Double.valueOf(quantity):0 );
		purchaseDetails.setPricePeople
			(!pricePeople.isEmpty()? Double.valueOf(pricePeople):0);
		purchaseDetails.setTotalPeople
			(!totalPeople.isEmpty()? Double.valueOf(totalPeople):0);
		purchaseDetails.setPricePharmacy
			(!pricePharmacy.isEmpty()?Double.valueOf(pricePharmacy):0);
		purchaseDetails.setTotalPharmacy
			(!totalPharmacy.isEmpty()?Double.valueOf(totalPharmacy):0);

		
		MyUtils.<PurchaseDetails>validateModel(purchaseDetails, errors);
		if(!errors.isEmpty()){
			MyUtils.showValidationErrors(errors);
			return;
		}

		
		//do the insertion 
		//Note(walid): the insertion method returns the generated index if sucesss and 0 if falied;
		
		long insertedId= this.purchaseDetailsService
			.insertPurchaseDetails(purchaseDetails);
		if (insertedId != 0) {
			purchaseDetails.setId(insertedId);
			this.purchasesDetailsTableView
				.getItems().add(purchaseDetails);

			if(this.insertBalanceTreat(purchaseDetails)){

			}else  {
				//TODO(walid): handle errors;
			}


		} else {
			Alert alert= new Alert(Alert.AlertType.ERROR);
			alert.show();
		}
			
	}


	@FXML
	public void editPurchaseDetails() throws IOException, SQLException {
		Stage stage= new Stage();
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(getClass()
				.getResource("/fxml/editPurchaseDetails.fxml"));
		EditPurchaseDetailsController editPurchaseDetails=
				new EditPurchaseDetailsController
			(this.getCurrentSelectedPurchaseDetailsId());
		editPurchaseDetails.setStage(stage);
		loader.setController(editPurchaseDetails);
		Parent root= loader.load();
		stage.setScene(new Scene(root,400, 680 ));
		stage.showAndWait();
		//NOTE(walid): a work around to update the table after edit;
		this.reInitalizeTableview();
	}

	private long getCurrentSelectedPurchaseDetailsId() {
		return ((PurchaseDetails)
			this.purchasesDetailsTableView.getSelectionModel()
			.getSelectedItem()
			).getId();
	}
	
	@FXML
	public void closeWindow(){
		this.stage.close();
	}

	@FXML
	public void deletePurchaseDetails() throws SQLException{
		long id= getCurrentSelectedPurchaseDetailsId();
		if(this.purchaseDetailsService.deletePurchaseDetailsById(id)) {
			this.purchasesDetailsTableView.getItems()
				.remove(this.purchasesDetailsTableView
					.getSelectionModel().getSelectedItem());
		}
	}

	public void setSelectedPurchaseId(long currentSelectedItemId)
	{
		this.currentPurchaseId= currentSelectedItemId;
	}


	private void reInitalizeTableview() throws SQLException {
		this.purchasesDetailsTableView.getColumns().clear();
		this.initalizeTableview();
	}

	
	
}
