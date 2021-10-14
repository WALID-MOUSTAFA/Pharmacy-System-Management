package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.BalanceTreatWithInventoryCountDetails;
import com.pharmacy.POGO.InventoryCount;
import com.pharmacy.POGO.InventoryCountDetails;
import com.pharmacy.YearMonthFilterable;
import com.pharmacy.services.InventoryCountsService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

import com.pharmacy.services.BalanceService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;


class DoingInventoryCountController extends MyController {

    private Executor executor;
    private InventoryCount inventoryCount;
    private BalanceService balanceService;
    private InventoryCountsService inventoryCountsService;
    private BalanceTreatWithInventoryCountDetails currentSelectedBalance;
    @FXML private TableView inventoryBalancesTableView;
    @FXML private TextField searchBox;
	@FXML private Button deleteBalanceButton;

    
    private void initializeThreadPool() {
	this.executor= Executors.newCachedThreadPool((runnable)-> {
		Thread thread= new Thread(runnable);
		thread.setDaemon(true);
		return thread;
	    });
    }

    public DoingInventoryCountController() throws SQLException {
	this.initializeThreadPool();
	this.balanceService= new BalanceService();
	this.inventoryCountsService= new InventoryCountsService();
    }


    
    private void renderBalanceTreatWithInventoryCountDetails
	(long id, boolean includeEmpty) {
	Task<List<BalanceTreatWithInventoryCountDetails>> task
	    = new Task<List<BalanceTreatWithInventoryCountDetails>>() {
		    @Override
		    protected List<BalanceTreatWithInventoryCountDetails>
			call() throws Exception {
			return DoingInventoryCountController
			.this.balanceService
			.findAllBalanceTreatWithInventoryCountDetails
			(id, includeEmpty);
		    }
		};
	task.setOnSucceeded(e-> {
		this.inventoryBalancesTableView.setItems
		    (FXCollections.observableArrayList(task.getValue()));
	    });
	task.setOnFailed(e-> task.getException().printStackTrace());
	this.executor.execute(task);
    }

    @FXML
    private void initialize() throws SQLException {
		this.initializeInventoryBalancesTableView(false);
    }

    

    private void initializeInventoryBalancesTableView(boolean includeEmpty)
	throws SQLException {
	
	this.inventoryBalancesTableView.getColumns().clear();
	this.renderBalanceTreatWithInventoryCountDetails
	    (this.inventoryCount.getId(), includeEmpty);
	this.inventoryBalancesTableView.setEditable(true);

	TableColumn<BalanceTreatWithInventoryCountDetails, String> treatName=
	    new TableColumn<>("اسم المنتج");
	treatName.setCellValueFactory((rowItem)-> {
		String name= rowItem.getValue().getTreat().getName();
		String type= rowItem.getValue().getTreat().getTypeTreatName();
		return new SimpleStringProperty(name + "---" + type);
	    });

	
	
	TableColumn<BalanceTreatWithInventoryCountDetails, String> expire=
	    new TableColumn<>("تاريخ الصلاحية");
	expire.setCellValueFactory((rowItem)-> {
			return new SimpleStringProperty(rowItem.getValue().getExpireDate());
	    });
	

	
	TableColumn<BalanceTreatWithInventoryCountDetails, String> afterQuantity=
	    new TableColumn<>("الكمية الفعلية");
	afterQuantity.setCellValueFactory((rowItem)-> {
		return rowItem.getValue().getAfterQuantity() == 0?
		    new SimpleStringProperty("0") :
		    new SimpleStringProperty
		    (String.valueOf(rowItem.getValue().getAfterQuantity()));
	    });

	TableColumn<BalanceTreatWithInventoryCountDetails, String> beforeQuantity=
	    new TableColumn<>("الكمية على السيستم");
	beforeQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

	afterQuantity.setCellFactory(TextFieldTableCell.forTableColumn());
	afterQuantity.setOnEditCommit((event)-> {
		
		if(event.getNewValue() == null ) {return; }
		long countId= this.currentSelectedBalance.getCountId();
		event.getTableView().getSelectionModel()
		    .getSelectedItem().setAfterQuantity
		    (Integer.parseInt(event.getNewValue()));
		if(countId == 0) {
		    InventoryCountDetails inventoryCountDetails=
			new InventoryCountDetails();
		    inventoryCountDetails.setBalanceId
			(this.currentSelectedBalance.getId());
		    inventoryCountDetails.setInventoryCountsId
			(this.inventoryCount.getId());
		    inventoryCountDetails.setSystemQuantity
			((int) this.currentSelectedBalance.getQuantity());
		    inventoryCountDetails.setActualQuantity
			(Integer.parseInt(event.getNewValue()));
		    inventoryCountDetails.setStatus
			(Integer.parseInt(event.getNewValue())
			 - (int)this.currentSelectedBalance.getQuantity());
		    try {
			long result = this.inventoryCountsService
			    .insertInventoryCountDetails
			    (inventoryCountDetails);
			this.initializeInventoryBalancesTableView(false);

		    } catch (SQLException e) {e.printStackTrace();}
		} else {
		    InventoryCountDetails inventoryCountDetails=
			new InventoryCountDetails();
		    inventoryCountDetails.setId
			(this.currentSelectedBalance.getCountDetailsId());
		    inventoryCountDetails.setSystemQuantity
			((int) this.currentSelectedBalance.getQuantity());
		    inventoryCountDetails.setActualQuantity
			(Integer.parseInt(event.getNewValue()));
		    inventoryCountDetails.setStatus
			(Integer.parseInt(event.getNewValue())
			 - (int)this.currentSelectedBalance.getQuantity());
		    try {
			long result = this.inventoryCountsService
			    .updateInventoryCountDetails
			    (inventoryCountDetails);
			this.initializeInventoryBalancesTableView(false);
		    } catch (SQLException e) {e.printStackTrace();}
		}
	    });


	TableColumn<BalanceTreatWithInventoryCountDetails, String> isCounted=
	    new TableColumn<>("تم الجرد؟");
	isCounted.setCellValueFactory((rowItem)-> {
		if(rowItem.getValue().getCountId() == 0) {
		    return new SimpleStringProperty("لم يتم الجرد");
		} else {
		    return new SimpleStringProperty("تم");
		}
	    });
	
	
	inventoryBalancesTableView.setRowFactory(tv->
		  new TableRow<BalanceTreatWithInventoryCountDetails>(){
		      @Override
		      protected void updateItem
			  (BalanceTreatWithInventoryCountDetails item,
			   boolean empty) {
			  super.updateItem(item, empty);
			  if(item != null && !empty) {
			      if (item.getCountId() != 0) {
				 // setStyle("-fx-background-color: lightgreen");
			    	return;
			      }
			  }
		      }
		  });


	inventoryBalancesTableView.getSelectionModel().selectedItemProperty()
	    .addListener((observable, oldValue, newValue)->{
		    this.currentSelectedBalance=
			(BalanceTreatWithInventoryCountDetails) newValue;
		});
	
	this.inventoryBalancesTableView.getColumns()
		.addAll(treatName,expire, beforeQuantity,afterQuantity, isCounted);
    }



