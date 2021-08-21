package com.pharmacy.POGO;

public class SupplierPayment {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;
    private  double supplierGet;
    private String supplierGetDate;
    private short status;
    private String notes;
    private long supplierId;
    private Supplier supplier;

    public double getSupplierGet() {
        return supplierGet;
    }

    public void setSupplierGet(double supplierGet) {
        this.supplierGet = supplierGet;
    }

    public String getSupplierGetDate() {
        return supplierGetDate;
    }

    public void setSupplierGetDate(String supplierGetDate) {
        this.supplierGetDate = supplierGetDate;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
