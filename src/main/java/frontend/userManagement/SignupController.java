package frontend.userManagement;

import backend.user.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.time.LocalDate;
import java.time.Period;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class SignupController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private DatePicker dobPicker;

    @FXML
    private Button signupButton;

    @FXML
    private Button loginPageButton;

    @FXML
    public void initialize() {
        signupButton.setOnAction(event -> handleSignup(event));
        loginPageButton.setOnAction(event -> navigateToLogin(event));
    }

    private void handleSignup(javafx.event.ActionEvent event) {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String dob = dobPicker.getValue() != null ? dobPicker.getValue().toString() : "";

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || dob.isEmpty()) {
            showAlert("All fields are required!");
        } else if (!isValidEmail(email)) {
            showAlert("Enter a valid email address.");
        } else if (password.length() < 8) {
            showAlert("Password must be at least 8 characters.");
        } else {
            LocalDate selectedDate = dobPicker.getValue();
            LocalDate currentDate = LocalDate.now();

            if (selectedDate.isAfter(currentDate)) {
                showAlert("The birth date cannot be in the future.");
            } else {
                Period age = Period.between(selectedDate, currentDate);
                if (age.getYears() < 14) {
                    showAlert("User must be at least 14 years old.");
                } else {

                    UserManager manager = new UserManager(new AddUser(new LoadUsers()),new LoadUsers(), new UserValidator(new LoadUsers()),new UpdateUser());
                    String msg = manager.signup(username, password,email, dob);
                    if(msg.equals("User created")){
                        showSuccess("Signup successful! Welcome, " + username + "!");
                        navigateToLogin(event);
                    }
                    else {
                        showAlert(msg);
                    }
                }
            }
        }
    }

    private void navigateToLogin(javafx.event.ActionEvent event) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/frontend/login.fxml"));
            Scene loginScene = new Scene(loginPage);

            // Get current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set new scene and show the stage
            currentStage.setScene(loginScene);
            currentStage.setTitle("Login");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error navigating to the Login page.");
        }
    }

    private void clearFields() {
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        dobPicker.setValue(null);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }
}
