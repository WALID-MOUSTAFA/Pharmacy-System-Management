package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.POGO.TreatForm;
import com.pharmacy.POGO.TypeTreat;
import com.pharmacy.services.TreatmentService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.ArrayList;
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

		List<String> errors= new ArrayList<>();
		String typename= "";
		String name;
		String parcode;
		String companyName;
		String treatPlace;
		double lowCount;
		String treatFormName="";
		
		if(!this.treatTypeCombo.getSelectionModel().isEmpty()) {
			typename= this.treatTypeCombo
				.getSelectionModel().getSelectedItem().toString();
			
		} else {
			errors.add("يجب اختيار النوع");
			MyUtils.showValidationErrors(errors);
			return;
		}


		if(!this.treatFormCombo.getSelectionModel().isEmpty()) {
			treatFormName= this.treatFormCombo.getSelectionModel()
				.getSelectedItem().toString();
			
		} else {
			errors.add("يجب اختيار التركيبة");
			MyUtils.showValidationErrors(errors);
			return;
		}
		
		name= this.treatName.getText();
		parcode= this.treatBarCode.getText();
		companyName= this.companyName.getText();
		treatPlace= this.treatPlace.getText();
		lowCount= !this.lowcount.getText().isEmpty()?
			Double.valueOf(this.lowcount.getText()) : 0;
		
		DetailedTreatment dt= this.getTheSpecificTreatment();
		 dt.setName(name);
		 dt.setTypeTreatName(typename);
		 long typet= this.treatmentService.getTypeIdFromName(typename);
		 dt.setTypet(typet);
		 dt.setParcode(parcode);
		 dt.setCompany(companyName);
		 dt.setPlace(treatPlace);
		 dt.setLowcount(lowCount);
		 dt.setFormTreatName(treatFormName);

		 		
		 MyUtils.<DetailedTreatment>validateModel(dt, errors);
		 if(!errors.isEmpty()){
			 MyUtils.showValidationErrors(errors);
			 return;
		 }

		 boolean result= this.treatmentService.updateTreatmentById(dt);
		 
		 if(result){
			 this.stage.close();
		 } else {
			 Alert alert= new Alert(Alert.AlertType.ERROR);
			 alert.setContentText("حدث خطأ ما أثناء الإضافة");
			 alert.show();
			 
		 }
	}


	public void setId(long id){
		this.id= id;
	}
	
	public long getId(){
		return this.id;
	}
}
