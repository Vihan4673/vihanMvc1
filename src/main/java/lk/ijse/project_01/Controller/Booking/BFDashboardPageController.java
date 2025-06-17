package lk.ijse.project_01.Controller.Booking;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lk.ijse.project_01.Model.BFDashboardModel;
import lk.ijse.project_01.Model.paymnetModel;

import java.io.IOException;
import java.util.List;

public class BFDashboardPageController {


    @FXML
    private VBox orderDashboard;
    @FXML
    private JFXButton addBooking;

    @FXML
    private JFXButton addFoodBeverage;

    @FXML
    private JFXButton btnPayment;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private TableColumn<BFDashboardModel, String> descriptionCol;

    @FXML
    private TableColumn<BFDashboardModel, String> guestNameCol;

    @FXML
    private TableColumn<BFDashboardModel, String> guestTelCol;

    @FXML
    private TableView<BFDashboardModel> orderTable;

    @FXML
    private TableColumn<BFDashboardModel, String> paymentCol;

    @FXML
    private TextField searchField;

    @FXML
    private TableColumn<BFDashboardModel, Double> totalCostCol;

    private final ObservableList<BFDashboardModel> orderList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        guestTelCol.setCellValueFactory(new PropertyValueFactory<>("guestPhone"));
        guestNameCol.setCellValueFactory(new PropertyValueFactory<>("guestName"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        totalCostCol.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        paymentCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

        loadOrdersFromDatabase();
    }

    private void loadOrdersFromDatabase() {
        List<BFDashboardModel> orders = BFDashboardModel.BFDashboardModelDAO.loadAllOrders();
        orderList.setAll(orders);
        orderTable.setItems(orderList);
    }

    @FXML
    void addBookingOnAction(ActionEvent event) {
        try {
            AnchorPane bookingPage = FXMLLoader.load(getClass().getResource("/View/BookingPage.fxml"));
            orderDashboard.getChildren().setAll(bookingPage);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load booking page!").show();
        }
    }

    @FXML
    void addFoodBeverageOnAction(ActionEvent event) {
        try {
            AnchorPane foodBeveragePage = FXMLLoader.load(getClass().getResource("/View/F&BOderPage.fxml"));
            orderDashboard.getChildren().setAll(foodBeveragePage);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load food & beverage page!").show();
        }
    }

    @FXML
    void paymentOnAction(ActionEvent event) {
        BFDashboardModel selectedOrder = orderTable.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) {
            new Alert(Alert.AlertType.WARNING, "Please select an order to make payment.").show();
            return;
        }

        if ("Paid".equalsIgnoreCase(selectedOrder.getPaymentStatus())) {
            new Alert(Alert.AlertType.INFORMATION, "This order is already paid.").show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/PaymentPage.fxml"));
            AnchorPane paymentPage = loader.load();

            PaymentPageController controller = loader.getController();


            controller.setPaymentData(
                    selectedOrder.getGuestPhone(),
                    selectedOrder.getTotalCost()
            );

            controller.setOnPaymentSuccess(() -> {
                boolean updated = paymnetModel.updatePaymentStatus(selectedOrder.getGuestPhone());

                if (updated) {
                    loadOrdersFromDatabase();
                    new Alert(Alert.AlertType.INFORMATION, "Payment successful and updated!").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Payment saved, but failed to update status!").show();
                }
            });

            orderDashboard.getChildren().setAll(paymentPage);

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load payment page!").show();
        }
    }


    @FXML
    void searchOnAction(ActionEvent event) {
        String searchText = searchField.getText().trim();

        if (searchText.isEmpty()) {
            loadOrdersFromDatabase();
            return;
        }


        List<BFDashboardModel> filteredOrders = BFDashboardModel.BFDashboardModelDAO.loadAllOrders();


        filteredOrders.removeIf(order -> !order.getGuestPhone().toLowerCase().contains(searchText.toLowerCase()));


        orderList.setAll(filteredOrders);
        orderTable.setItems(orderList);
    }


}
