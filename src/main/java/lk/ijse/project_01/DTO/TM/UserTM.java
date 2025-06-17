package lk.ijse.project_01.DTO.TM;

public class UserTM {
    private String userId;
    private String userName;
    private String password;
    private String phoneNo;
    private String role;

    public UserTM() {
    }


    public UserTM(String userName, String password, String phoneNo, String role) {
        this.userName = userName;
        this.password = password;
        this.phoneNo = phoneNo;
        this.role = role;
    }


    public UserTM(String userId, String userName, String password, String phoneNo, String role) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.phoneNo = phoneNo;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
