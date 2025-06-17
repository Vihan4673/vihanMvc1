package lk.ijse.project_01.DTO;

import java.time.LocalDate;

public class BookingDTO {
    private String bookingId;
    private String guestPhone;
    private String roomType;
    private String roomId;
    private LocalDate checkIn;
    private int duration;
    private double totalPrice;
    private String status;


    public BookingDTO() {
    }


    public BookingDTO(String bookingId, String guestPhone, String roomType, String roomId, LocalDate checkIn,
                      int duration, double totalPrice, String status) {
        this.bookingId = bookingId;
        this.guestPhone = guestPhone;
        this.roomType = roomType;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.duration = duration;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
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
}
