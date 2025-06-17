package lk.ijse.project_01.Model;

import lk.ijse.project_01.DB.DBConnection;
import lk.ijse.project_01.DTO.BookingDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingModel {

    public static boolean bookRoom(BookingDTO dto) throws SQLException {
        Connection con = null;
        PreparedStatement pstmBooking = null;
        PreparedStatement pstmRoom = null;

        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            String sqlBooking = "INSERT INTO booking(bookingId, guestPhone, roomType, roomId, checkIn, duration, totalPrice, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmBooking = con.prepareStatement(sqlBooking);
            pstmBooking.setString(1, dto.getBookingId());
            pstmBooking.setString(2, dto.getGuestPhone());
            pstmBooking.setString(3, dto.getRoomType());
            pstmBooking.setString(4, dto.getRoomId());
            pstmBooking.setDate(5, java.sql.Date.valueOf(dto.getCheckIn()));
            pstmBooking.setInt(6, dto.getDuration());
            pstmBooking.setDouble(7, dto.getTotalPrice());
            pstmBooking.setString(8, dto.getStatus());

            int affectedRows = pstmBooking.executeUpdate();
            if (affectedRows == 0) {
                con.rollback();
                return false;
            }

            String sqlUpdateRoom = "UPDATE room SET availability = ? WHERE roomId = ?";
            pstmRoom = con.prepareStatement(sqlUpdateRoom);
            pstmRoom.setBoolean(1, false);
            pstmRoom.setString(2, dto.getRoomId());

            int affectedRoomRows = pstmRoom.executeUpdate();
            if (affectedRoomRows == 0) {
                con.rollback();
                return false;
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw e;
        } finally {
            if (pstmBooking != null) try { pstmBooking.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmRoom != null) try { pstmRoom.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) {
                try { con.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    public static boolean isRoomAvailable(int roomId, LocalDate checkIn, int duration) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getInstance().getConnection();

            String sql = "SELECT COUNT(*) FROM booking WHERE roomId = ? AND status = 'Booked' AND (" +
                    "(? BETWEEN checkIn AND DATE_ADD(checkIn, INTERVAL duration DAY)) OR " +
                    "(DATE_ADD(?, INTERVAL ? DAY) BETWEEN checkIn AND DATE_ADD(checkIn, INTERVAL duration DAY)) OR " +
                    "(checkIn BETWEEN ? AND DATE_ADD(?, INTERVAL ? DAY))" +
                    ")";
            pstm = con.prepareStatement(sql);
            pstm.setInt(1, roomId);
            pstm.setDate(2, Date.valueOf(checkIn));
            pstm.setDate(3, Date.valueOf(checkIn));
            pstm.setInt(4, duration);
            pstm.setDate(5, Date.valueOf(checkIn));
            pstm.setDate(6, Date.valueOf(checkIn));
            pstm.setInt(7, duration);

            rs = pstm.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }
            return false;

        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstm != null) try { pstm.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static boolean cancelBooking(String bookingId) throws SQLException {
        String sqlGetRoomId = "SELECT roomId FROM booking WHERE bookingId = ?";
        String sqlDeleteBooking = "DELETE FROM booking WHERE bookingId = ?";
        String sqlUpdateRoom = "UPDATE room SET availability = true WHERE roomId = ?";


        try (Connection con = DBConnection.getInstance().getNewConnection()) {
            con.setAutoCommit(false);

            String roomId;

            try (PreparedStatement pstmGetRoomId = con.prepareStatement(sqlGetRoomId)) {
                pstmGetRoomId.setString(1, bookingId);
                try (ResultSet rs = pstmGetRoomId.executeQuery()) {
                    if (rs.next()) {
                        roomId = rs.getString("roomId");
                    } else {
                        con.rollback();
                        return false;
                    }
                }
            }

            try (PreparedStatement pstmDeleteBooking = con.prepareStatement(sqlDeleteBooking)) {
                pstmDeleteBooking.setString(1, bookingId);
                if (pstmDeleteBooking.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }
            }


            try (PreparedStatement pstmUpdateRoom = con.prepareStatement(sqlUpdateRoom)) {
                pstmUpdateRoom.setString(1, roomId);
                if (pstmUpdateRoom.executeUpdate() == 0) {
                    con.rollback();
                    return false;
                }
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }



    public static List<BookingDTO> getAllBookings() throws SQLException {
        List<BookingDTO> bookings = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getInstance().getConnection();
            String sql = "SELECT bookingId, guestPhone, roomType, roomId, checkIn, duration, totalPrice, status FROM booking";
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();

            while (rs.next()) {
                BookingDTO dto = new BookingDTO();
                dto.setBookingId(rs.getString("bookingId"));
                dto.setGuestPhone(rs.getString("guestPhone"));
                dto.setRoomType(rs.getString("roomType"));
                dto.setRoomId(rs.getString("roomId"));
                dto.setCheckIn(rs.getDate("checkIn").toLocalDate());
                dto.setDuration(rs.getInt("duration"));

                dto.setTotalPrice(rs.getDouble("totalPrice"));
                dto.setStatus(rs.getString("status"));
                bookings.add(dto);
            }

        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstm != null) try { pstm.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return bookings;
    }

    public static List<BookingDTO> searchBookings(String keyword) throws SQLException {
        List<BookingDTO> bookings = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getInstance().getConnection();
            String sql = "SELECT bookingId, guestPhone, roomType, roomId, checkIn, duration, totalPrice, status FROM booking WHERE guestPhone LIKE ? OR bookingId LIKE ?";
            pstm = con.prepareStatement(sql);
            String likeKeyword = "%" + keyword + "%";
            pstm.setString(1, likeKeyword);
            pstm.setString(2, likeKeyword);
            rs = pstm.executeQuery();

            while (rs.next()) {
                BookingDTO dto = new BookingDTO();
                dto.setBookingId(rs.getString("bookingId"));
                dto.setGuestPhone(rs.getString("guestPhone"));
                dto.setRoomType(rs.getString("roomType"));
                dto.setRoomId(rs.getString("roomId"));
                dto.setCheckIn(rs.getDate("checkIn").toLocalDate());
                dto.setDuration(rs.getInt("duration"));
                // dto.setQty(rs.getInt("qty"));  // Remove qty setting
                dto.setTotalPrice(rs.getDouble("totalPrice"));
                dto.setStatus(rs.getString("status"));
                bookings.add(dto);
            }

        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstm != null) try { pstm.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return bookings;
    }

    public static String getLastBookingId() throws SQLException {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getInstance().getConnection();
            String sql = "SELECT bookingId FROM booking ORDER BY bookingId DESC LIMIT 1";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                return rs.getString("bookingId");
            } else {
                return null;
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static List<String> getAvailableRooms(String roomType, LocalDate checkIn, int duration) throws SQLException {
        List<String> availableRooms = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getInstance().getConnection();

            String sql = "SELECT roomId FROM room WHERE roomType = ? AND availability = true AND roomId NOT IN (" +
                    "SELECT roomId FROM booking WHERE status = 'Booked' AND (" +
                    "(? BETWEEN checkIn AND DATE_ADD(checkIn, INTERVAL duration DAY)) OR " +
                    "(DATE_ADD(?, INTERVAL ? DAY) BETWEEN checkIn AND DATE_ADD(checkIn, INTERVAL duration DAY)) OR " +
                    "(checkIn BETWEEN ? AND DATE_ADD(?, INTERVAL ? DAY))" +
                    "))";

            pstm = con.prepareStatement(sql);
            pstm.setString(1, roomType);
            pstm.setDate(2, Date.valueOf(checkIn));
            pstm.setDate(3, Date.valueOf(checkIn));
            pstm.setInt(4, duration);
            pstm.setDate(5, Date.valueOf(checkIn));
            pstm.setDate(6, Date.valueOf(checkIn));
            pstm.setInt(7, duration);

            rs = pstm.executeQuery();
            while (rs.next()) {
                availableRooms.add(rs.getString("roomId"));
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstm != null) try { pstm.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return availableRooms;
    }

    public static String getGuestNameByPhone(String phoneNo) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getInstance().getConnection();
            String sql = "SELECT name FROM guest WHERE contact = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, phoneNo);
            rs = pstm.executeQuery();

            if (rs.next()) {
                return rs.getString("name");
            } else {
                return null;
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstm != null) try { pstm.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
