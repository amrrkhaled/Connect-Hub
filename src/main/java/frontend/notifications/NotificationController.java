package frontend.notifications;

import backend.friendship.FriendRequestService;
import backend.friendship.FriendRequestServiceFactory;
import backend.friendship.FriendShip;
import backend.friendship.FriendShipFactory;
import backend.notifications.FriendNotifications;
import backend.notifications.PostNotification;
import backend.notifications.ILoadNotifications;
import backend.notifications.LoadNotifications;
import backend.user.User;
import backend.user.UserFactory;
import backend.user.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static backend.user.User.userId;

public class NotificationController {
    public FriendShip friendShip = FriendShipFactory.createFriendShip();
    public FriendRequestServiceFactory factory = FriendRequestServiceFactory.getInstance();
    public FriendRequestService service = factory.createFriendRequestService();

    @FXML
    private ListView<String> postList;

    @FXML
    private ListView<String> requestList;

    @FXML
    private Button acceptButton;

    @FXML
    private Button rejectButton;

    private FriendNotifications friendNotifications;
    private PostNotification postNotification;
    private final String currentUserId = User.getUserId();
    private JSONArray postNotificationList = new JSONArray();  // Changed to JSONArray
    private JSONArray friendNotificationsList = new JSONArray();  // Changed to JSONArray
    private ILoadNotifications loadNotifications;

    @FXML
    private void initialize() {
        // Initialize the loaders and notification instances
        ILoadNotifications loader = new LoadNotifications();
        postNotification = new PostNotification(loader);
        friendNotifications = new FriendNotifications(loader);

        // Populate the JSONArray for friend requests
        List<String> friendRequestsList = service.getFriendRequests(currentUserId);
        JSONArray friendRequestsJSONArray = new JSONArray();
        for (String request : friendRequestsList) {
            JSONObject requestObject = new JSONObject();
            requestObject.put("request", request);
            friendRequestsJSONArray.put(requestObject);  // Storing as JSON objects inside the array
        }

        // Load and populate the notifications
        loadPostNotifications();
        loadFriendNotifications();

        // Set list views
        updatePostListView();
        updateFriendRequestListView();

        // Handle button actions
        acceptButton.setOnAction(event -> handleAccept());
        rejectButton.setOnAction(event -> handleReject());
    }

    private void loadPostNotifications() {
        JSONArray notifications = postNotification.getNotification();
        postNotificationList = new JSONArray();  // Clear existing notifications

        for (int i = 0; i < notifications.length(); i++) {
            JSONObject notification = notifications.getJSONObject(i);
            String authorId = notification.optString("authorId", "Unknown Author");

            String timestamp = notification.optString("timestamp", "Unknown Time");
            String author= friendShip.getUserRepository().getUsernameByUserId(authorId);
            String displayText =  author + " posted @: " + timestamp;
            JSONObject postNotificationJson = new JSONObject();
            postNotificationJson.put("notification", displayText);
            postNotificationList.put(postNotificationJson);  // Store as JSON object
        }
    }

    private void loadFriendNotifications() {
        JSONArray notifications = friendNotifications.getNotification();
        friendNotificationsList = new JSONArray();  // Clear existing friend notifications

        for (int i = 0; i < notifications.length(); i++) {
            JSONObject notification = notifications.getJSONObject(i);
            String senderId= notification.optString("senderId", "Unknown Sender");
            String sender= friendShip.getUserRepository().getUsernameByUserId(senderId);
            String receiver = notification.optString("receiver", "Unknown Receiver");

            String displayText = " Request from:-" + sender;
            JSONObject friendNotificationJson = new JSONObject();
            friendNotificationJson.put("notification", displayText);
            friendNotificationsList.put(friendNotificationJson);  // Store as JSON object
        }
    }

