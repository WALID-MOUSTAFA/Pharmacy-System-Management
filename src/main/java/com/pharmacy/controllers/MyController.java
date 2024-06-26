package com.pharmacy.controllers;

import com.pharmacy.Filters;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class MyController extends Filters {
    protected Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    protected void swapWithControlPanelScene() throws IOException {
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Main.fxml"));
        MainController mainController= new MainController();
        mainController.setStage(this.stage);
        loader.setController(mainController);
        Parent root= loader.load();
        this.stage.setTitle("صيدلية");
        this.stage.getScene().setRoot(root);

    }



    @FXML
    protected  void backToControlPanel() throws IOException {
        this.swapWithControlPanelScene();
    }


    @FXML
    public void closeWindow() {
	this.stage.close();
    }

}
