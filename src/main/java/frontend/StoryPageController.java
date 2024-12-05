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
 public void initialize() {
//     setStoryContent(imagepath,caption);
 }
    public void setStoryContent(String imagePath, String caption) {
        // Set the image if the path is valid
        if (imagePath != null && !imagePath.isEmpty()) {
            storyImageView.setImage(new Image("file:" + imagePath));
        } else {
            storyImageView.setImage(null); // Remove image if none provided
        }

        // Set the caption
        storyCaptionLabel.setText(caption != null ? caption : ""); // Show empty text if caption is null
    }
}


