package lk.ijse.project_01.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import lk.ijse.project_01.DB.DBConnection;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ReportpageController {

    @FXML
    private JFXButton cusReportbtn;

    @FXML
    private JFXButton empReport;

    @FXML
    private JFXButton inventory;

    @FXML
    void GuestreportOnAction(ActionEvent event) {
            try {
                JasperReport report = JasperCompileManager.compileReport(
                        getClass().getResourceAsStream("/Report/GuestDetailsReport.jrxml")
                );
                Connection connection = DBConnection.getInstance().getConnection();

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("P_DATE", LocalDate.now().toString());
                JasperPrint jasperPrint = JasperFillManager.fillReport(
                        report,
                        parameters,
                        connection
                );
                JasperViewer.viewReport(jasperPrint, false);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    public void BookingReportOnAction(ActionEvent actionEvent) {
        try {
            JasperReport report = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/Report/BookingReport.jrxml")
            );
            Connection connection = DBConnection.getInstance().getConnection();

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("P_DATE", LocalDate.now().toString());
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    report,
                    parameters,
                    connection
            );
            JasperViewer.viewReport(jasperPrint, false);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void EmployeeReportOnAction(ActionEvent actionEvent) {
        try {
            JasperReport report = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/Report/EmployeeReport.jrxml")
            );
            Connection connection = DBConnection.getInstance().getConnection();

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("P_DATE", LocalDate.now().toString());
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    report,
                    parameters,
                    connection
            );
            JasperViewer.viewReport(jasperPrint, false);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
