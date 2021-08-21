package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.TypeTreat;
import com.pharmacy.services.TreatmentService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class EditTreatTypeController extends MyController{

    private TypeTreat typeTreat;
    private TreatmentService treatmentService;
    @FXML private TextField typeName;


    @FXML
    private void initialize() {
        this.initializeForm();
    }

    private void initializeForm() {
        this.typeName.setText(this.typeTreat.getTypename());
    }

    public EditTreatTypeController() {
        this.treatmentService= new TreatmentService();
    }

    @FXML
    private void editType() throws SQLException {
        String typename=this.typeName.getText();
        if(typename.isEmpty()) {
            MyUtils.ALERT_ERROR("يجب كتابة نوع المنتج");
            return;
        }
        this.typeTreat.setTypename(typename);
        boolean result= this.treatmentService.updateTreatType(this.typeTreat);
        if(result == false) {
            MyUtils.ALERT_ERROR("حدث خطأ أثناء الحفظ!");
        } else {
            this.stage.close();
        }
    }

    public void setTreatType(TypeTreat currentType) {
        this.typeTreat= currentType;
    }
}