    private void handleAccept() {
        String selectedRequest = requestList.getSelectionModel().getSelectedItem();

        if (selectedRequest != null) {
            try {
                // Split the string to extract the sender username
                String[] parts = selectedRequest.split("-");
                String sender = parts[1].trim();  // Extract the token after "Request from:- "
                System.out.println("Extracted sender: " + sender);  // Debugging line

                // Check if the sender is actually found
                JSONObject user = friendShip.getUserRepository().findUserByUsername(sender);

                if (user != null) {
                    String newFriendId = user.getString("userId");
                    System.out.println("User found, User ID: " + newFriendId);  // Debugging line

                    // Accept the friend request
                    friendShip.acceptFriend(currentUserId, newFriendId);

                    // Remove the request from the JSONArray (assuming we use the request object)
                    removeRequestFromJSONArray(sender);

                    // Save the updated list to file
                    loadNotifications.saveNotification(friendNotificationsList, "data/FriendNotifications.json");

                    // Refresh the ListView with the updated list
                    updateFriendRequestListView();
                } else {
                    showAlert("Error", "User not found: " + sender);
                    System.out.println("Error: User not found");
                }
            } catch (Exception e) {
                showAlert("Error", "An error occurred while accepting the request. Please try again.");
                System.out.println("Exception during accept request: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Error", "No request selected to accept.");
        }
    }



    private void handleReject() {
        String selectedRequest = requestList.getSelectionModel().getSelectedItem();

        if (selectedRequest != null) {
            try {

                String[] parts = selectedRequest.split("-");
                String sender = parts[1].trim();
                showAlert("Rejected", "You rejected a friend request from: " + sender);

                // Remove the rejected request from the JSONArray
                removeRequestFromJSONArray(sender);

                // Refresh the ListView with the updated list
                updateFriendRequestListView();

            } catch (Exception e) {
                showAlert("Error", "Invalid request format: " + selectedRequest);
            }
        } else {
            showAlert("Error", "No request selected to reject.");
        }
    }

    private void removeRequestFromJSONArray(String sender) {
        for (int i = 0; i < friendNotificationsList.length(); i++) {
            JSONObject request = friendNotificationsList.getJSONObject(i);
            String requestString = request.optString("notification");
            if (requestString != null && requestString.contains(sender)) {
                friendNotificationsList.remove(i);
                break;
            }

        }
    }

    private void updatePostListView() {
        // Clear and update the Post ListView with the new list of notifications
        postList.getItems().clear();
        for (int i = 0; i < postNotificationList.length(); i++) {
            JSONObject postNotificationJson = postNotificationList.getJSONObject(i);
            String notification = postNotificationJson.optString("notification");
            if (notification != null) {
                postList.getItems().add(notification);
            }
        }
    }

    private void updateFriendRequestListView() {
        // Clear and update the ListView with the new list of friend notifications
        requestList.getItems().clear();
        for (int i = 0; i < friendNotificationsList.length(); i++) {
            JSONObject friendNotificationJson = friendNotificationsList.getJSONObject(i);
            String notification = friendNotificationJson.optString("notification");
            if (notification != null) {
                requestList.getItems().add(notification);
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void onHome(ActionEvent event) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/frontend/home.fxml"));
            Scene loginScene = new Scene(loginPage);

            // Get current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));

            // Set new scene and show the stage
            currentStage.setScene(loginScene);
            currentStage.setTitle("Home");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onProfile(ActionEvent event) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/frontend/profile.fxml"));
            Scene loginScene = new Scene(loginPage);

            // Get current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));

            // Set new scene and show the stage
            currentStage.setScene(loginScene);
            currentStage.setTitle("Profile");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onFriends(ActionEvent event) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/frontend/friend.fxml"));
            Scene loginScene = new Scene(loginPage);

            // Get current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));

            // Set new scene and show the stage
            currentStage.setScene(loginScene);
            currentStage.setTitle("Friends");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onLogout(ActionEvent event) {
        try {
            UserManager manager = UserFactory.getInstance().createUserManager();
            manager.logout(userId);
            Parent loginPage = FXMLLoader.load(getClass().getResource("/frontend/login.fxml"));
            Scene loginScene = new Scene(loginPage);

            // Get current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));

            // Set new scene and show the stage
            currentStage.setScene(loginScene);
            currentStage.setTitle("Login");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

