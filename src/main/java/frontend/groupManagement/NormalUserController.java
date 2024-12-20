package frontend.groupManagement;

import backend.Groups.*;
import backend.friendship.*;
import backend.user.User;
import backend.user.UserFactory;
import backend.user.UserManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicBoolean;

public class NormalUserController {

    @FXML
    public ImageView groupPhoto;
    @FXML
    public TextArea groupDescription;
    @FXML
    protected ListView<String> membersListView;
    @FXML
    protected ListView<VBox> postsListView;

    @FXML
    private Label GroupName;

    @FXML
    private ScrollPane postsScrollPane;
    private final ObservableList<String> friends = FXCollections.observableArrayList();
    private final String userId = User.getUserId();
    private final String postsFilePath = "data/groupsPosts.json";
    private final String membersFilePath = "data/group_members.json";
    private final String groupsFilePath = "data/groups.json";
    public FriendShip friendShip = FriendShipFactory.createFriendShip();
    public String GROUPNAME = Group.getGroupName();
    IStorageHandler storageHandler = new StorageHandler();

    ILoadGroups loadGroups = LoadGroups.getInstance(storageHandler);
    // Creating instances of controllers
    GroupManager groupManager = new GroupManager(loadGroups);


    // Create facade instance
    GroupManagementFacade facade = new GroupManagementFacade(loadGroups, storageHandler);

    @FXML
    public void initialize() {
        loadPosts(GROUPNAME);
        loadMembersList(GROUPNAME);
        GroupName.setText(GROUPNAME);
        loadGroupInfo(GROUPNAME);
    }


    public void loadGroupInfo(String name) {
        JSONArray Groups = storageHandler.loadDataAsArray(groupsFilePath);  // Load the JSON array
        for (int i = 0; i < Groups.length(); i++) {
            JSONObject group = Groups.getJSONObject(i);
            String image = null;
            // Check if the group name matches the one passed as a parameter
            if (group.getString("groupName").equals(name)) {
                String description = group.getString("description");
                if (group.has("groupImage"))
                    image = group.getString("groupImage");
                System.out.println(image);
                // Set the group image to the ImageView (if the image exists)
                if (image != null && !image.isEmpty()) {
                    System.out.println(image);

                    groupPhoto.setImage(new Image("file:" + image)); // Use setImage instead of creating a new ImageView
                }
                // Set the group description to the TextArea
                groupDescription.setText(description);
            }
        }
    }

    private void loadMembersList(String name) {

        JSONArray myGroups = storageHandler.loadDataAsArray(membersFilePath);  // Load the JSON array
        List<String> groupMembers = new ArrayList<>();  // Initialize an empty list for group members

        for (int i = 0; i < myGroups.length(); i++) {
            JSONObject myGroup = myGroups.getJSONObject(i);  // Get the current group object
            // Check if the group name matches the one we're looking for
            if (myGroup.getString("groupName").equals(name)) {

                // Get the "members" array from the group
                JSONArray members = myGroup.optJSONArray("members");

                // If members is not null and is a JSONArray, process it
                if (members != null) {
                    // Iterate through the members array, which contains the user IDs
                    for (int j = 0; j < members.length(); j++) {
                        String memberId = members.optString(j);  // Retrieve the userId (assuming it's a string)
                        if (memberId != null && !memberId.isEmpty() && !memberId.equals(userId)) {
                            System.out.println(userId + memberId);
                            String userName = friendShip.getUserRepository().getUsernameByUserId(memberId);
                            groupMembers.add(userName);  // Add the member's ID to the list
                        }
                    }
                }
                break;
            }
        }
        membersListView.getItems().clear(); // Clear existing items (if any)
        membersListView.getItems().addAll(groupMembers); // Add all friends from the array
    }


    protected boolean isFileValid(String path) {
        return new File(path).exists();
    }


    // Inside the loadPosts method
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
            // Load the post creation FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/groupPost.fxml"));
            Parent createPostPage = loader.load();

            // Get the current stage (the group feed stage)
            Stage currentStage = (Stage) postsListView.getScene().getWindow();
            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));

            // Get the controller of the post creation page and set the previous stage
            GroupPostController postController = loader.getController();
            postController.setPreviousStage(currentStage);  // Set the previous stage

            // Set up the scene and show it
            Scene createPostScene = new Scene(createPostPage);
            currentStage.setScene(createPostScene);
            currentStage.setTitle("Create Post");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onRefresh() {
        System.out.println("Refresh  button clicked");
        postsListView.getItems().clear();
        loadPosts(GROUPNAME);
        loadMembersList(GROUPNAME);
    }

    @FXML
    public void onLeaveGroup(ActionEvent event) {

        NormalUser user = new NormalUser(loadGroups, storageHandler);
        facade.userLeaveGroup(GROUPNAME, userId);
        onNewsFeed(event);
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

    public void onNewsFeed(ActionEvent event) {
        try {
            UserManager manager = UserFactory.getInstance().createUserManager();
            manager.logout(userId);
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
}
