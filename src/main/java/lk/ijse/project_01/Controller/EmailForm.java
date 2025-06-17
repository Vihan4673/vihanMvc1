package lk.ijse.project_01.Controller;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class EmailForm implements Initializable {

    @FXML
    private TextField emailField;

    @FXML
    private TextField subjectField;

    @FXML
    private TextArea messageArea;

    @FXML
    private Label statusLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emailField.requestFocus();
    }


    public void setEmail(String email) {
        emailField.setText(email);
    }


    @FXML
    private void handleSendEmail() {
        String to = emailField.getText().trim();
        String subject = subjectField.getText().trim();
        String message = messageArea.getText().trim();


        if (to.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            statusLabel.setText("Please fill all fields.");
            statusLabel.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }


        final String fromEmail = "vihanvimen46@gmail.com";
        final String password = "eecx rphh qwss sdss";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");


        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(fromEmail));
            mimeMessage.setRecipients(jakarta.mail.Message.RecipientType.TO, InternetAddress.parse(to));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);


            Transport.send(mimeMessage);


            statusLabel.setText("Email sent successfully!");
            statusLabel.setTextFill(javafx.scene.paint.Color.GREEN);


            emailField.clear();
            subjectField.clear();
            messageArea.clear();

        } catch (MessagingException e) {
            e.printStackTrace();
            statusLabel.setText("Failed to send email.");
            statusLabel.setTextFill(javafx.scene.paint.Color.RED);
        }
    }
}
