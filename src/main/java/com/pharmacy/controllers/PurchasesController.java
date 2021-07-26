package com.pharmacy.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.pharmacy.POGO.Purchase;
import com.pharmacy.services.PurchasesService;
import javafx.stage.Stage;

public class PurchasesController extends MyController{


	private PurchasesService purchasesService;

	private long currentSelectedItemId;

	@FXML
	private TableView purchasesTableView;
	
	
	public PurchasesController() throws SQLException{
		this.purchasesService = new PurchasesService();
	}

	private void initializeTableView() throws SQLException{
		ObservableList<Purchase> purchases= FXCollections
			.observableArrayList(this.purchasesService.getAllPurchases());

		TableColumn<Purchase, String> datePur = new TableColumn<>("تاريخ الفاتورة");
		datePur.setCellValueFactory(new PropertyValueFactory<>("datePur"));

		TableColumn<Purchase, String> pillNum = new TableColumn<>("رقم الفاتورة");
		pillNum.setCellValueFactory(new PropertyValueFactory<>("pillNum"));

		TableColumn<Purchase, String> totalPeople = new TableColumn<>("السعر للناس");
		totalPeople.setCellValueFactory(new PropertyValueFactory<>("totalPeople"));

		TableColumn<Purchase, String> totalPharmacy = new TableColumn<>("السعر للصيدلية");
		totalPharmacy.setCellValueFactory(new PropertyValueFactory<>("totalPharmacy"));

		TableColumn<Purchase, String> countUnit = new TableColumn<>("عدد الوحدات المتضمنة");
		countUnit.setCellValueFactory(new PropertyValueFactory<>("countUnit"));

		TableColumn<Purchase, String> profit = new TableColumn<>("الربح");
		profit.setCellValueFactory(new PropertyValueFactory<>("profit"));

		TableColumn<Purchase, String> description = new TableColumn<>("الوصف");
		description.setCellValueFactory(new PropertyValueFactory<>("description"));

		TableColumn<Purchase, String> dateAt = new TableColumn<>("تاريخ الإضافة");
		dateAt.setCellValueFactory(new PropertyValueFactory<>("dateAt"));

		TableColumn<Purchase, String> supplierName = new TableColumn<>("اسم المورد");
		// supplierName.setCellValueFactory(new PropertyValueFactory<>("supplier"));
		supplierName.setCellValueFactory(tf->new SimpleStringProperty(tf.getValue().getSupplier().getName()));

		this.purchasesTableView.getColumns().addAll( datePur,
							     pillNum,
							     totalPeople,
							     totalPharmacy,
							     countUnit,
							     profit,
							     description,
							     dateAt,
							     supplierName);
		this.purchasesTableView.setItems(purchases);

		purchasesTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				PurchasesController.this.setPurchaseId(((Purchase)newValue).getId());
			}
		});
	}

	
	
	@FXML
	private void initialize() throws SQLException {
		this.purchasesTableView.setRowFactory( tv->  {
				TableRow<Purchase> row= new TableRow<>();
				row.setOnMouseClicked(event -> {
						if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
							try {
								this.showPurchaseDetails();
							} catch (SQLException|IOException e){}
						}
					});
				return row ;
			});
		this.initializeTableView();
		
	}

	
	@FXML
	@Override
	protected  void backToControlPanel() throws IOException {
		this.swapWithControlPanelScene();
	}

	private void setPurchaseId(long id) {
		this.currentSelectedItemId= id;
	}
	private long getPurchaseId() {
		return this.currentSelectedItemId;
	}

	@FXML
	public void showAsddNewPurchase() throws IOException, SQLException {
		Stage stage= new Stage();
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(getClass()
				   .getResource("/fxml/createPurchase.fxml"));
		CreatePurchaseController createPurchaseController=
			new CreatePurchaseController();
		createPurchaseController.setStage(stage);
		loader.setController(createPurchaseController);
		Parent root= loader.load();
		stage.setScene(new Scene(root));
		stage.show();
	}


	@FXML
	public void showPurchaseDetails() throws IOException, SQLException {
		Stage stage= new Stage();
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(getClass()
				.getResource("/fxml/PurchaseDetails.fxml"));
		PurchaseDetailsController purchaseDetailsController=
				new PurchaseDetailsController();
		purchaseDetailsController.setStage(stage);
		purchaseDetailsController.setSelectedPurchaseId(this.currentSelectedItemId);
		loader.setController(purchaseDetailsController);
		Parent root= loader.load();
		stage.setScene(new Scene(root));
		stage.show();
	}
	
}
