package com.pharmacy.controllers;

import com.pharmacy.POGO.Purchase;
import com.pharmacy.POGO.PurchaseDetails;
import com.pharmacy.POGO.Supplier;
import com.pharmacy.services.SuppliersService;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

public class SuppliersController extends MyController{

	Executor executor;

	private SuppliersService suppliersService;
	private long currentSupplierId;
	private Supplier currentSupplier; //NOTE(walid): choose whether this or just the id, stupid;

	@FXML private TableView suppliersTableView;
	@FXML private Button editSupplierButton;
	@FXML private Button deleteSupplierButton;
	@FXML private Button showSupplierPaymentButton;
	@FXML private CreateSupplierController createSupplierController;
	@FXML private TextField searchBox;


	public void setCurrentSupplier(Supplier currentSupplier) {
		this.currentSupplier = currentSupplier;
	}

	private void initializeThreadPool() {
		this.executor= Executors.newCachedThreadPool((runnable)-> {
			Thread thread= new Thread(runnable);
			thread.setDaemon(true);
			return thread;
		});
	}

	public SuppliersController() throws SQLException{
        this.suppliersService= new SuppliersService();
        this.initializeThreadPool();
    }

    @FXML
    protected  void backToControlPanel() throws IOException {
        this.swapWithControlPanelScene();
    }

	private void renderSuppliers() {
		Task<List<Supplier>> task= new Task<List<Supplier>>() {
			@Override
			protected List<Supplier> call() throws Exception {
				return SuppliersController.this.suppliersService
						.getAllSuppliers();
			}
		};
		task.setOnSucceeded(e-> {
			this.suppliersTableView.setItems(FXCollections.observableArrayList(
					task.getValue()
			));
		});
		task.setOnFailed(e-> task.getException().printStackTrace());
		this.executor.execute(task);
	}


	public void initializeTableView() throws SQLException {

		this.suppliersTableView.getColumns().clear();
        this.renderSuppliers();
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


		//set listener to set currentSupplierId
		this.suppliersTableView.getSelectionModel()
			.selectedItemProperty()
			.addListener((obsvaal, oldval, newval)-> {
					this.setCurrentSuppleirId
						(((Supplier)newval).getId());
						this.setCurrentSupplier(((Supplier)newval));
				});

    }



	   
		public void addTableViewFocusListeners() {

		this.suppliersTableView.focusedProperty()
			.addListener((observableVal,oldval,newval)-> {
		if(newval) {
		    this.editSupplierButton.setDisable(false);
		    this.deleteSupplierButton.setDisable(false);
		    this.showSupplierPaymentButton.setDisable(false);
		} else {
		    this.editSupplierButton.setDisable(true);
		    this.deleteSupplierButton.setDisable(true);
			this.showSupplierPaymentButton.setDisable(true);
		}
	    });
	}

    @FXML
    public void initialize () throws SQLException{
        this.initializeTableView();
	//	this.addTableViewFocusListeners();
		this.editSupplierButton.disableProperty().bind(Bindings.isEmpty(this.suppliersTableView.getSelectionModel().getSelectedItems()));
		this.deleteSupplierButton.disableProperty().bind(Bindings.isEmpty(this.suppliersTableView.getSelectionModel().getSelectedItems()));
		this.showSupplierPaymentButton.disableProperty().bind(Bindings.isEmpty(this.suppliersTableView.getSelectionModel().getSelectedItems()));

		this.createSupplierController.setSuppliersController(this);
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

	
	@FXML
	private void showEditSupplier() throws SQLException, IOException {
		Stage stage= new Stage();
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation
			(getClass().getResource("/fxml/EditSupplier.fxml"));
		EditSupplierController editSupplierController=
			new EditSupplierController();
		editSupplierController.setId(this.currentSupplierId);
		editSupplierController.setStage(stage);
		loader.setController(editSupplierController);
		Parent root= loader.load();
		stage.setScene(new Scene(root));
		stage.showAndWait();
		this.reInitializeTableView();
	}

	
	
	@FXML
	private void deleteSupplier() throws SQLException{
		if(this.suppliersService.deleteSupplier(this.currentSupplierId)){
			this.suppliersTableView.getItems()
				.remove(this.suppliersTableView
						.getSelectionModel().getSelectedItem());

		}
	}


	private void setCurrentSuppleirId(long id){
		this.currentSupplierId= id;
	}

	private void reInitializeTableView() throws SQLException {
		this.suppliersTableView.getColumns().clear();
		this.initializeTableView();
	}



	@FXML
	private void showSupplierPayment() throws IOException, SQLException{
		Stage stage= new Stage();
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation
				(getClass().getResource("/fxml/SupplierPayment.fxml"));
		SupplierPaymentController supplierPaymentController=
				new SupplierPaymentController();
		supplierPaymentController.setSupplier(this.currentSupplier);
		supplierPaymentController.setStage(stage);
		loader.setController(supplierPaymentController);
		Parent root= loader.load();
		stage.setScene(new Scene(root));

		stage.showAndWait();
	}

	@FXML
	private void doSearch() throws SQLException {

		this.suppliersTableView.setItems(FXCollections.observableArrayList(this.suppliersService.getAllSuppliers()));

		String q= this.searchBox.getText();
		if(q.isEmpty()) {
			this.initializeTableView();
			return;
		}
		ObservableList<Supplier> items= this.suppliersTableView.getItems();
		FilteredList<Supplier> filteredList= new FilteredList<>(items);
		this.suppliersTableView.setItems(filteredList);
		filteredList.setPredicate(new Predicate<Supplier>() {
			@Override
			public boolean test(Supplier supplier) {
				return supplier.getName().contains(q)
						|| supplier.getAddress().contains(q)
						|| supplier.getDateAt().contains(q)
						|| supplier.getPhone().contains(q);
			}
		});
	}
}
