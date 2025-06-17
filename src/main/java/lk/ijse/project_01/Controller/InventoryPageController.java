package lk.ijse.project_01.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.net.URL;
import java.util.ResourceBundle;

public class InventoryPageController implements Initializable {

    @FXML
    private AnchorPane ancMainContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void AddSupplierOnAction(ActionEvent event) {
        navigateTo("/View/SupplierDetailPage.fxml");
    }

    @FXML
    private void  AddFoodBeverageOnAction(ActionEvent event) {
        navigateTo("/view/FoodAndBevarageAddPage.fxml");
    }

    @FXML
    private void btnAddRoomInventoryOnAction(ActionEvent event) {
        navigateTo("/View/AddRoomPage.fxml");
    }

    private void navigateTo(String fxmlPath) {
        try {
            ancMainContainer.getChildren().clear();

            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));

            if (root instanceof Region regionRoot) {
                regionRoot.prefWidthProperty().bind(ancMainContainer.widthProperty());
                regionRoot.prefHeightProperty().bind(ancMainContainer.heightProperty());
            }

            ancMainContainer.getChildren().add(root);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found: " + fxmlPath).show();
            e.printStackTrace();
        }
    }

}
