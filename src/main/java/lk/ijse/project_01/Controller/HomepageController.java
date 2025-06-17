package lk.ijse.project_01.Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lk.ijse.project_01.Model.DashboardModel;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ResourceBundle;

public class HomepageController implements Initializable {

    @FXML
    private Label availableRooms;

    @FXML
    private Label bookingCount;

    @FXML
    private BarChart<String, Number> fnbSalesChart;

    @FXML
    private Label mainDate;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private LineChart<String, Number> monthlyRevenueChart;

    @FXML
    private ComboBox<String> revenueFilterCombo;

    @FXML
    private Label totalFnbRevenue;

    @FXML
    private Label totalRevenue;

    @FXML
    private Label totalRoomPrice;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
        loadSummaryCards();
        loadRevenueChart("2025");
        loadTopFnbItems();
        loadYearsToComboBox();
    }

    private void loadSummaryCards() {
        try {
            totalRevenue.setText("Rs. " + DashboardModel.getTotalRevenue());
            bookingCount.setText(String.valueOf(DashboardModel.getBookingCount()));
            totalRoomPrice.setText("Rs. " + DashboardModel.getTotalRoomPrice());
            totalFnbRevenue.setText("Rs. " + DashboardModel.getFnbRevenue());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadRevenueChart(String year) {
        monthlyRevenueChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        try {
            Map<String, Double> revenueData = DashboardModel.getMonthlyRevenue(year);
            for (Map.Entry<String, Double> entry : revenueData.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        series.setName("Monthly Revenue - " + year);
        monthlyRevenueChart.getData().add(series);
    }

    private void loadTopFnbItems() {
        fnbSalesChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        try {
            Map<String, Integer> items = DashboardModel.getTopFnbItems();
            for (Map.Entry<String, Integer> entry : items.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        series.setName("Top F&B Items");
        fnbSalesChart.getData().add(series);
    }

    private void loadYearsToComboBox() {
        revenueFilterCombo.setItems(FXCollections.observableArrayList("2023", "2024", "2025"));
        revenueFilterCombo.setValue("2025"); // Default selected
        revenueFilterCombo.setOnAction(e -> {
            String selectedYear = revenueFilterCombo.getSelectionModel().getSelectedItem();
            loadRevenueChart(selectedYear);
        });
    }
}
