package com.pharmacy.controllers;

import com.pharmacy.POGO.BalanceTreat;
import com.pharmacy.services.BalanceService;
import javafx.collections.ObservableList;
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
import java.util.ArrayList;
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
			.getAllBalances();
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

		TreeItem<String> rootItem= new TreeItem<>("السنوات");
		List<TreeItem<String>> presentedYears= new ArrayList<>();

		
		String[] months= {
			"يناير",
			"فبراير"
			, "مارس"
			, "إبريل"
			, "مايو",
			"يونيو",
			"يوليو",
			"أغسطس",
			"سبتمبر",
			"أكتوبر",
			"نوفمبر",
			"ديسمبر"
		};

		//adding available years to the presented tree structure;
		for( String i : this.getAvailableYears(balances)) {
			presentedYears.add(new TreeItem<String>(i));
		}

		//adding months to presentedYears
		for(String m: months) {
			for (TreeItem<String> item : presentedYears) {
				item.getChildren().add(new TreeItem<String>(m));
			}
		}
		
		rootItem.getChildren().addAll(presentedYears.toArray
					      (new TreeItem[0]));
		for(BalanceTreat b : balances) {
			this.placeBalanceInsideTree(rootItem, b);
		}
		this.treeView.setRoot(rootItem);
	}


	private List<String> getAvailableYears(List<BalanceTreat> balances) {
		List<String> years= new ArrayList<>();
		for(BalanceTreat balance : balances) {
			String expireDate = balance.getExpireDate().split(" ")[0];
			String expireDateYear = expireDate.split("-")[0];
			if(years.contains(expireDateYear)) {
				continue;
			} else {
				years.add(expireDateYear);
			}
		}
		return years;
	}


	
	private void placeBalanceInsideTree(TreeItem<String> root,
					    BalanceTreat balance) {
		String expireDate;
		int expireDateYear, expireDateMonth;
		
		expireDate = balance.getExpireDate().split(" ")[0];
		expireDateYear = Integer.valueOf(expireDate.split("-")[0]);
		expireDateMonth = Integer.valueOf(expireDate.split("-")[1]) -1; //arrays is zero bases, while months starts from 1 in dates.

		ObservableList<TreeItem<String>> years= root.getChildren();
		for(TreeItem<String> i : years){
			if(i.getValue().equals(String.valueOf(expireDateYear))){
				String value= balance.getTreat().getName() +
					"           |           "
					+ balance.getTreat().getTypeTreatName()
					+ "           |           "
					+ balance.getPurchase().getPillNum();
				i.getChildren().get(expireDateMonth)
					.getChildren().add(new TreeItem<String>
							   (value));
			}
		}
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
