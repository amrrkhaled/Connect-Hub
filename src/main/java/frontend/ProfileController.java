package frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ProfileController {

    @FXML
    private ImageView profilePhotoImageView;
    @FXML
    private ImageView coverPhotoImageView;
    @FXML
    private TextArea bioTextArea;
    @FXML
    private TextField passwordField;
    @FXML
    private ListView<String> postsListView;
    @FXML
    private ListView<String> friendsListView;


    @FXML
    public void changeProfilePhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            Image profileImage = new Image(selectedFile.toURI().toString());
            profilePhotoImageView.setImage(profileImage);
        }
    }

    @FXML
    public void changeCoverPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            Image coverImage = new Image(selectedFile.toURI().toString());
            coverPhotoImageView.setImage(coverImage);
        }
    }

    @FXML
    public void editBio() {
        String newBio = bioTextArea.getText();
    }


    @FXML
    public void updatePassword() {
        String newPassword = passwordField.getText();
    }

    @FXML
    public void loadPosts() {
        postsListView.getItems().addAll("Post 1", "Post 2", "Post 3");
    }

    @FXML
    public void goToHome(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
            Scene homeScene = new Scene(loader.load());
            Stage stage = (Stage) bioTextArea.getScene().getWindow();
            stage.setScene(homeScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadFriendsList() {
        friendsListView.getItems().addAll("Omar (Online)", "Amr (Offline)", "Sameh (Online)");
    }
}
