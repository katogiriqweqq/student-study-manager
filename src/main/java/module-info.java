module net.katogiri.task12 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;

    opens net.katogiri.task12 to javafx.fxml;
    exports net.katogiri.task12;
}