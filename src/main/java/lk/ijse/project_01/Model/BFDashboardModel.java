package lk.ijse.project_01.Model;

import javafx.beans.property.*;
import lk.ijse.project_01.DB.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BFDashboardModel {

    private final StringProperty orderId = new SimpleStringProperty();
    private final StringProperty guestPhone = new SimpleStringProperty();
    private final StringProperty guestName = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final DoubleProperty bookingCost = new SimpleDoubleProperty();
    private final DoubleProperty fnbCost = new SimpleDoubleProperty();
    private final DoubleProperty totalCost = new SimpleDoubleProperty();
    private final StringProperty paymentStatus = new SimpleStringProperty();

    public BFDashboardModel(String orderId, String guestPhone, String guestName,
                            String description, double bookingCost, double fnbCost,
                            double totalCost, String paymentStatus) {
        this.orderId.set(orderId);
        this.guestPhone.set(guestPhone);
        this.guestName.set(guestName);
        this.description.set(description);
        this.bookingCost.set(bookingCost);
        this.fnbCost.set(fnbCost);
        this.totalCost.set(totalCost);
        this.paymentStatus.set(paymentStatus);
    }


    public String getOrderId() {
        return orderId.get();
    }

    public StringProperty orderIdProperty() {
        return orderId;
    }

    public String getGuestPhone() {
        return guestPhone.get();
    }

    public StringProperty guestPhoneProperty() {
        return guestPhone;
    }

    public String getGuestName() {
        return guestName.get();
    }

    public StringProperty guestNameProperty() {
        return guestName;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public double getBookingCost() {
        return bookingCost.get();
    }

    public DoubleProperty bookingCostProperty() {
        return bookingCost;
    }

    public double getFnbCost() {
        return fnbCost.get();
    }

    public DoubleProperty fnbCostProperty() {
        return fnbCost;
    }

    public double getTotalCost() {
        return totalCost.get();
    }

    public DoubleProperty totalCostProperty() {
        return totalCost;
    }

    public String getPaymentStatus() {
        return paymentStatus.get();
    }

    public StringProperty paymentStatusProperty() {
        return paymentStatus;
    }

    public void setPaymentStatus(String status) {
        this.paymentStatus.set(status);
    }


    public static class BFDashboardModelDAO {

        public static List<BFDashboardModel> loadAllOrders() {
            List<BFDashboardModel> list = new ArrayList<>();


            String sql = "SELECT b.guestPhone, " +
                    "GROUP_CONCAT(CONCAT(b.roomType, ' (', b.duration, ')') SEPARATOR ', ') AS roomDescriptions, " +
                    "GROUP_CONCAT(DISTINCT f.item_name SEPARATOR ', ') AS foodItems, " +
                    "SUM(b.totalPrice) AS bookingCost, " +
                    "IFNULL(SUM(f.total), 0) AS fnbCost, " +
                    "SUM(b.totalPrice) + IFNULL(SUM(f.total), 0) AS totalCost, " +
                    "MAX(b.status) AS paymentStatus " +
                    "FROM booking b " +
                    "LEFT JOIN foodorderdetail f ON b.guestPhone = f.guestPhone " +
                    "GROUP BY b.guestPhone";

            try (Connection conn = DBConnection.getInstance().getConnection();
                 PreparedStatement pst = conn.prepareStatement(sql);
                 ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {
                    String guestPhone = rs.getString("guestPhone");
                    String roomDescriptions = rs.getString("roomDescriptions");
                    String foodItems = rs.getString("foodItems");
                    String description = "Rooms: " + roomDescriptions +
                            (foodItems != null && !foodItems.isEmpty() ? " | Food: " + foodItems : "");
                    double bookingCost = rs.getDouble("bookingCost");
                    double fnbCost = rs.getDouble("fnbCost");
                    double totalCost = rs.getDouble("totalCost");
                    String paymentStatus = rs.getString("paymentStatus");

                    list.add(new BFDashboardModel(null, guestPhone, "", description, bookingCost, fnbCost, totalCost, paymentStatus));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return list;
        }


    }
}
