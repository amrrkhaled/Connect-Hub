package frontend;

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

public class HomeController {

    @FXML
    private ScrollPane postsScrollPane;

    @FXML
    private VBox postsContainer;

    @FXML
    public void initialize() {
        String[] texts = {
                "Post 1: Beautiful view!",
                "Post 2: Delicious meals!",
                "Post 3: A new design!"
        };

        String[][] imageUrls = {
                {"https://images.unsplash.com/photo-1537769060554-b41893a07504?q=80&w=1935&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"},
                {"https://media.istockphoto.com/id/588595864/photo/steaming-mixed-vegetables-in-the-wok-asian-style-cooking.jpg?s=612x612&w=0&k=20&c=NZWe4QUwFmEqPAwHa3s0u3Zak6JjlRm36gMgmXx8roA=", "https://media.istockphoto.com/id/1472680285/photo/healthy-meal-with-grilled-chicken-rice-salad-and-vegetables-served-by-woman.jpg?s=612x612&w=0&k=20&c=E4Y94oLIj8lXYk0OovBhsah3s_sC--WF95xPDvbJPlU="},
                {"https://media.istockphoto.com/id/1422735620/vector/the-graphic-design-element-and-abstract-geometric-background-with-isometric-vector-blocks.jpg?s=612x612&w=0&k=20&c=50x5ASx6AVhZcOzoNnj8z7FlWG2T3Ls_Ov3AEOgcz_k="}
        };

        for (int i = 0; i < texts.length; i++) {
            VBox post = createPost(texts[i], imageUrls[i]);
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
            ImageView imageView = new ImageView(new Image(imageUrl));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("profile.fxml"));
            Scene profileScene = new Scene(loader.load());
            Stage stage = (Stage) postsContainer.getScene().getWindow();
            stage.setScene(profileScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
