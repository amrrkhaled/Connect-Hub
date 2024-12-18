package frontend.searchManagement;

import backend.Groups.*;
import backend.friendship.*;
import backend.search.IGroupSearch;
import backend.search.SearchManager;
import backend.user.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class SearchController {

    @FXML
    private ListView<String> usersListView;

    @FXML
    private ListView<String> groupsListView;

    static String keyword;
    IStorageHandler storageHandler = new StorageHandler();
    ILoadGroups loadGroups = LoadGroups.getInstance(storageHandler);
    GroupManager groupManager = new GroupManager(loadGroups);
    public FriendShip friendShip = FriendShipFactory.createFriendShip();
    public FriendRequestServiceFactory factory = FriendRequestServiceFactory.getInstance();
    public FriendRequestService service = factory.createFriendRequestService();
    ILoadFriendShips loadFriendShips = LoadFriendShips.getInstance();
    ILoadUsers loadUsers = LoadUsers.getInstance();
    IUserRepository userRepository = UserRepository.getInstance(loadUsers);
    IFriendshipService friendshipService = FriendshipService.getInstance(userRepository, loadFriendShips);
    private final String userId = User.getUserId();
    NormalUser normalUser = new NormalUser(loadGroups, storageHandler);
    PrimaryAdmin primaryAdminController = new PrimaryAdmin(loadGroups, storageHandler);
    Request requestController = new Request(loadGroups, storageHandler);
    IGroupSearch search = new SearchManager(groupManager);
    private IGroupRepository groupRep = new GroupRepository(loadGroups, storageHandler);
    GroupManagementFacade facade = new GroupManagementFacade(loadGroups, storageHandler);

    public static void setKeyword(String searchText) {
        keyword = searchText;
    }

    @FXML
    public void initialize() {
        // Load users and groups from backend

        List<String> users = search.searchUsers(keyword);
        List<String> groups = search.searchGroups(keyword);

        // Populate the ListViews with the loaded data
        usersListView.setItems(FXCollections.observableArrayList(users));
        groupsListView.setItems(FXCollections.observableArrayList(groups));

        // Set ContextMenu for users
        ContextMenu userMenu = new ContextMenu();
        MenuItem addFriend = new MenuItem("Add Friend");
        addFriend.setOnAction(event -> handleAddFriend(usersListView.getSelectionModel().getSelectedItem()));

        MenuItem removeFriend = new MenuItem("Remove Friend");
        removeFriend.setOnAction(event -> handleRemoveFriend(usersListView.getSelectionModel().getSelectedItem()));

        // Check if the user is already a friend dynamically
        usersListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("pending" + friendshipService.areTheyPending(userId, newSelection));
                String friendId = userRepository.findUserIdByUsername(newSelection);
                if (friendshipService.areTheyPending(userId, friendId)) {
                    addFriend.setDisable(true);
                    removeFriend.setDisable(true);
                } else if (!friendshipService.areTheyFriends(userId, friendId)) {
                    addFriend.setDisable(false);
                    removeFriend.setDisable(true); // Disable "Remove Friend" if not a friend
                } else {
                    addFriend.setDisable(true);     // Disable "Add Friend" if already a friend
                    removeFriend.setDisable(false);
                }
            }
        });

        userMenu.getItems().addAll(addFriend, removeFriend);
        usersListView.setContextMenu(userMenu);

        // Set ContextMenu for groups
        ContextMenu groupMenu = new ContextMenu();
        MenuItem joinGroup = new MenuItem("Join Group");

        joinGroup.setOnAction(event -> handleJoinGroup(groupsListView.getSelectionModel().getSelectedItem()));

        MenuItem leaveGroup = new MenuItem("Leave Group");
        leaveGroup.setOnAction(event -> handleLeaveGroup(groupsListView.getSelectionModel().getSelectedItem()));

        groupsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("primary" + groupRep.isUserPrimaryAdmin(userId, newSelection));
                if (groupRep.isUserPrimaryAdmin(userId, newSelection) || groupRep.hasUserSentRequest(userId, newSelection)) {
                    joinGroup.setDisable(true);
                    leaveGroup.setDisable(true);
                } else if (!(groupRep.isUserGeneralAdmin(userId, newSelection) && groupRep.isUserMember(userId, newSelection))) {
                    joinGroup.setDisable(false);
                    leaveGroup.setDisable(true);

                } else {
                    joinGroup.setDisable(false);
                    leaveGroup.setDisable(true);
                }
            }
        });

        groupMenu.getItems().addAll(joinGroup, leaveGroup);
        groupsListView.setContextMenu(groupMenu);
    }


    private void handleAddFriend(String user) {
        if (user != null) {
            System.out.println("Adding friend: " + user);
            friendShip.addFriend(userId, user);
        }
    }

    private void handleRemoveFriend(String user) {
        if (user != null) {
            System.out.println("Removing friend: " + user);
            friendShip.removeFriend(userId, user);
        }
    }

    private void handleJoinGroup(String group) {
        if (group != null) {
            System.out.println("Joining group: " + group);
            requestController.sendJoinRequest(group, userId);


        }
    }

    private void handleLeaveGroup(String group) {
        if (group != null) {
            System.out.println("Leaving group: " + group);
            facade.userLeaveGroup(group, userId);
        }
    }

    public void onNewsFeed(ActionEvent actionEvent) {
        try {
            // Load the FXML file for the NewsFeed page
            Parent newsFeedParent = FXMLLoader.load(getClass().getResource("/frontend/NewsFeed.fxml"));

            // Create a new Scene with the loaded Parent (FXML)
            Scene newsFeedScene = new Scene(newsFeedParent);

            // Get the current Stage (window)
            Stage currentStage = (Stage) groupsListView.getScene().getWindow();

            // Set the new Scene and update the Stage's title
            currentStage.setScene(newsFeedScene);
            currentStage.setTitle("NewsFeed");

            // Show the updated Stage
            currentStage.show();
        } catch (IOException e) {
            // Handle exceptions in case the FXML file cannot be loaded
            e.printStackTrace();
        }
    }
}
