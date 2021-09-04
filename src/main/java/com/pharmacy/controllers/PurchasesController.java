package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.PurchaseDetails;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.pharmacy.POGO.Purchase;
import com.pharmacy.services.PurchasesService;
import javafx.stage.Stage;

public class PurchasesController extends MyController{

	Executor exec;
    private PurchasesService purchasesService;

    private long currentSelectedItemId;

    @FXML private TableView purchasesTableView;
    @FXML private Button editPurchaseButton;
    @FXML private Button deletePurchaseButton;
	@FXML private Button showPurchaseDetailsButton;
	@FXML private TextField searchBox;


	public PurchasesController() throws SQLException{
		this.purchasesService = new PurchasesService();
    	this.exec= Executors.newCachedThreadPool((runnable)-> {
    		Thread thread= new Thread(runnable);
			thread.setDaemon(true);
			return thread;
		});
	}

	private void renderPurchases(){
		Task<List<Purchase>> task= new Task<List<Purchase>>() {
			@Override
			protected List<Purchase> call() throws Exception {
				return PurchasesController.this.purchasesService.getAllPurchases();
			}
		};
		task.setOnSucceeded(e-> {
			this.purchasesTableView.setItems(FXCollections.observableArrayList(task.getValue()));
		});
		task.setOnFailed(e->task.getException().printStackTrace());
		this.exec.execute(task);
	}



    private void initializeTableView() throws SQLException{

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

//	TableColumn<Purchase, String> profit = new TableColumn<>("الربح");
//	profit.setCellValueFactory(new PropertyValueFactory<>("profit"));

	TableColumn<Purchase, String> description = new TableColumn<>("الوصف");
	description.setCellValueFactory(new PropertyValueFactory<>("description"));

	TableColumn<Purchase, String> dateAt = new TableColumn<>("تاريخ الإضافة");
	dateAt.setCellValueFactory(new PropertyValueFactory<>("dateAt"));

	TableColumn<Purchase, String> supplierName = new TableColumn<>("اسم المورد");
	// supplierName.setCellValueFactory(new PropertyValueFactory<>("supplier"));
	supplierName.setCellValueFactory
	    (tf->new SimpleStringProperty(tf.getValue().getSupplier().getName()));

	this.purchasesTableView.getColumns().addAll( datePur,
						     pillNum,
						     totalPeople,
						     totalPharmacy,
						     countUnit,
						   //  profit,
						     description,
						     dateAt,
						     supplierName);

	this.renderPurchases();

	purchasesTableView.getSelectionModel()
		.selectedItemProperty().addListener(new ChangeListener() {
		@Override
		public void changed(ObservableValue observable,
				    Object oldValue, Object newValue) {
			PurchasesController.this.setPurchaseId
				(((Purchase)newValue).getId());
		}
	    });
    }



    public void addTableViewFocusListeners() {

	    this.purchasesTableView.focusedProperty()
		    .addListener((observableVal,oldval,newval)-> {
		if(newval) {
		    this.editPurchaseButton.setDisable(false);
		    this.deletePurchaseButton.setDisable(false);
		    this.showPurchaseDetailsButton.setDisable(false);

		} else {
		    this.editPurchaseButton.setDisable(true);
		    this.deletePurchaseButton.setDisable(true);
		    this.showPurchaseDetailsButton.setDisable(true);
		}
	    });
    }


    @FXML
    private void initialize() throws SQLException {
	this.initializeTableView();
	addTableViewFocusListeners();
	addTableviewRowDoubleClickListener();

	}

    private void addTableviewRowDoubleClickListener() {
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
			   .getResource("/fxml/CreatePurchase.fxml"));
	CreatePurchaseController createPurchaseController=
	    new CreatePurchaseController();
	createPurchaseController.setStage(stage);
	loader.setController(createPurchaseController);
	Parent root= loader.load();
	stage.setScene(new Scene(root));
	stage.showAndWait();
	this.reInitializePurchaseTableView();
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


    @FXML
    public void deletePurchase() throws SQLException{
	if(this.purchasesService.deletePurchase(this.currentSelectedItemId)){
	    Alert alert= new Alert(Alert.AlertType.INFORMATION);
	    alert.setHeaderText(null);
	    alert.setContentText("تم حذف الفاتورة بنجاح");
	    alert.show();
	    this.initializeTableView();
	}
	
    }

    @FXML
    public void editPurchase() throws SQLException, IOException{
	Stage stage= new Stage();
	FXMLLoader loader= new FXMLLoader();
	loader.setLocation(getClass()
			   .getResource("/fxml/editPurchase.fxml"));
	EditPurchaseController editPurchaseController=
	    new EditPurchaseController
	    (this.getCurrentSelectedPurchaseId());
	editPurchaseController.setStage(stage);
	loader.setController(editPurchaseController);
	Parent root= loader.load();
	stage.setScene(new Scene(root,400, 680 ));
	stage.showAndWait();
	//NOTE(walid): a work around to update the table after edit;

	this.reInitializePurchaseTableView();
    }

    //Note(walid): this method to return the current seleced id of
    //the item in tableview raw, although I set the listener on selecting
    //tableview raw, so I have now two solutions for the same problem.
    //later you have to choose between the two of them.
    private long getCurrentSelectedPurchaseId() {
	return ((Purchase)
		this.purchasesTableView.getSelectionModel()
		.getSelectedItem()
		).getId();
    }


	private  void reInitializePurchaseTableView() throws SQLException {
    	this.purchasesTableView.getColumns().clear();
    	this.initializeTableView();
	}
	
	
	@FXML
	private void doSearch() throws SQLException{
    	String q= this.searchBox.getText();
    	if(q.isEmpty()) {
    		reInitializePurchaseTableView();
    		return;
		}
    	ObservableList<Purchase> items= this.purchasesTableView.getItems();
		FilteredList<Purchase> filteredList= new FilteredList<>(items);
		this.purchasesTableView.setItems(filteredList);

		filteredList.setPredicate(new Predicate<Purchase>() {
			@Override
			public boolean test(Purchase purchase) {
				return purchase.getPillNum().contains(q)
						|| purchase.getDatePur().contains(q)
						|| purchase.getSupplier().getName().contains(q)
						|| purchase.getDatePur().contains(q)
						|| purchase.getDescription().contains(q)
						|| String.valueOf(purchase.getCountUnit()).contains(q)
						|| String.valueOf(purchase.getTotalPeople()).contains(q)
						|| String.valueOf(purchase.getTotalPharmacy()).contains(q)
						|| purchase.getDateAt().contains(q);

			}
		});
	}
}
