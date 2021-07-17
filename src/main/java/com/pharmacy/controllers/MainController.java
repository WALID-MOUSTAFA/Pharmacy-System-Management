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
	private Button treatments;


	public void setMedicineGraphic() {
		Image image= new Image(getClass()
				       .getResourceAsStream("/assets/treatments.png"));
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
        treatmentController.setStage(this.stage);
        loader.setController(treatmentController);
        Parent root= loader.<VBox>load();
        this.stage.setScene(new Scene(root));
    }


	@FXML
	public void showPurchasesScene() throws IOException {
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/Purchases.fxml"));
		PurchasesController purchasesController= new PurchasesController();
		purchasesController.setStage(this.stage);
		loader.setController(purchasesController);
		Parent root= loader.<VBox>load();
		this.stage.setScene(new Scene(root));
	}

	@FXML
	public void showSuppliersScene() throws IOException, SQLException {
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/Suppliers.fxml"));
		SuppliersController suppliersController= new SuppliersController();
		suppliersController.setStage(this.stage);
		loader.setController(suppliersController);
		Parent root= loader.<VBox>load();
		this.stage.setScene(new Scene(root));
	}

}
