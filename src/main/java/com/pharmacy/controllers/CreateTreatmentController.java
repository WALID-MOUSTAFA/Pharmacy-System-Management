package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.POGO.TreatForm;
import com.pharmacy.POGO.TypeTreat;
import com.pharmacy.services.TreatmentService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.ArrayList;
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

	private TreatmentController treatmentController; //parent fxml controller ;

	public void setTreatmentController(TreatmentController treatmentController) {
		this.treatmentController = treatmentController;
	}


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

		lowCount= !this.lowCount.getText().isEmpty()?
			Double.valueOf(this.lowCount.getText()): 0;

		
		DetailedTreatment dt= new DetailedTreatment();
		dt.setName(name);
		dt.setTypeTreatName(typename);
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

		long inserted= this.treatmentService.insertTreatment(dt);
		if(inserted> 0){
			dt.setId(inserted);
			this.treatmentController
				.addTreatmentItemToTheTreatmentTableView(dt);
		} else {

			Alert alert= new Alert(Alert.AlertType.ERROR);
			alert.setContentText("حدث خطأ ما أثناء الإضافة");
			alert.show();
		}

	}


}
