package com.pharmacy.controllers;

import com.pharmacy.InputFilter;
import com.pharmacy.MyUtils;
import com.pharmacy.POGO.BalanceTreat;
import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.POGO.PurchaseDetails;
import com.pharmacy.services.BalanceService;
import com.pharmacy.services.TreatmentService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class AddBalanceTreatController extends MyController{

	private BalanceService balanceService;

	@FXML
	private TextField quantity;

//	@FXML
//	private TextField pricePharmacy;
//
//	@FXML
//	private TextField totalPharmacy;

	@FXML
	private TextField pricePeople;

//	@FXML
//	private TextField totalPeople;

	@FXML
	private DatePicker expireDate;

	@FXML
	private DatePicker productionDate;

//	@FXML
//	TextField discount;

	@FXML
	private ComboBox treatName;

	public void show() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/AddBalanceTreat.fxml"));
		Stage stage = new Stage();
		this.setStage(stage);
		loader.setController(this);
		Parent root = loader.<VBox>load();
		stage.setScene(new Scene(root));
		stage.setTitle("إضافة رصيد بدون فاتورة");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	@FXML
	private void initialize() throws SQLException {
		this.initializeTreatmentCombo();
		MyUtils.setDatePickerFormat(this.expireDate);
		MyUtils.setDatePickerFormat(this.productionDate);
		    
		try {
			StringConverter<? extends Number> converter= new DoubleStringConverter();
			SimpleDoubleProperty quantityProperty= new SimpleDoubleProperty();
			//SimpleDoubleProperty phramacyPriceProperty= new SimpleDoubleProperty();
			SimpleDoubleProperty peoplePriceProperty= new SimpleDoubleProperty();

			Bindings.bindBidirectional(this.quantity.textProperty(), quantityProperty, (StringConverter<Number>)converter);
			Bindings.bindBidirectional(this.pricePeople.textProperty(), peoplePriceProperty, (StringConverter<Number>)converter);
			//Bindings.bindBidirectional(this.pricePharmacy.textProperty(), phramacyPriceProperty, (StringConverter<Number>)converter);

			//this.totalPharmacy.textProperty().bind(Bindings.multiply(quantityProperty, phramacyPriceProperty).asString());
			//this.totalPeople.textProperty().bind(Bindings.multiply(quantityProperty, peoplePriceProperty).asString());

		} catch (NumberFormatException e) {
			MyUtils.ALERT_ERROR("ادخل الأرقام بصورة صحيحة");}

	}

	public AddBalanceTreatController() throws SQLException {
		this.balanceService= new BalanceService();
	}
	
	public void initializeTreatmentCombo() throws SQLException {
		TreatmentService ts = new TreatmentService();
		List<DetailedTreatment> treatments = ts.getAllTreatments();
		List<String> itemsString= new ArrayList<>();
		for (DetailedTreatment t : treatments) {
			itemsString.add(t.getName() + "-" + t.getTypeTreatName());
		}
		ObservableList<String > items = FXCollections.observableArrayList(itemsString);
		FilteredList<String> filteredItems = new FilteredList<>(items);
		treatName.getEditor().textProperty().addListener(new InputFilter(treatName, filteredItems, false));
		treatName.setEditable(true);
		treatName.setItems(filteredItems);

	}

	@FXML
	private void createBalanceTreat() throws SQLException {
		List<String> errors = new ArrayList<>();
		BalanceTreat balanceTreat;
		String quantity;
		//String pricePharmacy;
		//String totalPharmacy;
		String pricePeople;
		//String totalPeople;
		String expireDate;
		String productionDate;
		String treatName;
		//String discount;
		DetailedTreatment treatment;

		TreatmentService treatmentService = new TreatmentService();
		PurchaseDetails purchaseDetails = new PurchaseDetails();
		balanceTreat= new BalanceTreat();

		if (this.expireDate.getValue() == null) {
			errors.add("يجب اختيار تاريخ الصلاحية");
			MyUtils.showValidationErrors(errors);
			return;
		}

		if (this.productionDate.getValue() == null) {
			errors.add("يجب اختيار تاريخ الإنتاج");
			MyUtils.showValidationErrors(errors);
			return;
		}


		if (this.treatName.getValue() == null) {
			errors.add("يجب اختيار اسم المنتج");
			MyUtils.showValidationErrors(errors);
			return;
		}


		quantity = this.quantity.getText();
		// pricePharmacy = this.pricePharmacy.getText();
		// totalPharmacy = this.totalPharmacy.getText();
		pricePeople = this.pricePeople.getText();
	//	totalPeople = this.totalPeople.getText();
		expireDate = Timestamp
			.valueOf(this
				 .expireDate
				 .getValue()
				 .atStartOfDay()).toString();
		// productionDate = Timestamp
			// .valueOf(this
				 // .productionDate
				 // .getValue()
				 // .atStartOfDay()).toString();
		treatName = this.treatName.
			getSelectionModel().getSelectedItem().toString();

		treatment = treatmentService
			.getTreatmentByName(treatName.split("-")[0], treatName.split("-")[1]);
		treatment.setTypeTreatName(treatName.split("-")[1]);

		balanceTreat.setTreat(treatment);
		balanceTreat.setQuantity(Double.valueOf(quantity));
		balanceTreat.setTreatId(treatment.getId());
		balanceTreat.setExpireDate(expireDate.split(" ")[0]);
		balanceTreat.setPrice(Double.valueOf(pricePeople));
		balanceTreat.setPurchaseId(null);
		balanceTreat.setPurchaseDetailsId(null);

		//MyUtils.<PurchaseDetails>validateModel(purchaseDetails, errors);
		if (!errors.isEmpty()) {
			MyUtils.showValidationErrors(errors);
			return;
		} else {
			if(this.balanceService.insertBalanceTreat(balanceTreat)) {
				MyUtils.ALERT_SUCCESS("تم إضافة الرصيد بنجاح");
				this.stage.close();
			};
		}


	}
}
