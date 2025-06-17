package lk.ijse.project_01.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomModel {

    public static int getAvailableRoomCount(Connection con, String roomType) throws SQLException {
        String sql = "SELECT available_rooms FROM room_type_availability WHERE room_type = ?";
        try (PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, roomType);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("available_rooms");
                }
            }
        }
        return 0;
    }


    public static String getAvailableRoomId(Connection con, String roomType) throws SQLException {
        String sql = "SELECT room_id FROM room_status WHERE room_type = ? AND status = 'Available' LIMIT 1";
        try (PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, roomType);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("room_id");
                }
            }
        }
        return null;
    }


    public static boolean bookRoomById(Connection con, String roomId) throws SQLException {
        String roomType = getRoomTypeById(con, roomId);
        if (roomType == null) return false;

        con.setAutoCommit(false);
        try {

            String updateRoomSql = "UPDATE room_status SET status = 'Booked' WHERE room_id = ? AND status = 'Available'";
            try (PreparedStatement pstm = con.prepareStatement(updateRoomSql)) {
                pstm.setString(1, roomId);
                int updatedRows = pstm.executeUpdate();
                if (updatedRows == 0) {
                    con.rollback();
                    return false;
                }
            }


            String reduceSql = "UPDATE room_type_availability SET available_rooms = available_rooms - 1 WHERE room_type = ? AND available_rooms > 0";
            try (PreparedStatement pstm = con.prepareStatement(reduceSql)) {
                pstm.setString(1, roomType);
                int updated = pstm.executeUpdate();
                if (updated == 0) {
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
            con.setAutoCommit(true);
        }
    }


    public static boolean releaseRoomById(Connection con, String roomId) throws SQLException {
        String roomType = getRoomTypeById(con, roomId);
        if (roomType == null) return false;

        con.setAutoCommit(false);
        try {

            String updateRoomSql = "UPDATE room_status SET status = 'Available' WHERE room_id = ? AND status = 'Booked'";
            try (PreparedStatement pstm = con.prepareStatement(updateRoomSql)) {
                pstm.setString(1, roomId);
                int updatedRows = pstm.executeUpdate();
                if (updatedRows == 0) {
                    con.rollback();
                    return false;
                }
            }


            String increaseSql = "UPDATE room_type_availability SET available_rooms = available_rooms + 1 WHERE room_type = ?";
            try (PreparedStatement pstm = con.prepareStatement(increaseSql)) {
                pstm.setString(1, roomType);
                int updated = pstm.executeUpdate();
                if (updated == 0) {
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
            con.setAutoCommit(true);
        }
    }


    private static String getRoomTypeById(Connection con, String roomId) throws SQLException {
        String sql = "SELECT room_type FROM room_status WHERE room_id = ?";
        try (PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, roomId);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("room_type");
                }
            }
        }
        return null;
    }
}
