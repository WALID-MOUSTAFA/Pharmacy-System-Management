package com.pharmacy.POGO;

public class BalanceTreatWithInventoryCountDetails extends BalanceTreat {
    public int getAfterQuantity() {
        return afterQuantity;
    }

    public void setAfterQuantity(int afterQuantity) {
        this.afterQuantity = afterQuantity;
    }

    public long getCountId() {
        return countId;
    }

    public void setCountId(long countId) {
        this.countId = countId;
    }

    private int afterQuantity;

    public int getBeforeQuantity() {
        return beforeQuantity;
    }

    public void setBeforeQuantity(int beforeQuantity) {
        this.beforeQuantity = beforeQuantity;
    }

    private int beforeQuantity;
    private long countId; 
    private long countDetailsId;

    public long getCountDetailsId() {
        return countDetailsId;
    }

    public void setCountDetailsId(long countDetailsId) {
        this.countDetailsId = countDetailsId;
    }
}
