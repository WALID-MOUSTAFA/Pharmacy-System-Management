package com.pharmacy;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.util.Calendar;

public abstract class YearMonthFilterable {

    @FXML protected ComboBox yearFilter;
    @FXML protected ComboBox monthFilter;

    protected enum Filter {
        YEAR,
        MONTH,
    }

    @FXML
    protected void onYearFilterChange() {
        this.onFilterChange(Filter.YEAR);
    }

    @FXML
    protected void onMonthFilterChange() {
        this.onFilterChange(Filter.MONTH);
    }

    protected void initializeYearFilterYears() {
        int currentYear= Calendar.getInstance().get(Calendar.YEAR);
        for(int i = 0; i < 10; i++) {
            this.yearFilter.getItems().add(String.valueOf(currentYear+i));
        }
    }

    protected void initializeTypeFilterTypes() {

    }

    protected void onFilterChange(Filter filter) {};

}
