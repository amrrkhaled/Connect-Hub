package frontend.groupManagement;

import backend.Groups.*;
import backend.user.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class editPostController implements Initializable {
    private Stage previousStage; // A reference to the previous stage

    @FXML
    private TextArea contentArea;
    @FXML
    private Button uploadImage;
    @FXML
    private Button createPost;
    @FXML
    private Button back;

    @FXML
    private VBox imageContainer; // Container for displaying images
    private final String userId = User.getUserId();
    private List<String> selectedImagePaths = new ArrayList<>(); // Holds paths of selected images
    IStorageHandler storageHandler = new StorageHandler();
    public String GROUPNAME = Group.getGroupName();
    ILoadGroups loadGroups = LoadGroups.getInstance(storageHandler);
    static String postId;
    @FXML
    public void initialize() {
        uploadImage.setOnAction(event -> handleUploadImages());
        createPost.setOnAction(event -> handleCreatePost());
        back.setOnAction(event -> returnBack());
   
    }

    public static void setPostId(String contentId) {
        postId = contentId;
    }

    public void setPreviousStage(Stage stage) {
        this.previousStage = stage;
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
    boolean isUserPrimaryAdmin(String name) {
        // Load the group details by name
        JSONObject group = loadGroups.loadGroupByName(name);

        // Check if the "primaryAdminId" exists and if it matches the current user's ID
        if (group != null && group.has("primaryAdminId") && group.get("primaryAdminId").equals(userId)) {
            return true;
        } else {
            return false;
        }
    }

    boolean isUserAdmin(String name) {
        // Load the group details by name
        JSONObject group = loadGroups.loadGroupByName(name);

        // Check if the "admins" array exists
        if (group != null && group.has("admins")) {
            JSONArray admins = group.getJSONArray("admins");

            // Loop through the admins array to check if the current user is an admin
            for (int i = 0; i < admins.length(); i++) {
                if (admins.optString(i).equals(userId)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void returnBack() {
        boolean primaryAdmin = isUserPrimaryAdmin(GROUPNAME);
        boolean admin = isUserAdmin(GROUPNAME);

        // Determine the path to load based on admin status
        String path;
        if (primaryAdmin) {
            path = "/frontend/groupPAdmin.fxml";  // User is the primary admin
        } else if (admin) {
            path = "/frontend/groupAdmin.fxml";  // User is a regular admin
        } else {
            path = "/frontend/groups.fxml";  // User is not an admin
        }


        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource(path));
            Scene loginScene = new Scene(loginPage);

            // Get current stage
            Stage currentStage =(Stage) imageContainer.getScene().getWindow();
            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/frontend/icon.png")));

            // Set new scene and show the stage

            currentStage.setScene(loginScene);
            currentStage.setTitle("Group Feed");
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
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
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Assuming normalUserController.addPost does the actual saving of the post
        GeneralAdminController generalAdminController = new GeneralAdminController(loadGroups, storageHandler);
        generalAdminController.editPostContent(postId,content);
        generalAdminController.editPostImages(postId,selectedImagePaths);
        // Navigate back after creating the post
        returnBack();
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
