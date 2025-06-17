package lk.ijse.project_01.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lk.ijse.project_01.DTO.GuestDTO;
import lk.ijse.project_01.DTO.TM.GuestTM;
import lk.ijse.project_01.Model.GuestModel;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;




public class GuestPageController implements Initializable {
    public Label lblGuestId;
    public TextField txtName;
    public TextField txtNic;
    public TextField txtAddress;
    public TextField txtPhone;


    public TableView<GuestTM> tblGuest;
    public TableColumn<GuestTM, String> colId;
    public TableColumn<GuestTM, String> colName;
    public TableColumn<GuestTM, String> colNic;
    public TableColumn<GuestTM, String> colAddress;
    public TableColumn<GuestTM, String> colPhone;


    private final GuestModel guestModel = new GuestModel();

    public Button btnReport;
    public Button btnReset;
    public Button btnDelete;
    public Button btnUpdate;
    public Button btnSave;

    private final String namePattern = "^[A-Za-z ]+$";
    private final String NicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
    private final String addressPattern = "^[A-Za-z ]+$";
    private final String phonePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("GuestId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong.").show();
        }
    }

    private void loadTableData() throws SQLException {

        tblGuest.setItems(FXCollections.observableArrayList(
                guestModel.getAllGuest().stream()
                        .map(guestDTO -> new GuestTM(
                                guestDTO.getGuestId(),
                                guestDTO.getName(),
                                guestDTO.getNic(),
                                guestDTO.getAddress(),
                                guestDTO.getPhone()
                        )).toList()
        ));
    }

    private void resetPage() {
        try {
            loadTableData();
            loadNextId();


            btnSave.setDisable(false);


            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);

            txtName.setText("");
            txtNic.setText("");
            txtNic.setText("");
            txtAddress.setText("");
            txtPhone.setText("");

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong.").show();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String GuestId = lblGuestId.getText();
        String name = txtName.getText();
        String Nic = txtNic.getText();
        String address = txtAddress.getText();
        String phone = txtPhone.getText();

        boolean isValidName = name.matches(namePattern);
        boolean isValidNic = Nic.matches(NicPattern);
        boolean isValidAddress = address.matches(addressPattern);
        boolean isValidPhone = phone.matches(phonePattern);


        txtName.setStyle(txtName.getStyle() + ";-fx-border-color: #7367F0;");
        txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: #7367F0;");
        txtAddress.setStyle(txtAddress.getStyle() + ";-fx-border-color: #7367F0;");
        txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: #7367F0;");

        if (!isValidName) txtName.setStyle(txtName.getStyle() + ";-fx-border-color: red;");
        if (!isValidNic) txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: red;");
        if (!isValidAddress) txtAddress.setStyle(txtAddress.getStyle() + ";-fx-border-color: red;");
        if (!isValidPhone) txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: red;");


        GuestDTO guestDTO = new GuestDTO(
                GuestId,
                name,
                Nic,
                address,
                phone
        );


        if (isValidName && isValidNic && isValidPhone  &&  isValidAddress ) {
            try {
                boolean isSaved = guestModel.saveCustomer(guestDTO);

                if (isSaved) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Guest saved successfully.").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail to save customer.").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to save guest.").show();
            }
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String guestId = lblGuestId.getText();
        String name = txtName.getText();
        String nic = txtNic.getText();
        String address = txtAddress.getText();
        String phone = txtPhone.getText();

        GuestDTO guestDTO = new GuestDTO(
                guestId,
                name,
                nic,
                address,
                phone
        );

        try {
            boolean isUpdated = GuestModel.updateGuest(guestDTO);

            if (isUpdated) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Customer updated successfully.").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to update customer.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to update customer.").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure ?",
                ButtonType.YES,
                ButtonType.NO
        );

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES) {

            String guestId = lblGuestId.getText();
            try {
                boolean isDeleted = guestModel.deleteCustomer(guestId);

                if (isDeleted) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Customer deleted successfully.").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail to delete customer.").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to delete customer.").show();
            }
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) {

        resetPage();
    }

    private void loadNextId() throws SQLException {
        String nextId = guestModel.getNextGuestId();
        lblGuestId.setText(nextId);
    }

    public void onClickTable(MouseEvent mouseEvent) {
        GuestTM selectedItem = tblGuest.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblGuestId.setText(selectedItem.getGuestId());
            txtName.setText(selectedItem.getName());
            txtNic.setText(selectedItem.getNic());
            txtAddress.setText(selectedItem.getAddress());
            txtPhone.setText(selectedItem.getPhone());


            btnSave.setDisable(true);


            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }


    @FXML
    private JFXButton btnClose;

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }


}