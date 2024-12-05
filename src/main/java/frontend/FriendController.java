package frontend;

import backend.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class FriendController {
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

    // Class-level variables
    private final ObservableList<String> friends = FXCollections.observableArrayList();
    private final ObservableList<String> friendRequests = FXCollections.observableArrayList();
    private final ObservableList<String> pendingFriendRequests = FXCollections.observableArrayList();
    private final ObservableList<String> friendSuggestions = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize dependencies
        ILoadFriendShips loadFriendShips = new LoadFriendShips();
        ILoadUsers loadUsers = new LoadUsers();
        IUserRepository userRepository = new UserRepository(loadUsers);
        IFriendShipManager manager = new FriendShipManager(loadFriendShips, userRepository, loadUsers);

        // Populate lists from the backend
        List<String> friendsList = manager.getFriendsWithStatus("U1");
        List<String> friendRequestsList = manager.getFriendRequests("U1");
        List<String> pendingFriendsList = manager.getPendingFriends("U1");
        List<String> suggestionsList = manager.getFriendSuggestions("U1");

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


    @FXML
    protected void onHome() {
        showAlert("Home", "Navigating to the Home Page...");
    }

    @FXML
    protected void onProfile() {
        showAlert("Profile", "Navigating to your Profile Page...");
    }

    @FXML
    protected void onNewsFeed() {
        showAlert("NewsFeed", "Navigating to NewsFeed...");
    }

    @FXML
    protected void onLogout() {
        showAlert("Logout", "Logging out...");
    }

    @FXML
    protected void onAcceptRequest() {
        String selectedRequest = friendRequestListView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            ILoadFriendShips loadFriendShips = new LoadFriendShips();
            ILoadUsers loadUsers = new LoadUsers();
            IUserRepository userRepository = new UserRepository(loadUsers);
            IFriendShipManager manager = new FriendShipManager(loadFriendShips, userRepository, loadUsers);
            IFriendShipValidation managerValidation = new FriendShipValidation();
            FriendShip friendShip = new FriendShip(userRepository, loadFriendShips, managerValidation, manager);
            String currentUserId = "U1";
            String newFriendId = userRepository.findUserIdByUsername(selectedRequest);
            friendShip.acceptFriend(currentUserId, selectedRequest);
            String status = userRepository.getStatusByUserId(newFriendId);
            friends.add(selectedRequest + "(" + status + ")");
            // Remove from friend requests
            friendRequests.remove(selectedRequest);
            friendSuggestions.remove(selectedRequest);
            // Update ListView
//            friendListView.setItems(FXCollections.observableArrayList(friends));
//            friendRequestListView.setItems(FXCollections.observableArrayList(friendRequests));
        }
    }


    @FXML
    protected void onRejectRequest() {
        String selectedRequest = friendRequestListView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {

            ILoadFriendShips loadFriendShips = new LoadFriendShips();
            ILoadUsers loadUsers = new LoadUsers();
            IUserRepository userRepository = new UserRepository(loadUsers);
            IFriendShipManager manager = new FriendShipManager(loadFriendShips, userRepository, loadUsers);
            IFriendShipValidation managerValidation = new FriendShipValidation();
            FriendShip friendShip = new FriendShip(userRepository, loadFriendShips, managerValidation, manager);
            String currentUserId = "U1";
            friendShip.removeFriend(currentUserId, selectedRequest);
            friendRequests.remove(selectedRequest);
        }
    }

    @FXML
    protected void onBlockFriend() {
        String selectedFriend = friendListView.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {

            ILoadFriendShips loadFriendShips = new LoadFriendShips();
            ILoadUsers loadUsers = new LoadUsers();
            IUserRepository userRepository = new UserRepository(loadUsers);
            IFriendShipManager manager = new FriendShipManager(loadFriendShips, userRepository, loadUsers);
            IFriendShipValidation managerValidation = new FriendShipValidation();
            FriendShip friendShip = new FriendShip(userRepository, loadFriendShips, managerValidation, manager);
            String currentUserId = "U1";
            String selectedFriendUsername = manager.extractUsername(selectedFriend);
            friendShip.BlockFriendship(currentUserId, selectedFriendUsername);
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

            ILoadFriendShips loadFriendShips = new LoadFriendShips();
            ILoadUsers loadUsers = new LoadUsers();
            IUserRepository userRepository = new UserRepository(loadUsers);
            IFriendShipManager manager = new FriendShipManager(loadFriendShips, userRepository, loadUsers);
            IFriendShipValidation managerValidation = new FriendShipValidation();
            FriendShip friendShip = new FriendShip(userRepository, loadFriendShips, managerValidation, manager);
            String currentUserId = "U1";
           String selectedFriendUsername = manager.extractUsername(selectedFriend);
           System.out.println(selectedFriend);
            friendShip.removeFriend(currentUserId, selectedFriendUsername);

            // Remove from the local friends list
            friends.remove(selectedFriend);
        }
    }



    @FXML
    protected void onAddFriendFromSuggestions() {
        String selectedSuggestion = suggestionListView.getSelectionModel().getSelectedItem();
        if (selectedSuggestion != null) {
            ILoadFriendShips loadFriendShips = new LoadFriendShips();
            ILoadUsers loadUsers = new LoadUsers();
            String currentUserId = "U1"; // Replace with the actual method to get the current user ID
            IUserRepository userRepository = new UserRepository(loadUsers);
            FriendShip friendShip = new FriendShip(userRepository, loadFriendShips, new FriendShipValidation(),
                    new FriendShipManager(loadFriendShips, userRepository, loadUsers));
            friendShip.addFriend(currentUserId, selectedSuggestion);
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
