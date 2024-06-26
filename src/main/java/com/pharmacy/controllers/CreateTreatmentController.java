package com.pharmacy.controllers;

import com.pharmacy.InputFilter;
import com.pharmacy.MyUtils;
import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.POGO.TreatForm;
import com.pharmacy.POGO.TypeTreat;
import com.pharmacy.services.TreatmentService;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
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
		List<String> typesString = new ArrayList<>();
		for (TypeTreat t : types) {
			typesString.add(t.typename);
		}
		FilteredList<String> filteredList = new FilteredList<String>(FXCollections.observableArrayList(typesString));
		treatTypeCombo.getEditor().textProperty().addListener(new InputFilter(treatTypeCombo, filteredList, false));
		treatTypeCombo.setEditable(true);
		this.treatTypeCombo.setItems(filteredList);

	}

	
	private void initializeTreatFormCombo() throws SQLException{
		List<TreatForm> forms= this.treatmentService.getAllTreatForms();
		ArrayList<String> formsString = new ArrayList<>();
		for (TreatForm t : forms) {
			formsString.add(t.getName());
		}
		FilteredList<String> filteredList = new FilteredList<String>(FXCollections.observableArrayList(formsString));
		treatFormCombo.getEditor().textProperty().addListener(new InputFilter(treatFormCombo, filteredList, false));
		treatFormCombo.setEditable(true);
		this.treatFormCombo.setItems(filteredList);
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
		if (parcode != null && !parcode.isEmpty()) {
			if(Character.isLetter(parcode.charAt(0)) && Character.isLetter(parcode.charAt(parcode.length() - 1))) {
				parcode = parcode.substring(1, parcode.length() -1);
				
			}

		}
		companyName= this.companyName.getText();
		treatPlace= this.treatPlace.getText();

		lowCount= !this.lowCount.getText().isEmpty()?
			Double.valueOf(this.lowCount.getText()): 0;

		if(name.contains("-") ){
			errors.add("يجب ألا يحتوي الاسم على العلامة '-' ");
			MyUtils.showValidationErrors(errors);
			return;
		}

		List<DetailedTreatment> existing = this.treatmentService.getAllTreatmentsByNameAndType(name, typename);
		if(existing != null) {
			MyUtils.ALERT_ERROR("هذا المنتج متوفر مسبقا");
			return;
		}

		
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



	@FXML
	private void generateNewBarcode() throws BarcodeException, OutputException, SQLException {
		String number = String.valueOf(Math.floor(Math.random() * 9_000_000_000_000L) + 1_000_000_0000_00L);
		BigDecimal bigDecimal= new BigDecimal(number);
		number= bigDecimal.toString();
		//Barcode barcode= BarcodeFactory.createCodabar("123456789123");
		//barcode.setDrawingText(false);
		//barcode.setResolution(72);
		//File file= new File("test.png");
		//BarcodeImageHandler.savePNG(barcode, file);
		if(!this.treatmentService.checkIfBarcodeExists(number)) {
			this.treatBarCode.setText(number);
		} else {
			this.generateNewBarcode();
		}
	}

}
