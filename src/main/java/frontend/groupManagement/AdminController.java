package frontend.groupManagement;

import backend.Groups.*;
import backend.friendship.FriendShip;
import backend.friendship.FriendShipFactory;
import backend.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminController extends GroupsController {

    @FXML
    private ListView<String> requestsListView;
    private final ObservableList<String> friends = FXCollections.observableArrayList();
    private final String userId = User.getUserId();
    private final String requestsFilePath = "data/groups_join_requests.json";
    private final String membersFilePath = "data/group_members.json";
    private final String postsFilePath = "data/groupsPosts.json";

    public FriendShip friendShip = FriendShipFactory.createFriendShip();
    public String GROUPNAME = Group.getGroupName();
    IStorageHandler storageHandler = new StorageHandler();

    ILoadGroups loadGroups = LoadGroups.getInstance(storageHandler);
    GroupManager groupManager = new GroupManager(loadGroups);
    GeneralAdminController admin = new GeneralAdminController(loadGroups,storageHandler); // Make sure this class is properly imported and exists

    @FXML
    public void initialize() {
        super.initialize();
        loadRequests();

        // Initialize mouse click listener for requestsListView
        requestsListView.setOnMouseClicked(this::handleRequestClick);
        membersListView.setOnMouseClicked(this::handleMemberClick);
    }

    private void handleRequestClick(MouseEvent event) {
        String userName = requestsListView.getSelectionModel().getSelectedItem();
        String selectedRequestUserId = friendShip.getUserRepository().findUserIdByUsername(userName);
        if (selectedRequestUserId != null) {
            showAcceptRejectDialog(selectedRequestUserId);
        }
    }
    private void handleMemberClick(MouseEvent event) {
        String userName = membersListView.getSelectionModel().getSelectedItem();
        String selectedRequestUserId = friendShip.getUserRepository().findUserIdByUsername(userName);
        if (selectedRequestUserId != null) {
            showMemberDialog(selectedRequestUserId);
            System.out.println("Removing user..... " + selectedRequestUserId);
        }
    }
    boolean isUserPrimaryAdmin(String name) {
        // Load the group details by name
        JSONObject group = loadGroups.loadGroupByName(name);

        // Check if the "primaryAdminId" exists and if it matches the current user's ID
        if (group != null && group.has("primaryAdminId") && group.get("primaryAdminId").equals(userId)) {
            return true;
        } else {
            return false;
        }
    }

    boolean isUserAdmin(String name) {
        // Load the group details by name
        JSONObject group = loadGroups.loadGroupByName(name);

        // Check if the "admins" array exists
        if (group != null && group.has("admins")) {
            JSONArray admins = group.getJSONArray("admins");

            // Loop through the admins array to check if the current user is an admin
            for (int i = 0; i < admins.length(); i++) {
                if (admins.optString(i).equals(userId)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void showMemberDialog(String userId) {
        // Check if the user is an admin or primary member
        if ((isUserAdmin(userId) || isUserPrimaryAdmin(userId))) {
            showErrorDialog("Cannot Remove Member", "The user " + userId + " is an admin or primary member and cannot be removed.");
            return;  // Don't proceed if the user is admin or primary member
        }

        // Create a dialog box or alert for admin to confirm removal
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Remove Member");
        dialog.setHeaderText("Are you sure you want to remove this user?");
        dialog.setContentText("User: " + userId);

        // Show confirmation dialog
        dialog.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                admin.removeMember(GROUPNAME, userId); // Remove the user from the group
                showInfoDialog("Member Removed", "User " + userId + " has been successfully removed from the group.");
            }
        });

        // Reload the list of members after the removal action
        super.onRefresh(); // Call the refresh method to update the UI
    }



    // Show an error dialog (for when a user is an admin or primary member)
    protected void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    private void showAcceptRejectDialog(String userId) {
        // Create a dialog box or alert for admin to accept or reject
        Alert dialog = new Alert(AlertType.CONFIRMATION);
        dialog.setTitle("Request Action");
        dialog.setHeaderText("Do you want to accept or reject the request?");
        dialog.setContentText("User: " + userId);

        // Show accept and reject buttons
        dialog.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                admin.acceptMember(GROUPNAME, userId); // Accept request
                showInfoDialog("Request Accepted", "User " + userId + " has been added to the group.");
            } else if (response == javafx.scene.control.ButtonType.CANCEL) {
                admin.rejectMember(GROUPNAME, userId); // Reject request
                showInfoDialog("Request Rejected", "User " + userId + "'s request has been rejected.");
            }
        });
        loadRequests(); // Reload the list of requests after action
        super.onRefresh(); // Call the refresh method to update the UI
    }

    protected void showInfoDialog(String title, String message) {
        // Show an info dialog with the result of the action
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void loadRequests() {
        // Load the data from the JSON file (requestsFilePath is assumed to be the file path containing requests)
        JSONArray requests = storageHandler.loadDataAsArray(requestsFilePath);
        List<String> requestsUserNames = new ArrayList<>();

        // Loop through all groups in the data
        for (int i = 0; i < requests.length(); i++) {
            JSONObject group = requests.getJSONObject(i);

            // Check if the groupName matches the current group
            if (group.getString("groupName").equals(GROUPNAME)) {
                // Get the "requests" array
                JSONArray groupRequests = group.getJSONArray("requests");

                // Loop through all requests for the group
                for (int j = 0; j < groupRequests.length(); j++) {
                    JSONObject request = groupRequests.getJSONObject(j);
                    // Extract the necessary fields (userId)
                    String requestUserId = request.getString("userId");
                    String userName = friendShip.getUserRepository().getUsernameByUserId(requestUserId);

                    // Add the userId to the list of requests
                    requestsUserNames.add(userName);
                }
            }
        }

        // Populate the ListView with the requests (userIds)
        requestsListView.getItems().clear(); // Clear existing items
        requestsListView.getItems().addAll(requestsUserNames); // Add new requests
    }
    @Override
    protected void loadPosts(String name) {
        // Creating a mock JSONArray
        JSONArray allPosts = storageHandler.loadDataAsArray(postsFilePath);  // Load the JSON array
        JSONArray posts = new JSONArray();
        for (int i = 0; i < allPosts.length(); i++) {
            JSONObject post = allPosts.getJSONObject(i);  // Get each post

            // Check if the post's "groupName" matches the given name
            if (post.getString("groupName").equals(name)) {
                posts.put(post);  // Add the post to the filteredPosts JSONArray
            }
        }
        // Iterate over the posts JSONArray
        for (int i = 0; i < posts.length(); i++) {
            JSONObject post = posts.getJSONObject(i);  // Get each post

            VBox postBox = new VBox();
            postBox.setSpacing(10);

            // Add content (caption)

            String authorId = post.optString("authorId", "");
            String author = friendShip.getUserRepository().getUsernameByUserId(authorId);

            if (!author.isEmpty()) {
                Label authorLabel = new Label("Author: " + author);

                // Apply enhanced styles
                authorLabel.setStyle("-fx-font-size: 12px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #3b5998; " +
                        "-fx-background-color: #e7f3ff; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-padding: 8px 15px; " +
                        "-fx-border-color: #a1c4e9; " +
                        "-fx-border-width: 1px; " +
                        "-fx-background-insets: 0, 1;");

                authorLabel.setEffect(new javafx.scene.effect.DropShadow(5, Color.GRAY));

                postBox.getChildren().add(authorLabel);
            }

            String content = post.optString("content", "");
            if (!content.isEmpty()) {
                Text postText = new Text(content);
                postText.setStyle("-fx-font-size: 14px;");
                postBox.getChildren().add(postText);
            }

            // Add images
            JSONArray images = post.optJSONArray("images");
            if (images != null) {
                HBox imageContainer = new HBox();
                imageContainer.setSpacing(10);
                imageContainer.setStyle("-fx-padding: 10;");

                for (int j = 0; j < images.length(); j++) {
                    String imagePath = images.getString(j); // Get the image path
                    if (super.isFileValid(imagePath)) {
                        Image image = new Image("file:" + imagePath.trim());
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(200);
                        imageView.setPreserveRatio(true);
                        imageContainer.getChildren().add(imageView);
                    } else {
                        System.out.println("Could not load image: " + imagePath);
                    }
                }

                postBox.getChildren().add(imageContainer);
            }
            String time = post.optString("timestamp", "");
            if (!time.isEmpty()) {
                // Create a Label for the time
                Label timeLabel = new Label(time);

                // Style the Label for a distinct look
                timeLabel.setStyle("-fx-font-size: 12px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #555555; " +
                        "-fx-background-color: #f2f2f2; " +  // Light background
                        "-fx-border-radius: 5px; " +           // Rounded corners
                        "-fx-padding: 5px 10px; " +            // Padding inside the label
                        "-fx-border-color: #dddddd; " +        // Border color
                        "-fx-border-width: 1px;");             // Border width

                // Optional: Add a subtle shadow effect
                timeLabel.setEffect(new javafx.scene.effect.DropShadow(10, Color.GRAY));

                // Add the styled time label to the postBox
                postBox.getChildren().add(timeLabel);
            }

            postBox.setOnMouseClicked(event -> {
                // Action when post is clicked, for example, print the postId
                System.out.println("Post clicked: " + post.optString("contentId"));

               showEditDeleteDialog(post);
            });
            // Add the post box to the posts list view
            postsListView.getItems().add(postBox);
        }
    }

    private void showEditDeleteDialog(JSONObject post) {
        String postId = post.optString("contentId", "");  // Get the post ID

        // Create a choice dialog with Edit and Delete options
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Edit", "Edit", "Delete");
        dialog.setTitle("Post Options");
        dialog.setHeaderText("Choose an option for the post:");
        dialog.setContentText("What would you like to do with this post?");

        // Show the dialog and wait for the user's response
        dialog.showAndWait().ifPresent(action -> {
            if ("Edit".equals(action)) {
                // Handle Edit action
                System.out.println("Editing post: " + postId);
                // Open an edit view or trigger edit logic
               navigateToEditPost(postId);
            } else if ("Delete".equals(action)) {
                // Handle Delete action
                System.out.println("Deleting post: " + postId);
                // Call a method to delete the post
                admin.deletePost(postId);
                super.onRefresh();
            }
        });
    }
    public void navigateToEditPost(String postId) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/frontend/editPost.fxml"));
            Scene loginScene = new Scene(loginPage);

            // Get current stage
            Stage currentStage =(Stage) postsListView.getScene().getWindow();
            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));

            // Set new scene and show the stage
            currentStage.setScene(loginScene);
            currentStage.setTitle("Edit Post");
            currentStage.show();

        


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
