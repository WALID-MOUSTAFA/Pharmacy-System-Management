open module com.pharmacy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires java.validation;
    requires org.hibernate.validator;

    exports  com.pharmacy;
}