package frontend.notifications;

import backend.Groups.*;
import backend.contentCreation.IContent;
import backend.contentCreation.PostFactory;
import backend.friendship.FriendShip;
import backend.friendship.FriendShipFactory;
import backend.user.UserFactory;
import backend.user.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static backend.user.User.getUserId;
import static backend.user.User.userId;

public class PostViewController {
    @FXML
    public Label contentLabel;
    @FXML
    private Label userIdLabel;
    @FXML
    private VBox imageContainer;
    @FXML
    private List<String> selectedImagePaths = new ArrayList<>();
    private ListView<String> postDetailsListView;
    public FriendShip friendShip = FriendShipFactory.createFriendShip();
    PostFactory postFactory = PostFactory.getInstance();
    IContent contentCreation = postFactory.createPost();
    static JSONObject post = new JSONObject();
    IStorageHandler storageHandler = new StorageHandler();
    ILoadGroups loadGroups = LoadGroups.getInstance(storageHandler);
    private IGroupRepository groupRep = new GroupRepository(loadGroups,storageHandler);


    public void initialize() {
        String contentId = post.getString("contentId");
        String timestamp = post.getString("timestamp");
        JSONObject postbyId = groupRep.getPostById(contentId);
        System.out.println(postbyId);
        String postText = "Content: " + postbyId.getString("content");
        String postTimestamp = "Posted at: " + postbyId.getString("timestamp");
       setPost(postbyId);
    }

    public static void setPostDetails(JSONObject postDetails) {
        post = postDetails;
        System.out.println(postDetails);
    }

    /// ////////////////////////////////////////////////////////////


    // Assuming you already have a method to set story content from a JSONObject
    public void setPost(JSONObject post) {
        // Clear existing content
        imageContainer.getChildren().clear();

        if (post != null) {
            // Extract story details
            String userId = post.optString("authorId", "Unknown User");
            String caption = post.optString("content", "");
            String timestamp = post.optString("timestamp", "");
            JSONArray images = post.optJSONArray("images");
            contentLabel.setText(caption);
            System.out.println(post);
            // Set the user ID label
            String userName = friendShip.getUserRepository().getUsernameByUserId(userId);  // Assuming you have a method to get the username by userId
            userIdLabel.setText("User: " + userName);

            // Load images for the story
            if (images != null) {
                for (int i = 0; i < images.length(); i++) {
                    String imagePath = images.optString(i, null); // Get image path
                    if (imagePath != null && !imagePath.isEmpty()) {
                        try {
                            Image image = new Image("file:" + imagePath);
                            ImageView imageView = new ImageView(image);
                            imageView.setFitWidth(300); // Adjust width
                            imageView.setPreserveRatio(true); // Maintain aspect ratio
                            imageContainer.getChildren().add(imageView);
                        } catch (Exception e) {
                            System.err.println("Error loading image: " + imagePath + ". " + e.getMessage());
                        }
                    }
                }
            }
        }
    }

    /// ////////////////////////////////////////////////
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
