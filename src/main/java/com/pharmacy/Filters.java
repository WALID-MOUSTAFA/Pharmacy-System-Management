package com.pharmacy;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.util.Calendar;

public abstract class Filters {

	@FXML protected ComboBox<String> yearFilter;
	@FXML protected ComboBox<String> monthFilter;
	@FXML protected ComboBox<String> typeFilter;

	protected static enum Filter {
		YEAR,
		MONTH,
		TYPE
	}

	@FXML
	protected void onYearFilterChange() {
		this.onFilterChange(Filter.YEAR);
	}

	@FXML
	protected void onMonthFilterChange() {
		this.onFilterChange(Filter.MONTH);
	}

	@FXML
	protected void onTypeFilterChange() {
		this.onFilterChange(Filter.TYPE);
	}


    
	protected void onFilterChange(Filter filter) {};

	@FXML
	private void onDeleteFilter() throws Exception{
		this.deleteFilter();
	}

	protected void deleteFilter() throws Exception{};
}
