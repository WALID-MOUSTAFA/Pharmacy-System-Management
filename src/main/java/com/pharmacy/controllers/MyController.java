package com.pharmacy.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MyController {
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
        this.stage.setScene(new Scene(root, 900, 500));

    }
}
