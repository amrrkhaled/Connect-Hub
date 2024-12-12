package frontend.notifications;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class NotificationController {

    @FXML
    private ListView<String> postList;

    @FXML
    private ListView<String> requestList;

    @FXML
    private ListView<String> groupList;

    @FXML
    private Button acceptButton;

    @FXML
    private Button rejectButton;

    // Sample data for the lists
    private ObservableList<String> postNotifications = FXCollections.observableArrayList(
            "Post Notification 1", "Post Notification 2", "Post Notification 3"
    );

    private ObservableList<String> requestNotifications = FXCollections.observableArrayList(
            "Request Notification 1", "Request Notification 2", "Request Notification 3"
    );

    private ObservableList<String> groupNotifications = FXCollections.observableArrayList(
            "Group Notification 1", "Group Notification 2", "Group Notification 3"
    );

    @FXML
    private void initialize() {
        // Populate the list views with sample data
        postList.setItems(postNotifications);
        requestList.setItems(requestNotifications);
        groupList.setItems(groupNotifications);

        // Handle accept button click
        acceptButton.setOnAction(event -> handleAccept());

        // Handle reject button click
        rejectButton.setOnAction(event -> handleReject());
    }

    private void handleAccept() {
        String selectedRequest = requestList.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            showAlert("Accepted", "You have accepted: " + selectedRequest);
        } else {
            showAlert("Error", "No request selected to accept.");
        }
    }

    private void handleReject() {
        String selectedRequest = requestList.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            showAlert("Rejected", "You have rejected: " + selectedRequest);
        } else {
            showAlert("Error", "No request selected to reject.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
//    private void navigateToNotifications() {
//        try {
//            Parent loginPage = FXMLLoader.load(getClass().getResource("/frontend/NewsFeed.fxml"));
//            Scene loginScene = new Scene(loginPage);
//
//            // Get current stage
//            Stage currentStage = (Stage) imageContainer.getScene().getWindow();
//            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));
//
//            // Set new scene and show the stage
//            currentStage.setScene(loginScene);
//            currentStage.setTitle("NewsFeed");
//            currentStage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}

