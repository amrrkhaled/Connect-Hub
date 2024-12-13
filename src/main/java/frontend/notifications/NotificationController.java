package frontend.notifications;

import backend.friendship.FriendRequestService;
import backend.friendship.FriendRequestServiceFactory;
import backend.friendship.FriendShip;
import backend.friendship.FriendShipFactory;
import backend.notifications.*;
import backend.user.*;
import javafx.collections.FXCollections;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static backend.user.User.userId;

public class NotificationController {
    public FriendShip friendShip = FriendShipFactory.createFriendShip();
    public FriendRequestServiceFactory factory = FriendRequestServiceFactory.getInstance();
    public FriendRequestService service = factory.createFriendRequestService();
   @FXML
    public ListView groupList;

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
    private GroupNotifications groupNotifications;
    private final String currentUserId = User.getUserId();
    private JSONArray postNotificationList = new JSONArray();  // Changed to JSONArray
    private JSONArray friendNotificationsList = new JSONArray();  // Changed to JSONArray

    @FXML
    private void initialize() {
        // Initialize the loaders and notification instances
        ILoadNotifications loader = LoadNotifications.getInstance();
        postNotification = new PostNotification(loader);
        friendNotifications = new FriendNotifications(loader);
        groupNotifications = new GroupNotifications(loader);

        // Populate the JSONArray for friend requests

        // Load and populate the notifications
        loadPostNotifications();
        loadFriendNotifications();
        loadGroupNotifications();

        // Set list views
        updatePostListView();
        updateFriendRequestListView();

        // Handle button actions
        acceptButton.setOnAction(event -> handleAccept());
        rejectButton.setOnAction(event -> handleReject());
    }
    private void loadPostNotifications() {

        JSONArray notifications = postNotification.getNotification();
        System.out.println("Notifications: " + notifications);
        postNotificationList = new JSONArray();

        for (int i = 0; i < notifications.length(); i++) {
            JSONObject notification = notifications.getJSONObject(i);
            String author = notification.optString("authorId");
            String content = notification.optString("contentId");
            String timestamp = notification.optString("timestamp");
            String authorName=friendShip.getUserRepository().getUsernameByUserId(author);

            String displayText = authorName + " posted "+ content+ " @: "+ timestamp;
            JSONObject postNotificationJson = new JSONObject();
            postNotificationJson.put("notification", displayText);
            postNotificationList.put(postNotificationJson);
        }
   updatePostListView();
    }
    private void loadGroupNotifications() {
        JSONArray notifications = groupNotifications.getNotification();
        List<String> groupNot = new ArrayList<>();

        for (int i = 0; i < notifications.length(); i++) {
            String notification = notifications.getJSONObject(i).optString("notificationName", "");
            String name = notifications.getJSONObject(i).optString("groupName");
            groupNot.add(name + " " + notification);


        }
        groupList.setItems(FXCollections.observableArrayList(groupNot));
    }


    private void loadFriendNotifications() {
        ILoadNotifications loadNotifications = LoadNotifications.getInstance();
        // Load the existing notifications from the database
        JSONArray notifications = loadNotifications.LoadNotification("data/FriendNotifications.json");
        // Initialize friendNotificationsList with existing notifications
        friendNotificationsList = new JSONArray(notifications.toString());
        // Load new friend requests (usernames)
        List<String> friendRequestsList = service.getFriendRequests(currentUserId);
        System.out.println("Friend Requests: " + friendRequestsList);

        // Remove outdated notifications
        for (int i = 0; i < friendNotificationsList.length(); i++) {
            JSONObject existingNotification = friendNotificationsList.getJSONObject(i);
            String existingNotificationText = existingNotification.optString("notification", "");

            // Extract username from notification text (e.g., "Request from: username")
            String usernameInNotification = existingNotificationText.replace("Request from: ", "").trim();

            // If the username is not in the friend request list, remove the notification
            if (!friendRequestsList.contains(usernameInNotification)) {
                friendNotificationsList.remove(i);
                i--; // Adjust index after removal
            }
        }

        // Add any new friend requests to the notification list if not already present
        for (String username : friendRequestsList) {
            boolean isAlreadyNotified = false;

            // Check if the request (username) is already present in the notification list
            for (int i = 0; i < friendNotificationsList.length(); i++) {
                JSONObject existingNotification = friendNotificationsList.getJSONObject(i);
                String existingNotificationText = existingNotification.optString("notification", "");

                // Check if the notification already contains this username
                if (existingNotificationText.contains(username)) {
                    isAlreadyNotified = true;
                    break;
                }
            }

            // If the request is not in the notification list, add it
            if (!isAlreadyNotified) {
                String displayText = "Request from: " + username;

                // Use createNotifications to add the new notification to the system
                ILoadUsers loadUsers = LoadUsers.getInstance();
                IUserRepository userRepository = UserRepository.getInstance(loadUsers);
                String senderId = userRepository.findUserIdByUsername(username);
                friendNotifications.createNotifications(displayText, senderId, currentUserId, getCurrentTimestamp());
            }
        }

        // Log the updated list
        System.out.println("Updated Notifications: " + friendNotificationsList);

        // Update the ListView
        updateFriendRequestListView();
    }

    // Helper method to get the current timestamp (you can adjust this based on your date format)
    private String getCurrentTimestamp() {
        // Assuming you're using the current system time in a specific format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    private void handleAccept() {
        String selectedRequest = requestList.getSelectionModel().getSelectedItem();
        ILoadNotifications loadNotifications = LoadNotifications.getInstance();
        if (selectedRequest != null) {
            try {

                String sender = selectedRequest.replace("Request from: ", "").trim(); // "Request from: sender"

                // Find user ID by username
                String newFriendId = friendShip.getUserRepository().findUserIdByUsername(sender);
                System.out.println(sender);
                if (newFriendId != null) {
                    // Accept the friend request
                    friendShip.acceptFriend(currentUserId, sender);
                    // Remove the request from the friend notifications list
                    removeRequestFromJSONArray(sender);

                    // Save the updated notifications list
                    loadNotifications.saveNotification(friendNotificationsList, "data/FriendNotifications.json");

                    // Refresh the ListView
                    updateFriendRequestListView();
                } else {
                    showAlert("Error", "Sender not found: " + sender);
                }
            } catch (Exception e) {
                showAlert("Error", "Invalid request format: ");
                System.out.println(e);
            }
        } else {
            showAlert("Error", "No request selected to accept.");
        }
    }

    private void handleReject() {
        String selectedRequest = requestList.getSelectionModel().getSelectedItem();

        if (selectedRequest != null) {
            try {

                String sender = selectedRequest.replace("Request from: ", "").trim();
                showAlert("Rejected", "You rejected a friend request from: " + sender);
                friendShip.removeFriend(currentUserId, sender);
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

    public void navigateToPost(ActionEvent event) {
        String selectedPost = postList.getSelectionModel().getSelectedItem();
        String[] post = selectedPost.split(" ");
        JSONObject postDetails = new JSONObject();
        postDetails.put("authorName", post[0]);
        postDetails.put("contentId", post[2]);
        postDetails.put("timestamp", post[4]);

        try {
            //call function to set post details
            PostViewController.setPostDetails(postDetails);

            // Load the Notifications page
            Parent notificationPage = FXMLLoader.load(getClass().getResource("/frontend/postView.fxml"));
            Scene notificationScene = new Scene(notificationPage);
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();


            // Set the icon (if not already added
            if (currentStage.getIcons().isEmpty()) {
                currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));
            }

            // Set the new scene and update the stage
            currentStage.setScene(notificationScene);
            currentStage.setTitle("Notifications");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

