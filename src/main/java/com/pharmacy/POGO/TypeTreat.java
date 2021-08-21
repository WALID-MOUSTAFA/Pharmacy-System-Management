package com.pharmacy.POGO;

import java.sql.Timestamp;

public class TypeTreat {


    public void setTypename(String typename) {
        this.typename = typename;
    }

    public void setDateAt(String dateAt) {
        this.dateAt = dateAt;
    }

    public TypeTreat() {
        this.dateAt = new Timestamp(System.currentTimeMillis()).toString();
    }

    public TypeTreat(long id, String typename) {
        this.typename = typename;
        this.id= id;
    }

    public String getTypename() {
        return typename;
    }

    public String getDateAt() {
        return dateAt;
    }

    public String typename;
    public String dateAt;
    public long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
