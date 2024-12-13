package frontend.groupManagement;

import backend.Groups.*;
import backend.friendship.FriendShip;
import backend.friendship.FriendShipFactory;
import backend.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

public class PrimaryAdminController extends AdminController {

    @FXML
    private ListView<String> requestsListView;
    private final ObservableList<String> friends = FXCollections.observableArrayList();
    private final String userId = User.getUserId();
    private final String requestsFilePath = "data/groups_join_requests.json";
    private final String membersFilePath = "data/group_members.json";
    public FriendShip friendShip = FriendShipFactory.createFriendShip();
    public String GROUPNAME = Group.getGroupName();
    IStorageHandler storageHandler = new StorageHandler();

    ILoadGroups loadGroups = LoadGroups.getInstance(storageHandler);
    GroupManager groupManager = new GroupManager(loadGroups);
    PrimaryAdmin pAdmin = new PrimaryAdmin(loadGroups,storageHandler); // Make sure this class is properly imported and exists

    @FXML
    public void initialize() {
        super.initialize();

    }


        @Override
        protected void showMemberDialog(String userId) {
            // Check if the user is an admin or primary member (cannot be removed)

            // Create a dialog box with multiple options
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            dialog.setTitle("Member Options");
            dialog.setHeaderText("Choose an action for user: " + userId);
            dialog.setContentText("Select one of the following actions:");

            // Add custom buttons for Promoting, Demoting, and Removing
            dialog.getButtonTypes().setAll(
                    new javafx.scene.control.ButtonType("Promote"),
                    new javafx.scene.control.ButtonType("Demote")  ,
                    new javafx.scene.control.ButtonType("Remove")
            );

            // Handle the button actions
            dialog.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.YES) {
                    // Call your back-end method for promotion

                    if (isUserAdmin(userId) || isUserPrimaryAdmin(userId)) {
                        super.showErrorDialog("User is Already an Admin", "The user " + userId);
                        return;  // Don't proceed if the user is an admin or primary member
                    }

                    pAdmin.addAdminToGroup(GROUPNAME, userId);  // Promote the user
                    super.showInfoDialog("Promoted", "User " + userId + " has been promoted to admin.");
                } else if (response == javafx.scene.control.ButtonType.NO) {
                    // Call your back-end method for demotion
                    pAdmin.removeAdmin(GROUPNAME, userId);   // Demote the user
                    super.showInfoDialog("Demoted", "User " + userId + " has been demoted to a regular member.");
                } else if (response.getText().equals("Remove")) {
                    // Call your back-end method for removal
                    if (isUserAdmin(userId) || isUserPrimaryAdmin(userId)) {
                        super.showErrorDialog("Cannot Remove Member", "The user " + userId + " is an admin or primary member and cannot be removed.");
                        return;  // Don't proceed if the user is an admin or primary member
                    }
                    pAdmin.removeMember(GROUPNAME, userId);   // Remove the user from the group
                    super.showInfoDialog("Removed", "User " + userId + " has been removed from the group.");
                }
            });

            // Reload the member list after the action
            super.onRefresh();
        }

    }
