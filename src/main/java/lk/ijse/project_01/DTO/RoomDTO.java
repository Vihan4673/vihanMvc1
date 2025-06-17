package lk.ijse.project_01.DTO;

public class RoomDTO {
    private String roomId;
    private String roomType;
    private String status;


    public RoomDTO() {}

    public RoomDTO(String roomId, String roomType, String status) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.status = status;
    }

    // Getters and Setters
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RoomDTO{" +
                "roomId=" + roomId +
                ", roomType='" + roomType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
