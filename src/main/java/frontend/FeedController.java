package frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class FeedController {
    @FXML
    private HBox storiesBox;

    @FXML
    private ListView<VBox> postsListView;


    @FXML
    private ScrollPane storiesScrollPane;

    @FXML
    private ScrollPane postsScrollPane;
    // Initialize the newsfeed
    @FXML
    public void initialize() {
        loadStories();
        loadPosts();
        // Ensure the ScrollPanes resize with the window
        storiesScrollPane.setFitToWidth(true);
        postsScrollPane.setFitToWidth(true);
    }
/// //////////////////////////////////////////////////////////////////////////////
    private void loadStories() {
        // Example stories
        String[][] storyTitles = {
                {"omar", "C:/Users/franc/Desktop/me.jpg"},
                {"ahmed", null},
                {"ana", "C:/Users/franc/Desktop/me.jpg"}, {"sayed", null}
        };

        for (String[] title : storyTitles) {
            String imagePath = title[0];
            String text = title[1];
            VBox storyItem = new VBox();
            storyItem.setSpacing(5);
            storyItem.setStyle("-fx-alignment: center;");

            Circle storyCircle = new Circle(30); // Radius = 30

            ImageView imageView = null;
            if (title[1] != null) {
                // Load the image
                imageView = new ImageView(new Image("file:" + title[1]));
                imageView.setFitWidth(60); // Circle diameter
                imageView.setFitHeight(60); // Circle diameter
                imageView.setPreserveRatio(false); // Force fit
                imageView.setClip(new Circle(30, 30, 30)); // Clip to match circle
                storyItem.getChildren().add(imageView);
                imageView.setOnMouseClicked(event -> openStoryPage(imagePath));
            } else {
                storyCircle.setFill(Color.LIGHTBLUE);
                storyCircle.setStroke(Color.DARKBLUE);
                storyCircle.setStrokeWidth(2);
                storyItem.getChildren().add(storyCircle);
            }

            // Add story label
            Text storyLabel = new Text(title[0]);
            storyLabel.setStyle("-fx-font-size: 12px; -fx-fill: #333333;");
            storyItem.getChildren().add(storyLabel);

            // Add the story to the HBox
            storiesBox.getChildren().add(storyItem);
            storyCircle.setOnMouseClicked(event -> openStoryPage(imagePath));

        }
    }
    ///  ////////////////////////////////////////////////////////////////////////////////////////////////
    private void openStoryPage(String imagePath) {
        try {
            // Load the FXML for the Story Page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("storyPage.fxml"));
            Parent storyPageRoot = loader.load();

            // Get the controller of StoryPage
            StoryPageController storyPageController = loader.getController();

            // Pass the image path to the StoryPageController
            storyPageController.setStoryImage(imagePath);

            // Replace the current scene's center with the StoryPage
            BorderPane root = (BorderPane) storiesScrollPane.getScene().getRoot();
            root.setCenter(storyPageRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void loadPosts() {
        // Example posts

        String[][] posts = {

                {"Post 1 for omar: This is a text-only post","C:\\Users\\franc\\Desktop\\me.jpg" },
                {"Post 2: This is a text-only post", null},
                {"Post 3: This is a 222222222222222222222222222222222-only post", null},

        };
List<String> imagePaths = new ArrayList<>();
imagePaths.add("C:\\Users\\franc\\Desktop\\me.jpg");
        imagePaths.add("C:\\Users\\franc\\Desktop\\me.jpg");

        imagePaths.add("C:\\Users\\franc\\Desktop\\me.jpg");

        for (String[] post : posts) {
            VBox postBox = new VBox();
            postBox.setSpacing(10);

            if (post[0] != null) { // Text exists
                Text postText = new Text(post[0]);
                postText.setStyle("-fx-font-size: 14px;");
                postBox.getChildren().add(postText);
            }
            if (post[1] != null) { // Image path exists
                ImageView imageView = new ImageView(new Image(post[1]));
                imageView.setFitWidth(200);
                imageView.setPreserveRatio(true);
                postBox.getChildren().add(imageView);

            }



            postsListView.getItems().add(postBox);
        }
    }
/// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    private void onCreatePost() {
        // Placeholder for creating a post
        System.out.println("Create Post button clicked");
    }

    @FXML
    private void onRefreshNewsfeed() {
        // Placeholder for refreshing the newsfeed
        System.out.println("Refresh Newsfeed button clicked");
        postsListView.getItems().clear();
        loadPosts();
    }

    public void onHome(ActionEvent actionEvent) {
        System.out.println("home button clicked");
    }


    public void onProfile(ActionEvent actionEvent) {
        System.out.println("Profile button clicked");

    }

    public void onFriends(ActionEvent actionEvent) {
        System.out.println("Friends button clicked");

    }

    public void onLogout(ActionEvent actionEvent) {
        System.out.println("Logout button clicked");

    }
}