package com.pharmacy.controllers;

import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.POGO.TypeTreat;
import com.pharmacy.services.TreatmentService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.List;

public class CreateTreatmentController extends MyController{

    private TreatmentService treatmentService;

    @FXML
    private ComboBox treatTypeCombo;

    @FXML
    private TextField treatName;

    @FXML
    private TextField treatBarCode;


    public CreateTreatmentController() {
        this.treatmentService= new TreatmentService();
    }

    @FXML
    private void initialize() throws SQLException {
        List<TypeTreat> types= this.treatmentService.getAllTreatTypes();
        for (TypeTreat t : types) {
            this.treatTypeCombo.getItems().add(t.typename);
        }
    }

    @FXML
    private void addNewTreatment() throws SQLException {
        String typename= this.treatTypeCombo.getSelectionModel().getSelectedItem().toString();
        String name= this.treatName.getText();
        String  parcode= this.treatBarCode.getText();
        DetailedTreatment dt= new DetailedTreatment();
        dt.setName(name);
        dt.setTypeTreatName(typename);
        dt.setParcode(parcode);

        System.out.println(this.treatmentService.insertTreatment(dt));

    }


}
