package com.pharmacy;

import com.pharmacy.POGO.TableViewInitalize;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class MyUtils {

    public static void setDatePickerFormat(DatePicker datePicker) {

        datePicker.setConverter(new StringConverter<LocalDate>()
        {
            private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });
    }


    public static void ReInitalizeTableView(TableView tableView, TableViewInitalize tableViewInitalize )
    throws SQLException {
        tableView.getColumns().clear();
        tableViewInitalize.initializeTableView();
    }



    public static <T>void validateModel (T model, List<String> errors) {
        ValidatorFactory validatorFactory= Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> constraintViolationSet= validator.validate(model);
        for(ConstraintViolation<T> violation : constraintViolationSet) {
            errors.add(violation.getMessage());
        }
    }
	

	public static void showValidationErrors(List<String> errors) {
		String alertContent= "";
		for (String s : errors) {
			alertContent += s;
			alertContent+= System.lineSeparator();
		}
		Alert alert= new Alert(Alert.AlertType.ERROR);
		alert.setTitle("خطأ");
		alert.setHeaderText("خطأ في البيانات");
		alert.setContentText(alertContent);
		alert.showAndWait();

    }


	public static void ALERT_ERROR(String msg){
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setContentText(msg);
		alert.show();
	}
}
