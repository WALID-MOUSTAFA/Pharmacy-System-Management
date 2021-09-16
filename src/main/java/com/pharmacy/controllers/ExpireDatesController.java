package com.pharmacy.controllers;

import com.pharmacy.POGO.BalanceTreat;
import com.pharmacy.services.BalanceService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExpireDatesController extends MyController {

	private Executor executor;
	private BalanceService balanceService;
	@FXML
	private TreeView treeView;
	
	private void initializeThreadPool() {
		this.executor= Executors.newCachedThreadPool((runnable)-> {
				Thread thread= new Thread(runnable);
				thread.setDaemon(true);
				return thread;
			});
	}
	

	public ExpireDatesController() throws SQLException {
		this.initializeThreadPool();
		this.balanceService= new BalanceService();
	}


	

	private void renderBalanceTreats
	(String year) {
	Task<List<BalanceTreat>> task
	    = new Task<List<BalanceTreat>>() {
		    @Override
		    protected List<BalanceTreat>
			call() throws Exception {
			return ExpireDatesController
			.this.balanceService
			.getAlmostExpiredTreatments(year);
		    }
		};
	task.setOnSucceeded(e-> {
			ExpireDatesController.this.renderTreeView
				(task.getValue());
		});
	task.setOnFailed(e-> task.getException().printStackTrace());
	this.executor.execute(task);
       }


	private void renderTreeView(List<BalanceTreat> balances) {
		TreeItem<String> rootItem= new TreeItem<>(this.getCurrentYear());

		String[] months= {"يناير",
				"فبراير", "مارس"
				, "إبريل"
				, "مايو",
				"يونيو",
				"يوليو",
				"أغسطس",
				"سبتمبر",
				"أكتوبر",
				"نوفمبر",
				"ديسمبر"};
		for(String s: months) {
			rootItem.getChildren().addAll(new TreeItem<>(s));
		}


		for(BalanceTreat b : balances) {
			String expire=b.getExpireDate();
			String value= b.getTreat().getName() + " >> " + b.getExpireDate().split(" ")[0]+ " >> " +b.getTreat().getTypeTreatName() ;
			Timestamp ts= Timestamp.valueOf(expire);
			Calendar cal= Calendar.getInstance();
			cal.setTime(ts);
			int month= cal.get(Calendar.MONTH);
			TreeItem<String> tmp= new TreeItem<>(value);
			rootItem.getChildren().get(month).getChildren().add(tmp);
		}


		this.treeView.setRoot(rootItem);

	}
			
	@FXML
	private void initialize() {
		this.renderBalanceTreats(this.getCurrentYear());
	}


	
	
	private String getCurrentYear() {
		return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
	}

	public void showStage(String title) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/ExpireDates.fxml"));
		Stage stage = new Stage();
		this.setStage(stage);
		loader.setController(this);
		Parent root = loader.<VBox>load();
		stage.setScene(new Scene(root, 800, 600));
		stage.setTitle(title);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();

	}

	@FXML
	private void doSearch() {
		
	}
	
}
