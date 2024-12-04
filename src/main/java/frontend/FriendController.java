package frontend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import backend.*;

public class FriendController {
    @FXML
    private ListView<String> friendListView;
    @FXML
    private ListView<String> friendRequestListView;
    @FXML
    private ListView<String> suggestionListView;
    @FXML
    private TextField searchTextField;

    private final ObservableList<String> friends = FXCollections.observableArrayList();
    private final ObservableList<String> friendRequests = FXCollections.observableArrayList();
    private final ObservableList<String> friendSuggestions = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        friends.addAll("Omar", "Hany", "Hussein");
        friendRequests.addAll("Amr", "Sameh");
        friendSuggestions.addAll("Khaled", "Franco");


        ILoadUsers loadUsers = new LoadUsers();
        Validation valid = new UserValidator(loadUsers);
        IFriendShipValidation fsv = new FriendShipValidation();
        ILoadFriendShips loadFriendShips = new LoadFriendShips();
        IFriendShipManager manager = new FriendShipManager();
        FriendShip manager1 = new FriendShip(valid ,loadFriendShips,fsv,manager);
     //    manager1.addFriend(userId1,username1);
        //manager1.acceptFriend("U6","ahmed");
    //    manager1.BlockFriendship(userId1,username1);

        friendListView.setItems(friends);
        friendRequestListView.setItems(friendRequests);
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
            friends.add(selectedSuggestion + " (Pending)");
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
