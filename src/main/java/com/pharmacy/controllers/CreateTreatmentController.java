package com.pharmacy.controllers;

import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.POGO.TreatForm;
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
	
	@FXML
	private TextField companyName;

	@FXML
	private TextField treatPlace;

	@FXML
	private TextField lowCount;

	@FXML
	private ComboBox treatFormCombo;
	
	public CreateTreatmentController() {
		this.treatmentService= new TreatmentService();
	}

	private void initializeTreatTypeCombo() throws SQLException {
		List<TypeTreat> types= this.treatmentService.getAllTreatTypes();
		for (TypeTreat t : types) {
			this.treatTypeCombo.getItems().add(t.typename);
		}
	}

	
	private void initializeTreatFormCombo() throws SQLException{
		List<TreatForm> forms= this.treatmentService.getAllTreatForms();
		for (TreatForm t : forms) {
			this.treatFormCombo.getItems().add(t.getName());
		}
	}
	


	@FXML
	private void initialize() throws SQLException {
		this.initializeTreatTypeCombo();
		this.initializeTreatFormCombo();
	}

	@FXML
	private void addNewTreatment() throws SQLException {
		String typename= this.treatTypeCombo
			.getSelectionModel().getSelectedItem().toString();
		String name= this.treatName.getText();
		String parcode= this.treatBarCode.getText();
		String companyName= this.companyName.getText();
		String treatPlace= this.treatPlace.getText();
		double lowCount= Double.valueOf(this.lowCount.getText());
		String treatFormName= this.treatFormCombo.getSelectionModel()
			.getSelectedItem().toString();
			
		DetailedTreatment dt= new DetailedTreatment();
		dt.setName(name);
		dt.setTypeTreatName(typename);
		dt.setParcode(parcode);
		dt.setCompany(companyName);
		dt.setPlace(treatPlace);
		dt.setLowcount(lowCount);
		dt.setFormTreatName(treatFormName);
		//TODO(walid): handle the success message;
		System.out.println(this.treatmentService.insertTreatment(dt));

	}


}
