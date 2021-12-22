package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.BalanceTreatWithInventoryCountDetails;
import com.pharmacy.POGO.InventoryCount;
import com.pharmacy.POGO.InventoryCountDetails;
import com.pharmacy.POGO.TypeTreat;
import com.pharmacy.services.InventoryCountsService;
import com.pharmacy.services.TreatmentService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

import com.pharmacy.services.BalanceService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.transform.Scale;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import oshi.jna.platform.mac.SystemB;


class DoingInventoryCountController extends MyController {

	private boolean includeEmpty= false;
    private Executor executor;
    private ExecutorService executorService;
    private InventoryCount inventoryCount;
    private BalanceService balanceService;
    private InventoryCountsService inventoryCountsService;
    private BalanceTreatWithInventoryCountDetails currentSelectedBalance;
    @FXML private TableView inventoryBalancesTableView;
    @FXML private TextField searchBox;
	@FXML private Button deleteBalanceButton;

    
    private void initializeThreadPool() {
    	this.executorService=  Executors.newFixedThreadPool(4);

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
				return getAllBalanceTreatWithInventoryCountDetailsList(id, includeEmpty);
			}
		};
	task.setOnSucceeded(e-> {
		this.inventoryBalancesTableView.setItems
		    (FXCollections.observableArrayList(task.getValue()));
	    });
	task.setOnFailed(e-> task.getException().printStackTrace());
	this.executorService.execute(task);

	}

	private List<BalanceTreatWithInventoryCountDetails> getAllBalanceTreatWithInventoryCountDetailsList(long id, boolean includeEmpty) throws SQLException {
		return DoingInventoryCountController
		.this.balanceService
		.findAllBalanceTreatWithInventoryCountDetails
		(id, includeEmpty);
	}

	@FXML
    private void initialize() throws SQLException {
	    this.initializeInventoryBalancesTableView(includeEmpty);
	    this.renderTypesInTypeFilter();
	    this.deleteBalanceButton.disableProperty().bind(Bindings.isEmpty(this.inventoryBalancesTableView.getSelectionModel().getSelectedItems()));
    }


	@FXML
	private void renderTypesInTypeFilter() throws SQLException{
		TreatmentService treatmentSrevice= new TreatmentService();
		
		Task<List<TypeTreat>> task
			= new Task<List<TypeTreat>>() {
					@Override
					protected List<TypeTreat> call() throws Exception {
						return treatmentSrevice.getAllTreatTypes();
					}
				};
		
		task.setOnSucceeded(e-> {
				List<TypeTreat> types= task.getValue();
				for(TypeTreat t : types) {
					this.typeFilter.getItems().add(t.getTypename());
				}
			});
		task.setOnFailed(e-> task.getException().printStackTrace());
		this.executor.execute(task);

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
	this.includeEmpty= true;
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
	stage.initModality(Modality.APPLICATION_MODAL);
	stage.showAndWait();
	this.initializeInventoryBalancesTableView(false);
    }
	

	

    @FXML
	private void deleteBalance() throws SQLException {
		if(!MyUtils.ALERT_CONFIRM("هل أنت متأكد من حذف هذا الرصيد؟"))  {
			return;
		}
    	if(balanceService.deleteBalanceTreat(((BalanceTreatWithInventoryCountDetails)this.inventoryBalancesTableView.getSelectionModel().getSelectedItem()).getId())){
			MyUtils.ALERT_SUCCESS("تم حذف الرصيد بنجاح.");
			this.initializeInventoryBalancesTableView(this.includeEmpty);
		} else {
    		MyUtils.ALERT_ERROR("تعذر حذف الرصيد");
		}
	}
	


	@FXML
	private void printTableView() throws SQLException,
					     ClassNotFoundException,
					     URISyntaxException, JRException {
		List<BalanceTreatWithInventoryCountDetails> items=
			this.inventoryBalancesTableView.getItems();
		PrintInventory printInventory= new PrintInventory();
		printInventory.showReport(items);
	}


	
	@FXML
	private void doSearch() throws SQLException{
		this.inventoryBalancesTableView.setItems(FXCollections.observableArrayList
				(this.getAllBalanceTreatWithInventoryCountDetailsList(this.inventoryCount.getId(), includeEmpty)));
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
			(new Predicate<BalanceTreatWithInventoryCountDetails>()
			 {
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

	
	@Override
	protected synchronized void onFilterChange(Filter filter){

		//NOTE(walid): restore all items before applying the filter, this can be expensive!!!;
		//another apprach is to use static variable to hold the real items;
		try {
			this.inventoryBalancesTableView.setItems(FXCollections.observableArrayList
								 (this.getAllBalanceTreatWithInventoryCountDetailsList(this.inventoryCount.getId(), includeEmpty)));
		} catch (SQLException throwables) {
				throwables.printStackTrace();
		}

		switch(filter) {
		case TYPE: {
			String type= this.typeFilter.getValue();

			ObservableList<BalanceTreatWithInventoryCountDetails> items=
					this.inventoryBalancesTableView.getItems();
			FilteredList<BalanceTreatWithInventoryCountDetails> filteredList=
					new FilteredList<>(items);
			this.inventoryBalancesTableView.setItems(filteredList);
			filteredList.setPredicate
					(new Predicate<BalanceTreatWithInventoryCountDetails>()
					{
						@Override
						public boolean test(BalanceTreatWithInventoryCountDetails p) {
							return p.getTreat().getTypeTreatName().equals(type);
						}
					});
		};
		default: return;
			
		}
	}

	@Override
	protected void deleteFilter() throws SQLException{
    	this.initializeInventoryBalancesTableView(false);
	}

}

