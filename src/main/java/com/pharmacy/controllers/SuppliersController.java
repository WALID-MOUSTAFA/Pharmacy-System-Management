package com.pharmacy.controllers;

import com.pharmacy.POGO.Supplier;
import com.pharmacy.services.SuppliersService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class SuppliersController extends MyController{

    private SuppliersService suppliersService;

    @FXML
    private TableView suppliersTableView;


    public SuppliersController() throws SQLException{
        this.suppliersService= new SuppliersService();
    }

    @FXML
    protected  void backToControlPanel() throws IOException {
        this.swapWithControlPanelScene();
    }

    private void initializeTableView() throws SQLException {
        ObservableList<Supplier> suppliers= FXCollections
                .observableArrayList(this.suppliersService.getAllSuppliers());

        TableColumn<Supplier, String> nameColumn= new TableColumn<>("الاسم");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Supplier, String> phoneColumn= new TableColumn<>("رقم الهاتف");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Supplier, String> addressColumn= new TableColumn<>("العنوان");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Supplier, String> cashColumn= new TableColumn<>("الكاش");
        cashColumn.setCellValueFactory(new PropertyValueFactory<>("cash"));

        TableColumn<Supplier, String> dateAtColumn= new TableColumn<>("تاريخ الإضافة");
        dateAtColumn.setCellValueFactory(new PropertyValueFactory<>("dateAt"));

        this.suppliersTableView.getColumns().addAll(
                nameColumn,
                phoneColumn,
                addressColumn,
                cashColumn,
                dateAtColumn);

        this.suppliersTableView.setItems(suppliers);
    }


    @FXML
    public void initialize () throws SQLException{
        this.initializeTableView();
    }

    @FXML
    private void showAsddNewSupplier() throws IOException, SQLException {
        Stage stage= new Stage();
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/CreateSupplier.fxml"));
        CreateSupplierController createSupplierController= new CreateSupplierController();
        createSupplierController.setStage(stage);
        loader.setController(createSupplierController);
        Parent root= loader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
