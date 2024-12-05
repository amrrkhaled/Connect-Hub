package frontend.profileManagement;

import backend.contentCreation.ContentFiles;
import backend.contentCreation.IContent;
import backend.contentCreation.Post;
import backend.user.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class HomeController {

    @FXML
    private ScrollPane postsScrollPane;

    @FXML
    private VBox postsContainer;

//    private final String userId = User.getUserId();
    private final String userId = "U1";

    @FXML
    public void initialize() {
        String[] texts = {
                "Post 1: Beautiful view!",
                "Post 2: Delicious meals!"

        };

        String[][] imageUrls = {
                {"images/I1.png", "images/I2.png"},
                {"images/I3.png", "images/I4.png"}
        };
        IContent contentManager = new Post(new ContentFiles());
        JSONArray userPosts = contentManager.getUserContent(userId);
        System.out.println(userPosts);
        for (int i = 0; i < userPosts.length(); i++) {

            JSONArray postImages=userPosts.getJSONObject(i).getJSONArray("images");
            String[] imagePaths = new String[postImages.length()];
            for (int j = 0; j < postImages.length(); j++) {
                imagePaths[j] = postImages.getString(j);
            }

            String content = userPosts.getJSONObject(i).getString("content");
           VBox post = createPost(content, imagePaths);
           postsContainer.getChildren().add(post);
        }
    }

    private VBox createPost(String text, String[] imageUrls) {
        VBox postBox = new VBox(10);
        postBox.setStyle("-fx-border-color: gray; -fx-padding: 10; -fx-background-color: #f9f9f9;");
        postBox.setPrefWidth(400);

        Text postText = new Text(text);
        postText.setStyle("-fx-font-size: 14px;");

        HBox imagesBox = new HBox(10);
        for (String imageUrl : imageUrls) {
            ImageView imageView = new ImageView(new Image(new File(imageUrl).toURI().toString()));
//            System.out.println(new File(imageUrl).toURI().toString());
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setPreserveRatio(true);
            imagesBox.getChildren().add(imageView);
        }

        postBox.getChildren().addAll(postText, imagesBox);
        return postBox;
    }

    @FXML
    public void goToProfile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/profile.fxml"));
            Scene profileScene = new Scene(loader.load());
            Stage stage = (Stage) postsContainer.getScene().getWindow();
            stage.setScene(profileScene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
