package com.pharmacy.services;

import com.pharmacy.DatabaseConnection;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.List;

import com.pharmacy.POGO.Expense;

public class ExpenseService{

	private Connection dbConnection;

	public ExpenseService() throws SQLException {
		this.dbConnection= DatabaseConnection.getInstance().getConnection();
	}
	
	public List<Expense> getAllExpenses(){
		return null;
	}
	
	public Expense getExpenseById(long id){return null;}

	public boolean insertNewExpense(Expense ex) {return false;}

	public boolean deleteExpense(long id){return false;}
	
}
