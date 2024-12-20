package frontend.newsFeed;

import backend.Groups.*;
import backend.contentCreation.*;
import backend.friendship.*;
import backend.notifications.*;
import backend.profile.*;
import backend.threads.storiesThread;
import backend.user.*;
import frontend.notifications.notificationsThread;
import frontend.searchManagement.SearchController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;

import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class FeedController {

    @FXML
    public ListView<String> suggestedGroupsListView;

    @FXML
    public ListView<String> myGroupsListView;
    @FXML
    public TextField searchBar;
    @FXML
    private HBox storiesBox;
    @FXML
    private ListView<String> friendsListView;
    @FXML
    private ListView<VBox> postsListView;

    @FXML
    private ScrollPane storiesScrollPane;

    public FriendShip friendShip = FriendShipFactory.createFriendShip();
    private final IFriendshipService friendShipService = friendShip.getFriendshipService();

    public FriendRequestServiceFactory factory = FriendRequestServiceFactory.getInstance();
    public FriendRequestService service = factory.createFriendRequestService();
    @FXML
    private ScrollPane postsScrollPane;
    private final ObservableList<String> friends = FXCollections.observableArrayList();
    private final String userId = User.getUserId();

    IStorageHandler storageHandler = new StorageHandler();
    ILoadGroups loadGroups = LoadGroups.getInstance(storageHandler);
    NormalUser user = new NormalUser(loadGroups, storageHandler);
    // Creating instances of controllers
    GroupManager groupManager = new GroupManager(loadGroups);
    Request requestController = new Request(loadGroups, storageHandler);
    private IContentFiles contentFiles = ContentFiles.getInstance();
    ;

    @FXML
    public void initialize() {
        // suggestedGroups.setItems(FXCollections.observableArrayList("Suggested Group 1", "Suggested Group 2"));
        loadStories();
        loadPosts();
        loadFriendsList();
        loadMyGroupsList();
        loadSuggestions();
        storiesScrollPane.setFitToWidth(true);
        myGroupsListView.setOnMouseClicked(event -> openGroup(event));
        suggestedGroupsListView.setOnMouseClicked(this::handleSuggestionsClick);

    }

    public void handleSearch() {
        String searchText = searchBar.getText().toLowerCase();
        if (!searchText.isEmpty()) {
            SearchController.setKeyword(searchText);
            navigateSearchPage();
        }
    }

    public void navigateSearchPage() {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/frontend/search.fxml"));
            Scene loginScene = new Scene(loginPage);

            // Get current stage
            Stage currentStage = (Stage) searchBar.getScene().getWindow();
            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));

            // Set new scene and show the stage
            currentStage.setScene(loginScene);
            currentStage.setTitle("Search Results");
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleSuggestionsClick(MouseEvent event) {
        String groupName = suggestedGroupsListView.getSelectionModel().getSelectedItem();
        if (groupName != null) {
            showRequestDialog(groupName);
        }
    }

    private void showRequestDialog(String groupName) {
        // Create a dialog box or alert for admin to accept or reject
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Request Action");
        dialog.setHeaderText("Do you want to join" + groupName + " group?");
//        dialog.setContentText("User: " + userId);

        // Show accept and reject buttons
        dialog.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                requestController.sendJoinRequest(groupName, userId);
                showInfoDialog("Request", "Request to join to group will be reviewed by admins");
            } else if (response == javafx.scene.control.ButtonType.CANCEL) {
                return;

            }
        });
        loadSuggestions();
    }

    protected void showInfoDialog(String title, String message) {
        // Show an info dialog with the result of the action
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadFriendsList() {
        // Example friends array

        List<String> friendsList = service.getFriendshipService().getFriendsWithStatus(userId);
        // Add friends to the ListView
        friendsListView.getItems().clear(); // Clear existing items (if any)
        friendsListView.getItems().addAll(friendsList); // Add all friends from the array
    }

    private void loadSuggestions() {
        // Example friends array
        List<String> suggestions = loadGroups.loadGroupSuggestions(userId);
        // Add friends to the ListView
        suggestedGroupsListView.getItems().clear(); // Clear existing items (if any)
        suggestedGroupsListView.getItems().addAll(suggestions); // Add all friends from the array
    }

    private void loadMyGroupsList() {
        // Example friends array

        JSONArray myGroups = loadGroups.loadGroupsByUserId(userId);
        List<String> myGroupsNames = new ArrayList<>();  // Initialize the list

        for (int i = 0; i < myGroups.length(); i++) {
            JSONObject group = myGroups.getJSONObject(i);  // Get the JSONObject for the group
            String groupName = group.optString("groupName");  // Use optString() to safely get the group name
            if (groupName != null && !groupName.isEmpty()) {  // Ensure the group name is not null or empty
                myGroupsNames.add(groupName);  // Add the group name to the list
            }
        }
        // Add myGroups to the ListView
        myGroupsListView.getItems().clear(); // Clear existing items (if any)
        myGroupsListView.getItems().addAll(myGroupsNames); // Add all friends from the array
    }

    private void loadStories() {

        StoryFactory storyFactory = StoryFactory.getInstance();
        IContent contentManager = storyFactory.createStory();
//        JSONArray stories = contentManager.getNewsFeedContent(userId);

        List<String> friendsIDs = friendShipService.getFriends(userId);
        for (String Id : friendsIDs) {
            JSONArray stories = contentManager.getUserContent(Id);
            if (!stories.isEmpty()) {
                JSONObject storyObject = stories.getJSONObject(0); // Get the JSON object at index i
                String id = storyObject.getString("authorId");

                String userName = friendShip.getUserRepository().getUsernameByUserId(id);
                ProfileManager manager = ProfileManagerFactory.getInstance().createProfileManager(id);
                JSONObject profile = manager.getRepo().findProfileByUserId(id);

                String imagePath = null;
                if (profile != null && profile.get("ProfilePicture") != null) {
                    imagePath = profile.getString("ProfilePicture");
                }


                VBox storyItem = new VBox();
                storyItem.setSpacing(5);
                storyItem.setStyle("-fx-alignment: center;");

                Circle storyCircle = new Circle(30);


                if (imagePath != null && isFileValid(imagePath)) {
                    ImageView imageView = new ImageView(new Image("file:" + imagePath));
                    imageView.setFitWidth(60);
                    imageView.setFitHeight(60);
                    imageView.setPreserveRatio(false);
                    imageView.setClip(new Circle(30, 30, 30));
                    storyItem.getChildren().add(imageView);
                    imageView.setOnMouseClicked(event -> openStoryPage(stories));
                } else {
                    storyCircle.setFill(Color.LIGHTBLUE);
                    storyCircle.setStroke(Color.DARKBLUE);
                    storyCircle.setStrokeWidth(2);
                    storyItem.getChildren().add(storyCircle);
                    storyCircle.setOnMouseClicked(event -> openStoryPage(stories));
                }

                Text storyLabel = new Text(userName);
                storyLabel.setStyle("-fx-font-size: 12px; -fx-fill: #333333;");
                storyItem.getChildren().add(storyLabel);

                storiesBox.getChildren().add(storyItem);

            }
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

    private void openGroup(MouseEvent event) {
        // Get selected group name from the ListView
        String selectedGroup = myGroupsListView.getSelectionModel().getSelectedItem();
        if (selectedGroup == null)
            return;
        Group.setGroupName(selectedGroup);  // Set the group name for global access

        // Determine user role in the group
        boolean primaryAdmin = isUserPrimaryAdmin(selectedGroup);
        boolean admin = isUserAdmin(selectedGroup);

        // Determine the path to load based on admin status
        String path;
        if (primaryAdmin) {
            path = "/frontend/groupPAdmin.fxml";  // User is the primary admin
        } else if (admin) {
            path = "/frontend/groupAdmin.fxml";  // User is a regular admin
        } else {
            path = "/frontend/groupNUser.fxml";  // User is not an admin
        }

        // Load and switch to the corresponding scene
        try {
            Parent groupPage = FXMLLoader.load(getClass().getResource(path));
            Scene groupScene = new Scene(groupPage);

            // Get the current stage from the event source
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));

            // Set new scene and show the stage
            currentStage.setScene(groupScene);
            currentStage.setTitle("Group Feed");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();  // Print the exception stack trace for debugging
        }
    }

    private void openStoryPage(JSONArray stories) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/StoryPage.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            StoryPageController controller = loader.getController();

            stage.setTitle("Story Page");
            stage.show();
            controller.setStoryContent(stories);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isFileValid(String path) {
        return new File(path).exists();
    }

    protected void loadPosts() {

        PostFactory postFactory = PostFactory.getInstance();
        IContent contentCreation = postFactory.createPost();
        JSONArray posts = contentCreation.getNewsFeedContent(userId);


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
                    ILikeNotifications likeNotifications = new LikeNotifications(LoadNotifications.getInstance());
                    likeNotifications.createNotifications(userId,post.getString("contentId"),getCurrentTimestamp());
                    userLiked.set(true);
                }

                // Update the like button and label
                likeButton.setText(userLiked.get() ? "Unlike" : "Like");
                likesLabel.setText("Likes: " + finalLikedBy.length());

                // Save updated likes to the backend
                contentFiles.saveContent(posts, "data/posts.json");


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
                    contentCreation.addComment(post.getString("contentId"), comment);
                    ICommentNotifications commentNotifications = new CommentNotifications(LoadNotifications.getInstance());
                    commentNotifications.createNotifications(userId,post.getString("contentId"),getCurrentTimestamp());
                    commentField.clear();
                    System.out.println("Comment added to post: " + comment);
                }
            });

            Button viewCommentsButton = new Button("View Comments");
            viewCommentsButton.setOnAction(event -> {
                JSONArray comments = contentCreation.getCommentsById(post.getString("contentId"));

                if (comments != null) {
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
                }
            });

            HBox commentBox = new HBox(10, commentField, addCommentButton, viewCommentsButton);
            postBox.getChildren().add(commentBox);

            // Add the post box to the posts list view
            postsListView.getItems().add(postBox);
        }
    }

    @FXML
    private void onCreatePost() {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/frontend/post.fxml"));
            Scene loginScene = new Scene(loginPage);

            // Get current stage
            Stage currentStage = (Stage) postsListView.getScene().getWindow();
            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));

            // Set new scene and show the stage
            currentStage.setScene(loginScene);
            currentStage.setTitle("Create Post");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentTimestamp() {
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        return now.format(formatter);
    }
    @FXML
    private void onCreateStory() {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/frontend/story.fxml"));
            Scene loginScene = new Scene(loginPage);

            // Get current stage
            Stage currentStage = (Stage) postsListView.getScene().getWindow();
            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));

            // Set new scene and show the stage
            currentStage.setScene(loginScene);
            currentStage.setTitle("Create Story");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCreateGroup() {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/frontend/createGroup.fxml"));
            Scene loginScene = new Scene(loginPage);

            // Get current stage
            Stage currentStage = (Stage) postsListView.getScene().getWindow();
            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));

            // Set new scene and show the stage
            currentStage.setScene(loginScene);
            currentStage.setTitle("Create Group");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void onRefreshNewsfeed() {
        System.out.println("Refresh Newsfeed button clicked");
        postsListView.getItems().clear();
        loadPosts();
        loadMyGroupsList();
        loadSuggestions();
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

    public void onMessages(ActionEvent event) {
        try {
            Parent MessagesPage = FXMLLoader.load(getClass().getResource("/frontend/messages.fxml"));
            Scene MessagesScene = new Scene(MessagesPage);

            // Get current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));

            // Set new scene and show the stage
            currentStage.setScene(MessagesScene);
            currentStage.setTitle("Messages");
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

    public void navigateToNotifications(ActionEvent event) {
        try {
            // Load the Notifications page
            Parent notificationPage = FXMLLoader.load(getClass().getResource("/frontend/notifications.fxml"));
            Scene notificationScene = new Scene(notificationPage);

            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the icon (if not already added)
            if (currentStage.getIcons().isEmpty()) {
                currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));
            }

            // Set the new scene and update the stage
            currentStage.setScene(notificationScene);
            currentStage.setTitle("Notifications");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
