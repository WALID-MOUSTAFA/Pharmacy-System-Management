package com.pharmacy.controllers;

import com.pharmacy.services.SettingsService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ChooseReciptPrinterController extends MyController{

        private SettingsService settingsService;

        @FXML
        private ComboBox<String> printerCombo;

        public ChooseReciptPrinterController() throws SQLException {
                this.settingsService = new SettingsService();
        }

        public void openWindow(Stage stage, Parent root, String title) throws IOException {
                stage.setScene(new Scene(root, 300, 300));
                stage.setTitle(title);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
        }

        public void open() throws IOException {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/ChoosePrinter.fxml"));
                Stage stage = new Stage();
                this.setStage(stage);
                loader.setController(this);
                Parent root = loader.<VBox>load();
                openWindow(stage, root, "الطابعات");
        }

        @FXML
        private void initialize() throws SQLException {
                for(String s : this.settingsService.printList()) {
                        this.printerCombo.getItems().add(s);
                }

                this.printerCombo.setOnAction(e-> {
                        try {
                                this.settingsService.setSetting("reciptPrinterName", this.printerCombo.getSelectionModel().getSelectedItem());
                        } catch (SQLException throwables) {
                                throwables.printStackTrace();
                        }
                });

                this.printerCombo.setValue(this.settingsService.getSetting("reciptPrinterName"));

        }
}
