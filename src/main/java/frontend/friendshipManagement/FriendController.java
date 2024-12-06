package frontend.friendshipManagement;

import backend.friendship.FriendShip;
import backend.friendship.FriendShipFactory;
import backend.user.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class FriendController {
    public FriendShip friendShip= FriendShipFactory.createFriendShip();
    @FXML
    private ListView<String> friendListView;
    @FXML
    private ListView<String> friendRequestListView;
    @FXML
    private ListView<String> pendingFriendRequestListView;
    @FXML
    private ListView<String> suggestionListView;
    @FXML
    private TextField searchTextField;

    private final String userId = User.getUserId();

    // Class-level variables
    private final ObservableList<String> friends = FXCollections.observableArrayList();
    private final ObservableList<String> friendRequests = FXCollections.observableArrayList();
    private final ObservableList<String> pendingFriendRequests = FXCollections.observableArrayList();
    private final ObservableList<String> friendSuggestions = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize dependencies
        // Populate lists from the backend

        List<String> friendsList = friendShip.getManager().getFriendsWithStatus(userId);
        List<String> friendRequestsList = friendShip.getManager().getFriendRequests(userId);
        List<String> pendingFriendsList = friendShip.getManager().getPendingFriends(userId);
        List<String> suggestionsList = friendShip.getManager().getFriendSuggestions(userId);
        // Set the ListView items with the populated ObservableLists
        friends.addAll(friendsList);  // Add all elements to the ObservableList
        friendRequests.addAll(friendRequestsList);
        pendingFriendRequests.addAll(pendingFriendsList);
        friendSuggestions.addAll(suggestionsList);

        // Bind the ObservableLists to the corresponding ListViews
        friendListView.setItems(friends);
        friendRequestListView.setItems(friendRequests);
        pendingFriendRequestListView.setItems(pendingFriendRequests);
        suggestionListView.setItems(friendSuggestions);
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

    public void onNewsFeed(ActionEvent event) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/frontend/NewsFeed.fxml"));
            Scene loginScene = new Scene(loginPage);

            // Get current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));

            // Set new scene and show the stage
            currentStage.setScene(loginScene);
            currentStage.setTitle("NewsFeed");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onLogout(ActionEvent event) {
        try {
            ILoadUsers loadUsers = LoadUsers.getInstance();
            IAddUser user = new AddUser(loadUsers);
            Validation valid = new UserValidator(loadUsers);
            IUpdateUser updateUser = new UpdateUser();
            IUserRepository userRepository = UserRepository.getInstance(loadUsers);
            UserManager manager = new UserManager(user, loadUsers, valid, updateUser,userRepository);
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

    @FXML
    protected void onAcceptRequest() {
        String selectedRequest = friendRequestListView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {

            String newFriendId = friendShip.getUserRepository().findUserIdByUsername(selectedRequest);
            friendShip.acceptFriend(userId, selectedRequest);
            String status = friendShip.getUserRepository().getStatusByUserId(newFriendId);
            friends.add(selectedRequest + "(" + status + ")");
            // Remove from friend requests
            friendRequests.remove(selectedRequest);
            friendSuggestions.remove(selectedRequest);
            // Update ListView
        }
    }


    @FXML
    protected void onRejectRequest() {
        String selectedRequest = friendRequestListView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {

            friendShip.removeFriend(userId, selectedRequest);
            friendRequests.remove(selectedRequest);
        }
    }

    @FXML
    protected void onBlockFriend() {
        String selectedFriend = friendListView.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {

            String selectedFriendUsername = friendShip.getManager().extractUsername(selectedFriend);
            friendShip.BlockFriendship(userId, selectedFriendUsername);
            // Remove from the friends list
            friends.remove(selectedFriend);

            // Update ListView
            friendListView.setItems(friends);
        }
    }


    @FXML
    protected void onRemoveFriend() {
        String selectedFriend = friendListView.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {

            String selectedFriendUsername = friendShip.getManager().extractUsername(selectedFriend);
            System.out.println(selectedFriend);
            friendShip.removeFriend(userId, selectedFriendUsername);

            // Remove from the local friends list
            friends.remove(selectedFriend);
        }
    }


    @FXML
    protected void onAddFriendFromSuggestions() {
        String selectedSuggestion = suggestionListView.getSelectionModel().getSelectedItem();
        if (selectedSuggestion != null) {
             // Replace with the actual method to get the current user ID
            friendShip.addFriend(userId, selectedSuggestion);
            // Update UI Lists
            pendingFriendRequests.add(selectedSuggestion + " (pending)"); // Add to pending
            friendSuggestions.remove(selectedSuggestion); // Remove from suggestions
        }
    }


    @FXML
    protected void onSearch() {
        String searchTerm = searchTextField.getText().toLowerCase();
        ObservableList<String> filteredFriends = FXCollections.observableArrayList();

        if (searchTerm.isEmpty()) {
            // Reset to the full list if search is empty
            filteredFriends.addAll(friends);
        } else {
            for (String friend : friends) {
                if (friend.toLowerCase().contains(searchTerm)) {
                    filteredFriends.add(friend);
                }
            }
        }

        // Update the ListView
        friendListView.setItems(filteredFriends);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
