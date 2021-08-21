package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.Expense;
import com.pharmacy.POGO.ExpenseType;
import com.pharmacy.POGO.Supplier;
import com.pharmacy.services.ExpensesService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

public class ExpensesController extends MyController{

	Executor executor;
	private ExpensesService expensesService;
	private Expense currentExpense;
	private ExpenseType currentExpenseType;
	@FXML private TableView expensesTableView;
	@FXML private TextField expenseValue;
	@FXML private DatePicker expenseDate;
	@FXML private ComboBox expenseType;

	@FXML Button editExpenseButton;
	@FXML Button deleteExpenseButton;
	@FXML Button deleteExpenseTypeButton;
	@FXML Button editExpenseTypeButton;

	@FXML private TextField expenseTypeAddName; //for creating new Expense type
	@FXML private TableView expenseTypeTableView;
	@FXML private TextField searchBox;


	private List<ExpenseType> getAllExpenseTypes() throws SQLException{
		List<ExpenseType>types= this.expensesService.getAllExpenseTypes();
		return types;
	}

	private void initializeThreadPool() {
		this.executor= Executors.newCachedThreadPool((runnable)-> {
			Thread thread= new Thread(runnable);
			thread.setDaemon(true);
			return thread;
		});
	}

	public ExpensesController() throws SQLException {
		this.expensesService= new ExpensesService();
		this.initializeThreadPool();
	}

	@FXML
	private void initialize() throws SQLException{
		this.initializeExpenseTypeCombo();
		this.initializeExpenseTableView();
		this.initializeExpenseTypeTableView();
		MyUtils.setDatePickerFormat(expenseDate);
		
	}

	private List<Expense> getAllExpenses() throws SQLException {
		return this.expensesService.getAllExpenses();
	}


	private void renderSuppliers() {
		Task<List<Expense>> task= new Task<List<Expense>>() {
			@Override
			protected List<Expense> call() throws Exception {
				return ExpensesController.this.getAllExpenses();
			}
		};
		task.setOnSucceeded(e-> {
			this.expensesTableView.setItems(FXCollections.observableArrayList(
					task.getValue()
			));
		});
		task.setOnFailed(e-> task.getException().printStackTrace());
		this.executor.execute(task);
	}

	private void initializeExpenseTableView() throws SQLException {
		this.expensesTableView.getColumns().clear();
		this.renderSuppliers();

		TableColumn valueColumn= new TableColumn("القيمة");
		valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
		TableColumn dateColumn= new TableColumn("التاريخ");
		dateColumn.setCellValueFactory(new PropertyValueFactory<>
					       ("dateAt"));
		TableColumn<Expense, String> typeColumn=
			new TableColumn("نوع المصروف");
		typeColumn.setCellValueFactory
			(row-> new SimpleStringProperty
			 (row.getValue().getExpenseType().getName()));
		this.expensesTableView.getColumns().addAll
			(valueColumn, dateColumn, typeColumn);

		this.expensesTableView.getSelectionModel()
			.selectedItemProperty()
			.addListener((obsvaal, oldval, newval)-> {
					this.setCurrentExpense
						(((Expense)newval));
				});

		this.expensesTableView.focusedProperty().addListener(((observable, oldValue, newValue) -> {
			if(newValue) {
				this.editExpenseButton.setDisable(false);
				this.deleteExpenseButton.setDisable(false);
			}else {
				this.editExpenseButton.setDisable(true);
				this.deleteExpenseButton.setDisable(true);
			}
		}));

	}


		
	private void initializeExpenseTypeCombo() throws SQLException{
		for(ExpenseType et : this.getAllExpenseTypes()){
			this.expenseType.getItems().add(et.getName());
		}
	}

	@FXML
	public void addExpense() throws SQLException {
		List<String> errors = new ArrayList<>();
		String value;
		String date;
		String typeName;
		Expense expense = new Expense();
		ExpenseType expenseType = new ExpenseType();

		if (this.expenseType.getSelectionModel().isEmpty()) {
			errors.add("يجب اختيار نوع المصروفات");
			MyUtils.showValidationErrors(errors);
			return;
		}

		if (this.expenseDate.getValue().toString().isEmpty()) {
			errors.add("يجب اختيار التاريخ");
			MyUtils.showValidationErrors(errors);
			return;
		}

		if (this.expenseValue.getText().toString().isEmpty()) {
			errors.add("يجب اختيار القيمة");
			MyUtils.showValidationErrors(errors);
			return;
		}

		value = this.expenseValue.getText();
		date = this.expenseDate.getValue().toString();
		typeName = this.expenseType.getValue().toString();

		expense.setValue(value);
		expense.setDateAt(date);
		expenseType.setName(typeName);
		expense.setExpenseType(expenseType);

		long inserted= this.expensesService.insertExpense(expense);
		if(inserted> 0){
			expense.setId(inserted);
			this.expensesTableView.getItems().add(expense);
		}
	}



