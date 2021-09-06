package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController extends MyController{
    private Object sender;
    private boolean loggedIn= false;
    private UserService userService;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    public LoginController() throws SQLException {
        this.userService= new UserService();
    }

    public boolean showLoginForm() throws IOException {
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/Login.fxml"));
        Stage stage= new Stage();
        this.setStage(stage);
        loader.setController(this);
        Parent root= loader.<VBox>load();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
        return loggedIn;
    }

    public void setSender(Object sender) {
        this.sender= sender;
    }

    @FXML private void doLogin() throws SQLException {
        String username= this.username.getText();
        String password= this.password.getText();
        if(this.userService.logIn(username, password)) {
            this.loggedIn= true;
            ((MainController)this.sender).isLogined= true;
            this.stage.close();
            return;
        } else {
            MyUtils.ALERT_ERROR("خطأ في اسم المستخدم أو كلمة السر.");
        }
    }
}
