package com.pharmacy.controllers;

import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.POGO.Treatment;
import com.pharmacy.services.TreatmentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.pharmacy.DatabaseConnection;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;

import java.io.IOException;
import java.sql.SQLException;

public class TreatmentController extends MyController{


	private long currentTreatmentId;
	
	@FXML
	private Button backToControlPanelButton;
	@FXML
	private TableView treatmentsTableView;
	@FXML
	private Button editTreatmentButton;
	@FXML
	private Button deleteTreatmentButton;
	
	// @FXML
	// private Text detailedTreatment;

	private TreatmentService treatmentService;

	public TreatmentController() {
		this.treatmentService= new TreatmentService();
	}

	private void initializeTableView() throws SQLException{
		ObservableList<Treatment> treatments= FXCollections
			.observableArrayList(treatmentService.getAllTreatments());

		TableColumn<Treatment, String> nameColumn=
			new TableColumn<>("الاسم");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Treatment, String> dateAtColumn=
			new TableColumn<>("تاريخ الإضافة");
		dateAtColumn.setCellValueFactory
			(new PropertyValueFactory<>("dateAt"));

		TableColumn<Treatment, String> statusColumn=
			new TableColumn<>("الحالة");
		statusColumn.setCellValueFactory
			(new PropertyValueFactory<>("status"));

		TableColumn<Treatment, String> placeColumn=
			new TableColumn<>("المكان");
		placeColumn.setCellValueFactory
			(new PropertyValueFactory<>("place"));

		TableColumn<Treatment, String> lowCountColumn=
			new TableColumn<>("العدد الحرج");
		lowCountColumn.setCellValueFactory
			(new PropertyValueFactory<>("lowcount"));

		TableColumn<Treatment, String> companyColumn=
			new TableColumn<>("الشركة");
		companyColumn.setCellValueFactory
			(new PropertyValueFactory<>("company"));

		TableColumn<Treatment, String> quantityColumn=
			new TableColumn<>("الكمية المتوفرة حالياً");
		quantityColumn.setCellValueFactory
			(new PropertyValueFactory<>("quantity"));

	
		this.treatmentsTableView.getColumns().addAll(nameColumn,
							     dateAtColumn,
							     placeColumn,
							     companyColumn,
							     quantityColumn);
		this.treatmentsTableView.setItems(treatments);

		this.treatmentsTableView.getSelectionModel()
			.selectedItemProperty()
			.addListener((obsvaal, oldval, newval)-> {
					this.setCurrentTreatmentId
						(((Treatment)newval).getId());
					System.out.println(((Treatment)newval).getId());
				
				});

	}

	public void addTableViewFocusListeners() {

		this.treatmentsTableView.focusedProperty()
			.addListener((observableVal,oldval,newval)-> {
		if(newval) {
		    this.editTreatmentButton.setDisable(false);
		    this.deleteTreatmentButton.setDisable(false);
		} else {
		    this.editTreatmentButton.setDisable(true);
		    this.deleteTreatmentButton.setDisable(true);
		}
	    });
	}

	
    // private void addTableviewRowDoubleClickListener() {
    // 	this.purchasesTableView.setRowFactory( tv->  {
    // 		TableRow<Purchase> row= new TableRow<>();
    // 		row.setOnMouseClicked(event -> {
    // 			if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
    // 			    try {
    // 				this.showPurchaseDetails();
    // 			    } catch (SQLException|IOException e){}
    // 			}
    // 		    });
    // 		return row ;
    // 	    });
    // }



	private void setCurrentTreatmentId(long id){
		this.currentTreatmentId= id;
	}
	
	@FXML
	private void initialize() throws SQLException {
		this.initializeTableView();
		this.addTableViewFocusListeners();
	}

	@FXML
	protected  void backToControlPanel() throws IOException {
		this.swapWithControlPanelScene();
	}


	@FXML
	private void editTreatment() throws IOException, SQLException{
		Stage stage= new Stage();
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation
			(getClass().getResource("/fxml/editTreatment.fxml"));
		EditTreatmentController editTreatmentController=
			new EditTreatmentController();
		editTreatmentController.setStage(stage);
		editTreatmentController.setId(this.currentTreatmentId);
		loader.setController(editTreatmentController);
		Parent root= loader.load();
		stage.setScene(new Scene(root));

		stage.showAndWait();
		this.initializeTableView();

	}

	
	@FXML
	private void deleteTreatment(){
	}

	
	
	@FXML
	private void viewDetailedTreatment() throws  SQLException{}
	

	@FXML
	private void showAsddNewTreatment() throws IOException, SQLException {
		Stage stage= new Stage();
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation
			(getClass().getResource("/fxml/CreateTreatment.fxml"));
		CreateTreatmentController createTreatmentController=
			new CreateTreatmentController();
		createTreatmentController.setStage(stage);
		loader.setController(createTreatmentController);
		Parent root= loader.load();
		stage.setScene(new Scene(root));
		stage.showAndWait();
		this.initializeTableView();
	}

}
