package lk.ijse.project_01.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lk.ijse.project_01.DTO.SupplierDTO;
import lk.ijse.project_01.DTO.TM.SupplierTM;
import lk.ijse.project_01.Model.SupplierModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierDetailPageController implements Initializable {

    @FXML private Label lblSupplierId;
    @FXML private TextField txtName;
    @FXML private TextField txtNic;
    @FXML private TextField txtAddress;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtSupply;

    @FXML private TableView<SupplierTM> tblSupplier;
    @FXML private TableColumn<SupplierTM, String> colId;
    @FXML private TableColumn<SupplierTM, String> colName;
    @FXML private TableColumn<SupplierTM, String> colNic;
    @FXML private TableColumn<SupplierTM, String> colAddress;
    @FXML private TableColumn<SupplierTM, String> colPhone;
    @FXML private TableColumn<SupplierTM, String> colEmail;
    @FXML private TableColumn<SupplierTM, String> colSupply;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnReset;
    @FXML private JFXButton btnClose;

    private final SupplierModel supplierModel = new SupplierModel();

    // Validation patterns
    private final String namePattern = "^[A-Za-z ]+$";
    private final String nicPattern = "^[0-9]{9}[vVxX]$|^[0-9]{12}$";
    private final String addressPattern = "^[A-Za-z0-9 ,./-]+$";
    private final String phonePattern = "^\\d{10}$";
    private final String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSupply.setCellValueFactory(new PropertyValueFactory<>("supply"));

        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to initialize data.");
        }
    }

    private void loadTableData() throws SQLException, ClassNotFoundException {
        List<SupplierDTO> suppliers = supplierModel.getAllSuppliers();
        tblSupplier.setItems(FXCollections.observableArrayList(
                suppliers.stream()
                        .map(s -> new SupplierTM(
                                s.getSupplierId(),
                                s.getName(),
                                s.getNic(),
                                s.getAddress(),
                                s.getPhone(),
                                s.getEmail(),
                                s.getSupply()
                        )).toList()
        ));
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadTableData();
        loadNextId();
        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        clearFields();
    }

    private void clearFields() {

        txtName.clear();
        txtNic.clear();
        txtAddress.clear();
        txtPhone.clear();
        txtEmail.clear();
        txtSupply.clear();


        txtName.setStyle(null);
        txtNic.setStyle(null);
        txtAddress.setStyle(null);
        txtPhone.setStyle(null);
        txtEmail.setStyle(null);
        txtSupply.setStyle(null);

        tblSupplier.getSelectionModel().clearSelection();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextId = supplierModel.getNextSupplierId();
        lblSupplierId.setText(nextId);
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validateFields()) return;

        SupplierDTO supplierDTO = new SupplierDTO(
                lblSupplierId.getText(),
                txtName.getText(),
                txtNic.getText(),
                txtAddress.getText(),
                txtPhone.getText(),
                txtEmail.getText(),
                txtSupply.getText()
        );

        try {
            boolean saved = supplierModel.saveSupplier(supplierDTO);
            if (saved) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Supplier saved successfully.");
                resetPage();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed", "Failed to save supplier.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while saving.");
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validateFields()) return;

        SupplierDTO supplierDTO = new SupplierDTO(
                lblSupplierId.getText(),
                txtName.getText(),
                txtNic.getText(),
                txtAddress.getText(),
                txtPhone.getText(),
                txtEmail.getText(),
                txtSupply.getText()
        );

        try {
            boolean updated = supplierModel.updateSupplier(supplierDTO);
            if (updated) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Supplier updated successfully.");
                resetPage();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed", "Failed to update supplier.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating.");
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure to delete this supplier?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES) {
            try {
                boolean deleted = supplierModel.deleteSupplier(lblSupplierId.getText());
                if (deleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Supplier deleted successfully.");
                    resetPage();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed", "Failed to delete supplier.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting.");
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to reset page.");
        }
    }

    @FXML
    void onClickTable(MouseEvent event) {
        SupplierTM selected = tblSupplier.getSelectionModel().getSelectedItem();
        if (selected != null) {
            lblSupplierId.setText(selected.getSupplierId());
            txtName.setText(selected.getName());
            txtNic.setText(selected.getNic());
            txtAddress.setText(selected.getAddress());
            txtPhone.setText(selected.getPhone());
            txtEmail.setText(selected.getEmail());
            txtSupply.setText(selected.getSupply());

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }


    private boolean validateFields() {
        boolean isValid = true;

        if (!txtName.getText().matches(namePattern)) {
            txtName.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            txtName.setStyle(null);
        }

        if (!txtNic.getText().matches(nicPattern)) {
            txtNic.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            txtNic.setStyle(null);
        }

        if (!txtAddress.getText().matches(addressPattern)) {
            txtAddress.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            txtAddress.setStyle(null);
        }

        if (!txtPhone.getText().matches(phonePattern)) {
            txtPhone.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            txtPhone.setStyle(null);
        }

        if (!txtEmail.getText().matches(emailPattern)) {
            txtEmail.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            txtEmail.setStyle(null);
        }

        if (txtSupply.getText().isBlank()) {
            txtSupply.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            txtSupply.setStyle(null);
        }

        if (!isValid) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please correct highlighted fields.");
        }

        return isValid;
    }

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void btnSendEmailOnAction() {
        SupplierTM selectedSupplier = tblSupplier.getSelectionModel().getSelectedItem();
        if (selectedSupplier == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a supplier first.");
            return;
        }
        String email = selectedSupplier.getEmail();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EmailForm.fxml"));
            Parent root = loader.load();

            lk.ijse.project_01.Controller.EmailForm emailController = loader.getController();
            emailController.setEmail(email);

            Stage stage = new Stage();
            stage.setTitle("Send Email");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open email form.");
        }
    }



}
