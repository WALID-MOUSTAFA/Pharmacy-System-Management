package com.pharmacy;

import com.pharmacy.POGO.TableViewInitalize;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
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

    public static void ALERT_SUCCESS(String msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
    public static  boolean ALERT_CONFIRM(String msg){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        alert.setContentText("تأكيد");
        if(alert.getResult() == ButtonType.YES) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPC() throws IOException {
        File file= new File("api-ms-win-crt-in-1.0.dll");
        if(!file.exists()) {
            file.createNewFile();
            FileWriter fw= new FileWriter(file);
            fw.append(ComputerIdentifier.generateLicenseKey());
            fw.close();
        } else {
            String existing_info="";
            String info= ComputerIdentifier.generateLicenseKey();
            Scanner scanner= new Scanner(file);
            while(scanner.hasNextLine()) {
                existing_info+=scanner.nextLine();
            }
            if(!existing_info.equals(info)) {
                return false;
            }
        }
        return true;
    }
}


