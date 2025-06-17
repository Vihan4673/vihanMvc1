module lk.ijse.project_01 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires lombok;
    requires java.desktop;
    requires com.jfoenix;
    requires mysql.connector.j;

    requires com.google.protobuf;
    requires jakarta.mail;
    requires net.sf.jasperreports.core;

    opens lk.ijse.project_01.Controller to javafx.fxml;
    opens lk.ijse.project_01.DTO.TM to javafx.base;
    opens lk.ijse.project_01.Controller.Booking to javafx.fxml;
    opens lk.ijse.project_01.Model to javafx.base;

    exports lk.ijse.project_01;
    opens lk.ijse.project_01.DB to javafx.fxml;
    opens lk.ijse.project_01.DTO to javafx.base;
}
