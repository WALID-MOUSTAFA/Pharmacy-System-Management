package com.pharmacy.controllers;

import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.POGO.TreatForm;
import com.pharmacy.POGO.TypeTreat;
import com.pharmacy.services.TreatmentService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.List;

public class EditTreatmentController extends MyController{

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
	private TextField lowcount;

	@FXML
	private ComboBox treatFormCombo;

	private long id;

	public EditTreatmentController() {
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
	

	private DetailedTreatment getTheSpecificTreatment() throws SQLException{
		return this.treatmentService.getDetailedTreatmentById(this.id);
	}
	
	private void initializeForm() throws SQLException{
		DetailedTreatment dt= this.getTheSpecificTreatment();
		this.treatTypeCombo.setValue(dt.getTypeTreatName());
		this.treatFormCombo.setValue(dt.getFormTreatName());
		this.treatName.setText(dt.getName());
		this.treatBarCode.setText(dt.getParcode());
		this.companyName.setText(dt.getCompany());
		this.treatPlace.setText(dt.getPlace());
		this.lowcount.setText(String.valueOf(dt.getLowcount()));
	}


	
	@FXML
	private void initialize() throws SQLException{
		this.initializeTreatTypeCombo();
		this.initializeTreatFormCombo();
		this.initializeForm();
	}

	@FXML
	private void updateTreatment() throws SQLException{
		String typename= this.treatTypeCombo
			.getSelectionModel().getSelectedItem().toString();
		String name= this.treatName.getText();
		String parcode= this.treatBarCode.getText();
		String companyName= this.companyName.getText();
		String treatPlace= this.treatPlace.getText();
		double lowcount= Double.valueOf(this.lowcount.getText());
		String treatFormName= this.treatFormCombo.getSelectionModel()
			.getSelectedItem().toString();
			
		DetailedTreatment dt= this.getTheSpecificTreatment();
		dt.setName(name);
		dt.setTypeTreatName(typename);
		dt.setParcode(parcode);
		dt.setCompany(companyName);
		dt.setPlace(treatPlace);
		dt.setLowcount(lowcount);
		dt.setFormTreatName(treatFormName);
		boolean result= this.treatmentService.updateTreatmentById(dt);
		System.out.println(result);
		if(result){
			this.stage.close();
		} else {
			//TODO(walid): handle error messages;
		}
	}


	public void setId(long id){
		this.id= id;
	}
	
	public long getId(){
		return this.id;
	}
}
