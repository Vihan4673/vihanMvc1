package lk.ijse.project_01.Controller.Booking;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.project_01.Model.BookingModel;
import lk.ijse.project_01.Model.FoodItem;
import lk.ijse.project_01.Model.OrderItem;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import lk.ijse.project_01.Model.FoodOrderModel;



public class FBOderPageController {

    @FXML
    private ComboBox<String> cmbCategory;

    @FXML
    private ComboBox<String> cmbItem;

    @FXML
    private Spinner<Integer> spinnerQty;

    @FXML
    private TextField txtPhoneNo;

    @FXML
    private Label lblGuestName;

    @FXML
    private Label lblPrice;

    @FXML
    private TableView<OrderItem> tblOrder;

    @FXML
    private TableColumn<OrderItem, String> colItem;

    @FXML
    private TableColumn<OrderItem, Integer> colQty;

    @FXML
    private TableColumn<OrderItem, Double> colUnitPrice;

    @FXML
    private TableColumn<OrderItem, Double> colTotal;

    @FXML
    private TableColumn<OrderItem, Button> colAction;

    @FXML
    private Label lblOrderTotal;
    @FXML private AnchorPane mainAnchorPane;

    private final ObservableList<OrderItem> orderList = FXCollections.observableArrayList();

    private final Map<String, List<FoodItem>> categoryItems = new HashMap<>();

    public void initialize() {
        initSpinner();
        loadFoodDataFromDatabase();
        loadCategories();
        setupTable();
        addListeners();
    }

    private void initSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        spinnerQty.setValueFactory(valueFactory);
    }

    private void loadFoodDataFromDatabase() {
        try {
            Map<String, List<FoodItem>> foodMap = FoodOrderModel.getAllFoodItemsGroupedByCategory();
            categoryItems.clear();
            categoryItems.putAll(foodMap);
            cmbCategory.setItems(FXCollections.observableArrayList(categoryItems.keySet()));
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load food data from database!").show();
        }
    }


    private void loadCategories() {
        cmbCategory.setItems(FXCollections.observableArrayList(categoryItems.keySet()));
    }

    private void addListeners() {
        cmbCategory.setOnAction(e -> {
            String selectedCategory = cmbCategory.getValue();
            if (selectedCategory != null) {
                List<FoodItem> items = categoryItems.getOrDefault(selectedCategory, Collections.emptyList());
                cmbItem.setItems(FXCollections.observableArrayList(
                        items.stream().map(FoodItem::getName).toList()
                ));
            }
        });

        cmbItem.setOnAction(e -> {
            String selectedCategory = cmbCategory.getValue();
            String selectedItem = cmbItem.getValue();
            if (selectedCategory != null && selectedItem != null) {
                Optional<FoodItem> match = categoryItems.get(selectedCategory).stream()
                        .filter(item -> item.getName().equals(selectedItem))
                        .findFirst();
                match.ifPresent(item -> lblPrice.setText(String.format("Rs. %.2f", item.getPrice())));
            }
        });

        txtPhoneNo.setOnAction(e -> loadGuest());

        txtPhoneNo.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                loadGuest();
            }
        });
    }

    private void loadGuest() {
        String phone = txtPhoneNo.getText().trim();
        try {
            if (phone.isEmpty()) {
                lblGuestName.setText("Unknown Guest");
                return;
            }

            String name = BookingModel.getGuestNameByPhone(phone);
            lblGuestName.setText(name != null ? name : "Unknown Guest");

        } catch (SQLException e) {
            e.printStackTrace();
            lblGuestName.setText("Unknown Guest");
        }
    }

    private void setupTable() {
        colItem.setCellValueFactory(new PropertyValueFactory<>("item"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));


        tblOrder.setItems(orderList);
    }

    @FXML
    void btnAddToCartOnAction() {
        String item = cmbItem.getValue();
        String category = cmbCategory.getValue();
        int qty = spinnerQty.getValue();

        if (item == null || category == null) return;

        double price = (double) categoryItems.get(category).stream()
                .filter(i -> i.getName().equals(item))
                .findFirst()
                .map(FoodItem::getPrice)
                .orElse(0.0);

        double total = price * qty;

        for (OrderItem existing : orderList) {
            if (existing.getItem().equals(item)) {
                existing.setQuantity(existing.getQuantity() + qty);
                existing.setTotal(existing.getUnitPrice() * existing.getQuantity());
                tblOrder.refresh();
                updateTotal();
                return;
            }
        }

        Button btnDelete = new Button("Remove");
        OrderItem orderItem = new OrderItem(item, qty, price, total, btnDelete);

        btnDelete.setOnAction(e -> {
            orderList.remove(orderItem);
            updateTotal();
        });

        orderList.add(orderItem);
        updateTotal();
    }

    @FXML
    void btnClearOnAction() {
        cmbCategory.getSelectionModel().clearSelection();
        cmbItem.getSelectionModel().clearSelection();
        spinnerQty.getValueFactory().setValue(1);
        lblPrice.setText("0.00");
    }

    @FXML
    void btnConfirmOrderOnAction() {
        String phone = txtPhoneNo.getText().trim();

        if (phone.isEmpty() || lblGuestName.getText().equals("Unknown Guest")) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid guest phone number.").show();
            return;
        }

        if (orderList.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "No items in the order!").show();
            return;
        }

        try {
            boolean success = FoodOrderModel.saveFoodOrder(phone, orderList);


            if (success) {
                new Alert(Alert.AlertType.INFORMATION, "Order confirmed successfully!").show();
                clearAll();
            } else {
                new Alert(Alert.AlertType.ERROR, "Order saving failed!").show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    private void clearAll() {
        cmbCategory.getSelectionModel().clearSelection();
        cmbItem.getSelectionModel().clearSelection();
        spinnerQty.getValueFactory().setValue(1);
        lblPrice.setText("0.00");
        txtPhoneNo.clear();
        lblGuestName.setText("");
        orderList.clear();
        updateTotal();
    }

    private void updateTotal() {
        double total = orderList.stream().mapToDouble(OrderItem::getTotal).sum();
        lblOrderTotal.setText(String.format("Rs. %.2f", total));
    }

    @FXML
    void btnBackOnAction(ActionEvent event) {
        try {
            Parent previousView = FXMLLoader.load(getClass().getResource("/view/B&FDashboardPage.fxml"));
            mainAnchorPane.getChildren().setAll(previousView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
  @FXML
    public void btnAddGuestOnAction(ActionEvent actionEvent) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Guestpage.fxml"));
                Parent guestRoot = loader.load();

                Stage guestStage = new Stage();
                guestStage.setTitle("Add Guest");
                guestStage.setScene(new Scene(guestRoot));
                guestStage.initModality(Modality.APPLICATION_MODAL);
                guestStage.showAndWait();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
