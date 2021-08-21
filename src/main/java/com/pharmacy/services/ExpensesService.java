package com.pharmacy.services;

import com.pharmacy.DatabaseConnection;
import com.pharmacy.POGO.Expense;
import com.pharmacy.POGO.ExpenseType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpensesService{

	private Connection dbConnection;

	public ExpensesService() throws SQLException {
		this.dbConnection= DatabaseConnection.getInstance().getConnection();
	}
	

	public boolean insertExpenseType() throws SQLException {return false;}

//	public ExpenseType getExpenseTypeByName(String name) throws SQLException {
//		String query= "SELECT * FROM expensemain WHERE name=\""+ name + "\" LIMIT 1;";
//		ResultSet rs= this.dbConnection.createStatement().executeQuery(query);
//		if(!rs.isBeforeFirst()) {
//			return null;
//		}
//		ExpenseType expenseType=  new ExpenseType();
//		while(rs.next()){
//			expenseType.setId(rs.getLong("id"));
//			expenseType.setName(rs.getString("name"));
//		}
//		return expenseType;
//	}

	public long insertExpense(Expense expense) throws SQLException {
		expense.setExpenseTypeId(this.getExpenseTypeByName(expense.getExpenseType().getName()).getId());

		String query= "INSERT INTO expenses (expensemain, value, date_in) VALUES (? ,? ,?);";
		PreparedStatement preparedStatement= this.dbConnection.prepareStatement(query);
		preparedStatement.setLong(1, expense.getExpenseTypeId());
		preparedStatement.setString(2, expense.getValue());
		preparedStatement.setString(3, expense.getDateAt());

		if(preparedStatement.executeUpdate() > 0){
			ResultSet keys= preparedStatement.getGeneratedKeys();
			if(keys.next()) {
				return keys.getLong(1);
			}
		}
		
		return 0;
	}

	public boolean updateExpense(Expense expense) throws SQLException{
		long id= expense.getId();

		long expenseTypeId= this.getExpenseTypeByName
			(expense.getExpenseType().getName()).getId();
		expense.getExpenseType().setId(expenseTypeId);
		
		String query="UPDATE expenses SET value=?, date_in=?, expensemain=? WHERE id="+id+";";
		PreparedStatement preparedStatement=
			this.dbConnection.prepareStatement(query);
		preparedStatement.setString(1, expense.getValue());
		preparedStatement.setString(2, expense.getDateAt());
		preparedStatement.setLong(3, expense.getExpenseType().getId());

		if(preparedStatement.executeUpdate()> 0){
			return true ;
		}
		return false;
	}

	public List<Expense> getAllExpenses() throws SQLException {
		List<Expense> expenses= new ArrayList<>();
		String query="SELECT  expenses.*, expensemain.name as type FROM expenses INNER JOIN expensemain ON expenses.expensemain= expensemain.id";
		Statement stmt= this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);

		if(!rs.isBeforeFirst()){
			return null;
		}
		Expense expense;
		ExpenseType expenseType;
		
		while(rs.next()){
			expense= new Expense();
			expenseType= new ExpenseType();
			expense.setId(rs.getLong("id"));
			expense.setDateAt(rs.getString("date_in"));
			expense.setValue(rs.getString("value"));
			expenseType.setName(rs.getString("type"));
			expense.setExpenseType(expenseType);
			expenses.add(expense);
			expense=null;
			expenseType= null;
		}
		return expenses;
	}
	

	public List<ExpenseType> getAllExpenseTypes() throws SQLException {
		ExpenseType expenseType;
		List<ExpenseType> expenseTypes= new ArrayList<>();
		String query= "SELECT * FROM expensemain";
		Statement stmt= this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);

		while(rs.next()){
			expenseType= new ExpenseType();
			expenseType.setId(rs.getLong("id"));
			expenseType.setName(rs.getString("name"));
			expenseTypes.add(expenseType);
			expenseType= null;
		}
		return expenseTypes;
	}


	public long insertExpenseType(ExpenseType expenseType)
		throws SQLException {
		String query= "INSERT INTO expensemain (name) VALUES (?)";
		PreparedStatement preparedStatement= this.dbConnection.prepareStatement(query);
		preparedStatement.setString(1, expenseType.getName());
		if(preparedStatement.executeUpdate() > 0){
			ResultSet keys= preparedStatement.getGeneratedKeys();
			if(keys.next()) {
				return keys.getLong(1);
			}
		}
		return 0;
	}

	public boolean updateExpenseType(ExpenseType expenseType)
		throws SQLException {
		String query= "UPDATE expensemain SET name=? WHERE id="
			+expenseType.getId()+";";
		PreparedStatement preparedStatement=
			this.dbConnection.prepareStatement(query);
		preparedStatement.setString(1, expenseType.getName());
		if(preparedStatement.executeUpdate() > 0){
			return true;
		}
		return false;
	}


	public ExpenseType getExpenseTypeByName(String name) throws SQLException{
		String query="SELECT * FROM expensemain WHERE name='"+name+"' LIMIT 1";
		Statement stmt= this.dbConnection.createStatement();
		ResultSet rs= stmt.executeQuery(query);
		if(!rs.isBeforeFirst()){
			return null;
		}
		ExpenseType expenseType= new ExpenseType();
		
		while(rs.next()){
			expenseType.setId(rs.getLong("id"));
			expenseType.setName(rs.getString("name"));
		}
		return expenseType;
	}



	public boolean deleteExpense(long id) throws SQLException {
		String query= "DELETE FROM expenses WHERE id=" +id +";";
		Statement stmt= this.dbConnection.createStatement();
		if(stmt.executeUpdate(query) > 0){
			return true;
		}
		return false;
	}


	public boolean deleteExpenseType(long id) throws SQLException {
		String query= "DELETE FROM expensemain WHERE id=" +id +";";
		Statement stmt= this.dbConnection.createStatement();
		if(stmt.executeUpdate(query) > 0){
			return true;
		}
		return false;
	}


}
