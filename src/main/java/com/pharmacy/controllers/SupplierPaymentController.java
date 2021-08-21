package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.Supplier;
import com.pharmacy.POGO.SupplierPayment;
import com.pharmacy.services.SupplierPaymentService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierPaymentController extends MyController {

    private Supplier supplier;
    private SupplierPaymentService supplierPaymentService ;
    private SupplierPayment currentPayment;

    @FXML private TableView paymentsTableView;
    @FXML private Button editPaymentButton;
    @FXML private Button deletePaymentButton;

    //Add
    @FXML private TextField supplierGet;
    @FXML private DatePicker supplierGetDate;
    @FXML TextField notes;
    @FXML CheckBox status;

    public SupplierPaymentController() throws SQLException {
        this.supplierPaymentService= new SupplierPaymentService();
    }

    public void setCurrentPayment(SupplierPayment currentPayment) {
        this.currentPayment = currentPayment;
    }


    public void setSupplier(Supplier supplier) {
        this.supplier= supplier;
    }

    @FXML
    public void initialize() throws SQLException{
        this.initializePaymentsTableView();
        addTableViwFocusListener();
    }

    @FXML
    public void initializePaymentsTableView() throws SQLException {
        TableColumn<SupplierPayment, String> paymentCoulmn= new TableColumn<>("المبلغ");
        paymentCoulmn.setCellValueFactory(new PropertyValueFactory<>("supplierGet"));

        TableColumn<SupplierPayment, String> dateColumn= new TableColumn<>("التاريخ");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("supplierGetDate"));

        TableColumn<SupplierPayment, String> statusColumn= new TableColumn<>("تم الدفع");
        statusColumn.setCellValueFactory(row-> new SimpleStringProperty(row.getValue().getStatus() ==1? "تم الدفع" : "لم يتم الدفع"));

        TableColumn<SupplierPayment, String> notesColumn= new TableColumn<>("ملاحظات");
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));

        this.paymentsTableView.getColumns().addAll(paymentCoulmn, dateColumn, statusColumn, notesColumn);
        this.paymentsTableView.setItems(
                FXCollections.observableArrayList(this.supplierPaymentService.getAllSupplierPayment(this.supplier)));

        this.paymentsTableView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obsvaal, oldval, newval)-> {
                    this.setCurrentPayment(
                            (SupplierPayment)newval);
                });

    }

    @FXML
    public  void showEditPayment() throws IOException, SQLException {
        Stage stage= new Stage();
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass()
                .getResource("/fxml/EditSupplierPayment.fxml"));
        EditSupplierPaymentController editSupplierPaymentController=
                new EditSupplierPaymentController();
        editSupplierPaymentController.setStage(stage);
        editSupplierPaymentController.setSupplierPayment(this.currentPayment);
        loader.setController(editSupplierPaymentController);
        Parent root= loader.load();
        stage.setScene(new Scene(root,400, 680 ));
        stage.showAndWait();
        this.paymentsTableView.getColumns().clear();
        this.initializePaymentsTableView();
    }

    @FXML
    public void deletePayement() throws SQLException {
        if(this.supplierPaymentService.deleteSupplier(this.currentPayment.getId())) {
            this.paymentsTableView.getItems().remove(this.currentPayment);
        }
    }

    @FXML
    public void addNewPayment() throws SQLException {
        List<String> errors = new ArrayList<>();
        String value;
        boolean status;
        String date;
        String notes;
        SupplierPayment supplierPayment;

        value= this.supplierGet.getText();
        status= this.status.isSelected();
        date= this.supplierGetDate.getValue().toString();
        notes= this.notes.getText();
        supplierPayment= new SupplierPayment();

        if(date == null || date.isEmpty()) {
            errors.add("يجب أن تختار التاريخ");
            MyUtils.showValidationErrors(errors);
            return;
        }

        if(value.isEmpty()) {
            errors.add("يجب أن تختار المبلغ");
            MyUtils.showValidationErrors(errors);
            return;
        }

        supplierPayment.setSupplierGet(value.isEmpty()? 0: Double.valueOf(value));
        supplierPayment.setSupplierId(this.supplier.getId());
        supplierPayment.setStatus(status? (short) 1 : (short) 0);
        supplierPayment.setNotes(notes);
        supplierPayment.setSupplierGetDate(date);

        if(this.supplierPaymentService.insertSupplierPayment(supplierPayment)) {
            this.paymentsTableView.getItems().add(supplierPayment);
        }else {
            MyUtils.ALERT_ERROR("حدث خطأ أثناء الإضافة!!!");
        }

    }

    private void addTableViwFocusListener() {
        this.paymentsTableView.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue) {
                this.editPaymentButton.setDisable(false);
                this.deletePaymentButton.setDisable(false);
            } else {
                this.editPaymentButton.setDisable(true);
                this.deletePaymentButton.setDisable(true);
            }
        }));
    }

}
