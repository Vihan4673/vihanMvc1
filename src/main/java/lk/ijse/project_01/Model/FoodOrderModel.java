package lk.ijse.project_01.Model;

import lk.ijse.project_01.DB.DBConnection;

import java.sql.*;
import java.util.*;

public class FoodOrderModel {

    public static String getGuestNameByPhone(String phone) throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT name FROM Guest WHERE Contact = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        }
        return null;
    }

    public static boolean saveFoodOrder(String phone, List<OrderItem> orderList) throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        PreparedStatement detailStmt = null;

        try {
            con.setAutoCommit(false);

            String detailSql = "INSERT INTO FoodOrderDetail (item_name, quantity, unit_price, total, guestPhone, date) VALUES (?, ?, ?, ?, ?, NOW())";
            detailStmt = con.prepareStatement(detailSql);

            for (OrderItem item : orderList) {
                detailStmt.setString(1, item.getItem());
                detailStmt.setInt(2, item.getQuantity());
                detailStmt.setDouble(3, item.getUnitPrice());
                detailStmt.setDouble(4, item.getTotal());
                detailStmt.setString(5, phone);
                detailStmt.addBatch();
            }

            int[] result = detailStmt.executeBatch();

            for (int res : result) {
                if (res == 0) {
                    con.rollback();
                    return false;
                }
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            if (detailStmt != null) detailStmt.close();
            con.setAutoCommit(true);
        }
    }



    public static Map<String, List<FoodItem>> getAllFoodItemsGroupedByCategory() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT name, price, category FROM FoodItem";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            Map<String, List<FoodItem>> categoryMap = new HashMap<>();

            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String category = rs.getString("category");

                FoodItem item = new FoodItem(name, price);
                categoryMap.computeIfAbsent(category, k -> new ArrayList<>()).add(item);
            }

            return categoryMap;
        }
    }

}