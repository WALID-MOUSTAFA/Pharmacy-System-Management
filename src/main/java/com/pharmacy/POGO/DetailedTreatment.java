package com.pharmacy.POGO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class DetailedTreatment extends Treatment {
	@NotNull
	@NotEmpty
	String typeTreatName;

	@NotEmpty
	@NotNull
	String formTreatName;

    public String getTypeTreatName() {
        return typeTreatName;
    }

    public void setTypeTreatName(String typeTreatName) {
        this.typeTreatName = typeTreatName;
    }

    public String getFormTreatName() {
        return formTreatName;
    }

    public void setFormTreatName(String formTreatName) {
        this.formTreatName = formTreatName;
    }
}
