package com.pharmacy.POGO;

import java.sql.Timestamp;

public class InventoryCountDetails {
    private long id;
    private long balanceId;
    private long inventoryCountsId;
    private int systemQuantity;

    public InventoryCountDetails() {
        this.DateIn= new Timestamp(System.currentTimeMillis()).toString();
    }

    public String getDateIn() {
        return DateIn;
    }

    public void setDateIn(String dateIn) {
        DateIn = dateIn;
    }

    private String DateIn;


    public int getSystemQuantity() {
        return systemQuantity;
    }

    public void setSystemQuantity(int systemQuantity) {
        this.systemQuantity = systemQuantity;
    }

    public int getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(int actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public InventoryCount getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(InventoryCount inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    private int actualQuantity;
    private int status;
    private BalanceTreat balanceTreat;
    private InventoryCount inventoryCount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(long balanceId) {
        this.balanceId = balanceId;
    }

    public long getInventoryCountsId() {
        return inventoryCountsId;
    }

    public void setInventoryCountsId(long inventoryCountsId) {
        this.inventoryCountsId = inventoryCountsId;
    }

    public BalanceTreat getBalanceTreat() {
        return balanceTreat;
    }

    public void setBalanceTreat(BalanceTreat balanceTreat) {
        this.balanceTreat = balanceTreat;
    }

    public InventoryCount getInventoryCounts() {
        return inventoryCount;
    }

    public void setInventoryCounts(InventoryCount inventoryCount) {
        this.inventoryCount = inventoryCount;
    }
}
