package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.Sale;
import com.pharmacy.POGO.SaleDetails;
import com.pharmacy.POGO.Treatment;
import com.pharmacy.services.SalesDetailsService;
import com.pharmacy.services.SalesService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class BillController extends MyController{

    private SalesService salesService;
    private SalesDetailsService salesDetailsService;
    private long currentSelectedBillId;
    @FXML private TableView billsTableView;
    @FXML private Button deleteBillButton;
    @FXML private Button editBillButton;


    public BillController() throws SQLException {
        this.salesService= new SalesService();
        this.salesDetailsService= new SalesDetailsService();
    }

    @FXML
    public void initialize() throws SQLException{
        this.inititalizeBillTableView();
        this.addBillsTableViewDoubleClickListener();
        this.addBillsTableViewFocusListener();
    }

    private void addBillsTableViewFocusListener() {

        this.billsTableView.focusedProperty()
                .addListener((observableVal, oldval, newval) -> {
                    if (newval) {
                       // this.editBillButton.setDisable(false);
                        this.deleteBillButton.setDisable(false);
                    } else {
                        //this.editBillButton.setDisable(true);
                        this.deleteBillButton.setDisable(true);
                    }
                });
    }

    private void addBillsTableViewDoubleClickListener() {
        this.billsTableView.setRowFactory
                ( tv->  {
                    TableRow<Treatment> row= new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                            try {
				    this.showBillDetails();
                            } catch (IOException|SQLException e){}
                        }
                    });
                    return row ;
                });
    }

    private void showBillDetails() throws IOException, SQLException{
        Stage stage= new Stage();
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation
                (getClass().getResource("/fxml/BillDetails.fxml"));
        BillDetailsController billDetailsController=
                new BillDetailsController();
        billDetailsController.setStage(stage);
        billDetailsController.setId(this.currentSelectedBillId);
        loader.setController(billDetailsController);
        Parent root= loader.load();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    private void inititalizeBillTableView() throws SQLException{
        List<Sale> sales= salesService.getAllSales();

        TableColumn<Sale, String> customerName= new TableColumn<>("العميل");
        customerName.setCellValueFactory(tv-> {
            return new SimpleStringProperty(tv.getValue().getCustomer().getName());
        });

        TableColumn<Sale, Double> netTotalColumn= new TableColumn<>("الإجمالي");
        netTotalColumn.setCellValueFactory(new PropertyValueFactory<>("netTotal"));

        TableColumn<Sale, Double> discountColumn= new TableColumn<>("الخصم");
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));

        TableColumn<Sale, Double> dateColumn= new TableColumn<>("التاريخ");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateIn"));

        this.billsTableView.getColumns().addAll(customerName, netTotalColumn, discountColumn, dateColumn);

        if(sales != null) {
            this.billsTableView.setItems(FXCollections.observableArrayList(sales));
        }

        //set currentTreatmentId
        this.billsTableView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldvalue, newvalue)->{
                    this.currentSelectedBillId=((Sale)newvalue).getId();
                });

    }


	@FXML
	public void deleteSale() throws SQLException{
		List<SaleDetails> relatedSaleDetails= this.salesDetailsService.getRelatedSaleDetails(this.currentSelectedBillId);
		if(relatedSaleDetails != null) {
	    	for (SaleDetails sd: relatedSaleDetails) {
		    this.salesDetailsService.restoreBalance(sd);
	    	}
		}
		if(this.salesService.deleteSale(this.currentSelectedBillId)){
			this.billsTableView.getItems().remove(this.billsTableView.getSelectionModel().getSelectedItem());
		} else {
			MyUtils.ALERT_ERROR("حدث خطأ أثناء الحذف");
		}
		
	}
}
