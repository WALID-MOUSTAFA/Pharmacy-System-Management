package com.pharmacy.POGO;

import java.sql.Timestamp;

public class Sale {

	private long id;
	private long customerId;
	private Customer customer;
	private String name;
	private double total;
	private double discount;
	private double netTotal;
	private String dateIn; //choosed by the operator

	public Sale() {
		this.dateIn= (new Timestamp(System.currentTimeMillis())).toString();
		this.discount= 0;
		this.netTotal= this.total - this.discount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getNetTotal() {
		return netTotal;
	}

	public void setNetTotal(double netTotal) {
		this.netTotal = netTotal;
	}

	public String getDateIn() {
		return dateIn;
	}

	public void setDateIn(String dateIn) {
		this.dateIn = dateIn;
	}
}
