package com.pharmacy.POGO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class Customer {


	private long id;
	@NotNull
	@NotEmpty
	private String name;
	private String address;
	private Double cash;
	private String dateAt;


	public Customer() {
		this.dateAt= new Timestamp(System.currentTimeMillis()).toString();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getCash() {
		return cash;
	}

	public void setCash(Double cash) {
		this.cash = cash;
	}

	public String getDateAt() {
		return dateAt;
	}

	public void setDateAt(String dateAt) {
		this.dateAt = dateAt;
	}
	

}
