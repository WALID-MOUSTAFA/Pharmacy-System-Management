package com.pharmacy.controllers;


import com.pharmacy.MyUtils;
import com.pharmacy.POGO.ExpenseType;
import com.pharmacy.services.ExpensesService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EditExpenseTypeController extends MyController {

	private ExpenseType expenseType;
	private ExpensesService expensesService;
	@FXML
	private TextField expenseTypeName;
	

	public EditExpenseTypeController() throws SQLException {
		this.expensesService= new ExpensesService();
	}

	public void setExpenseType(ExpenseType expenseType){
		this.expenseType= expenseType;
	}
	
	@FXML
	private void initialize(){
		this.initializeForm();
	}

	private void initializeForm() {
		this.expenseTypeName.setText(expenseType.getName());
	}

	@FXML
	private void editExpenseType() throws SQLException {
		List<String> errors= new ArrayList<>();
		String name= this.expenseTypeName.getText();

		if (name.isEmpty()) {
			errors.add("يجب اختيار نوع المصروفات");
			MyUtils.showValidationErrors(errors);
			return;
		}

		this.expenseType.setName(name);

		if(this.expensesService.updateExpenseType(expenseType)){
			this.stage.close();
		}
	}
}
