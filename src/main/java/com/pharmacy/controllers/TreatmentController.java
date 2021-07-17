package com.pharmacy.controllers;

import com.pharmacy.POGO.DetailedTreatment;
import com.pharmacy.POGO.Treatment;
import com.pharmacy.services.TreatmentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.pharmacy.DatabaseConnection;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;

import java.io.IOException;
import java.sql.SQLException;

public class TreatmentController extends MyController{


    @FXML
    private Button backToControlPanelButton;
    @FXML
    private TableView treatmentsTableView;

   // @FXML
   // private Text detailedTreatment;

    private TreatmentService treatmentService;

    public TreatmentController() {
        this.treatmentService= new TreatmentService();
    }

    private void initializeTableView() throws SQLException{
        ObservableList<Treatment> treatments= FXCollections
                .observableArrayList(treatmentService.getAllTreatments());

        TableColumn<Treatment, String> nameColumn= new TableColumn<>("الاسم");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Treatment, String> dateAtColumn= new TableColumn<>("تاريخ الإضافة");
        dateAtColumn.setCellValueFactory(new PropertyValueFactory<>("dateAt"));

        TableColumn<Treatment, String> statusColumn= new TableColumn<>("الحالة");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Treatment, String> placeColumn= new TableColumn<>("المكان");
        placeColumn.setCellValueFactory(new PropertyValueFactory<>("place"));

        TableColumn<Treatment, String> lowCountColumn= new TableColumn<>("العدد الحرج");
        lowCountColumn.setCellValueFactory(new PropertyValueFactory<>("lowcount"));

        TableColumn<Treatment, String> companyColumn= new TableColumn<>("الشركة");
        companyColumn.setCellValueFactory(new PropertyValueFactory<>("company"));


        this.treatmentsTableView.getColumns().addAll(nameColumn,
                                                    dateAtColumn,
                                                    placeColumn,
                                                    companyColumn);
        this.treatmentsTableView.setItems(treatments);


    }

    @FXML
    private void initialize() throws SQLException {
        this.initializeTableView();

    }

    @FXML
    private void backToControlPanel() throws IOException {
        this.swapWithControlPanelScene();
    }


    @FXML
    private void viewDetailedTreatment() throws  SQLException{
//        this.detailedTreatment.setText("");
//        Treatment treatment= (Treatment) this.treatmentsTableView.getSelectionModel().getSelectedItem();
//        long id= treatment.getId();
//        DetailedTreatment detailedTreatment= treatmentService.getDetailedTreatment(id);
//        StringBuilder detailsBuilder= new StringBuilder();
//        detailsBuilder.append("اسم الدواء: " + detailedTreatment.getName());
//        detailsBuilder.append("\n");
//        detailsBuilder.append("التركيبة:"  + detailedTreatment.getFormTreatName());
//        detailsBuilder.append("\n");
//        detailsBuilder.append("اسم الشركة: " + detailedTreatment.getCompany());
//        detailsBuilder.append("\n");
//        detailsBuilder.append(": تاريخ الإضافة: " + detailedTreatment.getDateAt());
//        detailsBuilder.append("\n");
//        detailsBuilder.append("النوع: " + detailedTreatment.getTypeTreatName());
//        detailsBuilder.append("\n");
//        detailsBuilder.append("الحالة: " + detailedTreatment.getStringStatus());
//        detailsBuilder.append("\n");
//        detailsBuilder.append("العدد الحرج: " + detailedTreatment.getLowcount());
//        detailsBuilder.append("\n");
//        this.detailedTreatment.setText(detailsBuilder.toString());
//        detailsBuilder.delete(0, detailsBuilder.length()-1);
//        System.out.println(this.detailedTreatment.getText());
    }


    @FXML
    private void showAsddNewTreatment() throws IOException {
        Stage stage= new Stage();
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/CreateTreatment.fxml"));
        CreateTreatmentController createTreatmentController= new CreateTreatmentController();
        createTreatmentController.setStage(stage);
        loader.setController(createTreatmentController);
        Parent root= loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
