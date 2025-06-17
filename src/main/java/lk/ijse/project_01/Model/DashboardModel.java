package lk.ijse.project_01.Model;



import lk.ijse.project_01.DB.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardModel {

    public static double getTotalRevenue() throws SQLException {
        String sql = "SELECT SUM(Amount) FROM payment";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getDouble(1);
        }
        return 0.0;
    }

    public static int getBookingCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM booking";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public static double getTotalRoomPrice() throws SQLException {
        String sql = "SELECT SUM(totalPrice) FROM Booking";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getDouble(1);
        }
        return 0.0;
    }

    public static double getFnbRevenue() throws SQLException {
        String sql = "SELECT SUM(total) FROM foodOrderdetail";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getDouble(1);
        }
        return 0.0;
    }

    public static Map<String, Double> getMonthlyRevenue(String year) throws SQLException {
        String sql = "SELECT MONTH(PaymentDate) AS month, SUM(Amount) AS revenue " +
                "FROM payment WHERE YEAR(PaymentDate) = ? GROUP BY MONTH(PaymentDate)";
        Map<String, Double> map = new LinkedHashMap<>();

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, year);
            ResultSet rs = ps.executeQuery();

            String[] months = {
                    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
            };
            for (String month : months) {
                map.put(month, 0.0);
            }

            while (rs.next()) {
                int monthIndex = rs.getInt("month") - 1;
                double revenue = rs.getDouble("revenue");
                map.put(months[monthIndex], revenue);
            }
        }
        return map;
    }


    public static Map<String, Integer> getTopFnbItems() throws SQLException {
        String sql = "SELECT fi.name AS item_name, SUM(fod.quantity) AS total " +
                "FROM foodorderdetail fod " +
                "JOIN fooditem fi ON fod.item_name = fi.name " +
                "GROUP BY fi.name ORDER BY total DESC LIMIT 5";

        Map<String, Integer> map = new LinkedHashMap<>();

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getString("item_name"), rs.getInt("total"));
            }
        }

        return map;
    }

}
