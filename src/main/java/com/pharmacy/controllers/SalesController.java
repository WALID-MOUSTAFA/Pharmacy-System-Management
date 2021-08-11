package com.pharmacy.controllers;

import com.pharmacy.POGO.BalanceTreat;
import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.POGO.Treatment;
import com.pharmacy.POGO.TypeTreat;
import com.pharmacy.services.BalanceService;
import com.sun.glass.ui.Cursor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import com.pharmacy.services.TreatmentService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalesController {

	BalanceService balanceService;
	TreatmentService treatmentService;

	@FXML private TextField treatName;
	@FXML private ComboBox treatType;
	@FXML private ListView searchResult;
	@FXML private TableView	relatedBalancesTableView;
	
	//the detials of the balance treat;
	@FXML private Text treatNameDetails;
	@FXML private Text treatTypeDetails;
	@FXML private Text treatPriceDetails;
	@FXML private Text treatQuantityDetials;
	
	

	public void initializeTreatTypeCombo() throws SQLException {
		TreatmentService treatmentService= new TreatmentService();
		List<TypeTreat> types= treatmentService.getAllTreatTypes();
		for (TypeTreat t : types) {
			this.treatType.getItems().add(t.typename);
		}
	}

	@FXML
	private void treatTypeOnActionHandler(ActionEvent e) throws SQLException {
		String typeName= ((ComboBox)e.getSource()).getValue().toString();
		this.searchForTreat(this.treatName.getText(), typeName);
	}

	private void searchForTreat(String treatNameText, String typeName) throws SQLException {
		String treatName= this.treatName.getText();
		String treatType= this.treatType.getValue().toString();
		if(treatName.isEmpty()) {
			return;
		}
		List<DetailedTreatment> results= treatmentService.getAllTreatmentsByNameAndType(treatName, treatType);
		if(results == null){
			this.clearSearchResult();
		} else {
			this.clearSearchResult();
			this.renderResults(results);
		}

	}

	private void renderResults(List<DetailedTreatment> results) {
		ObservableList<DetailedTreatment> bs= FXCollections.observableArrayList(results);
		this.searchResult.setItems(bs);
		this.searchResult.setCellFactory((lv)->{
			return new ListCell<DetailedTreatment>(){
				@Override
				protected void updateItem(DetailedTreatment item, boolean empty) {
					super.updateItem(item, empty);
					setText(empty? null : item.getName() + " ---- " + item.getTypeTreatName());
				}
			};
		});
	}

	private void initializeRelatedBalanceTableView(long treatId) throws SQLException {
		this.relatedBalancesTableView.getColumns().clear();

		//add double click listener;

		List<BalanceTreat> balances = this.balanceService.getBalanceTreatbyId(treatId);
		if(balances == null) {
			balances = new ArrayList<>();
		}
		ObservableList<BalanceTreat> balancesObservableList= FXCollections.observableArrayList
				(balances);

		TableColumn<BalanceTreat, Double> quantityColumn= new TableColumn<>("الكمية");
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		TableColumn<BalanceTreat, Double> priceColumn= new TableColumn<>("السعر");
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
		TableColumn<BalanceTreat, String> expireDateColumn= new TableColumn<>("تاريخ الصلاحية");
		expireDateColumn.setCellValueFactory(new PropertyValueFactory<>("expireDate"));


		this.relatedBalancesTableView.setItems(balancesObservableList);
		this.relatedBalancesTableView.getColumns().addAll(quantityColumn,
								  priceColumn,
								  expireDateColumn);
		

	}

	public void clearSearchResult() {
		this.searchResult.getItems().clear();
	}

	public void addResultToResultList(String itemLabel) {
		this.clearSearchResult();
		HBox hBox= new HBox(new Text(itemLabel));
		this.searchResult.getItems().add(hBox);
	}

	public SalesController() throws SQLException{
			this.balanceService= new BalanceService();
			this.treatmentService= new TreatmentService();
	}

	


	
	private void addTableviewRowDoubleClickListener() {
		this.relatedBalancesTableView.setRowFactory
			( tv-> {
			  TableRow<BalanceTreat> row= new TableRow<>();
			 row.setOnMouseClicked(event -> {
			    if (event.getClickCount() == 2 &&(!row.isEmpty()) ) {
			       try {
			       		System.out.println("clicked");
				       this.showBalanceTreatDetails(row.getItem());
					} catch (Exception e){}
						}
					});
				return row ;
			});
	}
	
	private void showBalanceTreatDetails(BalanceTreat balanceTreat) {
		this.treatTypeDetails.setText(balanceTreat.getTreat().getName());
	}

	

	private void addSearchResultDoubleClickListener() {
		this.searchResult.setOnMouseClicked((ev)->{
			if (ev.getClickCount() ==2){
				try {
					this.initializeRelatedBalanceTableView
							(((DetailedTreatment) this.searchResult.getSelectionModel().getSelectedItem()).getId());
				}catch (SQLException e) {

				}

			}
			});
	}



	@FXML
	public void initialize() throws SQLException{
		this.initializeTreatTypeCombo();
		this.addSearchResultDoubleClickListener();
		this.addTableviewRowDoubleClickListener();
	}

}
