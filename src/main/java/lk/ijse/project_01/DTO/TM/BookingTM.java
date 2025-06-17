package lk.ijse.project_01.DTO.TM;

import java.time.LocalDate;


public class BookingTM {
    private String bookingId;
    private String roomId;
    private LocalDate checkIn;
    private int duration;
    private double totalPrice;
    private String status;
    private String roomType;


    public BookingTM(String bookingId, String roomId, LocalDate checkIn, int duration, double totalPrice, String status, String roomType) {
        this.bookingId = bookingId;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.duration = duration;
        this.totalPrice = totalPrice;
        this.status = status;
        this.roomType = roomType;
    }

    // Getters and Setters
    public String getBookingId() {
        return bookingId;
    }
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }
    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoomType() {
        return roomType;
    }
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }


}
