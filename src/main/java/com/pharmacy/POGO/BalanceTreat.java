package com.pharmacy.POGO;

public class  BalanceTreat {

	private Long Id;
	private Long treatId;

	public DetailedTreatment getTreat() {
		return treat;
	}

	public void setTreat(DetailedTreatment treat) {
		this.treat = treat;
	}

	private DetailedTreatment treat;
	private Long purchaseId;

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

	private Purchase purchase;
	private Long purchaseDetailsId;
	private Long typeId;
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
//
//	public Treatment getTreatment() {
//		return treatment;
//	}
//
//	public void setTreatment(Treatment treatment) {
//		this.treatment = treatment;
//	}
//
//	private Treatment treatment;
	
	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	
	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Long getTreatId() {
		return treatId;
	}

	public void setTreatId(Long treatId) {
		this.treatId = treatId;
	}

	public Long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public Long getPurchaseDetailsId() {
		return purchaseDetailsId;
	}

	public void setPurchaseDetailsId(Long purchaseDetailsId) {
		this.purchaseDetailsId = purchaseDetailsId;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
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

