package com.pharmacy;

import com.pharmacy.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.io.IOException;
/**
 * JavaFX App
 */
public class App extends Application {


	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation(getClass().getResource("/fxml/Main.fxml"));
		MainController mainController= new MainController();
		mainController.setStage(primaryStage);
		loader.setController(mainController);
		Parent root= loader.<VBox>load();
		primaryStage.setTitle("صيدلية");
		primaryStage.setScene(new Scene(root, 900, 500));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch();
	}

}
