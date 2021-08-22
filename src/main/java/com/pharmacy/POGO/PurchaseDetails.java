package com.pharmacy.POGO;

import javax.validation.constraints.NotNull;

public class PurchaseDetails {
	private long id;
	private long treat_id;
	private long purchase_id;
	private String discount;

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public long getTreat_id() {
		return treat_id;
	}

	public void setTreat_id(long treat_id) {
		this.treat_id = treat_id;
	}

	public long getPurchase_id() {
		return purchase_id;
	}

	public void setPurchase_id(long purchase_id) {
		this.purchase_id = purchase_id;
	}

	@NotNull(message="يجب ملء البيانات بالكامل")
	private String expireDate;
	@NotNull(message="يجب ملء البيانات بالكامل")
	private String productionDate;
	private String dateAt;
	@NotNull(message="يجب ملء البيانات بالكامل")
	private double quantity;
	@NotNull(message="يجب ملء البيانات بالكامل")
	private double pricePeople;
	@NotNull(message="يجب ملء البيانات بالكامل")
	private double pricePharmacy;
	@NotNull(message="يجب ملء البيانات بالكامل")
	private double totalPeople;
	@NotNull(message="يجب ملء البيانات بالكامل")
	private double totalPharmacy;
	private DetailedTreatment treat;
	private Purchase purchase;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}

	public String getDateAt() {
		return dateAt;
	}

	public void setDateAt(String dateAt) {
		this.dateAt = dateAt;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getPricePeople() {
		return pricePeople;
	}

	public void setPricePeople(double pricePeople) {
		this.pricePeople = pricePeople;
	}

	public double getPricePharmacy() {
		return pricePharmacy;
	}

	public void setPricePharmacy(double pricePharmacy) {
		this.pricePharmacy = pricePharmacy;
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

	public DetailedTreatment getTreat() {
		return treat;
	}

	public void setTreat(DetailedTreatment treat) {
		this.treat = treat;
	}

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}
}
