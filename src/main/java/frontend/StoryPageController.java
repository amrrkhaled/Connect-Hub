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
        String[][] stories = {
                {"storie 1 for Omar: This is a text-only post", "images\\image1.png,images\\image2.png,images\\image2.png,images\\image2.png,images\\image2.png,images\\image2.png,images\\image2.png,images\\image2.png,images\\image2.png"},
                {"storei 2: Another post with images", "images\\image3.png,images\\image4.png"},
                {"storei 3: Another text-only post", "images\\image5.png"}
        };
        
        if (imagePath != null && !imagePath.isEmpty()) {
            storyImageView.setImage(new Image("file:" + imagePath));
        } else {
            storyImageView.setImage(null); // Remove image if none provided
        }

        // Set the caption
        storyCaptionLabel.setText(caption != null ? caption : ""); // Show empty text if caption is null
    }
}


