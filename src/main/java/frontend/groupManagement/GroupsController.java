package frontend.groupManagement;

import backend.Groups.*;
import backend.contentCreation.IContent;
import backend.contentCreation.PostFactory;
import backend.contentCreation.StoryFactory;
import backend.friendship.*;
import backend.profile.ProfileManager;
import backend.profile.ProfileManagerFactory;
import backend.user.User;
import backend.user.UserFactory;
import backend.user.UserManager;
import frontend.newsFeed.StoryPageController;
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
import javafx.scene.control.TextArea;
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
import java.util.ArrayList;
import java.util.List;

public class GroupsController {

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
    public FriendShip friendShip= FriendShipFactory.createFriendShip();
    public String GROUPNAME = Group.getGroupName();
    IStorageHandler storageHandler = new StorageHandler();

    ILoadGroups loadGroups = LoadGroups.getInstance(storageHandler);
    // Creating instances of controllers
    GroupManager groupManager = new GroupManager(loadGroups);

    @FXML
    public void initialize() {
        loadPosts(GROUPNAME);
        loadMembersList(GROUPNAME);
        GroupName.setText(GROUPNAME);
        loadGroupInfo(GROUPNAME);
    }



    public void loadGroupInfo(String name){
        JSONArray Groups = storageHandler.loadDataAsArray(groupsFilePath);  // Load the JSON array
        for (int i = 0; i < Groups.length(); i++) {
            JSONObject group = Groups.getJSONObject(i);
            String image=null;
            // Check if the group name matches the one passed as a parameter
            if (group.getString("groupName").equals(name)) {
                String description = group.getString("description");
                if(group.has("groupImage"))
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
  private void loadMembersList(String name){

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

        NormalUserController user = new NormalUserController(loadGroups,storageHandler);
        user.leaveGroup(GROUPNAME,userId);
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
