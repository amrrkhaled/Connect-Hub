package frontend.contentCreation;

import backend.contentCreation.ContentFiles;
import backend.user.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import backend.contentCreation.IContent;
import backend.contentCreation.Story;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StoryController {

    @FXML
    private TextArea contentArea;
    @FXML
    private Button uploadImage;
    @FXML
    private Button createStory;
    @FXML
    private Button back;

    @FXML
    private VBox imageContainer;
    private List<String> selectedImagePaths = new ArrayList<>(); // Holds paths of selected images
    private final String userId = User.getUserId();

    @FXML
    public void initialize() {
        uploadImage.setOnAction(event -> handleUploadImages());
        createStory.setOnAction(event -> handleCreateStory());
        back.setOnAction(event -> navigateToNewsFeed());

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

        contentCreation.createContent(userId,content, now.format(formatter), selectedImagePaths);
        navigateToNewsFeed();
    }

    private void navigateToNewsFeed() {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/frontend/NewsFeed.fxml"));
            Scene loginScene = new Scene(loginPage);

            // Get current stage
            Stage currentStage = (Stage) imageContainer.getScene().getWindow();
            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));

            // Set new scene and show the stage
            currentStage.setScene(loginScene);
            currentStage.setTitle("NewsFeed");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
