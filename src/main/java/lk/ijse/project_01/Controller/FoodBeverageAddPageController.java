package lk.ijse.project_01.Controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.project_01.DTO.AddFoodbevarge;
import lk.ijse.project_01.Model.FoodBevarageAddPageModel;


import java.sql.SQLException;
import java.util.List;

public class FoodBeverageAddPageController {

    @FXML
    private TableView<AddFoodbevarge> tblFoodBeverage;
    @FXML
    private TableColumn<AddFoodbevarge, String> colItemId;
    @FXML
    private TableColumn<AddFoodbevarge, String> colItemName;
    @FXML
    private TableColumn<AddFoodbevarge, String> colCategory;
    @FXML
    private TableColumn<AddFoodbevarge, Double> colPrice;

    @FXML
    private TextField txtItemId;
    @FXML
    private TextField txtName;
    @FXML
    private JFXComboBox<String> cmbCategory;
    @FXML
    private TextField txtPrice;

    @FXML
    private JFXButton btnClear;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnUpdate;
    @FXML
    private JFXButton btnDelete;

    private FoodBevarageAddPageModel model = new FoodBevarageAddPageModel();

    public void initialize() {

        colItemId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


        cmbCategory.setItems(FXCollections.observableArrayList("Main Course", "Drink", "Dessert"));


        cmbCategory.setOnAction(event -> generateItemId());


        loadAllItems();
    }

    private void generateItemId() {
        String category = cmbCategory.getValue();
        if (category == null) {
            txtItemId.clear();
            return;
        }

        String prefix = switch (category) {
            case "Main Course" -> "MC";
            case "Drink" -> "DR";
            case "Dessert" -> "DS";
            default -> "XX";
        };

        try {

            String lastId = model.getLastItemIdByPrefix(prefix);
            String newId = generateNextId(prefix, lastId);
            txtItemId.setText(newId);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to generate Item ID.");
        }
    }


    private String generateNextId(String prefix, String lastId) {
        if (lastId == null) {
            return prefix + "001";
        }

        int number = Integer.parseInt(lastId.substring(prefix.length()));
        number++;
        return String.format("%s%03d", prefix, number);
    }

    private void loadAllItems() {
        try {
            List<AddFoodbevarge> items = model.getAllItems();
            ObservableList<AddFoodbevarge> obList = FXCollections.observableArrayList(items);
            tblFoodBeverage.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load items.");
        }
    }


    @FXML
    private void btnClearOnAction() {
        txtItemId.clear();
        txtName.clear();
        txtPrice.clear();
        cmbCategory.getSelectionModel().clearSelection();
    }

    @FXML
    private void btnSaveOnAction() {
        String id = txtItemId.getText();
        String name = txtName.getText();
        String category = cmbCategory.getValue();
        String priceText = txtPrice.getText();

        if (id.isEmpty() || name.isEmpty() || category == null || priceText.isEmpty()) {
            showAlert("Validation Error", "Please fill all fields.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Invalid price format.");
            return;
        }

        AddFoodbevarge item = new AddFoodbevarge(id, name, category, price);

        try {
            boolean added = model.addItem(item);
            if (added) {
                showAlert("Success", "Item added successfully.");
                loadAllItems();
                btnClearOnAction();
            } else {
                showAlert("Failure", "Failed to add item.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to add item.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void btnUpdateOnAction(ActionEvent actionEvent) {
        String id = txtItemId.getText();
        String name = txtName.getText();
        String category = cmbCategory.getValue();
        String priceText = txtPrice.getText();

        if (id.isEmpty() || name.isEmpty() || category == null || priceText.isEmpty()) {
            showAlert("Validation Error", "Please fill all fields.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Invalid price format.");
            return;
        }

        AddFoodbevarge item = new AddFoodbevarge(id, name, category, price);

        try {
            boolean updated = model.updateItem(item);
            if (updated) {
                showAlert("Success", "Item updated successfully.");
                loadAllItems();
                btnClearOnAction();
            } else {
                showAlert("Failure", "Failed to update item.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to update item.");
        }
    }

    @FXML
    private void btnDeleteOnAction(ActionEvent actionEvent) {
        String id = txtItemId.getText();

        if (id.isEmpty()) {
            showAlert("Validation Error", "Please select an item to delete.");
            return;
        }

        try {
            boolean deleted = model.deleteItem(id);
            if (deleted) {
                showAlert("Success", "Item deleted successfully.");
                loadAllItems();
                btnClearOnAction();
            } else {
                showAlert("Failure", "Failed to delete item.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to delete item.");
        }
    }

    @FXML
    private void tblFoodBeverageOnMouseClicked() {
        AddFoodbevarge selectedItem = tblFoodBeverage.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            txtItemId.setText(selectedItem.getId());
            txtName.setText(selectedItem.getName());
            txtPrice.setText(String.valueOf(selectedItem.getPrice()));
            cmbCategory.setValue(selectedItem.getCategory());
        }
    }


}
