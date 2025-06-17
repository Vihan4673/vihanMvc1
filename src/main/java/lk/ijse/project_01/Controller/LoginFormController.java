 package lk.ijse.project_01.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.project_01.Util.Regex;
import lk.ijse.project_01.Model.UserModel;


import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class LoginFormController {
    public TextField txtUserName;
    public PasswordField txtPassword;
    public AnchorPane rootNode;


    public void loginbtnOnAction(ActionEvent actionEvent) throws IOException {
        String userName = txtUserName.getText();
        String pw = txtPassword.getText();
        if(!userName.isEmpty() && !pw.isEmpty()){
            try {
                boolean isCorrect = UserModel.checkCredential(userName, pw);
                if (isCorrect ) {
                    navigateToDashBoard(userName);
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
        else{
            new Alert(Alert.AlertType.ERROR, "Please insert user name & password").show();
        }

    }


    private void navigateToDashBoard(String userName) throws IOException {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboardpage.fxml"));
            Parent dashboardRoot = loader.load();
             DashboardpageController controller = loader.getController();
            controller.setUsername(userName);
            Scene scene = new Scene(dashboardRoot);
            Stage stage = (Stage) this.rootNode.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Bakery Management System");


            InputStream iconStream = getClass().getResourceAsStream("/icon/sns-removebg-preview.png");
            if (iconStream != null) {
                Image icon = new Image(iconStream);
                stage.getIcons().add(icon);
            } else {
                System.out.println("Failed to load application icon. Icon file not found.");
            }
        } catch (IOException e) {
            System.out.println("Error loading dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void linkRegisterOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/signup_form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("signUp form");

    }

    @FXML
    void PasswordOnKeyReleased(KeyEvent event) {
        Regex.setTextColor(Regex.getPasswordPattern(), txtPassword);
    }

    @FXML
    void UserNameOnKeyOnreleased(KeyEvent event) {
        Regex.setTextColor(Regex.getNamePattern(), txtUserName);

    }

    @FXML
    void txtPasswordOnAction(ActionEvent event) {

    }

    @FXML
    void txtUserNameOnAction(ActionEvent event) {
        txtPassword.requestFocus();
    }

}
