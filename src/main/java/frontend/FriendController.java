package frontend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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

    private final ObservableList<String> friends = FXCollections.observableArrayList();
    private final ObservableList<String> friendRequests = FXCollections.observableArrayList();
    private final ObservableList<String> pendingFriendRequests = FXCollections.observableArrayList();
    private final ObservableList<String> friendSuggestions = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        friends.addAll("Omar", "Hany", "Hussein");
        friendRequests.addAll("Amr", "Sameh");
        pendingFriendRequests.addAll("Khaled (Pending)");
        friendSuggestions.addAll("Franco");

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
            friends.add(selectedRequest);
            friendRequests.remove(selectedRequest);
        }
    }

    @FXML
    protected void onRejectRequest() {
        String selectedRequest = friendRequestListView.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            friendRequests.remove(selectedRequest);
        }
    }

    @FXML
    protected void onBlockFriend() {
        String selectedFriend = friendListView.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {
            friends.remove(selectedFriend);
        }
    }

    @FXML
    protected void onRemoveFriend() {
        String selectedFriend = friendListView.getSelectionModel().getSelectedItem();
        if (selectedFriend != null) {
            friends.remove(selectedFriend);
        }
    }

    @FXML
    protected void onAddFriendFromSuggestions() {
        String selectedSuggestion = suggestionListView.getSelectionModel().getSelectedItem();
        if (selectedSuggestion != null) {
            pendingFriendRequests.add(selectedSuggestion + " (Pending)");
            friendSuggestions.remove(selectedSuggestion);
        }
    }

    @FXML
    protected void onSearch() {
        String searchTerm = searchTextField.getText().toLowerCase();
        if (searchTerm.isEmpty()) {
            friendListView.setItems(friends); // Reset to full list if search is empty
        } else {
            ObservableList<String> filteredFriends = FXCollections.observableArrayList();
            for (String friend : friends) {
                if (friend.toLowerCase().contains(searchTerm)) {
                    filteredFriends.add(friend);
                }
            }
            friendListView.setItems(filteredFriends);
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
