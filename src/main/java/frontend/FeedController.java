package frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

import java.io.File;
import java.io.IOException;

public class FeedController {
    @FXML
    private HBox storiesBox;

    @FXML
    private ListView<VBox> postsListView;

    @FXML
    private ScrollPane storiesScrollPane;

    @FXML
    private ScrollPane postsScrollPane;

    @FXML
    public void initialize() {
        loadStories();
        loadPosts();
        storiesScrollPane.setFitToWidth(true);
    }

    private void loadStories() {
        String[][] storyTitles = {
                {"omar", "images/image1.png"},
                {"ahmed", "images/image2.png"},
                {"ana", "images/image3.png"},
                {"hiiii", null}
        };

        for (String[] story : storyTitles) {
            String userName = story[0];
            String imagePath = story[1];
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
                imageView.setOnMouseClicked(event -> openStoryPage(imagePath, userName));
            } else {
                storyCircle.setFill(Color.LIGHTBLUE);
                storyCircle.setStroke(Color.DARKBLUE);
                storyCircle.setStrokeWidth(2);
                storyItem.getChildren().add(storyCircle);
                storyCircle.setOnMouseClicked(event -> openStoryPage(null, userName));
            }

            Text storyLabel = new Text(userName);
            storyLabel.setStyle("-fx-font-size: 12px; -fx-fill: #333333;");
            storyItem.getChildren().add(storyLabel);

            storiesBox.getChildren().add(storyItem);
        }
    }

    private void openStoryPage(String imagePath, String caption) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StoryPage.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            StoryPageController controller = loader.getController();
            controller.setStoryContent(imagePath, caption);

            stage.setTitle("Story Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isFileValid(String path) {
        return new File(path).exists();
    }

    private void loadPosts() {
        String[][] posts = {
                {"Post 1 for Omar: This is a text-only post", "images/image1.png,images/image2.png"},
                {"Post 2: Another post with images", "images/image3.png,images/image4.png"},
                {"Post 3: Another text-only post", null}
        };

        for (String[] post : posts) {
            VBox postBox = new VBox();
            postBox.setSpacing(10);

            if (post[0] != null) {
                Text postText = new Text(post[0]);
                postText.setStyle("-fx-font-size: 14px;");
                postBox.getChildren().add(postText);
            }

            if (post[1] != null) {
                String[] imagePaths = post[1].split(",");
                HBox imageContainer = new HBox();
                imageContainer.setSpacing(10);
                imageContainer.setStyle("-fx-padding: 10;");

                for (String path : imagePaths) {
                    if (isFileValid(path)) {
                        Image image = new Image("file:" + path.trim());
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(200);
                        imageView.setPreserveRatio(true);
                        imageContainer.getChildren().add(imageView);
                    } else {
                        System.out.println("Could not load image: " + path);
                    }
                }

                postBox.getChildren().add(imageContainer);
            }

            postsListView.getItems().add(postBox);
        }
    }

    @FXML
    private void onCreatePost() {
        System.out.println("Create Post button clicked");
    }

    @FXML
    private void onRefreshNewsfeed() {
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
