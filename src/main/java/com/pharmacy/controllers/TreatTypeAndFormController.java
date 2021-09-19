package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.TreatForm;
import com.pharmacy.POGO.TypeTreat;
import com.pharmacy.services.TreatmentService;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TreatTypeAndFormController extends MyController {

    private TreatmentService treatmentService;
    private TypeTreat currentType;
    private TreatForm currentForm;

    @FXML private Button editTypeButton;
    @FXML private Button deleteTypeButton;
    @FXML private Button editFormButton;
    @FXML private Button deleteFormButton;
    @FXML private TableView typeTableView;
    @FXML private TableView formTableView;
    @FXML private TextField typeName;
    @FXML private TextField formName;

    public TreatTypeAndFormController() {
        this.treatmentService= new TreatmentService();
    }

    @FXML
    private void initialize() throws SQLException{
        this.initializeFormTableView();
        this.initializeTypeTableView();
        this.editFormButton.disableProperty().bind(Bindings.isEmpty(this.formTableView.getSelectionModel().getSelectedItems()));
        this.deleteFormButton.disableProperty().bind(Bindings.isEmpty(this.formTableView.getSelectionModel().getSelectedItems()));
        this.editTypeButton.disableProperty().bind(Bindings.isEmpty(this.typeTableView.getSelectionModel().getSelectedItems()));
        this.deleteTypeButton.disableProperty().bind(Bindings.isEmpty(this.typeTableView.getSelectionModel().getSelectedItems()));
    }



    @FXML
    private void showEditForm() throws IOException, SQLException {
           Stage stage= new Stage();
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation
                    (getClass().getResource("/fxml/EditTreatForm.fxml"));
            EditTreatFormController editTreatFormController=
                    new EditTreatFormController();
            editTreatFormController.setStage(stage);
            editTreatFormController.setTreatForm(this.currentForm);
            loader.setController(editTreatFormController);
            Parent root= loader.load();
            stage.setScene(new Scene(root));
            stage.showAndWait();
            this.formTableView.getColumns().clear();
            this.initializeFormTableView();

    }

    @FXML
    private void deleteForm() throws SQLException {
      try {
          if (this.treatmentService.deleteTreatForm(this.currentForm.getId())) {
              this.formTableView.getItems().remove(this.currentForm);
          } else {
              MyUtils.ALERT_ERROR("لا يمكن الحذف!");
          }
      } catch (SQLException e){
          MyUtils.ALERT_ERROR("لا يمكن الحذف!");
      }
    }

    private void initializeFormTableView() throws SQLException {
        List<TreatForm> forms= this.treatmentService.getAllTreatForms();
        TableColumn<TypeTreat, String> nameColumn = new TableColumn<>("التركيبة");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.formTableView.getColumns().add(nameColumn);
        this.formTableView.setItems(FXCollections.observableArrayList(forms));

        this.formTableView.getSelectionModel().selectedItemProperty().addListener((observable, old, neo)->
        {
            this.currentForm= (TreatForm) neo;
        });


    }


    @FXML
    private void addForm() throws SQLException {
        String formname=this.formName.getText();
        if(formname.isEmpty()) {
            MyUtils.ALERT_ERROR("يجب كتابة التركيبة");
            return;
        }
        TreatForm treatForm= new TreatForm();
        treatForm.setName(formname);
        long inserted= this.treatmentService.insertTreatForm(treatForm);
        if(inserted== 0) {
            MyUtils.ALERT_ERROR("حدث خطأ أثناء الحفظ!");
        } else {
            treatForm.setId(inserted);
        }
        this.formTableView.getItems().add(treatForm);
    }



    @FXML void deleteType() throws SQLException {
       try{
        if(this.treatmentService.deleteTreatType(this.currentType.getId())){
            this.typeTableView.getItems().remove(this.currentType);
        } else {
            MyUtils.ALERT_ERROR("لا يمكن الحذف!");
        }
       } catch (SQLException e) {
           MyUtils.ALERT_ERROR("لا يمكن الحذف!");

       }
    }

    @FXML
    private void initializeTypeTableView() throws SQLException {
        Task<List<TypeTreat>> fetchTypesTask= new Task<List<TypeTreat>>() {
            @Override
            protected List<TypeTreat> call() throws Exception {
               return TreatTypeAndFormController.this.treatmentService.getAllTreatTypes();
            }
        };
        fetchTypesTask.setOnSucceeded(e-> {
            this.typeTableView.setItems(FXCollections.observableArrayList(fetchTypesTask.getValue()));
        });
        Thread t= new Thread(fetchTypesTask);
        t.start();

        TableColumn<TypeTreat, String> nameColumn = new TableColumn<>("النوع");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("typename"));
        this.typeTableView.getColumns().add(nameColumn);

        this.typeTableView.getSelectionModel().selectedItemProperty().addListener((observable, old, neo)->
        {
        this.currentType= (TypeTreat) neo;
        });

    }

    @FXML
    private void addType() throws SQLException {
        String typename=this.typeName.getText();
        if(typename.isEmpty()) {
            MyUtils.ALERT_ERROR("يجب كتابة نوع المنتج");
            return;
        }
        TypeTreat typeTreat= new TypeTreat();
        typeTreat.setTypename(typename);
        long inserted= this.treatmentService.insertTreatType(typeTreat);
        if(inserted== 0) {
            MyUtils.ALERT_ERROR("حدث خطأ أثناء الحفظ!");
        } else {
            typeTreat.setId(inserted);
        }
        this.typeTableView.getItems().add(typeTreat);
    }

    @FXML
    private void showEditType() throws IOException, SQLException {
        Stage stage= new Stage();
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation
                (getClass().getResource("/fxml/EditTreatType.fxml"));
        EditTreatTypeController editTreatTypeController=
                new EditTreatTypeController();
        editTreatTypeController.setStage(stage);
        editTreatTypeController.setTreatType(this.currentType);
        loader.setController(editTreatTypeController);
        Parent root= loader.load();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        this.typeTableView.getColumns().clear();
        this.initializeTypeTableView();
    }
}
