package frontend.newsFeed;

import backend.contentCreation.*;
import backend.friendship.*;
import backend.profile.*;
import backend.user.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FeedController {
    @FXML
    private HBox storiesBox;
    @FXML
    private ListView<String> friendsListView;
    @FXML
    private ListView<VBox> postsListView;

    @FXML
    private ScrollPane storiesScrollPane;

    public FriendShip friendShip= FriendShipFactory.createFriendShip();
    private final IFriendshipService friendShipService = friendShip.getFriendshipService();

    public FriendRequestServiceFactory factory = FriendRequestServiceFactory.getInstance();
    public FriendRequestService service = factory.createFriendRequestService();
    @FXML
    private ScrollPane postsScrollPane;
    private final ObservableList<String> friends = FXCollections.observableArrayList();
    private final String userId = User.getUserId();
    @FXML
    public void initialize() {
        loadStories();
        loadPosts();
        loadFriendsList();
        storiesScrollPane.setFitToWidth(true);

    }
    private void loadFriendsList() {
        // Example friends array

        List<String> friendsList = service.getFriendshipService().getFriendsWithStatus(userId);
        // Add friends to the ListView
        friendsListView.getItems().clear(); // Clear existing items (if any)
        friendsListView.getItems().addAll(friendsList); // Add all friends from the array
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

    private void loadPosts() {
        // Creating a mock JSONArray



        PostFactory postFactory = PostFactory.getInstance();
        IContent contentCreation = postFactory.createPost();
        JSONArray posts = contentCreation.getNewsFeedContent(userId);

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
            Stage currentStage =(Stage) postsListView.getScene().getWindow();
            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));

            // Set new scene and show the stage
            currentStage.setScene(loginScene);
            currentStage.setTitle("Create Post");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void onCreateStory() {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/frontend/story.fxml"));
            Scene loginScene = new Scene(loginPage);

            // Get current stage
            Stage currentStage =(Stage) postsListView.getScene().getWindow();
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
    private void onRefreshNewsfeed() {
        System.out.println("Refresh Newsfeed button clicked");
        postsListView.getItems().clear();
        loadPosts();
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
