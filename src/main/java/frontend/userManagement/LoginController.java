package frontend.userManagement;

import backend.user.*;
import javafx.event.ActionEvent;
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
        loginButton.setOnAction(event -> handleLogin(null));
        signupPageButton.setOnAction(this::navigateToSignup);
    }

    private void handleLogin(javafx.event.ActionEvent event) {
        String usernameOrEmail = usernameField.getText().trim();
        String password = passwordField.getText();

        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            showAlert("All fields are required!");
        } else {
            ILoadUsers loadUser = LoadUsers.getInstance();
            IUserRepository userRepository = UserRepository.getInstance(loadUser);
            UserManager manager = new UserManager(new AddUser(loadUser),loadUser, new UserValidator(loadUser),new UpdateUser(), userRepository);
            String msg = manager.login(usernameOrEmail, password);
            User.setUserId(msg);
            if (msg.matches("U\\d+")) {
                showSuccess("Login successful!");
                navigateToNewsFeed();

            }
            else showAlert(msg);

        }
    }

    private void navigateToNewsFeed() {
        try {
            // Load the FXML file for the NewsFeed page
            Parent newsFeedParent = FXMLLoader.load(getClass().getResource("/frontend/NewsFeed.fxml"));

            // Create a new Scene with the loaded Parent (FXML)
            Scene newsFeedScene = new Scene(newsFeedParent);

            // Get the current Stage (window)
            Stage currentStage = (Stage) loginButton.getScene().getWindow();

            // Set the new Scene and update the Stage's title
            currentStage.setScene(newsFeedScene);
            currentStage.setTitle("NewsFeed");

            // Show the updated Stage
            currentStage.show();
        } catch (IOException e) {
            // Handle exceptions in case the FXML file cannot be loaded
            e.printStackTrace();
            showAlert("Error navigating to the NewsFeed page.");
        }
    }

    private void navigateToSignup(javafx.event.ActionEvent event) {
        try {
            Parent signupPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/frontend/signup.fxml")));
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
