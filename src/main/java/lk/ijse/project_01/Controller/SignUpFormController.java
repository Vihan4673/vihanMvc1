package lk.ijse.project_01.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.project_01.Util.Regex;
import lk.ijse.project_01.DTO.TM.UserTM;
import lk.ijse.project_01.Model.UserModel;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpFormController {
    @FXML
    private Button btnSignup;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtPhoneNo;

    @FXML
    private TextField txtRole;

    @FXML
    private TextField txtUserName;

    public void signupbtnOnAction(ActionEvent actionEvent) {
        String userName = txtUserName.getText();
        String password = txtPassword.getText();
        String role = txtRole.getText();
        String phoneNo = txtPhoneNo.getText();

        UserTM user = new UserTM(userName, password, phoneNo, role);

        switch (isValied()) {
            case 0:
                try {
                    boolean isSaved = UserModel.saveUser(user);
                    if (isSaved) {
                        new Alert(Alert.AlertType.CONFIRMATION, "User saved.").show();
                        clearFields();
                    }
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                }
                break;

            case 1:
                new Alert(Alert.AlertType.ERROR, "Invalid User Name format").show();
                break;
            case 2:
                new Alert(Alert.AlertType.ERROR, "Invalid password format").show();
                break;
            case 3:
                new Alert(Alert.AlertType.ERROR, "Invalid phone no format").show();
                break;
            case 4:
                new Alert(Alert.AlertType.ERROR, "Invalid input for role field").show();
                break;
        }
    }

    private void clearFields() {
        txtUserName.setText("");
        txtPassword.setText("");
        txtPhoneNo.setText("");
        txtRole.setText("");
    }

    @FXML
    void linkSignInOnAction(ActionEvent event) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Login form");
    }

    @FXML
    void PasswordOnKeyOnreleased(KeyEvent event) {
        Regex.setTextColor(Regex.PASSWORD, txtPassword);
    }

    @FXML
    void PhoneNoOnKeyOnreleased(KeyEvent event) {
        Regex.setTextColor(Regex.PHONENO, txtPhoneNo);
    }

    @FXML
    void RoleOnkeyReleased(KeyEvent event) {
        Regex.setTextColor(Regex.NAME, txtRole);
    }

    @FXML
    void UserNameOnKeyOnreleased(KeyEvent event) {
        Regex.setTextColor(Regex.NAME, txtUserName);
    }

    public int isValied() {
        if (!Regex.setTextColor(Regex.NAME, txtUserName)) return 1;
        if (!Regex.setTextColor(Regex.PASSWORD, txtPassword)) return 2;
        if (!Regex.setTextColor(Regex.PHONENO, txtPhoneNo)) return 3;
        if (!Regex.setTextColor(Regex.NAME, txtRole)) return 4;
        return 0;
    }
}
