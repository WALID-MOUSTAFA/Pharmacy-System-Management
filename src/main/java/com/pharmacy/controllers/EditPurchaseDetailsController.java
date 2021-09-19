package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.BalanceTreat;
import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.POGO.PurchaseDetails;
import com.pharmacy.POGO.Treatment;
import com.pharmacy.services.BalanceService;
import com.pharmacy.services.PurchaseDetailsService;
import com.pharmacy.services.TreatmentService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EditPurchaseDetailsController extends MyController{
		
	
	private PurchaseDetailsService purchaseDetailsService;
	
	
	@FXML
	private TextField quantity;
	
	@FXML
	private TextField pricePharmacy ;
	
	@FXML
	private TextField totalPharmacy;

	@FXML
	private TextField pricePeople;

	@FXML
	private TextField totalPeople;

	@FXML
	private DatePicker expireDate;

	@FXML
	private DatePicker productionDate;
	@FXML TextField discount;

	@FXML
	private ComboBox treatName;

	private long id;
	
	public EditPurchaseDetailsController(long id) throws SQLException {
		this.purchaseDetailsService= new PurchaseDetailsService();
		this.id= id;
	}

	
	public PurchaseDetails getSpecificPurchaseDetails() throws SQLException
	{
		PurchaseDetails purchaseDetails=
			this.purchaseDetailsService
			.getPurchaseDetailsById(this.id);
		return purchaseDetails;
	}

	
	public void initializeTreatmentCombo() throws SQLException {
		TreatmentService ts= new TreatmentService();
		List<DetailedTreatment> treatments= ts.getAllTreatments();
		for (DetailedTreatment t: treatments) {
			String m= t.getName()+"-"+t.getTypeTreatName();
			this.treatName.getItems().add(m);

		}
	}

	
	public void initializeForm() throws SQLException{
		PurchaseDetails pd= this.getSpecificPurchaseDetails();

		this.quantity.setText(String.valueOf(pd.getQuantity()));
		this.totalPharmacy.setText(String.valueOf(pd.getTotalPharmacy()));
		this.pricePharmacy.setText(String.valueOf(pd.getPricePharmacy()));
		this.totalPeople.setText(String.valueOf(pd.getTotalPeople()));
		this.pricePeople.setText(String.valueOf(pd.getPricePeople()));
		this.expireDate.setValue(LocalDate.parse
					 (pd.getExpireDate().split(" ")[0] ));
		this.productionDate
			.setValue(LocalDate
				  .parse(pd.getProductionDate().split(" ")[0]));
		this.discount.setText(pd.getDiscount());
		//this.initializeTreatmentCombo();
		this.treatName.setValue(pd.getTreat().getName()+"-"+pd.getTreat().getTypeTreatName());

		try {
			StringConverter<? extends Number> converter= new DoubleStringConverter();
			SimpleDoubleProperty quantityProperty= new SimpleDoubleProperty(pd.getQuantity());
			SimpleDoubleProperty phramacyPriceProperty= new SimpleDoubleProperty(pd.getPricePharmacy());
			SimpleDoubleProperty peoplePriceProperty= new SimpleDoubleProperty(pd.getPricePeople());

			Bindings.bindBidirectional(this.quantity.textProperty(), quantityProperty, (StringConverter<Number>)converter);
			Bindings.bindBidirectional(this.pricePeople.textProperty(), peoplePriceProperty, (StringConverter<Number>)converter);
			Bindings.bindBidirectional(this.pricePharmacy.textProperty(), phramacyPriceProperty, (StringConverter<Number>)converter);

			this.totalPharmacy.textProperty().bind(Bindings.multiply(quantityProperty, phramacyPriceProperty).asString());
			this.totalPeople.textProperty().bind(Bindings.multiply(quantityProperty, peoplePriceProperty).asString());

		} catch (NumberFormatException e) {MyUtils.ALERT_ERROR("ادخل الأرقام بصورة صحيحة");}

	}


	@FXML
	public void initialize() throws SQLException {
		this.initializeForm();

			MyUtils.setDatePickerFormat(this.expireDate);
		MyUtils.setDatePickerFormat(this.productionDate);
	}

	
	@FXML
	public void editPurchaseDetails() throws SQLException{

		List<String> errors= new ArrayList<>();
		TreatmentService treatmentService= new TreatmentService();
		PurchaseDetails purchaseDetails= this.getSpecificPurchaseDetails();
		double oldQantity= purchaseDetails.getQuantity();

		String quantity;
		String pricePharmacy;
		String totalPharmacy;
		String pricePeople;
		String totalPeople;
		String expireDate;
		String productionDate;
		String treatName;
		String discount;
		Treatment treat;


		
		if(this.expireDate.getValue() == null) {
			errors.add("يجب اختيار تاريخ الصلاحية");
			MyUtils.showValidationErrors(errors);
			return;
		}

		if(this.productionDate.getValue() == null) {
			errors.add("يجب اختيار تاريخ الإنتاج");
			MyUtils.showValidationErrors(errors);
			return;
		}

		
		if(this.treatName.getValue() == null) {
			errors.add("يجب اختيار اسم المنتج");
			MyUtils.showValidationErrors(errors);
			return;
		}

		
		quantity= this.quantity.getText();
		totalPharmacy= this.totalPharmacy.getText();
		totalPeople = this.totalPeople.getText();
		pricePharmacy=this.pricePharmacy.getText();
		pricePeople= this.pricePeople.getText();
		expireDate= this.expireDate.getValue().toString();
		productionDate= this.productionDate.getValue().toString();
		discount= this.discount.getText();
		String name= this.treatName.getValue().toString();
		treat= treatmentService
			.getTreatmentByName(name.split("-")[0], name.split("-")[1]);

		try {
		purchaseDetails.setQuantity
			(!quantity.isEmpty()? Double.valueOf(quantity):0);
		purchaseDetails.setTotalPharmacy
			(!totalPharmacy.isEmpty()?Double.valueOf(totalPharmacy):0);
		purchaseDetails.setPricePharmacy
			(!pricePharmacy.isEmpty()?Double.valueOf(pricePharmacy):0);
		purchaseDetails.setTotalPeople
			(!totalPeople.isEmpty()? Double.valueOf(totalPeople):0);
		purchaseDetails.setPricePeople
			(!pricePeople.isEmpty()? Double.valueOf(pricePeople):0);
		} catch (NumberFormatException e) {
			MyUtils.ALERT_ERROR("ادخل البيانات الرقمية بصورة صحيحة");
			return;
		}
		purchaseDetails.setExpireDate
			(expireDate);
		purchaseDetails.setProductionDate
			(productionDate);

		purchaseDetails.setTreat_id(treat.getId());
		purchaseDetails.setDiscount(discount);
		
		
		MyUtils.<PurchaseDetails>validateModel(purchaseDetails, errors);
		if(!errors.isEmpty()){
			MyUtils.showValidationErrors(errors);
			return;
		}

		
		if(this.purchaseDetailsService
		   .updatePurchaseDetails(purchaseDetails)) {
			this.fixBalanceTreatDates(purchaseDetails);
			this.fixBalanceTreat(purchaseDetails, oldQantity);
			this.stage.close();
		} else {
			Alert alert= new Alert(Alert.AlertType.ERROR);
			alert.setContentText("حدث خطأ ما أثناء عملية الحفظ");
			alert.show();
		}
	}

	private void fixBalanceTreat(PurchaseDetails purchaseDetails, double oldQantity) throws SQLException{
		BalanceService balanceService= new BalanceService();
		double purchaseNewQantity= purchaseDetails.getQuantity();
		BalanceTreat relatedBalanceTreat = balanceService.getBalanceTreatbyPurchaseDetailsId(purchaseDetails.getId());
		if (relatedBalanceTreat == null) return;
		double oldbalanceQantity= relatedBalanceTreat.getQuantity();
		if(purchaseNewQantity - oldQantity > 0) {
			double diff= purchaseNewQantity- oldQantity;
			balanceService.increaseBalance(relatedBalanceTreat.getId(), diff);
		}else if (purchaseNewQantity-oldQantity < 0 && oldQantity != 0) {
			double diff= oldQantity-purchaseNewQantity;
			if(oldbalanceQantity - diff < 0) {
				return; //this will cause minus quantity
			}
			balanceService.decreaseQuantity(relatedBalanceTreat.getId(), diff);
		}else {
			return; //do nothing
		}
	}

	public void fixBalanceTreatDates(PurchaseDetails purchaseDetails) throws SQLException {
		BalanceService balanceService= new BalanceService();
		BalanceTreat relatedBalanceTreat = balanceService.getBalanceTreatbyPurchaseDetailsId(purchaseDetails.getId());
		relatedBalanceTreat.setExpireDate(purchaseDetails.getExpireDate());
		balanceService.updateBalanceTreatExpire(relatedBalanceTreat);
	}

}
