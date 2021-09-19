package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.POGO.InventoryCount;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.css.SimpleSelector;
import javafx.fxml.FXML;
import com.pharmacy.services.InventoryCountsService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

public class InventoryCountsController extends MyController{

    private Executor executor;
    private InventoryCountsService inventoryCountsService;
    private InventoryCount currentSelectedInventoryCount;
    @FXML private TableView inventoryCountsTableView;
	@FXML
	private Button addNewInventoryCountButton;
	@FXML
	private Button startInventoryCountButton;
	@FXML
	private Button inventoryCountReportButton;
	@FXML
	private Button deleteInventoryCountDeleteButton;

	@FXML
	TextField searchBox;

    private void initializeThreadPool() {
	this.executor= Executors.newCachedThreadPool((runnable)-> {
		Thread thread= new Thread(runnable);
		thread.setDaemon(true);
		return thread;
	    });
    }
	
    public InventoryCountsController() throws SQLException {
	this.inventoryCountsService= new InventoryCountsService();
	this.initializeThreadPool();
    }

	public void addTableViewFocusListeners() {
		this.deleteInventoryCountDeleteButton.disableProperty().bind(Bindings.isEmpty(this.inventoryCountsTableView.getSelectionModel().getSelectedItems()));
		this.startInventoryCountButton.disableProperty().bind(Bindings.isEmpty(this.inventoryCountsTableView.getSelectionModel().getSelectedItems()));
		this.inventoryCountReportButton.disableProperty().bind(Bindings.isEmpty(this.inventoryCountsTableView.getSelectionModel().getSelectedItems()));

	}

    @FXML
    public void initialize() throws SQLException{
	this.initializeInventoryCountsTableView();
	this.addTableViewFocusListeners();
	//this.addInventoryCountsTableviewRowDoubleClickListener();
    }


    private void renderInventoryCounts() {
	Task<List<InventoryCount>> task
	    = new Task<List<InventoryCount>>() {
		@Override
		protected List<InventoryCount> call() throws Exception {
		    return InventoryCountsController.this.inventoryCountsService
		    .findAllInventoryCounts();
		}
	    };
	task.setOnSucceeded(e-> {
		this.inventoryCountsTableView.setItems
		    (FXCollections.observableArrayList(task.getValue()));
	    });
	task.setOnFailed(e-> task.getException().printStackTrace());
	this.executor.execute(task);
    }
    

    
    public void initializeInventoryCountsTableView() {
	this.inventoryCountsTableView.getColumns().clear();
	this.renderInventoryCounts();
	TableColumn<InventoryCount, String> dateInColumn=
	    new TableColumn<>("تاريخ الجرد");
	dateInColumn.setCellValueFactory(param -> {
		return new SimpleStringProperty(param.getValue().getDateIn().split(" ")[0]);
	});
	this.inventoryCountsTableView.getColumns().add(dateInColumn);	

	//set current Inventory Count;
        this.inventoryCountsTableView.getSelectionModel().selectedItemProperty()
	    .addListener((observable, oldvalue, newvalue)->{
                    this.currentSelectedInventoryCount=(InventoryCount)newvalue;
                });
    }
	

    private void addInventoryCountsTableviewRowDoubleClickListener() {
	this.inventoryCountsTableView.setRowFactory
	    ( tv->  {
		TableRow<InventoryCount> row= new TableRow<>();
		row.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
			    try {
				this.showDoingInventoryCount();
			    } catch (IOException | SQLException e){}
			}
		    });
		return row ;
	    });
    }

    
    @FXML
    public void addNewInventoryCount() throws SQLException {
	InventoryCount inventoryCount= new InventoryCount();
		long result = this.inventoryCountsService.insertInventoryCount(inventoryCount);
		if(result > 0){
		inventoryCount.setId(result);
	    this.inventoryCountsTableView.getItems().add(inventoryCount);
	} else {
	    MyUtils.ALERT_ERROR("حدث خطأ ما أثناء إنشاء جرد جديد");
	}
    }




    private void showInventoryCountDetails() {
    }
    

    @FXML
    private void showDoingInventoryCount() throws IOException, SQLException {
	Stage stage= new Stage();
	stage.setTitle("جرد: " + this.currentSelectedInventoryCount.getDateIn().split(" ")[0]);
	FXMLLoader loader= new FXMLLoader();
	loader.setLocation
	    (getClass().getResource("/fxml/DoingInventoryCount.fxml"));
	DoingInventoryCountController doingInventoryCountController=
	    new DoingInventoryCountController();
	doingInventoryCountController.setStage(stage);
	doingInventoryCountController.setInvnetoryCount
	    (this.currentSelectedInventoryCount);
	loader.setController(doingInventoryCountController);
	Parent root= loader.load();
	stage.setScene(new Scene(root));
	stage.showAndWait();
    }


    
    @FXML
    private void showInventoryCountReport() throws IOException, SQLException {
	Stage stage= new Stage();
	stage.setTitle("تقرير عن جرد : " + this.currentSelectedInventoryCount.getDateIn().split(" ")[0]);
	FXMLLoader loader= new FXMLLoader();
	loader.setLocation
	    (getClass().getResource("/fxml/InventoryCountReport.fxml"));
	InventoryCountReport inventoryCountReport=
	    new InventoryCountReport();
	inventoryCountReport.setStage(stage);
	inventoryCountReport.setInvnetoryCount
	    (this.currentSelectedInventoryCount);
	loader.setController(inventoryCountReport);
	Parent root= loader.load();
	stage.setScene(new Scene(root));
	stage.showAndWait();
    }



    @FXML
	private void deleteInventoryCount() throws SQLException {
    	if(!MyUtils.ALERT_CONFIRM("هل تريد حذف هذا الجرد؟")) {
    		return;
		}
    	if(this.inventoryCountsService.deleteInventoryCount
				(this.currentSelectedInventoryCount.getId())){
    		this.inventoryCountsTableView.getItems().remove(this.currentSelectedInventoryCount);
    		MyUtils.ALERT_SUCCESS("تم حذف الجرد بنجاح.");
		} else {
    		MyUtils.ALERT_ERROR("حدث خطأ أثناء الحذف!");
		}
	}


	@FXML
	private void doSearch() throws SQLException {
		String q= this.searchBox.getText();
		if(q.isEmpty()) {
			this.initializeInventoryCountsTableView();
			return;
		}
		ObservableList<InventoryCount> items= this.inventoryCountsTableView.getItems();
		FilteredList<InventoryCount> filteredList= new FilteredList<>(items);
		this.inventoryCountsTableView.setItems(filteredList);
		filteredList.setPredicate(new Predicate<InventoryCount>() {
			@Override
			public boolean test(InventoryCount i) {
				return i.getDateIn().contains(q);
			}
		});
	}
}
