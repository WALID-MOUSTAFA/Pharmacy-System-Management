package com.pharmacy.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;

public class MainController extends MyController {

	@FXML
	private VBox mainCanvas;
	private VBox originalMainCanvas;
	@FXML
	private Button treatments;

	@FXML
	private VBox nav;

	public void swapMainCanvasContent(Parent root) {
		if(this.originalMainCanvas== null) {
			this.originalMainCanvas = new VBox();
			this.originalMainCanvas.getChildren().setAll(this.mainCanvas.getChildren());
		}
		mainCanvas.getChildren().clear();
		mainCanvas.getChildren().addAll(root);
	}

	public void setMedicineGraphic() {
		Image image= new Image(getClass().getResourceAsStream
				       ("/assets/treatments.png"));
		ImageView imageView= new ImageView(image);
		imageView.setFitHeight(100);
		imageView.setFitWidth(100);
		this.treatments.setGraphic(imageView);
	}

	@FXML
	public void initialize() {
		//this.setMedicineGraphic();

	}

	@FXML
	public void showTreatmentsScene() throws IOException {
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Medicine.fxml"));
        TreatmentController treatmentController = new TreatmentController();
        this.stage.setTitle("المنتجات");
        treatmentController.setStage(this.stage);

        loader.setController(treatmentController);
        Parent root= loader.<VBox>load();
		swapMainCanvasContent(root);
    }


	@FXML
	public void showPurchasesScene() throws IOException, SQLException {
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/Purchases.fxml"));
		PurchasesController purchasesController= new PurchasesController();
		purchasesController.setStage(this.stage);
		loader.setController(purchasesController);
		Parent root= loader.<VBox>load();
		swapMainCanvasContent(root);

	}

	@FXML
	public void showSuppliersScene() throws IOException, SQLException {
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/Suppliers.fxml"));
		SuppliersController suppliersController= new SuppliersController();
		this.stage.setTitle("الموردين");
		suppliersController.setStage(this.stage);
		loader.setController(suppliersController);
		Parent root= loader.<VBox>load();
		swapMainCanvasContent(root);
	}


	@FXML
	public void showCustomerScene() throws IOException, SQLException {
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/Customer.fxml"));
		CustomerController customerController= new CustomerController();
		customerController.setStage(this.stage);
		stage.setTitle("العملاء");
		loader.setController(customerController);
		Parent root= loader.<VBox>load();
		swapMainCanvasContent(root);
	}


	@FXML
	public void showBillScene() throws IOException, SQLException {
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/Bill.fxml"));
		BillController billController= new BillController();
		billController.setStage(this.stage);
		stage.setTitle("المبيعات");
		loader.setController(billController);
		Parent root= loader.<VBox>load();
		swapMainCanvasContent(root);
	}


	@FXML
	public void showExpensesScene() throws IOException, SQLException {
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/Expenses.fxml"));
		ExpensesController expensesController= new ExpensesController();
		expensesController.setStage(this.stage);
		stage.setTitle("المصروفات");
		loader.setController(expensesController);
		Parent root= loader.<VBox>load();
		swapMainCanvasContent(root);
	}

	@FXML
	public void showFormTypeTreatScene () throws IOException, SQLException {
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/TreatTypeAndForm.fxml"));
		TreatTypeAndFormController treatTypeAndFormController= new TreatTypeAndFormController();
		treatTypeAndFormController.setStage(this.stage);
		stage.setTitle("نوع وتركيب المنتجات");
		loader.setController(treatTypeAndFormController);
		Parent root= loader.<VBox>load();
		swapMainCanvasContent(root);
	}

}
