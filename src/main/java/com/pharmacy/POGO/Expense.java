package com.pharmacy.POGO;

//NOTE(walid): this table in the inhireted database have a bad design;
public class Expense {
	private long id;
	private long expenseTypeId;
	private String value;
	private String dateAt;
	private ExpenseType expenseType;

	public ExpenseType getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(ExpenseType expenseType) {
		this.expenseType = expenseType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getExpenseTypeId() {
		return expenseTypeId;
	}

	public void setExpenseTypeId(long expenseTypeId) {
		this.expenseTypeId = expenseTypeId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDateAt() {
		return dateAt;
	}

	public void setDateAt(String dateAt) {
		this.dateAt = dateAt;
	}

	
}

