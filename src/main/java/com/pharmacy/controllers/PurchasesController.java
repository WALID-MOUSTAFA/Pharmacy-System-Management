package com.pharmacy.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
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
		
	}

	
	
	@FXML
	private void initialize() throws SQLException {
		this.initializeTableView();

	}

	
	@FXML
	private void backToControlPanel() throws IOException {
		this.swapWithControlPanelScene();
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
	public void showPurchaseDetails() throws IOException {
		Stage stage= new Stage();
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(getClass()
				.getResource("/fxml/PurchaseDetails.fxml"));
		PurchaseDetailsController purchaseDetailsController=
				new PurchaseDetailsController();
		purchaseDetailsController.setStage(stage);
		loader.setController(purchaseDetailsController);
		Parent root= loader.load();
		stage.setScene(new Scene(root));
		stage.show();
	}
	
}
