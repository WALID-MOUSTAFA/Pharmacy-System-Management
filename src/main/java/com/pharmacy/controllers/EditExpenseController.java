package com.pharmacy.controllers;

import com.pharmacy.MyUtils;
import com.pharmacy.POGO.Expense;
import com.pharmacy.POGO.ExpenseType;
import com.pharmacy.services.ExpensesService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EditExpenseController extends MyController{
	private Expense expense;

	private ExpensesService expensesService;
	
	@FXML private TextField expenseValue;
	@FXML private DatePicker expenseDate;
	@FXML private ComboBox expenseType;

	private List<ExpenseType> getAllExpenseTypes() throws SQLException {
		List<ExpenseType>types= this.expensesService.getAllExpenseTypes();
		return types;

	}

	private void initializeExpenseTypeCombo() throws SQLException{
		for(ExpenseType et : this.getAllExpenseTypes()){
			this.expenseType.getItems().add(et.getName());
		}
	}

	public EditExpenseController() throws SQLException {
		this.expensesService= new ExpensesService();
	}

	private void initializeEditForm() {
		this.expenseValue.setText(this.expense.getValue());
		this.expenseDate.setValue(LocalDate.parse(this.expense.getDateAt().split(" ")[0]));
		this.expenseType.setValue(this.expense.getExpenseType().getName());
	}
	
	@FXML
	private void initialize() throws SQLException{
		this.initializeEditForm();
		this.initializeExpenseTypeCombo();
		MyUtils.setDatePickerFormat(expenseDate);
		
	}
	
	public void setExpense(Expense expense){
		this.expense= expense;
	}
	
	@FXML
	private void editExpense() throws SQLException {
		List<String> errors = new ArrayList<>();
		String value;
		String date;
		String typeName;
		// Expense expense = new Expense();
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

		if(this.expensesService.updateExpense(this.expense)){
			this.stage.close();
		}

	}

}
