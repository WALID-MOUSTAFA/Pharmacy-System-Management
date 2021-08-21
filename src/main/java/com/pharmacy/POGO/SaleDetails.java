package com.pharmacy.POGO;

import java.sql.Timestamp;

public class SaleDetails {

	private long id;
	private long saleId;
	private Sale sale;
	private long treatId;
	private DetailedTreatment treat;
	private double quantity;
	private long balanceId;
	private BalanceTreat balanceTreat;
	private String expireDate;
	private String DateIn; //for the sales_details;
	private double priceOne;
	private double total;

	public SaleDetails() {
		//Note(walid): work around the bad design of the database;
		this.DateIn= new Timestamp(System.currentTimeMillis()).toString();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSaleId() {
		return saleId;
	}

	public void setSaleId(long saleId) {
		this.saleId = saleId;
	}

	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public long getTreatId() {
		return treatId;
	}

	public void setTreatId(long treatId) {
		this.treatId = treatId;
	}

	public DetailedTreatment getTreat() {
		return treat;
	}

	public void setTreat(DetailedTreatment treat) {
		this.treat = treat;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public long getBalanceId() {
		return balanceId;
	}

	public void setBalanceId(long balanceId) {
		this.balanceId = balanceId;
	}

	public BalanceTreat getBalanceTreat() {
		return balanceTreat;
	}

	public void setBalanceTreat(BalanceTreat balanceTreat) {
		this.balanceTreat = balanceTreat;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getDateIn() {
		return DateIn;
	}

	public void setDateIn(String dateIn) {
		this.DateIn = dateIn;
	}



	public double getPriceOne() {
		return priceOne;
	}

	public void setPriceOne(double priceOne) {
		this.priceOne = priceOne;
	}
}
