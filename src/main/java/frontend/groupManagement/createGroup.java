package frontend.groupManagement;

import backend.Groups.*;
import backend.user.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class createGroup {
    private Stage previousStage; // A reference to the previous stage

    @FXML
    private TextArea Title; // Group name field
    @FXML
    private TextArea Description; // Group description field
    @FXML
    private Button uploadImage;
    @FXML
    private Button createGroup; // Create Group button
    @FXML
    private Button back;

    @FXML
    private VBox imageContainer; // Container for displaying images
    private final String userId = User.getUserId();
    private String selectedImagePaths = null;// Holds paths of selected images
    IStorageHandler storageHandler = new StorageHandler();
    ILoadGroups loadGroups = LoadGroups.getInstance(storageHandler);

    @FXML
    public void initialize() {
        uploadImage.setOnAction(event -> handleUploadImages());
        createGroup.setOnAction(event -> handleCreateGroup());
        back.setOnAction(event -> navigateToFeed());
    }

    @FXML
    private void handleUploadImages() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        // Open file chooser and allow only one file selection
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            imageContainer.getChildren().clear();  // Clear the image container
            selectedImagePaths = selectedFile.getAbsolutePath();

            Image image = new Image("file:" + selectedFile.getAbsolutePath());

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(300);
            imageView.setPreserveRatio(true);
            imageContainer.getChildren().add(imageView);  // Display the selected image
        }
    }

    @FXML
    private void handleCreateGroup() {
        GroupManager groupManager = new GroupManager(loadGroups);

        // Get group name and description from the TextArea
        String groupName = Title.getText().trim();
        String description = Description.getText().trim();

        // Check if group name or description is empty and no image is selected
        if (groupName.isEmpty() && description.isEmpty()) {
            showAlert("Please provide content or upload an image.");
            return;
        }

        // Create the group
        groupManager.createGroup(userId, groupName, description, selectedImagePaths);

        // Show success message
        showSuccess("Group created successfully!");

        // Navigate back to the feed
        navigateToFeed();
    }

    public void navigateToFeed() {
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