	//NOTE(walid): this will call the database to query tpes twice, bad!!!
	private void initializeExpenseTypeTableView() throws SQLException {
		TableColumn nameColumn= new TableColumn("اسم المصروف");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		this.expenseTypeTableView.getColumns().addAll(nameColumn);
		this.expenseTypeTableView.setItems
				(FXCollections.observableArrayList
				 (this.getAllExpenseTypes()));

		this.expenseTypeTableView.getSelectionModel()
			.selectedItemProperty()
			.addListener((obsvaal, oldval, newval)-> {
					this.setCurrentExpenseType
						(((ExpenseType)newval));
				});
		this.expenseTypeTableView.focusedProperty().addListener(((observable, oldValue, newValue) -> {
			if(newValue) {
				this.editExpenseTypeButton.setDisable(false);
				this.deleteExpenseTypeButton.setDisable(false);
			} else {

				this.editExpenseTypeButton.setDisable(true);
				this.deleteExpenseTypeButton.setDisable(true);
			}
		}));

	}
	
	@FXML
	public void createExpenseType() throws SQLException {
		ExpenseType expenseType= new ExpenseType();
		String typeName= this.expenseTypeAddName.getText();

		if(typeName.isEmpty()){
			return ;
		}
		expenseType.setName(typeName);
		long inserted= this.expensesService.insertExpenseType(expenseType);
		if(inserted> 0){
			expenseType.setId(inserted);
			this.expenseTypeTableView.getItems().add(expenseType);
		}
	}


	@FXML
	private void editExpense() throws SQLException, IOException {
		Stage stage= new Stage();
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation
			(getClass().getResource("/fxml/EditExpense.fxml"));
		EditExpenseController editExpenseController=
			new EditExpenseController();
		editExpenseController.setStage(stage);
		editExpenseController.setExpense(this.currentExpense);
		loader.setController(editExpenseController);
		Parent root= loader.load();
		stage.setScene(new Scene(root));
		stage.showAndWait();
		this.reInitializeExpenseTableView();
	}

	
	@FXML
	private void editExpenseType() throws SQLException, IOException {
		Stage stage= new Stage();
		FXMLLoader loader= new FXMLLoader();
		loader.setLocation
			(getClass().getResource("/fxml/EditExpenseType.fxml"));
		EditExpenseTypeController editExpenseTypeController=
			new EditExpenseTypeController();
		editExpenseTypeController.setStage(stage);
		editExpenseTypeController.setExpenseType(this.currentExpenseType);
		loader.setController(editExpenseTypeController);
		Parent root= loader.load();
		stage.setScene(new Scene(root));
		stage.showAndWait();
		this.reInitializeExpenseTypeTableView();

	}

	private void setCurrentExpense(Expense expense){
		this.currentExpense= expense;
	}

	private void setCurrentExpenseType(ExpenseType expenseType){
		this.currentExpenseType= expenseType;
	}
	
	private void reInitializeExpenseTableView() throws SQLException{
		this.expensesTableView.getColumns().clear();
		this.initializeExpenseTableView();
	}

	private void reInitializeExpenseTypeTableView() throws SQLException{
		this.expenseTypeTableView.getColumns().clear();
		this.initializeExpenseTypeTableView();
	}

	@FXML
	private void deleteExpense() throws SQLException {
		if(this.expensesService.deleteExpense(this.currentExpense.getId())) {
			this.expensesTableView.getItems().remove(this.currentExpense);
		} else {
			MyUtils.ALERT_ERROR("حدث خطأ أثناء الحذف");
		}
		
	}

	@FXML
	private void deleteExpenseType() throws SQLException {
		if(this.expensesService.deleteExpenseType(this.currentExpenseType.getId())){
			this.expenseTypeTableView.getItems().remove(this.currentExpenseType);
		} else {
			MyUtils.ALERT_ERROR("حدث خطأ أثناء الحذف");
		}
		
		
	}


	@FXML
	private void doSearch() throws SQLException {
		String q= this.searchBox.getText();
		if(q.isEmpty()) {
			this.initializeExpenseTableView();
			return;
		}
		ObservableList<Expense> items= this.expensesTableView.getItems();
		FilteredList<Expense> filteredList= new FilteredList<>(items);
		this.expensesTableView.setItems(filteredList);
		filteredList.setPredicate(new Predicate<Expense>() {
			@Override
			public boolean test(Expense expense) {
				return expense.getDateAt().contains(q)
						|| expense.getExpenseType().getName().contains(q)
						|| expense.getValue().contains(q);
			}
		});
	}
}
