package com.pharmacy.POGO;

//NOTE(walid): this table in the inhireted database have a bad design;
public class Expense {
	private long id;
	private long expenseMain;
	private String value;
	private String dateAt;

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getExpenseMain() {
		return expenseMain;
	}

	public void setExpenseMain(long expenseMain) {
		this.expenseMain = expenseMain;
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

