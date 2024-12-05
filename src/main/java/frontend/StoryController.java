package frontend;

import backend.ContentFiles;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import backend.IContent;
import backend.Story;
import javafx.scene.control.*;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StoryController {

    @FXML
    private TextArea contentArea;
    @FXML
    private Button uploadImage;
    @FXML
    private Button createStory;

    @FXML
    private VBox imageContainer;
    private List<String> selectedImagePaths = new ArrayList<>(); // Holds paths of selected images


    @FXML
    public void initialize() {
        uploadImage.setOnAction(event -> handleUploadImages());
        createStory.setOnAction(event -> handleCreateStory());
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
    private void handleCreateStory() {
        String content = contentArea.getText();

        if (content.isEmpty()) {
            showAlert("Please enter content and upload an image.");
            return;
        }


        showSuccess("Story shared successfully!");
        IContent contentCreation = new Story(new ContentFiles());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        contentCreation.createContent("authorId",content, now.format(formatter), selectedImagePaths);
        //navigateToNewsFeed();
    }

//    private void navigateToNewsFeed() {
//        try {
//            Parent newsFeedPage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("newsfeed.fxml")));
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
