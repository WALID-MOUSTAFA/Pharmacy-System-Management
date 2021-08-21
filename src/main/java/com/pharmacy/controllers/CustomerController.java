//TODO(walid): add edit and delete customer
package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.Customer;
import com.pharmacy.services.CustomerService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerController extends  MyController{

    private  CustomerService customerService;
	//note(walid): a new approach لسة خاطر على بالي;
	private Customer currentCustomer;
	@FXML private TableView customersTableView;
    @FXML private Button deleteCustomerButton;
    @FXML private Button editCustomerButton;
    @FXML private TextField name;
    @FXML private TextField address;
    @FXML private TextField cash;
    

    public CustomerController() throws SQLException {
        this.customerService= new CustomerService();
    }
    
    @FXML
    public void initialize() throws SQLException {
        this.initializeCustomerTableView();
	this.addTableViewFocusListeners();
    }

    private void initializeCustomerTableView() throws SQLException {
        List<Customer> customers= this.customerService.getAllCustomers();
        if(customers == null || customers.size()== 0) {
            return;
        }

        TableColumn<Customer, String> nameColumn= new TableColumn<>("الاسم");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customer, String> addressColumn= new TableColumn<>("العنوان");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Customer, String> cashColumn= new TableColumn<>("الكاش");
        cashColumn.setCellValueFactory(new PropertyValueFactory<>("cash"));

        TableColumn<Customer, String> dateColumn= new TableColumn<>("تاريخ الإنشاء");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateAt"));


        this.customersTableView.getColumns().addAll(nameColumn, addressColumn, cashColumn, dateColumn);
        this.customersTableView.setItems(FXCollections.observableArrayList(customers));


	
	this.customersTableView.getSelectionModel()
		.selectedItemProperty()
		.addListener((obsvaal, oldval, newval)-> {
				this.setCurrentCustomer
					(((Customer)newval));
			});


    }

	@FXML
	private void addCustomer() throws SQLException {
		List<String> errors= new ArrayList<>();
		String name;
		String address;
		double cash;
		
		name = this.name.getText();
		address= this.address.getText();
		cash=!this.cash.getText().isEmpty()?
			Double.valueOf(this.cash.getText()) : 0;

		Customer customer= new Customer();
		customer.setName(name);
		customer.setCash(cash);
		customer.setAddress(address);
		
		MyUtils.<Customer>validateModel(customer, errors);
		if(!errors.isEmpty()){
			MyUtils.showValidationErrors(errors);
			return;
		}

		if(this.customerService.insertCustomer(customer)) {
			this.customersTableView.getItems().add(customer);
		} else {
			//TODO(walid): handle error;
		}
	}


	
	public void addTableViewFocusListeners() {

		this.customersTableView.focusedProperty()
			.addListener((observableVal,oldval,newval)-> {
		if(newval) {
		    this.editCustomerButton.setDisable(false);
		    this.deleteCustomerButton.setDisable(false);
		} else {
		    this.editCustomerButton.setDisable(true);
		    this.deleteCustomerButton.setDisable(true);
		}
	    });
	}

	
	@FXML
	private void editCustomer() throws SQLException, IOException {

		Stage stage= new Stage();
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation
			(getClass().getResource("/fxml/EditCustomer.fxml"));
		EditCustomerController editCustomerController=
			new EditCustomerController();
		editCustomerController.setStage(stage);
		editCustomerController.setCustomer(this.currentCustomer);
		loader.setController(editCustomerController);
		Parent root= loader.load();
		stage.setScene(new Scene(root));

		stage.showAndWait();
		this.reInitializeTableView();
		
	}


	private void reInitializeTableView() throws SQLException {
		this.customersTableView.getColumns().clear();
		this.initializeCustomerTableView();
	}


	
	private void setCurrentCustomer(Customer customer){
		this.currentCustomer= customer;
	}


	@FXML
	private void deleteCustomer() throws SQLException {
		if(this.customerService.deleteCustomer
		   (this.currentCustomer.getId())) {
			this.customersTableView.getItems().remove(currentCustomer);
		}
	}

}