    public void setInvnetoryCount(InventoryCount inventoryCount) {
	this.inventoryCount= inventoryCount;
    }



    @FXML
    private void switchToIncludeEmpty() throws SQLException{
	this.initializeInventoryBalancesTableView(true);
    }


    
    @FXML
    private void showUpdateBalanceQuantityForm() throws IOException, SQLException{
	Stage stage= new Stage();
	FXMLLoader loader= new FXMLLoader();
	loader.setLocation
	    (getClass().getResource("/fxml/UpdateBalanceQuantity.fxml"));
	UpdateBalanceQuantityController updateBalanceQuantityController=
	    new UpdateBalanceQuantityController();
	updateBalanceQuantityController.setStage(stage);
	updateBalanceQuantityController.setBalance
	    (this.currentSelectedBalance);
	loader.setController(updateBalanceQuantityController);
	Parent root= loader.load();
	stage.setScene(new Scene(root));
	stage.initModality(Modality.WINDOW_MODAL);
	stage.showAndWait();
	this.initializeInventoryBalancesTableView(false);
    }
	

    
    @FXML
    private void doSearch() throws SQLException{
    	String q= this.searchBox.getText();
    	if(q.isEmpty()) {
	    initializeInventoryBalancesTableView(false);
	    return;
	}
    	ObservableList<BalanceTreatWithInventoryCountDetails> items=
	    this.inventoryBalancesTableView.getItems();
	FilteredList<BalanceTreatWithInventoryCountDetails> filteredList=
	    new FilteredList<>(items);
	this.inventoryBalancesTableView.setItems(filteredList);

	filteredList.setPredicate
	    (new Predicate<BalanceTreatWithInventoryCountDetails>() {
		@Override
		public boolean test(BalanceTreatWithInventoryCountDetails p) {
		    return String.valueOf(p.getBeforeQuantity()).contains(q)
			|| p.getTreat().getParcode().contains(q)
			|| p.getTreat().getName().contains(q)
			|| p.getTreat().getTypeTreatName().contains(q)
			|| p.getExpireDate().contains(q);
					

		}
	    });
    }

    @FXML
	private void deleteBalance() throws SQLException {
		if(!MyUtils.ALERT_CONFIRM("هل أنت متأكد من حذف هذا الرصيد؟"))  {
			return;
		}
    	if(balanceService.deleteBalanceTreat(((BalanceTreatWithInventoryCountDetails)this.inventoryBalancesTableView.getSelectionModel().getSelectedItem()).getId())){
			MyUtils.ALERT_SUCCESS("تم حذف الرصيد بنجاح.");
		} else {
    		MyUtils.ALERT_ERROR("تعذر حذف الرصيد");
		}
	}


	@Override
	protected void onFilterChange(Filter filter){

	}
}
