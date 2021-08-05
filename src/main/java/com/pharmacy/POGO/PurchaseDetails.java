package com.pharmacy.POGO;

public class PurchaseDetails {
	private long id;
	private long treat_id;
	private long purchase_id;

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


	private String expireDate;
	private String productionDate;
	private String dateAt;
	private double quantity;
	private double pricePeople;
	private double pricePharmacy;
	private double totalPeople;
	private double totalPharmacy;
	private Treatment treat;
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

	public Treatment getTreat() {
		return treat;
	}

	public void setTreat(Treatment treat) {
		this.treat = treat;
	}

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}
}
