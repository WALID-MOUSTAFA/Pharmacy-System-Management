package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.InventoryCount;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import com.pharmacy.services.InventoryCountsService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InventoryCountsController extends MyController{

    private Executor executor;
    private InventoryCountsService inventoryCountsService;
    private InventoryCount currentSelectedInventoryCount;
    @FXML private TableView inventoryCountsTableView;

	
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
	
    @FXML
    public void initialize() throws SQLException{
	this.initializeInventoryCountsTableView();
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
	dateInColumn.setCellValueFactory(new PropertyValueFactory("dateIn"));
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
	if(this.inventoryCountsService.insertInventoryCount(inventoryCount) > 0){
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
	stage.setTitle("جرد" + this.currentSelectedInventoryCount.getId());
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
	stage.setTitle("تقرير عن جرد" + this.currentSelectedInventoryCount.getDateIn());
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

    
}
