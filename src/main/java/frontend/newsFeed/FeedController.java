package frontend.newsFeed;

import backend.contentCreation.ContentFiles;
import backend.contentCreation.IContent;
import backend.contentCreation.Post;
import backend.friendship.FriendShip;
import backend.friendship.FriendShipFactory;
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

public class FeedController {
    @FXML
    private HBox storiesBox;
    @FXML
    private ListView<String> friendsListView;
    @FXML
    private ListView<VBox> postsListView;

    @FXML
    private ScrollPane storiesScrollPane;

    @FXML
    private ScrollPane postsScrollPane;
    public FriendShip friendShip= FriendShipFactory.createFriendShip();
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

        List<String> friendsList = friendShip.getManager().getFriendsWithStatus(userId);
        // Add friends to the ListView
        friendsListView.getItems().clear(); // Clear existing items (if any)
        friendsListView.getItems().addAll(friendsList); // Add all friends from the array
    }
    private void loadStories() {
//        String[][] storyTitles = {
//                {"omar", "images/image1.png"},
//                {"ahmed", "images/image2.png"},
//                {"ana", "images/image3.png"},
//
//        };
//        JSONArray stories = new JSONArray();
//
//        // Add mock posts
//        JSONObject story1 = new JSONObject();
//        story1.put("images", new JSONArray(Arrays.asList("images\\image4.png", "images\\image5.png")));
//        story1.put("contentId", "P1");
//        story1.put("authorId", "amr");
//        story1.put("content", "s1 i am sharing,Hello i am sharing,Hello i am sharing,Hello i am sharing,Hello i am sharing,Hello i am sharing");
//        story1.put("timestamp", "2024-12-05 01:32:22");
//
//        JSONObject story2 = new JSONObject();
//        story2.put("images", new JSONArray(Arrays.asList("images\\image4.png", "images\\image5.png")));
//        story2.put("contentId", "P2");
//        story2.put("authorId", "ahmed");
//        story2.put("content", "s2,Hello i am sharing,Hello i am sharing,Hello i am sharing,Hello i am sharing,Hello i am sharing,Hello i am sharing");
//        story2.put("timestamp", "2024-12-04 01:32:22");
//        JSONObject story3 = new JSONObject();
//        story3.put("images", new JSONArray(Arrays.asList("images\\image4.png", "images\\image5.png")));
//        story3.put("contentId", "P2");
//        story3.put("authorId", "omar");
//        story3.put("content", "s3 sharing,Hello i am sharing,Hello i am sharing,Hello i am sharing,Hello i am sharing,Hello i am sharing");
//        story3.put("timestamp", "2024-12-04 01:32:22");
//
//        // Add posts to the JSONArray
//        stories.put(story1);
//        stories.put(story2);
//        stories.put(story3);

        IContent contentManager = new Post(new ContentFiles());
        JSONArray stories = contentManager.getNewsFeedContent(userId);

        int count = 0;
        for (int i = 0; i < stories.length(); i++){
            JSONObject storyObject = stories.getJSONObject(i); // Get the JSON object at index i
            String id = storyObject.getString("authorId");

            String userName = friendShip.getUserRepository().getUsernameByUserId(id);
            ProfileManager manager = ProfileManagerFactory.getInstance().createProfileManager(id);
            JSONObject profile =manager.getRepo().findProfileByUserId(id);

            String imagePath = profile.getString("ProfilePicture");
            VBox storyItem = new VBox();
            storyItem.setSpacing(5);
            storyItem.setStyle("-fx-alignment: center;");

            Circle storyCircle = new Circle(30);
            int finalCount = count;

            if (imagePath != null && isFileValid(imagePath)) {
                ImageView imageView = new ImageView(new Image("file:" + imagePath));
                imageView.setFitWidth(60);
                imageView.setFitHeight(60);
                imageView.setPreserveRatio(false);
                imageView.setClip(new Circle(30, 30, 30));
                storyItem.getChildren().add(imageView);
                imageView.setOnMouseClicked(event -> openStoryPage(stories.getJSONObject(finalCount)));
            } else {
                storyCircle.setFill(Color.LIGHTBLUE);
                storyCircle.setStroke(Color.DARKBLUE);
                storyCircle.setStrokeWidth(2);
                storyItem.getChildren().add(storyCircle);
                storyCircle.setOnMouseClicked(event -> openStoryPage(stories.getJSONObject(finalCount)));
            }

            Text storyLabel = new Text(userName);
            storyLabel.setStyle("-fx-font-size: 12px; -fx-fill: #333333;");
            storyItem.getChildren().add(storyLabel);

            storiesBox.getChildren().add(storyItem);
            count ++;
       }








//        for (String[] story : storyTitles) {
//            String userName = story[0];
//            String imagePath = story[1];
//            VBox storyItem = new VBox();
//            storyItem.setSpacing(5);
//            storyItem.setStyle("-fx-alignment: center;");
//
//            Circle storyCircle = new Circle(30);
//            int finalCount = count;
//
//            if (imagePath != null && isFileValid(imagePath)) {
//                ImageView imageView = new ImageView(new Image("file:" + imagePath));
//                imageView.setFitWidth(60);
//                imageView.setFitHeight(60);
//                imageView.setPreserveRatio(false);
//                imageView.setClip(new Circle(30, 30, 30));
//                storyItem.getChildren().add(imageView);
//                imageView.setOnMouseClicked(event -> openStoryPage(stories.getJSONObject(finalCount)));
//            } else {
//                storyCircle.setFill(Color.LIGHTBLUE);
//                storyCircle.setStroke(Color.DARKBLUE);
//                storyCircle.setStrokeWidth(2);
//                storyItem.getChildren().add(storyCircle);
//                storyCircle.setOnMouseClicked(event -> openStoryPage(stories.getJSONObject(finalCount)));
//            }
//
//            Text storyLabel = new Text(userName);
//            storyLabel.setStyle("-fx-font-size: 12px; -fx-fill: #333333;");
//            storyItem.getChildren().add(storyLabel);
//
//            storiesBox.getChildren().add(storyItem);
//            count ++;
//        }
    }

    private void openStoryPage(JSONObject story) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/StoryPage.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            StoryPageController controller = loader.getController();

            stage.setTitle("Story Page");
            stage.show();
//            JSONObject story = new JSONObject();

            // Add the images array
//            JSONArray images = new JSONArray();
//            images.put("images\\image4.png");
//            images.put("images\\image5.png");
//            images.put("images\\image4.png");
//            images.put("images\\image5.png");
//            images.put("images\\image3.png");
//            story.put("images", images);
//
//            story.put("authorName", "authorId");
//            story.put("content",caption);
//            story.put("timestamp", "2024-12-05 01:32:22");
            controller.setStoryContent(story);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isFileValid(String path) {
        return new File(path).exists();
    }

    private void loadPosts() {
        // Creating a mock JSONArray



        IContent contentManager = new Post(new ContentFiles());
        JSONArray posts = contentManager.getNewsFeedContent(userId);

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
}
