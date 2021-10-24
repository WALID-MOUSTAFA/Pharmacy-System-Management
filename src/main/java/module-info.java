open module com.pharmacy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires java.validation;
    requires org.hibernate.validator;
    requires com.github.oshi;
    requires java.desktop;
    requires jasperreports;
    requires barbecue;

    exports  com.pharmacy;
}