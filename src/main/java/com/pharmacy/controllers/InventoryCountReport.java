package com.pharmacy.controllers;


import com.pharmacy.POGO.BalanceTreatWithInventoryCountDetails;
import com.pharmacy.POGO.InventoryCount;
import com.pharmacy.POGO.InventoryCountDetails;
import com.pharmacy.services.BalanceService;
import com.pharmacy.services.InventoryCountsService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InventoryCountReport extends MyController {
    
    private InventoryCount inventoryCount;
    private BalanceService balanceService;
    private InventoryCountsService inventoryCountsService;
    private Executor executor;
    @FXML
    private TableView inventoryCountReportTableView;
    
	
    private void initializeThreadPool() {
	this.executor= Executors.newCachedThreadPool((runnable)-> {
		Thread thread= new Thread(runnable);
		thread.setDaemon(true);
		return thread;
	    });
    }


    public InventoryCountReport() throws SQLException {
	this.initializeThreadPool();
	this.balanceService= new BalanceService();
	this.inventoryCountsService= new InventoryCountsService();

    }
    

    private void renderBalanceTreatWithInventoryCountDetails(long id) {
	Task<List<InventoryCountDetails>> task
	    = new Task<List<InventoryCountDetails>>() {
		    @Override
		    protected List<InventoryCountDetails>
			call() throws Exception {
			return InventoryCountReport
			.this.inventoryCountsService
			.findInventoryCountsDetails(id);
		    }
		};
	task.setOnSucceeded(e-> {
		this.inventoryCountReportTableView.setItems
		    (FXCollections.observableArrayList(task.getValue()));
	    });
	task.setOnFailed(e-> task.getException().printStackTrace());
	this.executor.execute(task);
    }

    
    private void initializeInventoryCountReportTableView() {
	this.inventoryCountReportTableView.getColumns().clear();
	this.renderBalanceTreatWithInventoryCountDetails(this.inventoryCount.getId());

	TableColumn<InventoryCountDetails, String> treatName=
	    new TableColumn<>("اسم المنتج");
	treatName.setCellValueFactory((rowItem)-> {
		String name= rowItem.getValue().getBalanceTreat().getTreat().getName();
		String type= rowItem.getValue().getBalanceTreat().getTreat().getTypeTreatName();
		return new SimpleStringProperty(name + "---" + type);
	    });

	TableColumn<InventoryCountDetails, String> afterQuantity=
	    new TableColumn<>("الكمية الفعلية");
	afterQuantity.setCellValueFactory(new PropertyValueFactory<>("actualQuantity"));


		TableColumn<InventoryCountDetails, String> status=
				new TableColumn<>("الحالة");
		status.setCellValueFactory(new PropertyValueFactory<>("status"));



		TableColumn<InventoryCountDetails, String> beforeQuantity=
	    new TableColumn<>(" الكمية على السيستم (ساعة الجرد)");
	beforeQuantity.setCellValueFactory(new PropertyValueFactory<>("systemQuantity"));
	
	
	TableColumn<InventoryCountDetails, String> dateIn=
	    new TableColumn<>("التاريخ");
	dateIn.setCellValueFactory(new PropertyValueFactory<>("dateIn"));

	this.inventoryCountReportTableView.getColumns()
	    .addAll(treatName,beforeQuantity,afterQuantity, status,dateIn);

    }

    
    @FXML
    private void initialize() {
	this.initializeInventoryCountReportTableView();
    }


    public void setInvnetoryCount(InventoryCount inventoryCount) {

    	this.inventoryCount= inventoryCount;
    }

    
}
