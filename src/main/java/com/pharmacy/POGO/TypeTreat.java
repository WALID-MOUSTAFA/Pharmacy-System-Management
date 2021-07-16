package com.pharmacy.POGO;

import java.sql.Date;

public class TypeTreat {
    public void setTypename(String typename) {
        this.typename = typename;
    }

    public void setDate_now(Date date_now) {
        this.date_now = date_now;
    }

    public TypeTreat() {
    }

    public TypeTreat(String typename) {
        this.typename = typename;
    }

    public String getTypename() {
        return typename;
    }

    public Date getDate_now() {
        return date_now;
    }

    public String typename;
    public Date date_now;
    public long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
