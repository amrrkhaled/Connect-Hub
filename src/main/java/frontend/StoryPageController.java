package frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StoryPageController {

    @FXML
    private ImageView storyImageView;

    @FXML
    private Label storyCaptionLabel;

    public void setStoryImage(String imagePath) {
        Image image = new Image("file:" + imagePath);
        storyImageView.setImage(image);
    }

    public void setStoryContent(String imagePath) {



     // Set the image if the path is valid
        if (imagePath != null && !imagePath.isEmpty()) {
            storyImageView.setImage(new Image("file:" + imagePath));
        } else {
            storyImageView.setImage(null); // Remove image if none provided
        }


}


