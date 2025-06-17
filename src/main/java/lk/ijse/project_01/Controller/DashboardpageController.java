package lk.ijse.project_01.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import lk.ijse.project_01.DB.DBConnection;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;



public class DashboardpageController implements Initializable {

    public AnchorPane ancMainContainer;

    private String username;

    public void setUsername(String username) {
        this.username = username;
        System.out.println("Logged in user is: " + username);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        navigateTo("/View/Homepage.fxml");
    }
    public void btnGoHomePageOnAction(ActionEvent actionEvent) { navigateTo("/view/Homepage.fxml");
    }
    public void btnGoGuestPageOnAction(ActionEvent actionEvent) {navigateTo("/View/Guestpage.fxml");
    }
    public void btnorderPageOnAction(ActionEvent actionEvent) { navigateTo("/View/B&FDashboardPage.fxml");
    }
    public void btnEmployeePageOnAction(ActionEvent actionEvent) { navigateTo("/View/employee_form.fxml");
    }
    public void btnReportPageOnAction(ActionEvent actionEvent) {navigateTo("/View/ReportPage.fxml");
    }
    public void btnSupplierPageOnAction(ActionEvent actionEvent) {navigateTo("/View/InventoryPage.fxml");
    }
    public void btnLogouttPageOnAction(ActionEvent event) throws IOException {

        Parent loginRoot = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


        stage.setScene(new Scene(loginRoot));
        stage.centerOnScreen();
        stage.setTitle("Login Form");
    }


    public void onOrder(ActionEvent actionEvent) {
    }

    public void onStock(ActionEvent actionEvent) {
    }


    private void navigateTo(String path) {
        try {
            ancMainContainer.getChildren().clear();

            Parent root = FXMLLoader.load(getClass().getResource(path));

            if (root instanceof Region) {
                Region regionRoot = (Region) root;
                regionRoot.prefWidthProperty().bind(ancMainContainer.widthProperty());
                regionRoot.prefHeightProperty().bind(ancMainContainer.heightProperty());
            }

            ancMainContainer.getChildren().add(root);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            e.printStackTrace();
        }
    }



}