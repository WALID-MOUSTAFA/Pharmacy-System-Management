package com.pharmacy.controllers;

import com.pharmacy.POGO.SaleDetails;
import com.pharmacy.services.SalesDetailsService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class BillDetailsController extends MyController{

    private long id;
    private long currentSelectedBillDetails;
    private SalesDetailsService salesDetailsService;
    @FXML private TableView billDetailsTableView;
    @FXML private Button deleteBillDetailButton;

    public void setId(long id) {
        this.id= id;
    }

    public BillDetailsController() throws SQLException {
        this.salesDetailsService= new SalesDetailsService();
    }

    @FXML
    public void initialize() throws SQLException {
        this.inititalizeBillDetailsTableView();
    }

    private void inititalizeBillDetailsTableView() throws SQLException {
        List<SaleDetails> saleDetailsList= this.salesDetailsService.getRelatedSaleDetails(this.id);

        TableColumn<SaleDetails, String> treatNameColumn= new TableColumn<>("اسم الدواء");
        treatNameColumn.setCellValueFactory(val->{
            return new SimpleStringProperty(
                    ((SaleDetails)val.getValue())
                    .getTreat().getName()
            );
        });

        TableColumn<SaleDetails, String> treatTypeName= new TableColumn<>("نوع الدواء");
        treatTypeName.setCellValueFactory(val->{
            return new SimpleStringProperty(
                    ((SaleDetails)val.getValue())
                            .getTreat().getTypeTreatName()
            );
        });

        TableColumn<SaleDetails, Double> totalColumn= new TableColumn<>("الاجمالي");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<SaleDetails, Double> priceOneColumn= new TableColumn<>("سعر الوحدة");
        priceOneColumn.setCellValueFactory(new PropertyValueFactory<>("priceOne"));

        TableColumn<SaleDetails, String> expireColumn= new TableColumn<>("الصلاحية");
        expireColumn.setCellValueFactory(new PropertyValueFactory<>("expireDate"));

        TableColumn<SaleDetails, Double> quantityColumn = new TableColumn<>("الكمية");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        this.billDetailsTableView.getColumns().addAll(
                  treatNameColumn,
                treatTypeName,
                quantityColumn,
                priceOneColumn,
                totalColumn,
                expireColumn
        );

        if(saleDetailsList!= null) {
            this.billDetailsTableView.setItems(FXCollections.observableArrayList(saleDetailsList));
        } else {
            this.billDetailsTableView.setItems(null);
        }

        this.billDetailsTableView.getSelectionModel().selectedItemProperty()
                .addListener(val->{
                    this.currentSelectedBillDetails=((SaleDetails)val).getId();
                });


    }


    private void addBillsTableViewFocusListener() {

        this.billDetailsTableView.focusedProperty()
                .addListener((observableVal, oldval, newval) -> {
                    if (newval) {
                        this.deleteBillDetailButton.setDisable(false);
                    } else {
                        this.deleteBillDetailButton.setDisable(true);
                    }
                });
    }

//    @FXML
//    private void deleteBillDetails() throws SQLException{
//        boolean deleted= this.salesDetailsService.DeleteSaleDetail(this.currentSelectedBillDetails);
//        if(deleted){
//            Alert alert= new Alert(Alert.AlertType.INFORMATION);
//            alert.setContentText("تم الحذف بنجاح");
//            alert.show();
//        }
//    }

}
