package lk.ijse.project_01.Controller.Booking;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.project_01.DTO.BookingDTO;
import lk.ijse.project_01.DTO.TM.BookingTM;
import lk.ijse.project_01.Model.BookingModel;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class BookingPageController {

    @FXML private TextField txtPhoneNo;
    @FXML private Label lblGuestName;
    @FXML private ComboBox<String> cmbRoomType;
    @FXML private ComboBox<String> cmbRoomId;
    @FXML private DatePicker dpCheckIn;
    @FXML private TextField txtDuration;
    @FXML private Label lblTotal;
    @FXML private Button btnBook;
    @FXML private Button btnClear;
    @FXML private TableView<BookingTM> tblBookings;
    @FXML private TableColumn<BookingTM, String> colBookingId;
    @FXML private TableColumn<BookingTM, String> colRoomId;
    @FXML private TableColumn<BookingTM, LocalDate> colCheckIn;
    @FXML private TableColumn<BookingTM, Integer> colDuration;
    @FXML private TableColumn<BookingTM, Double> colTotal;
    @FXML private TableColumn<BookingTM, String> colStatus;
    @FXML private TableColumn<BookingTM, String> colRoomType;
    @FXML private TableColumn<BookingTM, Void> colAction;
    @FXML private AnchorPane mainAnchorPane;

    private ObservableList<BookingTM> bookingList = FXCollections.observableArrayList();

    public void initialize() {
        cmbRoomType.getItems().addAll("AC", "Non-AC");

        colBookingId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colRoomId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colCheckIn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));

        addCancelButtonToTable();
        loadAllBookings();
        btnBook.setDisable(true);

        addListeners();

    }

    private void addListeners() {
        txtPhoneNo.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                loadGuestName(newVal.trim());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            updateUIState();
        });

        cmbRoomType.valueProperty().addListener((obs, oldVal, newVal) -> {
            try {
                loadAvailableRoomIds();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            updateUIState();
        });

        dpCheckIn.valueProperty().addListener((obs, oldVal, newVal) -> {
            try {
                loadAvailableRoomIds();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            updateUIState();
        });

        txtDuration.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                loadAvailableRoomIds();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            updateUIState();
        });

        cmbRoomId.valueProperty().addListener((obs, oldVal, newVal) -> updateUIState());
    }

    private void loadGuestName(String phoneNo) throws SQLException {
        if (phoneNo.isEmpty()) {
            lblGuestName.setText("Unknown Guest");
            return;
        }
        String name = BookingModel.getGuestNameByPhone(phoneNo);
        lblGuestName.setText(name != null ? name : "Unknown Guest");
    }

    private void loadAvailableRoomIds() throws SQLException {
        cmbRoomId.getItems().clear();

        String roomType = cmbRoomType.getValue();
        LocalDate checkIn = dpCheckIn.getValue();
        if (roomType == null || checkIn == null) return;

        int duration;
        try {
            duration = Integer.parseInt(txtDuration.getText().trim());
            if (duration < 1) return;
        } catch (NumberFormatException e) {
            return;
        }

        List<String> availableRooms = BookingModel.getAvailableRooms(roomType, checkIn, duration);
        if (availableRooms == null || availableRooms.isEmpty()) return;

        ObservableList<String> roomIds = FXCollections.observableArrayList();
        for (String roomId : availableRooms) {
            if (roomId != null && roomId.startsWith(roomType.equals("AC") ? "A" : "N")) {
                roomIds.add(roomId);
            }
        }

        cmbRoomId.setItems(roomIds);
        if (!roomIds.isEmpty()) {
            cmbRoomId.getSelectionModel().selectFirst();
        }
    }



    private String formatRoomId(String roomId, String prefix) {
        try {
            int idNum = Integer.parseInt(roomId);
            return String.format("%s%03d", prefix, idNum);
        } catch (NumberFormatException e) {
            return prefix + roomId;
        }
    }


    private void updateUIState() {
        try {
            updateTotalPriceAndBookingAvailability();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateTotalPriceAndBookingAvailability() throws SQLException {
        String roomType = cmbRoomType.getValue();
        LocalDate checkIn = dpCheckIn.getValue();

        if (roomType == null || checkIn == null) {
            clearTotalAndDisableBooking();
            return;
        }

        int duration;
        try {
            duration = Integer.parseInt(txtDuration.getText().trim());
            if (duration < 1) {
                clearTotalAndDisableBooking();
                return;
            }
        } catch (NumberFormatException e) {
            clearTotalAndDisableBooking();
            return;
        }

        String selectedRoomId = cmbRoomId.getValue();
        if (selectedRoomId == null) {
            clearTotalAndDisableBooking();
            btnBook.setTooltip(new Tooltip("Please select a Room ID."));
            return;
        }

        double ratePerDay = roomType.equals("AC") ? 10000 : 5000;
        double total = ratePerDay * duration;
        lblTotal.setText("Rs " + total);

        List<String> availableRooms = BookingModel.getAvailableRooms(roomType, checkIn, duration);
        if (availableRooms.contains(selectedRoomId)) {
            btnBook.setDisable(false);
            btnBook.setTooltip(null);
        } else {
            btnBook.setDisable(true);
            btnBook.setTooltip(new Tooltip("Selected room is not available for the given dates."));
        }
    }


    private void clearTotalAndDisableBooking() {
        lblTotal.setText("");
        btnBook.setDisable(true);
        btnBook.setTooltip(null);
    }

    @FXML
    void btnBookOnAction(ActionEvent event) {
        try {
            if (validateInput()) {
                String bookingId = generateBookingId();
                String guestPhone = txtPhoneNo.getText().trim();
                String guestName = lblGuestName.getText();
                String roomType = cmbRoomType.getValue();
                int duration = Integer.parseInt(txtDuration.getText().trim());
                LocalDate checkIn = dpCheckIn.getValue();
                String roomId = cmbRoomId.getValue();
                double ratePerDay = roomType.equals("AC") ? 10000 : 5000;
                double totalPrice = ratePerDay * duration;

                BookingDTO dto = new BookingDTO(
                        bookingId, guestPhone, roomType, roomId, checkIn, duration, totalPrice, "Confirmed"
                );

                List<String> availableRooms = BookingModel.getAvailableRooms(roomType, checkIn, duration);
                if (!availableRooms.contains(roomId)) {
                    showAlert("Room not available for the selected dates.", Alert.AlertType.ERROR);
                    return;
                }

                boolean booked = BookingModel.bookRoom(dto);

                if (booked) {
                    showAlert("Booking Confirmed!", Alert.AlertType.INFORMATION);
                    loadAllBookings();
                    clearForm();
                } else {
                    showAlert("Booking Failed!", Alert.AlertType.ERROR);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database error occurred!", Alert.AlertType.ERROR);
        }
    }

    private boolean validateInput() {
        String phone = txtPhoneNo.getText().trim();
        if (phone.isEmpty()) return showWarning("Please enter Guest Phone No.");
        if ("Unknown Guest".equals(lblGuestName.getText())) return showWarning("Guest not found.");
        if (cmbRoomType.getValue() == null) return showWarning("Please select Room Type.");
        if (dpCheckIn.getValue() == null) return showWarning("Please select Check-in date.");

        String durationText = txtDuration.getText().trim();

        if (durationText.isEmpty()) return showWarning("Duration is required.");

        int duration;
        try {
            duration = Integer.parseInt(durationText);
        } catch (NumberFormatException e) {
            return showWarning("Duration must be a valid number.");
        }

        if (duration < 1) return showWarning("Duration must be greater than 0.");
        if (cmbRoomId.getValue() == null) return showWarning("Please select a Room ID.");

        return true;
    }

    private void clearForm() {
        txtPhoneNo.clear();
        lblGuestName.setText("Unknown Guest");
        cmbRoomType.getSelectionModel().clearSelection();
        cmbRoomId.getItems().clear();
        dpCheckIn.setValue(null);
        txtDuration.clear();
        lblTotal.setText("");
        btnBook.setDisable(true);
    }

    private void loadAllBookings() {
        bookingList.clear();
        try {
            List<BookingDTO> allBookings = BookingModel.getAllBookings();
            for (BookingDTO dto : allBookings) {
                bookingList.add(new BookingTM(
                        dto.getBookingId(),
                        formatRoomId(dto.getRoomId(), dto.getRoomType().equals("AC") ? "A" : "N"),
                        dto.getCheckIn(),
                        dto.getDuration(),
                        dto.getTotalPrice(),
                        dto.getStatus(),
                        dto.getRoomType()
                ));
            }
            tblBookings.setItems(bookingList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String generateBookingId() {
        try {
            String lastId = BookingModel.getLastBookingId();
            if (lastId != null && lastId.matches("B\\d+")) {
                int nextId = Integer.parseInt(lastId.substring(1)) + 1;
                return String.format("B%03d", nextId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "B001";
    }

    private void addCancelButtonToTable() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Cancel");

            {
                btn.setOnAction(event -> {
                    BookingTM booking = getTableView().getItems().get(getIndex());
                    if (booking.getStatus().equalsIgnoreCase("Confirmed")) {
                        boolean confirm = showConfirmDialog("Are you sure to cancel booking " + booking.getBookingId() + "?");
                        if (confirm) {
                            try {
                                boolean cancelled = BookingModel.cancelBooking(booking.getBookingId());
                                if (cancelled) {
                                    loadAllBookings();
                                    showAlert("Booking cancelled successfully.", Alert.AlertType.INFORMATION);
                                } else {
                                    showAlert("Failed to cancel booking.", Alert.AlertType.ERROR);
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                                showAlert("DB Error during cancellation.", Alert.AlertType.ERROR);
                            }
                        }
                    } else {
                        showAlert("Booking already cancelled.", Alert.AlertType.WARNING);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

    private boolean showWarning(String message) {
        showAlert(message, Alert.AlertType.WARNING);
        return false;
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private boolean showConfirmDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmation");
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }


    @FXML
    void btnBackOnAction(ActionEvent event) {
        try {
            Parent previousView = FXMLLoader.load(getClass().getResource("/view/B&FDashboardPage.fxml"));
            mainAnchorPane.getChildren().setAll(previousView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnGuestAddOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Guestpage.fxml"));
            Parent guestRoot = loader.load();

            Stage guestStage = new Stage();
            guestStage.setTitle("Add Guest");
            guestStage.setScene(new Scene(guestRoot));
            guestStage.initModality(Modality.APPLICATION_MODAL); // Block main window
            guestStage.showAndWait(); // Wait until guest window is closed


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        txtPhoneNo.clear();
        lblGuestName.setText("Unknown Guest");
        cmbRoomType.getSelectionModel().clearSelection();
        dpCheckIn.setValue(null);
        txtDuration.clear();
        cmbRoomId.getSelectionModel().clearSelection();
        lblTotal.setText("");
    }
}
