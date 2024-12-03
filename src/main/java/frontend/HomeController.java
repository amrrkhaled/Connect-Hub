package frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    private ScrollPane postsScrollPane;

    @FXML
    private VBox postsContainer;

    @FXML
    public void initialize() {
        String[] texts = {
                "Post 1: Beautiful scenery!",
                "Post 2: Had a great day!",
                "Post 3: Check out this new design!"
        };

        String[] imageUrls = {
                "https://ibb.co/6b93JMF",
                "https://ibb.co/6b93JMF",
                "https://ibb.co/6b93JMF"
        };

        for (int i = 0; i < texts.length; i++) {
            VBox post = createPost(texts[i], imageUrls[i]);
            postsContainer.getChildren().add(post);
        }
    }

    private VBox createPost(String text, String imageUrl) {
        VBox postBox = new VBox(10);
        postBox.setStyle("-fx-border-color: gray; -fx-padding: 10; -fx-background-color: #f9f9f9;");
        postBox.setPrefWidth(400);

        ImageView imageView = new ImageView(new Image(imageUrl));
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        Text postText = new Text(text);
        postText.setStyle("-fx-font-size: 14px;");

        postBox.getChildren().addAll(imageView, postText);
        return postBox;
    }
    @FXML
    public void goToProfile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("profile.fxml"));
            Scene profileScene = new Scene(loader.load());
            Stage stage = (Stage) postsContainer.getScene().getWindow();
            stage.setScene(profileScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
