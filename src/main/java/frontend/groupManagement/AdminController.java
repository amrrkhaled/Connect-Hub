package frontend.groupManagement;

import backend.Groups.*;
import backend.friendship.FriendShip;
import backend.friendship.FriendShipFactory;
import backend.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
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
import java.util.concurrent.atomic.AtomicBoolean;

public class AdminController extends NormalUserController {

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
    GeneralAdmin admin = new GeneralAdmin(loadGroups, storageHandler); // Make sure this class is properly imported and exists

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

    @FXML
    public void onRefresh() {
        super.onRefresh();
        loadRequests();
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
                facade.removeGroupMember(GROUPNAME, userId); // Remove the user from the group
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
                facade.handleMemberJoinRequest(GROUPNAME, userId, true); // Accept request
                showInfoDialog("Request Accepted", "User " + userId + " has been added to the group.");
            } else if (response == javafx.scene.control.ButtonType.CANCEL) {
                facade.handleMemberJoinRequest(GROUPNAME, userId, false);
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

    protected void loadPosts(String name) {
        JSONArray allPosts = storageHandler.loadDataAsArray(postsFilePath);
        JSONArray posts = new JSONArray();

        for (int i = 0; i < allPosts.length(); i++) {
            JSONObject post = allPosts.getJSONObject(i);
            if (post.getString("groupName").equals(name)) {
                posts.put(post);
            }
        }

        for (int i = 0; i < posts.length(); i++) {
            JSONObject post = posts.getJSONObject(i);

            VBox postBox = new VBox();
            postBox.setSpacing(10);
            postBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 10px; -fx-padding: 15px; -fx-background-color: #f9f9f9;");

            // Add author
            String authorId = post.optString("authorId", "");
            String author = friendShip.getUserRepository().getUsernameByUserId(authorId);

            if (!author.isEmpty()) {
                Label authorLabel = new Label("Author: " + author);
                authorLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #3b5998;");
                postBox.getChildren().add(authorLabel);
            }

            // Add content
            String content = post.optString("content", "");
            if (!content.isEmpty()) {
                Text postText = new Text(content);
                postText.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
                postBox.getChildren().add(postText);
            }

            // Add images
            JSONArray images = post.optJSONArray("images");
            if (images != null) {
                HBox imageContainer = new HBox();
                imageContainer.setSpacing(10);

                for (int j = 0; j < images.length(); j++) {
                    String imagePath = images.getString(j);
                    if (isFileValid(imagePath)) {
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

            // Add timestamp
            String time = post.optString("timestamp", "");
            if (!time.isEmpty()) {
                Label timeLabel = new Label(time);
                timeLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #555;");
                postBox.getChildren().add(timeLabel);
            }

            // Add Like/Unlike Button
            JSONArray likedBy = post.optJSONArray("likedBy");
            if (likedBy == null) {
                likedBy = new JSONArray();
                post.put("likedBy", likedBy);
            }

            // Use AtomicBoolean to track if the current user liked the post
            AtomicBoolean userLiked = new AtomicBoolean(likedBy.toList().contains(userId));

            Label likesLabel = new Label("Likes: " + likedBy.length());
            likesLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

            Button likeButton = new Button(userLiked.get() ? "Unlike" : "Like");
            likeButton.setStyle("-fx-font-size: 12px; -fx-background-color: #3b5998; -fx-text-fill: white; -fx-padding: 5 10;");
            JSONArray finalLikedBy = likedBy;
            likeButton.setOnAction(event -> {
                if (userLiked.get()) {
                    // Remove user's like
                    for (int j = 0; j < finalLikedBy.length(); j++) {
                        if (finalLikedBy.getString(j).equals(userId)) {
                            finalLikedBy.remove(j);
                            break;
                        }
                    }
                    userLiked.set(false);
                } else {
                    // Add user's like
                    finalLikedBy.put(userId);
                    userLiked.set(true);
                }

                // Update the like button and label
                likeButton.setText(userLiked.get() ? "Unlike" : "Like");
                likesLabel.setText("Likes: " + finalLikedBy.length());

                // Save updated likes to the backend
                storageHandler.saveDataAsArray(allPosts, postsFilePath);
            });

            HBox likeBox = new HBox(10, likeButton, likesLabel);
            likeBox.setAlignment(Pos.CENTER_LEFT);
            postBox.getChildren().add(likeBox);

            // Add Comment Section
            TextField commentField = new TextField();
            commentField.setPromptText("Write a comment...");
            Button addCommentButton = new Button("Add Comment");

            addCommentButton.setOnAction(event -> {
                String comment = commentField.getText();
                if (!comment.isEmpty()) {
                    facade.addCommentToPost(post.getString("contentId"), comment);
                    commentField.clear();
                    System.out.println("Comment added to post: " + comment);
                }
            });

            Button viewCommentsButton = new Button("View Comments");
            viewCommentsButton.setOnAction(event -> {
                JSONArray comments = facade.getCommentsByPost(post.getString("contentId"));

                VBox commentsBox = new VBox();
                commentsBox.setSpacing(5);

                for (int j = 0; j < comments.length(); j++) {
                    String comment = comments.getString(j);
                    Label commentLabel = new Label(comment);
                    commentLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #333;");
                    commentsBox.getChildren().add(commentLabel);
                }

                Stage commentsStage = new Stage();
                commentsStage.setTitle("Comments");

                ScrollPane scrollPane = new ScrollPane(commentsBox);
                scrollPane.setFitToWidth(true);

                Scene scene = new Scene(scrollPane, 300, 400);
                commentsStage.setScene(scene);
                commentsStage.show();
            });

            HBox commentBox = new HBox(10, commentField, addCommentButton, viewCommentsButton);
            postBox.getChildren().add(commentBox);
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
                facade.deletePost(postId);
                super.onRefresh();
            }
        });
    }

    public void navigateToEditPost(String postId) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/frontend/editPost.fxml"));
            Scene loginScene = new Scene(loginPage);

            // Get current stage
            Stage currentStage = (Stage) requestsListView.getScene().getWindow();
            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));
            editPostController.setPostId(postId);
            // Set new scene and show the stage
            currentStage.setScene(loginScene);
            currentStage.setTitle("Edit Post");
            currentStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
