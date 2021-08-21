package com.pharmacy.POGO;


import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class Purchase {

	public Purchase() {
		this.dateAt= new Timestamp(System.currentTimeMillis()).toString();
	}

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private String datePur;

	@NotEmpty(message = "رقم الفاتورة مطلوب")
	@NotNull(message = "رقم الفاتورة مطلوب")
	private String pillNum;

	@NotNull(message="يجب ملء البيانات بالكامل")
	private double totalPeople;

	@NotNull(message="يجب ملء البيانات بالكامل")
	@Digits(integer = 100, fraction = 100)
	private double totalPharmacy;

	@NotNull(message="يجب ملء البيانات بالكامل")
	private double countUnit;

	@NotNull(message="يجب ملء البيانات بالكامل")
	private double discount;

	private double profit;

	@NotEmpty(message="يجب ملء البيانات بالكامل")
	@NotNull(message="يجب ملء البيانات بالكامل")
	private String description;

	@NotNull(message="يجب ملء البيانات بالكامل")
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
