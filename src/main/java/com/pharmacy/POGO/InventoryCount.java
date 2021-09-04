package com.pharmacy.POGO;

import java.sql.Timestamp;

public class InventoryCount {

    private long id;
    private String dateIn;

    public InventoryCount() {
        this.dateIn = new Timestamp(System.currentTimeMillis()).toString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDateIn() {
        return dateIn;
    }

    public void setDateIn(String date) {
        this.dateIn = date;
    }
}
