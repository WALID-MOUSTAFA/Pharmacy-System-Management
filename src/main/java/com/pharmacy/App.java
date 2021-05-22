package com.pharmacy;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JavaFX App
 */
public class App extends Application {


	@Override
	public void start(Stage stage) throws SQLException {

		Connection con= DatabaseConnection.getInstance().getConnection();
		String query= "select * from customer";
		Statement stmt= con.createStatement();
		ResultSet rs= stmt.executeQuery(query);
		while(rs.next()) {
			System.out.println(rs.getString("name"));
		}
		System.out.println("up and running النيبت");
		stage.close();
	}
	
	public static void main(String[] args) {
		launch();
	}

}
