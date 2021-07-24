package com.pharmacy.POGO;

public class Purchase {

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private String datePur;

	private String pillNum;

	private double totalPeople;

	private double totalPharmacy;

	private double countUnit;

	private double discount;

	private double profit;

	private String description;

	private String dateAt;

	private long supplierID;

	private Supplier supplier;

	public String getDatePur() {
		return datePur;
	}

	public void setDatePur(String datePur) {
		this.datePur = datePur;
	}

	public String getPillNum() {
		return pillNum;
	}

	public void setPillNum(String pillNum) {
		this.pillNum = pillNum;
	}

	public double getTotalPeople() {
		return totalPeople;
	}

	public void setTotalPeople(double totalPeople) {
		this.totalPeople = totalPeople;
	}

	public double getTotalPharmacy() {
		return totalPharmacy;
	}

	public void setTotalPharmacy(double totalPharmacy) {
		this.totalPharmacy = totalPharmacy;
	}

	public double getCountUnit() {
		return countUnit;
	}

	public void setCountUnit(double countUnit) {
		this.countUnit = countUnit;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDateAt() {
		return dateAt;
	}

	public void setDateAt(String dateAt) {
		this.dateAt = dateAt;
	}

	public long getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(long supplierID) {
		this.supplierID = supplierID;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	
}
