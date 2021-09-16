package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.SupplierPayment;
import com.pharmacy.services.SupplierPaymentService;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EditSupplierPaymentController extends MyController{
    private SupplierPayment supplierPayment;
    private SupplierPaymentService supplierPaymentService ;

    @FXML private TextField supplierGet;
    @FXML private DatePicker supplierGetDate;
    @FXML TextField notes;
    @FXML CheckBox status;


    public void setSupplierPayment(SupplierPayment supplierPayment) {
        this.supplierPayment = supplierPayment;
    }

    public EditSupplierPaymentController() throws SQLException {
        this.supplierPaymentService= new SupplierPaymentService();
    }

    @FXML
    private void initialize() {
        this.initializeForm();
    }

    private void initializeForm() {
        this.supplierGet.setText(String.valueOf(this.supplierPayment.getSupplierGet()));
        this.supplierGetDate.setValue(LocalDate.parse(this.supplierPayment.getSupplierGetDate().split(" ")[0]));
        this.notes.setText(this.supplierPayment.getNotes());
        this.status.setSelected(this.supplierPayment.getStatus() == 1? true : false);
    }

    @FXML
    public void editSupplierPayment() throws SQLException {
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
        supplierPayment= this.supplierPayment;

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
        supplierPayment.setStatus(status? (short) 1 : (short) 0);
        supplierPayment.setNotes(notes);
        supplierPayment.setSupplierGetDate(date);

        if(this.supplierPaymentService.updateSupplierPayment(supplierPayment)) {
            this.stage.close();
        }else {
            MyUtils.ALERT_ERROR("حدث خطأ أثناء التعديل!!!");
        }

    }


}
