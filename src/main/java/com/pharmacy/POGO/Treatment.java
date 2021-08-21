package com.pharmacy.POGO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Treatment {
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

	public long getTypet() {
		return typet;
	}

	public void setTypet(long typet) {
		this.typet = typet;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getParcode() {
		return parcode;
	}

	public void setParcode(String parcode) {
		this.parcode = parcode;
	}

	public String getDateAt() {
		return dateAt;
	}

	public void setDateAt(String dateAt) {
		this.dateAt = dateAt;
	}

	public double getLowcount() {
		return lowcount;
	}

	public void setLowcount(double lowcount) {
		this.lowcount = lowcount;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public long getFormtreat() {
		return formtreat;
	}

	public void setFormtreat(long formtreat) {
		this.formtreat = formtreat;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getStringStatus() {
		return this.getStatus() == 0 ? "غير متاح" : "متاح" ;
	}

	public void setQuantity(double quantity){
		this.quantity= quantity;
	}

	public double getQuantity() {
		return this.quantity;
	}

	//Note(walid): overloaded to avoid shit
	public Treatment() {

	}

	public Treatment(long id,
			 String name,
			 long typet,
			 int status,
			 String parcode,
			 String data_at,
			 int lowcount,
			 String company,
			 long formtreat,
			 String place) {
		this.id = id;
		this.name = name;
		this.typet = typet;
		this.status = status;
		this.parcode = parcode;
		this.dateAt = data_at;
		this.lowcount = lowcount;
		this.company = company;
		this.formtreat = formtreat;
		this.place = place;
	}

	protected long id;
	@NotNull
	@NotEmpty
	protected String name;
	protected long typet;
	protected int status;

	
	protected String parcode;
	protected String dateAt;
	protected double lowcount;
	protected String company;
	protected long formtreat;
	protected double quantity;

	@NotNull
	@NotEmpty
	protected String place;
}
