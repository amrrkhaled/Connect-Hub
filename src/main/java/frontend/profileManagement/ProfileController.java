package frontend.profileManagement;

import backend.profile.*;
import backend.user.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

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

    private final String userId = User.getUserId();



    @FXML
    public void initialize(){
        ILoadProfiles loadProfiles = new LoadProfiles();
        IUpdateProfile updateProfile = new UpdateProfile();
        ProfileManager manager = new ProfileManager(loadProfiles,userId,updateProfile);
        JSONObject profile =manager.findProfileByUserId(userId);

        String pP = (profile.get("ProfilePicture").toString());
        String cP = (profile.get("CoverPhoto").toString());
        String bio = (profile.get("Bio").toString());

        profilePhotoImageView.setImage(new Image(new File(pP).toURI().toString()));
        coverPhotoImageView.setImage(new Image(new File(cP).toURI().toString()));
        bioTextArea.clear();
        bioTextArea.appendText(bio);

    }
    @FXML
    public void changeProfilePhoto() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            Image ProfileImage = new Image(selectedFile.toURI().toString());
            ILoadProfiles loadProfiles = new LoadProfiles();
            IUpdateProfile updateProfile = new UpdateProfile();
            ProfileManager manager = new ProfileManager(loadProfiles,userId,updateProfile);
            manager.updateProfilePhoto(selectedFile.getAbsolutePath());

             profilePhotoImageView.setImage(ProfileImage);

        }
    }

    @FXML
    public void changeCoverPhoto() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            Image coverImage = new Image(selectedFile.toURI().toString());
            ILoadProfiles loadProfiles = new LoadProfiles();
            IUpdateProfile updateProfile = new UpdateProfile();
            ProfileManager manager = new ProfileManager(loadProfiles,userId,updateProfile);
            manager.updateCoverPhoto(selectedFile.getAbsolutePath());
            coverPhotoImageView.setImage(coverImage);
        }
    }

    @FXML
    public void editBio() {
        String newBio = bioTextArea.getText();
        ILoadProfiles loadProfiles = new LoadProfiles();
        IUpdateProfile updateProfile = new UpdateProfile();
        ProfileManager manager = new ProfileManager(loadProfiles,userId,updateProfile);
        manager.updateBio(newBio);
        bioTextArea.clear();
        bioTextArea.appendText(newBio);
    }


    @FXML
    public void updatePassword() {
        String newPassword = passwordField.getText();
        ILoadUsers loadUsers = new LoadUsers();
        IUpdateUser updateUser = new UpdateUser();
        PasswordUtils passwordUtils = new PasswordUtils(loadUsers,updateUser);

        passwordUtils.updatePasswordHashForUser(userId, newPassword);
    }

    @FXML
    public void loadPosts() {
        postsListView.getItems().addAll("Post 1", "Post 2", "Post 3");
    }

    @FXML
    public void goToHome(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/home.fxml"));
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