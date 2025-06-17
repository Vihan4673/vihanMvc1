package lk.ijse.project_01.Model;

import javafx.scene.control.Alert;
import lk.ijse.project_01.DB.DBConnection;
import lk.ijse.project_01.DTO.TM.UserTM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {

    public static boolean saveUser(UserTM user) throws SQLException {
        String sql = "INSERT INTO user (userName, password, phoneNo, role) VALUES (?, ?, ?, ?)";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1, user.getUserName());
        pstm.setObject(2, user.getPassword());
        pstm.setObject(3, user.getPhoneNo());
        pstm.setObject(4, user.getRole());

        return pstm.executeUpdate() > 0;
    }

    public static boolean checkCredential(String userName, String pw) throws SQLException {
        String sql = "SELECT userName, password FROM user WHERE userName = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, userName);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String dbPw = resultSet.getString("password");

            if (pw.equals(dbPw)) {
                return true;
            } else {
                new Alert(Alert.AlertType.ERROR, "Sorry! Password is incorrect!").show();
                return false;
            }
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Sorry! Username can't be found!").show();
            return false;
        }
    }
}
