//TODO(walid): add order by to the queries;
package com.pharmacy.controllers;

import com.pharmacy.InputFilter;
import com.pharmacy.MyUtils;
import com.pharmacy.POGO.*;
import com.pharmacy.services.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SalesController {

	BalanceService balanceService;
	TreatmentService treatmentService;
	SalesService salesService;
	SalesDetailsService salesDetailsService;
	CustomerService customerService;
	
	@FXML private TextField treatName;
    @FXML private TextField parcode;
    @FXML private ComboBox<String> treatType;
	@FXML private ListView searchResult;
	@FXML private TableView	relatedBalancesTableView;
	@FXML private TextField billClientName;
	
	@FXML private Label total;
	//the detials of the balance treat;
	@FXML private Text treatNameDetails;
	@FXML private Text treatTypeDetails;
	@FXML private Text treatPriceDetails;
	@FXML private TextField treatQuantityDetials;

	@FXML private TableView billProductsTableView;
	@FXML private GridPane productsDetails;
	@FXML private ComboBox storedCustomerName;
    
	@FXML private ListView alternateResults;
    
	public void initializeTreatTypeCombo() throws SQLException {
		this.treatType.getItems().clear();

		TreatmentService treatmentService= new TreatmentService();
		List<TypeTreat> types= treatmentService.getAllTreatTypes();
		List<String> typesNameStringList = new ArrayList<>();
		for (TypeTreat t : types) {
			typesNameStringList.add(t.typename);
		}
		ObservableList<String> items = FXCollections.observableArrayList(typesNameStringList);
		FilteredList<String> filteredList = new FilteredList<>(items);
		treatType.getEditor().textProperty().addListener(new InputFilter(treatType,filteredList , false));
		treatType.setEditable(true);
		treatType.setItems(filteredList);
	}

	@FXML
	private void startSearchForTreat() throws SQLException {
		String typeName;
		String parcode= this.parcode.getText();

		if(!parcode.isEmpty()) {
		    this.searchForTreatByParcode(this.parcode.getText());
		}else {
			try {
				typeName = this.treatType.getValue().toString();
			} catch (NullPointerException e) {
				MyUtils.ALERT_ERROR("اختر نوع المنتج.");
				return;
			}
		    this.searchForTreat(this.treatName.getText(), typeName);
		}
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


    
	private void searchForTreatByParcode(String parcode) throws SQLException {
		if (parcode.length() != 13) {
			if(Character.isLetter(parcode.charAt(0)) && Character.isLetter(parcode.charAt(parcode.length() - 1))) {
				parcode = parcode.substring(1, parcode.length() -1);
				
			}
		}
		List<DetailedTreatment> results= treatmentService.getAllTreatmentsByParcode(parcode);
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

	public void clearAlternateResults() {
		this.alternateResults.getItems().clear();
	}


	public SalesController() throws SQLException{
			this.balanceService= new BalanceService();
			this.treatmentService= new TreatmentService();
			this.customerService= new CustomerService();
			this.salesService= new SalesService();
			this.salesDetailsService= new SalesDetailsService();
	}
	
	


	
	private void addTableviewRowDoubleClickListener() {
		this.relatedBalancesTableView.setRowFactory
			( tv-> {

			  TableRow<BalanceTreat> row= new TableRow<>();
			 row.setOnMouseClicked(event -> {
			    if (event.getClickCount() == 2 &&(!row.isEmpty()) ) {
				    this.productsDetails.setVisible(true);

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
					this.initializeAltListView(((DetailedTreatment) this.searchResult.getSelectionModel().getSelectedItem()));
				}catch (SQLException e) {

				}

			}
			});
	}



	private void addAlternateResultDoubleClickListener() {
		this.alternateResults.setOnMouseClicked((ev)->{
			if (ev.getClickCount() ==2){

				try {
					this.clearRelatedBalancesTableView();
					this.initializeRelatedBalanceTableView
							(((DetailedTreatment) this.alternateResults.getSelectionModel().getSelectedItem()).getId());
				}catch (SQLException e) {

				}

			}
		});
	}

	private void initializeAltListView(DetailedTreatment selectedItem) throws SQLException {
		List<DetailedTreatment> alts= this.treatmentService.getAltTreatments(selectedItem);
		this.alternateResults.setItems(FXCollections.observableArrayList(alts));
		this.alternateResults.setCellFactory((lv)->{
			return new ListCell<DetailedTreatment>(){
				@Override
				protected void updateItem(DetailedTreatment item, boolean empty) {
					super.updateItem(item, empty);
					setText(empty? null : item.getName() + " ---- " + item.getTypeTreatName());
				}
			};
		});
	}

	private void initalizeBillTableView() {

		this.billProductsTableView.getColumns().clear();

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


		TableColumn<BillItemModel, String> priceColumn= new TableColumn<>("السعر");
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


		this.billProductsTableView.getColumns().addAll(treatNameColumn, typeColumn, priceColumn, quantityColumn, totalColumn);
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

		this.clearRelatedBalancesTableView();
		this.productsDetails.setVisible(false);
		
	}

	private void adjustTotal() {
		List<BillItemModel> items;
		double totalBill=0;
		items= this.billProductsTableView.getItems();
		for(BillItemModel b : items){
			totalBill+= b.getBalanceTreat().getPrice()* b.getQuantity();
		}
		this.total.setText(totalBill>0? String.valueOf(totalBill) : "");
	}

	private void addToBillTableView(BillItemModel billItemModel) {
		this.billProductsTableView.getItems().add(billItemModel);
		adjustTotal();

	}


	public long getChoosenStoredClientId() throws SQLException {
		if(this.storedCustomerName.getSelectionModel().isEmpty()){
			return 0;
		}
		String name= this.storedCustomerName.getValue().toString();
		return this.customerService.getCustomerByName(name).getId();
	}
	
	@FXML
	private void saveBill() throws SQLException{

		List<BillItemModel> items;
		double totalBill=0;
		Sale sale= new Sale();
		String storedCustomerName;
		String clientName;

		if (this.billProductsTableView.getItems().isEmpty()) {
			return;
		}

		clientName= this.billClientName.getText();

		items= this.billProductsTableView.getItems();
		for(BillItemModel b : items){
			totalBill+= b.getBalanceTreat().getPrice()* b.getQuantity();
		}

		sale.setTotal(totalBill);
		sale.setNetTotal(totalBill-sale.getDiscount());
		if(this.getChoosenStoredClientId() == 0){
			MyUtils.ALERT_ERROR("يلزم اختيار عميل للمتابعة!");
			return;
		}
		
		sale.setCustomerId(this.getChoosenStoredClientId());

		sale.setName(clientName);

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
			if(b.getBalanceTreat().getQuantity() < b.getQuantity() || b.getQuantity() == 0) {
				Alert alert= new Alert(Alert.AlertType.ERROR);
				alert.setContentText("الكمية المطلوبة من هذا الدواء أكبر من المتوافر حالياً: " + b.getBalanceTreat().getTreat().getName());
				this.salesService.deleteSale(insertedSaleId);
				alert.show();
				return;
			} else {

			    if(this.balanceService.decreaseQuantity(b.getBalanceTreat().getId(), b.getQuantity())){
					if (this.salesDetailsService.insertSaleDetail(saleDetails)){
						Alert alert= new Alert(Alert.AlertType.INFORMATION);
						alert.setContentText("تم إضافة الفاتورة بنجاح");
						alert.show();
						//this.billProductsTableView.getItems().clear();

						this.relatedBalancesTableView.getItems().clear();
						this.alternateResults.getItems().clear();
						this.searchResult.getItems().clear();
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
		adjustTotal();
	}


	@FXML
	private void clearRelatedBalancesTableView(){
		this.relatedBalancesTableView.getItems().clear();
	}

	
	private void initalizeCustomerCombo() throws SQLException{
		this.storedCustomerName.getItems().clear();
		List<Customer> customers= this.customerService.getAllCustomers();
		for (Customer c : customers) {
			this.storedCustomerName.getItems().add(c.getName());
		}
	}


	@FXML
	private void printReport() throws ClassNotFoundException, URISyntaxException, JRException, SQLException {
	    List<BillItemModel> items;
	    double totalBill=0;
	    items= this.billProductsTableView.getItems();
	    for(BillItemModel b : items){
		totalBill+= b.getBalanceTreat().getPrice()* b.getQuantity();
	    }
	    PrintReport p= new PrintReport();
	    p.showReport(items, this.billClientName.getText(), totalBill);
	}
	
	@FXML
	public void initialize() throws SQLException{
		this.initializeTreatTypeCombo();
		this.addSearchResultDoubleClickListener();
		this.addAlternateResultDoubleClickListener();
		this.addTableviewRowDoubleClickListener();
		this.initalizeBillTableView();
		this.initalizeCustomerCombo();
	}

	@FXML
	private void choosePrinterAction() throws IOException, SQLException {
		ChooseReciptPrinterController chooseReciptPrinterController = new ChooseReciptPrinterController();
		chooseReciptPrinterController.open();
	}

	@FXML
	private void doRefresh() throws SQLException {
		this.initialize();
	}
}
