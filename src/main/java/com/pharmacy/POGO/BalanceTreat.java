package com.pharmacy.POGO;

public class  BalanceTreat {

	private long Id;
	private long treatId;

	public DetailedTreatment getTreat() {
		return treat;
	}

	public void setTreat(DetailedTreatment treat) {
		this.treat = treat;
	}

	private DetailedTreatment treat;
	private long purchaseId;
	private long purchaseDetailsId;
	private long typeId;
	private String expireDate;
	private double quantity;
	private double price;
	private double total;
	private String dateIn;

	public PurchaseDetails getPurchaseDetails() {
		return purchaseDetails;
	}

	public void setPurchaseDetails(PurchaseDetails purchaseDetails) {
		this.purchaseDetails = purchaseDetails;
	}

	private PurchaseDetails purchaseDetails;

	public Treatment getTreatment() {
		return treatment;
	}

	public void setTreatment(Treatment treatment) {
		this.treatment = treatment;
	}

	private Treatment treatment;
	
	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	
	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public long getTreatId() {
		return treatId;
	}

	public void setTreatId(long treatId) {
		this.treatId = treatId;
	}

	public long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public long getPurchaseDetailsId() {
		return purchaseDetailsId;
	}

	public void setPurchaseDetailsId(long purchaseDetailsId) {
		this.purchaseDetailsId = purchaseDetailsId;
	}

	public long getTypeId() {
		return typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getDateIn() {
		return dateIn;
	}

	public void setDateIn(String dateIn) {
		this.dateIn = dateIn;
	}
}

