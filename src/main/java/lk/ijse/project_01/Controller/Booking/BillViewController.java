package lk.ijse.project_01.Controller.Booking;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BillViewController {

    @FXML
    private Label lblGuestPhone;

    @FXML
    private Label lblTotalAmount;

    @FXML
    private Label lblPaidAmount;

    @FXML
    private Label lblPaymentMethod;

    @FXML
    private Label lblBalance;

    @FXML
    private Label lblDateTime;

    public void setBillData(String guestPhone, double totalAmount, double paidAmount, String paymentMethod, double balance) {
        lblGuestPhone.setText("Guest Phone: " + guestPhone);
        lblTotalAmount.setText(String.format("Total Amount: %.2f", totalAmount));
        lblPaidAmount.setText(String.format("Paid Amount: %.2f", paidAmount));
        lblPaymentMethod.setText("Payment Method: " + paymentMethod);
        lblBalance.setText(String.format("Balance: %.2f", balance));
        setDateTime();
    }

    private void setDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        lblDateTime.setText("Date/Time: " + now.format(formatter));
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) lblGuestPhone.getScene().getWindow();
        stage.close();
    }
}
