package lk.ijse.project_01.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.project_01.DTO.Employee;
import lk.ijse.project_01.DTO.TM.EmployeeTm;
import lk.ijse.project_01.Model.EmployeeModel;
import lk.ijse.project_01.Util.Regex;

import java.sql.SQLException;
import java.util.List;

public class EmployeeFormController {

    @FXML private AnchorPane rootNode;
    @FXML private TableView<EmployeeTm> tblEmployee;
    @FXML private TableColumn<?, ?> colId, colName, colPhoneno, colAddress, colPosition, colSalary;
    @FXML private TextField txtEmployeeId, txtEmployeeName, txtEmployeeAddress, txtEmployeeTel, txtEmployeePosition, txtEmployeeSalary;

    public void initialize() {
        setCellValueFactory();
        loadAllEmployees();
        tableListener();
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        colPhoneno.setCellValueFactory(new PropertyValueFactory<>("employeeTel"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("employeeAddress"));
        colPosition.setCellValueFactory(new PropertyValueFactory<>("employeePosition"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("employeeSalary"));
    }

    private void loadAllEmployees() {
        ObservableList<EmployeeTm> obList = FXCollections.observableArrayList();
        try {
            List<Employee> employees = EmployeeModel.getAll();
            for (Employee e : employees) {
                obList.add(new EmployeeTm(
                        e.getEmployeeId(), e.getEmployeeName(), e.getEmployeeTel(),
                        e.getEmployeeAddress(), e.getEmployeePosition(), e.getEmployeeSalary()
                ));
            }
            tblEmployee.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load employees.").show();
        }
    }

    private void tableListener() {
        tblEmployee.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtEmployeeId.setText(newVal.getEmployeeId());
                txtEmployeeName.setText(newVal.getEmployeeName());
                txtEmployeeTel.setText(newVal.getEmployeeTel());
                txtEmployeeAddress.setText(newVal.getEmployeeAddress());
                txtEmployeePosition.setText(newVal.getEmployeePosition());
                txtEmployeeSalary.setText(String.valueOf(newVal.getEmployeeSalary()));

            }
        });
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = txtEmployeeId.getText();
        String name = txtEmployeeName.getText();
        String tel = txtEmployeeTel.getText();
        String address = txtEmployeeAddress.getText();
        String position = txtEmployeePosition.getText();

        double salary = 0;
        try {
            salary = Double.parseDouble(txtEmployeeSalary.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid number for the salary!").show();
            return;
        }

        Employee employee = new Employee(id, name, tel, address, position, salary);

        try {
            boolean isSaved = EmployeeModel.save(employee);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Employee saved successfully!").show();
                clearFields();
                loadAllEmployees();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to save!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        double salary = 0;
        try {
            salary = Double.parseDouble(txtEmployeeSalary.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid number for the salary!").show();
            return;
        }

        Employee employee = new Employee(
                txtEmployeeId.getText(),
                txtEmployeeName.getText(),
                txtEmployeeTel.getText(),
                txtEmployeeAddress.getText(),
                txtEmployeePosition.getText(),
                salary
        );

        try {
            boolean isUpdated = EmployeeModel.update(employee);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Update Succes!").show();
                clearFields();
                loadAllEmployees();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to update!").show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtEmployeeId.getText();

        try {
            boolean isDeleted = EmployeeModel.delete(id);
            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Deleted successfully!").show();
                clearFields();
                loadAllEmployees();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Delete failed!").show();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnTaskFormOnAction(ActionEvent event) {

    }

    private void clearFields() {
        txtEmployeeId.clear();
        txtEmployeeName.clear();
        txtEmployeeAddress.clear();
        txtEmployeeTel.clear();
        txtEmployeePosition.clear();
        txtEmployeeSalary.clear();
        tblEmployee.getSelectionModel().clearSelection();
    }


    @FXML void txtEmployeeIdOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(Regex.getIdPattern(), txtEmployeeId);
    }

    @FXML void txtEmployeeNameOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(Regex.getNamePattern(), txtEmployeeName);
    }

    @FXML void txtEmployeeAddressOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(Regex.getAddressPattern(), txtEmployeeAddress);
    }

    @FXML void txtPhoneNoOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(Regex.getMobilePattern(), txtEmployeeTel);
    }

    @FXML void txtPositionOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(Regex.getNamePattern(), txtEmployeePosition);
    }

    @FXML void txtSalaryOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(Regex.getSalaryPattern(), txtEmployeeSalary);
    }

    @FXML
    void txtEmpIdOnAction(ActionEvent event) {
    }
}
