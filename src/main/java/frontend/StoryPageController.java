package frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StoryPageController {

    @FXML
    private Label captionLabel;

    @FXML
    private ImageView storyImage;

    /**
     * Set the content of the story page.
     *
     * @param imagePath The path to the story image.
     * @param caption   The caption text for the story.
     */
    public void setStoryContent(String imagePath, String caption) {
        // Set the caption
        if (caption != null && !caption.isEmpty()) {
            captionLabel.setText(caption);
        } else {
            captionLabel.setText("No caption available.");
        }

        // Load and display the image
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Image image = new Image("file:" + imagePath);
                storyImage.setImage(image);
            } catch (Exception e) {
                System.out.println("Could not load image: " + imagePath);
                storyImage.setImage(null);
            }
        } else {
            storyImage.setImage(null);
        }
    }
}
