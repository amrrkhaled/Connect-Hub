package frontend.contentCreation;

import backend.contentCreation.ContentFiles;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import backend.contentCreation.IContent;
import backend.contentCreation.Post;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PostController {

    @FXML
    private TextArea contentArea;
    @FXML
    private Button uploadImage;
    @FXML
    private Button createPost;
    @FXML
    private ImageView imageView;
    @FXML
    private VBox imageContainer; // Container for displaying images
    //    private final String userId = User.getUserId();
    private final String userId = "U1";
    private List<String> selectedImagePaths = new ArrayList<>(); // Holds paths of selected images


    @FXML
    public void initialize() {
        uploadImage.setOnAction(event -> handleUploadImages());
        createPost.setOnAction(event -> handleCreatePost());
    }
    @FXML
    private void handleUploadImages() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());
        if (selectedFiles != null) {
            selectedImagePaths.clear();
            imageContainer.getChildren().clear();

            for (File file : selectedFiles) {
                selectedImagePaths.add(file.getAbsolutePath());
                Image image = new Image("file:" + file.getAbsolutePath());

                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(300);
                imageView.setPreserveRatio(true);
                imageContainer.getChildren().add(imageView);
            }
        }
    }

    @FXML
    private void handleCreatePost() {
        String content = contentArea.getText();

        if (content.isEmpty()) {
            showAlert("Please enter content and upload an image.");
            return;
        }


        showSuccess("Post created successfully!");
        IContent contentCreation = new Post(new ContentFiles());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        contentCreation.createContent(userId,content, now.format(formatter), selectedImagePaths);
        //navigateToNewsFeed();
    }

//    private void navigateToNewsFeed() {
//        try {
//            Parent newsFeedPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/frontend/newsfeed.fxml")));
//            Scene newsFeedScene = new Scene(newsFeedPage);
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            stage.setScene(newsFeedScene);
//            stage.setTitle("News Feed");
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//            showAlert("Error navigating to the Newsfeed page.");
//        }
//    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
