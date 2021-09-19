package com.pharmacy.controllers;


import com.pharmacy.POGO.BalanceTreat;
import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.services.BalanceService;
import com.pharmacy.services.TreatmentService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class SpecificTreatmentController extends MyController {

	private long id;
	private BalanceService balanceService;
	private TreatmentService treatmentService;

	@FXML private Text treatName;
	@FXML private Text treatType;
	@FXML private Text treatForm;
	@FXML private Text treatParcode;
	@FXML private Text treatDateAt;
	@FXML private Text treatLowcount;
	@FXML private Text treatCompany;
	@FXML private Text treatPlace;
	
	@FXML
	private TableView availableBalancesTableView;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	
	
	public SpecificTreatmentController() throws SQLException {
		this.balanceService= new BalanceService();
		this.treatmentService= new TreatmentService();
	}


	private void initializeBalanceTableView() throws SQLException{
		ObservableList<BalanceTreat> balances= FXCollections
			.observableArrayList
			(this.balanceService.getAllBalanceTreat(this.id));

		TableColumn<BalanceTreat, String> quantityColumn=
			new TableColumn<>("الكمية");
		quantityColumn.setCellValueFactory
			(new PropertyValueFactory<>("quantity"));

		TableColumn<BalanceTreat, Double> priceColumn=
			new TableColumn<>("السعر");
		priceColumn.setCellValueFactory
			(new PropertyValueFactory<>("price"));

		TableColumn<BalanceTreat, String> expireColumn=
			new TableColumn<>("تاريخ الصلاحية");
	        expireColumn.setCellValueFactory
			(new PropertyValueFactory<>("expireDate"));

		TableColumn<BalanceTreat, String> pill_num=
				new TableColumn<>("رقم الفاتورة");
		pill_num.setCellValueFactory(param -> {
			return new SimpleStringProperty(param.getValue().getPurchase().getPillNum());
		});


		this.availableBalancesTableView.getColumns()
			.addAll(quantityColumn,
				priceColumn,
				expireColumn,
					pill_num);

		this.availableBalancesTableView.setItems(balances);
	}
	


	private void inititlazeDetailedTreatmentInfo() throws SQLException {
		DetailedTreatment dt= this.treatmentService
			.getDetailedTreatmentById(this.id);

		this.treatName.setText(dt.getName());    
		this.treatType.setText(dt.getTypeTreatName());    		
		this.treatForm.setText(dt.getFormTreatName());      
		this.treatParcode.setText(dt.getParcode()); 	
		this.treatDateAt.setText(dt.getDateAt());  
		this.treatLowcount.setText(String.valueOf(dt.getLowcount()));
		this.treatCompany.setText(dt.getCompany()); 
		this.treatPlace.setText(dt.getPlace());   

	}
	


	@FXML
	private void initialize() throws SQLException {
		this.initializeBalanceTableView();
		this.inititlazeDetailedTreatmentInfo();
	}

	
}
