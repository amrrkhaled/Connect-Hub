package frontend.notifications;

import backend.Groups.*;
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
import frontend.groupManagement.GroupsController;
import backend.user.*;

import java.io.IOException;
import java.util.ArrayList;
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
    private final String postsFilePath = "data/groupsPosts.json";
    private FriendNotifications friendNotifications;
    private PostNotification postNotification;
    private final String currentUserId = User.getUserId();
    private JSONArray postNotificationList = new JSONArray();  // Changed to JSONArray
    private JSONArray friendNotificationsList = new JSONArray();  // Changed to JSONArray

    @FXML
    private void initialize() {
        // Initialize the loaders and notification instances
        ILoadNotifications loader = LoadNotifications.getInstance();
        postNotification = new PostNotification(loader);
        friendNotifications = new FriendNotifications(loader);

        // Populate the JSONArray for friend requests

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

    private JSONArray getPostsOfGroups(String groupName) {
        IStorageHandler storageHandler = new StorageHandler();
        JSONArray allPosts = storageHandler.loadDataAsArray(postsFilePath);  // Load the JSON array
        JSONArray posts = new JSONArray();
        for (int i = 0; i < allPosts.length(); i++) {
            JSONObject post = allPosts.getJSONObject(i);  // Get each post

            // Check if the post's "groupName" matches the given name
            if (post.getString("groupName").equals(groupName)) {
                posts.put(post);  // Add the post to the filteredPosts JSONArray
            }
        }
        return posts;
    }
    private void displayPostList(JSONArray postNotifications) {
        System.out.println("Updated Post Notifications:");
        for (int i = 0; i < postNotifications.length(); i++) {
            JSONObject notification = postNotifications.getJSONObject(i);
            // Display the notification message
            System.out.println(notification.getString("notification"));
        }
    }

    // Helper method to display posts in the UI or console
    private void loadPostNotifications() {
        // Initialize the storage handler and group loader
        IStorageHandler storageHandler = new StorageHandler();
        ILoadGroups loadGroups = LoadGroups.getInstance(storageHandler);
        NormalUserController userController = new NormalUserController(loadGroups, storageHandler);

        // Get the list of groups the user is a member of
        List<String> userGroups = userController.getGroupsForUser(currentUserId);

        // Load previously saved notifications
        postNotificationList = storageHandler.loadDataAsArray("data/PostNotifications.json");

        // Track content IDs already in the notification list to avoid duplicates
        List<String> existingContentIds = new ArrayList<>();
        for (int i = 0; i < postNotificationList.length(); i++) {
            existingContentIds.add(postNotificationList.getJSONObject(i).getString("contentId"));
        }

        // Fetch new posts for the groups the user is part of
        for (String groupName : userGroups) {
            // Get all posts for the group
            JSONArray groupPosts = getPostsOfGroups(groupName);

            // Iterate through the posts to add new notifications
            for (int i = 0; i < groupPosts.length(); i++) {
                JSONObject post = groupPosts.getJSONObject(i);
                String contentId = post.getString("contentId");

                // If the contentId is not already in the notifications list, add it
                if (!existingContentIds.contains(contentId)) {
                    String authorId = post.optString("authorId");
                    ILoadUsers loadUsers = LoadUsers.getInstance();
                    IUserRepository userRepository = UserRepository.getInstance(loadUsers);
                    String username = userRepository.getUsernameByUserId(authorId);
                    String timestamp = post.optString("timestamp");
                    String displayText = username + " posted @: " + timestamp;

                    // Create a new notification JSON object
                    JSONObject newNotification = new JSONObject();
                    newNotification.put("notification", displayText);
                    newNotification.put("contentId", contentId);  // Track content ID to avoid duplicates
                    newNotification.put("authorId", authorId);
                    newNotification.put("timestamp", timestamp);
                    // Add the new notification to the list
                    postNotificationList.put(newNotification);
                }
            }
        }

        // Save the updated post notifications to the JSON file
        storageHandler.saveDataAsArray(postNotificationList, "data/PostNotifications.json");

        // Display the updated notifications in the UI
        displayPostList(postNotificationList);
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
                JSONObject newFriendNotification = new JSONObject();
                newFriendNotification.put("notification", displayText);
                friendNotificationsList.put(newFriendNotification);
            }
        }
        // Save the updated notification list to the database
        loadNotifications.saveNotification(friendNotificationsList, "data/FriendNotifications.json");
        // Log the updated list
        System.out.println("Updated Notifications: " + friendNotificationsList);
        // Update the ListView
        updateFriendRequestListView();
    }


    private void handleAccept() {
        String selectedRequest = requestList.getSelectionModel().getSelectedItem();
        ILoadNotifications loadNotifications = LoadNotifications.getInstance();
        if (selectedRequest != null) {
            try {
                // Split the string to extract the sender username
                String[] parts = selectedRequest.split("-");
                String sender = parts[1].trim();
                // Find user ID by username
                String newFriendId = friendShip.getUserRepository().findUserIdByUsername(sender);
                System.out.println(sender);
                if (newFriendId != null) {
                    // Accept the friend request
                    friendShip.acceptFriend(currentUserId, sender);
                    // Remove the request from the JSONArray (assuming we use the request object)
                    removeRequestFromJSONArray(sender);
                    // Save the updated list
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

