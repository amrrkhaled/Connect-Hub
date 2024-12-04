package frontend;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import backend.*;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signupPageButton;

    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> handleLogin());
        signupPageButton.setOnAction(this::navigateToSignup);
    }

    private void handleLogin() {
        String usernameOrEmail = usernameField.getText().trim();
        String password = passwordField.getText();

        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            showAlert("All fields are required!");
        } else {
            UserManager manager = new UserManager(new AddUser(new LoadUsers()),new LoadUsers(), new UserValidator(new LoadUsers()),new UpdateUser());
            String msg = manager.login(usernameOrEmail, password);
            User.setUserId(msg);
            if (msg.matches("U\\d+")) {
                showSuccess("Login successful!");
            }
            else showAlert(msg);

        }
    }

    private void navigateToSignup(javafx.event.ActionEvent event) {
        try {
            Parent signupPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Signup.fxml")));
            Scene signupScene = new Scene(signupPage);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(signupScene);
            stage.setTitle("Signup");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error navigating to the Signup page.");
        }
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
}
