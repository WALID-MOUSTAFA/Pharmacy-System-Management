package com.pharmacy.controllers;

import com.pharmacy.POGO.*;
import com.pharmacy.services.BalanceService;
import com.pharmacy.services.SalesDetailsService;
import com.pharmacy.services.SalesService;
import javafx.beans.property.SimpleStringProperty;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SalesController {

	BalanceService balanceService;
	TreatmentService treatmentService;
	SalesService salesService;
	SalesDetailsService salesDetailsService;

	@FXML private TextField treatName;
	@FXML private ComboBox treatType;
	@FXML private ListView searchResult;
	@FXML private TableView	relatedBalancesTableView;
	@FXML private TextField billClientName;

	//the detials of the balance treat;
	@FXML private Text treatNameDetails;
	@FXML private Text treatTypeDetails;
	@FXML private Text treatPriceDetails;
	@FXML private TextField treatQuantityDetials;

	@FXML private TableView billProductsTableView;

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
			this.salesService= new SalesService();
			this.salesDetailsService= new SalesDetailsService();
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
		this.treatNameDetails.setText(balanceTreat.getTreat().getName());
		this.treatTypeDetails.setText(balanceTreat.getTreat().getTypeTreatName());
		this.treatPriceDetails.setText(String.valueOf(balanceTreat.getPrice()));
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

	private void initalizeBillTableView() {

		List<BillItemModel> productsList = new ArrayList<>();
		ObservableList<BillItemModel> products= FXCollections.observableArrayList(productsList);

		TableColumn<BillItemModel, String> treatNameColumn= new TableColumn<>("الاسم");
		treatNameColumn.setCellValueFactory(val-> {
			return new SimpleStringProperty(
					((BillItemModel) val.getValue())
							.getBalanceTreat().getTreat().getName());
		});

		TableColumn<BillItemModel, Double> quantityColumn= new TableColumn<>("الكمية");
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		TableColumn<BillItemModel, String> typeColumn= new TableColumn<>("النوع");
		typeColumn.setCellValueFactory(val-> {
			return new SimpleStringProperty(
					((BillItemModel) val.getValue())
							.getBalanceTreat().getTreat().getTypeTreatName());
		});


		TableColumn<BillItemModel, String> priceColumn= new TableColumn<>("السعرس");
		priceColumn.setCellValueFactory(val-> {
			return new SimpleStringProperty(
					String.valueOf(((BillItemModel) val.getValue())
							.getBalanceTreat().getPrice()));
		});

		TableColumn<BillItemModel, String> totalColumn= new TableColumn<>("الإجمالي");
		totalColumn.setCellValueFactory(val-> {
			return new SimpleStringProperty(
					String.valueOf(((BillItemModel) val.getValue())
							.getBalanceTreat().getPrice()*((BillItemModel) val.getValue()).getQuantity()));
		});


		this.billProductsTableView.getColumns().addAll(treatNameColumn, typeColumn, priceColumn, totalColumn);
		this.billProductsTableView.setItems(products);
	}

	@FXML
	private void addTreatmentToBill() {
		BillItemModel billItemModel= new BillItemModel();
		BalanceTreat currentSelected=(BalanceTreat)this.relatedBalancesTableView.
				getSelectionModel().getSelectedItem();
		billItemModel.setBalanceTreat(currentSelected);
		billItemModel.setQuantity(Double.valueOf(this.treatQuantityDetials.getText()));
		addToBillTableView(billItemModel);
	}

	private void addToBillTableView(BillItemModel billItemModel) {
		this.billProductsTableView.getItems().add(billItemModel);
	}

	@FXML
	private void saveBill() throws SQLException{

		List<BillItemModel> items;
		double totalBill=0;
		Sale sale= new Sale();

		if (this.billProductsTableView.getItems().isEmpty()) {
			return;
		}

		items= this.billProductsTableView.getItems();
		for(BillItemModel b : items){
			totalBill+= b.getBalanceTreat().getPrice()* b.getQuantity();
		}

		sale.setTotal(totalBill);
		sale.setNetTotal(totalBill-sale.getDiscount());
		sale.setCustomerId(1);
		sale.setName(this.billClientName.getText());
		long insertedSaleId= this.salesService.insertSale(sale);

		//inserting salesDetails 
		for(BillItemModel b : items) {
			SaleDetails saleDetails= new SaleDetails();
			saleDetails.setSaleId(insertedSaleId);
			saleDetails.setBalanceId(b.getBalanceTreat().getId());
			saleDetails.setBalanceTreat(b.getBalanceTreat());
			saleDetails.setQuantity(b.getQuantity());
			saleDetails.setTotal(b.getQuantity()*b.getBalanceTreat().getPrice());
			saleDetails.setDateIn(new Timestamp(System.currentTimeMillis()).toString());
			saleDetails.setExpireDate(b.getBalanceTreat().getExpireDate());
			saleDetails.setPriceOne(b.getBalanceTreat().getPrice());
			saleDetails.setTreat(b.getBalanceTreat().getTreat());
			saleDetails.setTreatId(b.getBalanceTreat().getTreatId());
			if(b.getBalanceTreat().getQuantity() < b.getQuantity()) {
				Alert alert= new Alert(Alert.AlertType.ERROR);
				alert.setContentText("الكمية المطلوبة من هذا الدواء أكبر من المتوافر حالياً: " + b.getBalanceTreat().getTreat().getName());
				alert.show();
				return;
			} else {

				if(this.balanceService.decreaseQuantity(b.getBalanceTreat(), b.getQuantity())){
					if (this.salesDetailsService.insertSaleDetail(saleDetails)){
						Alert alert= new Alert(Alert.AlertType.INFORMATION);
						alert.setContentText("تم إضافة الفاتورة بنجاح");
						alert.show();
						this.billProductsTableView.getItems().clear();
						return;
					}
				}

			}
		}

	}

	@FXML
	private void deleteItemFromBillTable(){
		this.billProductsTableView.getItems().remove(
				this.billProductsTableView.getSelectionModel().getSelectedItem());
	}

	@FXML
	public void initialize() throws SQLException{
		this.initializeTreatTypeCombo();
		this.addSearchResultDoubleClickListener();
		this.addTableviewRowDoubleClickListener();
		this.initalizeBillTableView();
	}

}
