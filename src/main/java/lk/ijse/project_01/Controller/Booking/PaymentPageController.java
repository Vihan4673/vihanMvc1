package lk.ijse.project_01.Controller.Booking;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lk.ijse.project_01.Model.paymnetModel;

import java.io.IOException;

public class PaymentPageController {

    @FXML
    private TextField txtGuestPhone;

    @FXML
    private TextField txtTotalAmount;

    @FXML
    private TextField txtGuestPaid;

    @FXML
    private TextField txtAmountToPay;

    @FXML
    private RadioButton rbCash;

    @FXML
    private RadioButton rbCard;

    @FXML
    private ToggleGroup paymentToggleGroup;

    @FXML
    private TextField txtCardNumber;

    @FXML
    private Label lblStatus;

    private String guestPhone;
    private double totalAmount;

    private Runnable onPaymentSuccess;

    public void setPaymentData(String guestPhone, double totalAmount) {
        this.guestPhone = guestPhone;
        this.totalAmount = totalAmount;

        txtGuestPhone.setText(guestPhone);
        txtTotalAmount.setText(String.format("%.2f", totalAmount));
        txtGuestPaid.setText("");
        txtAmountToPay.setText("");
        txtCardNumber.setText("");
        lblStatus.setText("");

        updateCardNumberField();
        txtGuestPaid.setEditable(true);
        txtAmountToPay.setEditable(false);
        rbCash.setDisable(false);
        rbCard.setDisable(false);
    }

    public void setOnPaymentSuccess(Runnable onPaymentSuccess) {
        this.onPaymentSuccess = onPaymentSuccess;
    }

    @FXML
    private void handlePayAction() {
        lblStatus.setText("");

        String guestPaidStr = txtGuestPaid.getText().trim();
        if (guestPaidStr.isEmpty()) {
            lblStatus.setStyle("-fx-text-fill: red;");
            lblStatus.setText("Please enter the paid amount.");
            return;
        }

        double guestPaid;
        try {
            guestPaid = Double.parseDouble(guestPaidStr);
            if (guestPaid <= 0) {
                lblStatus.setStyle("-fx-text-fill: red;");
                lblStatus.setText("Amount must be greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            lblStatus.setStyle("-fx-text-fill: red;");
            lblStatus.setText("Invalid paid amount.");
            return;
        }



        Toggle selectedToggle = paymentToggleGroup.getSelectedToggle();
        if (selectedToggle == null) {
            lblStatus.setStyle("-fx-text-fill: red;");
            lblStatus.setText("Please select a payment method.");
            return;
        }

        RadioButton selectedMethod = (RadioButton) selectedToggle;
        String paymentMethod = selectedMethod.getText();

        if ("Card".equalsIgnoreCase(paymentMethod)) {
            String cardNum = txtCardNumber.getText().trim();
            if (cardNum.isEmpty()) {
                lblStatus.setStyle("-fx-text-fill: red;");
                lblStatus.setText("Please enter card number for card payment.");
                return;
            }

        }

        double balance = totalAmount - guestPaid;

        boolean success = paymnetModel.savePayment(guestPhone, totalAmount, paymentMethod);

        if (success) {
            boolean statusUpdated = paymnetModel.updatePaymentStatus(guestPhone);

            if (statusUpdated) {
                lblStatus.setStyle("-fx-text-fill: green;");

                if (balance < 0) {
                    lblStatus.setText("Payment successful! Return change to guest: " + String.format("%.2f", -balance));
                } else {
                    lblStatus.setText("Payment successful! Balance to pay: " + String.format("%.2f", balance));
                }

                txtGuestPaid.setEditable(false);
                txtAmountToPay.setEditable(false);
                rbCash.setDisable(true);
                rbCard.setDisable(true);
                txtCardNumber.setEditable(false);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Payment");
                alert.setHeaderText(null);
                if (balance < 0) {
                    alert.setContentText("Payment processed successfully.\nReturn change to guest: " + String.format("%.2f", -balance));
                } else {
                    alert.setContentText("Payment processed successfully.\nBalance to pay: " + String.format("%.2f", balance));
                }
                alert.showAndWait();

                if (onPaymentSuccess != null) {
                    onPaymentSuccess.run();
                }
            } else {
                lblStatus.setStyle("-fx-text-fill: orange;");
                lblStatus.setText("Payment saved, but status not updated.");
            }
        } else {
            lblStatus.setStyle("-fx-text-fill: red;");
            lblStatus.setText("Payment failed. Try again.");
        }
    }

    @FXML
    public void calculateBalance(KeyEvent keyEvent) {
        String guestPaidStr = txtGuestPaid.getText().trim();
        if (!guestPaidStr.isEmpty()) {
            try {
                double guestPaid = Double.parseDouble(guestPaidStr);

                double balance = totalAmount - guestPaid;

                if (balance < 0) {
                    txtAmountToPay.setText(String.format("%.2f", balance));
                    lblStatus.setStyle("-fx-text-fill: orange;");
                    lblStatus.setText("Guest paid more than total. Return change: " + String.format("%.2f", -balance));
                } else if (guestPaid < 0) {
                    txtAmountToPay.setText("");
                    lblStatus.setStyle("-fx-text-fill: red;");
                    lblStatus.setText("Amount must be positive.");
                } else {
                    txtAmountToPay.setText(String.format("%.2f", balance));
                    lblStatus.setText("");
                }
            } catch (NumberFormatException e) {
                txtAmountToPay.setText("");
                lblStatus.setStyle("-fx-text-fill: red;");
                lblStatus.setText("Invalid number format.");
            }
        } else {
            txtAmountToPay.setText("");
            lblStatus.setText("");
        }
    }

    @FXML
    public void handlePaymentMethodChange(ActionEvent event) {
        updateCardNumberField();
    }

    private void updateCardNumberField() {
        boolean isCardSelected = rbCard.isSelected();
        txtCardNumber.setDisable(!isCardSelected);
        txtCardNumber.setVisible(isCardSelected);

        if (!isCardSelected) {
            txtCardNumber.clear();
        }
    }

    @FXML
    public void handlePrintBillAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/BillView.fxml"));
            Parent root = loader.load();

            BillViewController controller = loader.getController();

            String paymentMethod = ((RadioButton) paymentToggleGroup.getSelectedToggle()).getText();
            double paidAmount = 0;
            try {
                paidAmount = Double.parseDouble(txtGuestPaid.getText().trim());
            } catch (Exception e) {
            }
            double balance = totalAmount - paidAmount;

            controller.setBillData(guestPhone, totalAmount, paidAmount, paymentMethod, balance);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Bill");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            lblStatus.setText("Failed to open bill: " + e.getMessage());
            lblStatus.setStyle("-fx-text-fill: red;");
        }
    }

}
