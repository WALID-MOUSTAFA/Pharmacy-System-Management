package com.pharmacy.controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class StoreController extends MyController {


	//NOTE(walid):this is passed from the MainController to swap the treatment
	//scene on it.
	private VBox mainCanvas;

	public VBox getMainCanvas() {
		return mainCanvas;
	}

	public void setMainCanvas(VBox mainCanvas) {
		this.mainCanvas = mainCanvas;
	}


	public void swapMainCanvasContent(Parent root) {
		mainCanvas.getChildren().clear();
		mainCanvas.getChildren().addAll(root);
	}
	
	@FXML
	public void showTreatmentsScene() throws IOException {
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/Medicine.fxml"));
		TreatmentController treatmentController= new TreatmentController();
		treatmentController.setStage(this.stage);
		loader.setController(treatmentController);
		Parent root= loader.<VBox>load();
		swapMainCanvasContent(root);
	}

}
