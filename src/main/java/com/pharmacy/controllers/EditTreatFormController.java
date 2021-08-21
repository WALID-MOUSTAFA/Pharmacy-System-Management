package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.TreatForm;
import com.pharmacy.POGO.TypeTreat;
import com.pharmacy.services.TreatmentService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class EditTreatFormController extends MyController{
    private TreatForm treatForm;
    private TreatmentService treatmentService;
    @FXML
    private TextField formName;


    @FXML
    private void initialize() {
        this.initializeForm();
    }

    private void initializeForm() {
        this.formName.setText(this.treatForm.getName());
    }

    public EditTreatFormController() {
        this.treatmentService= new TreatmentService();
    }

    @FXML
    private void editForm() throws SQLException {
        String formname=this.formName.getText();
        if(formname.isEmpty()) {
            MyUtils.ALERT_ERROR("يجب كتابة تركيب المنتج");
            return;
        }
        this.treatForm.setName(formname);
        boolean result= this.treatmentService.updateTreatForm(this.treatForm);
        if(result == false) {
            MyUtils.ALERT_ERROR("حدث خطأ أثناء الحفظ!");
        } else {
            this.stage.close();
        }
    }

    public void setTreatForm(TreatForm currentForm) {
        this.treatForm= currentForm;
    }
}
