package com.pharmacy.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainController extends MyController {

    @FXML
    private Button treatments;
    @FXML
    Button purchasesButton;
    public boolean isLogined = true;

    public void openWindow(Stage stage, Parent root, String title) throws IOException {
        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    private boolean logInMiddleWare() throws IOException, SQLException {
        if (isLogined) {
            return true;
        }
        LoginController loginController = new LoginController();
        loginController.setSender(this);
        return loginController.showLoginForm();
    }


    @FXML
    public void initialize() {
    }

    @FXML
    public void showTreatmentsScene() throws IOException, SQLException {
        if(!this.logInMiddleWare()) {return;}
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Medicine.fxml"));
        Stage stage = new Stage();
        TreatmentController treatmentController = new TreatmentController();
        treatmentController.setStage(stage);
        loader.setController(treatmentController);
        Parent root = loader.<VBox>load();
        this.openWindow(stage, root, "المنتجات");
    }


    @FXML
    public void showPurchasesScene() throws IOException, SQLException {
        if(!this.logInMiddleWare()) {return;}
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Purchases.fxml"));
        Stage stage = new Stage();
        PurchasesController purchasesController = new PurchasesController();
        purchasesController.setStage(stage);
        loader.setController(purchasesController);
        Parent root = loader.<VBox>load();
        openWindow(stage, root, "المشتريات");
    }

    @FXML
    public void showSuppliersScene() throws IOException, SQLException {
        if(!this.logInMiddleWare()) {return;}
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Suppliers.fxml"));
        Stage stage = new Stage();
        SuppliersController suppliersController = new SuppliersController();
        this.stage.setTitle("الموردين");
        suppliersController.setStage(stage);
        loader.setController(suppliersController);
        Parent root = loader.<VBox>load();
        this.openWindow(stage, root, "الموردين");
    }


    @FXML
    public void showCustomerScene() throws IOException, SQLException {
        if(!this.logInMiddleWare()) {return;}
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Customer.fxml"));
        Stage stage = new Stage();
        CustomerController customerController = new CustomerController();
        customerController.setStage(stage);
        loader.setController(customerController);
        Parent root = loader.<VBox>load();
        this.openWindow(stage, root, "العملاء");
    }


    @FXML
    public void showBillScene() throws IOException, SQLException {
        //if(!this.logInMiddleWare()) {return;}
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Bill.fxml"));
        Stage stage = new Stage();
        BillController billController = new BillController();
        billController.setStage(stage);
        loader.setController(billController);
        Parent root = loader.<VBox>load();
        this.openWindow(stage, root, "المبيعات");
    }


    @FXML
    public void showExpensesScene() throws IOException, SQLException {
        if(!this.logInMiddleWare()) {return;}
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Expenses.fxml"));
        Stage stage = new Stage();
        ExpensesController expensesController = new ExpensesController();
        expensesController.setStage(stage);
        loader.setController(expensesController);
        Parent root = loader.<VBox>load();
        this.openWindow(stage, root, "المصروفات");
    }

    @FXML
    public void showFormTypeTreatScene() throws IOException, SQLException {
        if(!this.logInMiddleWare()) {return;}
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/TreatTypeAndForm.fxml"));
        Stage stage = new Stage();
        TreatTypeAndFormController treatTypeAndFormController = new TreatTypeAndFormController();
        treatTypeAndFormController.setStage(stage);
        loader.setController(treatTypeAndFormController);
        Parent root = loader.<VBox>load();
        this.openWindow(stage, root, "نوع وتركيب المنتجات");
    }

    @FXML
    public void showInventoryCountsScene() throws IOException, SQLException {
        if(!this.logInMiddleWare()) {return;}
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/InventoryCounts.fxml"));
        Stage stage = new Stage();
        InventoryCountsController inventoryCountsController = new InventoryCountsController();
        inventoryCountsController.setStage(stage);
        loader.setController(inventoryCountsController);
        Parent root = loader.<VBox>load();
        this.openWindow(stage, root, "الجرد");
    }


    @FXML
    public void showExpireDatesScene() throws SQLException, IOException {
	if(!this.logInMiddleWare()) {return;}
        ExpireDatesController expireDatesController= new ExpireDatesController();
	    expireDatesController.showStage("تواريخ الصلاحية");
    }

}
