package lk.ijse.project_01.Model;

import lk.ijse.project_01.DB.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class paymnetModel {

    public static boolean savePayment(String guestPhone, double totalAmount,  String paymentMethod) {
        String sql = "INSERT INTO Payment (Amount, PaymentDate, PaymentMethod, GuestPhone) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setDouble(1, totalAmount);
            pst.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            pst.setString(3, paymentMethod);
            pst.setString(4, guestPhone);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updatePaymentStatus(String guestPhone) {
        String sql = "UPDATE booking SET status = 'Paid' WHERE guestPhone = ?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, guestPhone);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
