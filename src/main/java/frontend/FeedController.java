package frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
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
    private HBox imageContainer;
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
//       postsScrollPane.setFitToWidth(true);
    }
/// //////////////////////////////////////////////////////////////////////////////
    private void loadStories() {
        // Example stories
        String[][] storyTitles = {
                {"omar", "images\\image1.png"},
                {"ahmed", "images\\image1.png"},
                {"ana", "images\\image1.png"}
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
                imageView.setOnMouseClicked(event -> openStoryPage(imagePath,text));
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
            storyCircle.setOnMouseClicked(event -> openStoryPage(imagePath, text));


        }
    }
    ///  ////////////////////////////////////////////////////////////////////////////////////////////////
    private void openStoryPage(String imagePath, String text) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StoryPage.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

//            StoryPageController controller = loader.getController();
//            controller.setStory(imagePath, text);

            stage.setTitle("Story Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void loadPosts() {
        String[][] posts = {
                {"Post 1 for Omar: This is a text-only post", "images\\image1.png,images\\image2.png,images\\image2.png,images\\image2.png,images\\image2.png,images\\image2.png,images\\image2.png,images\\image2.png,images\\image2.png"},
                {"Post 2: Another post with images", "images\\image3.png,images\\image4.png"},
                {"Post 3: Another text-only post", "images\\image5.png"}
        };

        for (String[] post : posts) {
            VBox postBox = new VBox();
            postBox.setSpacing(10);

            // Add post text
            if (post[0] != null) {
                Text postText = new Text(post[0]);
                postText.setStyle("-fx-font-size: 14px;");
                postBox.getChildren().add(postText);
            }

            // Add images for the post
            if (post[1] != null) {
                String[] imagePaths = post[1].split(","); // Split image paths by comma
                HBox imageContainer = new HBox();
                imageContainer.setSpacing(10);
                imageContainer.setStyle("-fx-padding: 10;");

                // Load images into the container
                for (String path : imagePaths) {
                    try {
                        Image image = new Image("file:" + path.trim());
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(200);
                        imageView.setPreserveRatio(true);
                        imageContainer.getChildren().add(imageView);
                    } catch (Exception e) {
                        System.out.println("Could not load image: " + path);
                    }
                }



                // Add the ScrollPane with images to the postBox
                postBox.getChildren().add(imageContainer);
            }

            // Add the postBox to the ListView
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