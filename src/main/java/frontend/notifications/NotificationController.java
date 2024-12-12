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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class NotificationController {
    public FriendShip friendShip= FriendShipFactory.createFriendShip();
    public FriendRequestServiceFactory factory = FriendRequestServiceFactory.getInstance();
    public FriendRequestService service = factory.createFriendRequestService();
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

    private FriendNotifications friendNotifications;
    private PostNotification postNotification;
    private ObservableList<String> postNotifications = FXCollections.observableArrayList();
    private ObservableList<String> friendNotificationsList = FXCollections.observableArrayList();
    private final String currentUserId = User.getUserId();
    private final ObservableList<String> friendRequests = FXCollections.observableArrayList();
    @FXML
    private void initialize() {
        // Initialize the loaders and notification instances
        ILoadNotifications loader = new LoadNotifications();
        postNotification = new PostNotification(loader);
        friendNotifications = new FriendNotifications(loader);
        // Initialize dependencies
        // Populate lists from the backend
        List<String> friendRequestsList = service.getFriendRequests(currentUserId);
        // Set the ListView items with the populated ObservableLists

        friendRequests.addAll(friendRequestsList);

        // Load and populate the notifications
        loadPostNotifications();
        loadFriendNotifications();

        // Set list views
        postList.setItems(postNotifications);
        requestList.setItems(friendNotificationsList);

        // Handle button actions
        acceptButton.setOnAction(event -> handleAccept());
        rejectButton.setOnAction(event -> handleReject());
    }

    private void loadPostNotifications() {
        JSONArray notifications = postNotification.getNotification();
        postNotifications.clear();

        for (int i = 0; i < notifications.length(); i++) {
            JSONObject notification = notifications.getJSONObject(i);
            String author = notification.optString("id1", "Unknown Author");
            String timestamp = notification.optString("timestamp", "Unknown Time");

            String displayText = "Author: " + author + " just posted @ " + timestamp;
            postNotifications.add(displayText);
        }
    }

    private void loadFriendNotifications() {
        JSONArray notifications = friendNotifications.getNotification();
        friendNotificationsList.clear();

        for (int i = 0; i < notifications.length(); i++) {
            JSONObject notification = notifications.getJSONObject(i);
            String sender = notification.optString("sender", "Unknown Sender");
            String receiver = notification.optString("receiver", "Unknown Receiver");

            String displayText = "Friend Request from: " + sender + " to: " + receiver;
            friendNotificationsList.add(displayText);
        }
    }

    private void handleAccept() {
        String selectedRequest = requestList.getSelectionModel().getSelectedItem();

            if (selectedRequest != null) {

                String newFriendId = friendShip.getUserRepository().findUserIdByUsername(selectedRequest);
                friendShip.acceptFriend(currentUserId, selectedRequest);
                String status = friendShip.getUserRepository().getStatusByUserId(newFriendId);
                // Remove from friend requests
                friendRequests.remove(selectedRequest);
                // Update ListView


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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
