package frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class StoryPageController {

    @FXML
    private Label userIdLabel;

    @FXML
    private Label captionLabel;

    @FXML
    private Label timeRemainingLabel;

    @FXML
    private ListView<HBox> imageListView;
    @FXML
    private VBox imageContainer;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void setStoryContent(JSONObject story) {
        // Extract story details
        String userId = story.optString("authorId", "Unknown User");
        String caption = story.optString("content", "");
        String timestamp = story.optString("timestamp", "");
        JSONArray images = story.optJSONArray("images");

        // Set the user ID and caption
        userIdLabel.setText("User: " + userId);
        captionLabel.setText(caption);

        // Calculate and display remaining time
        calculateTimeRemaining(timestamp);

        // Clear the container to avoid duplicating images
        imageContainer.getChildren().clear();

        // Load and display all images
        if (images != null) {
            for (int i = 0; i < images.length(); i++) {
                String imagePath = images.optString(i, null); // Get image path or null
                if (imagePath != null && !imagePath.isEmpty()) {
                    try {
                        System.out.println("Loading image: " + imagePath);  // Debugging line
                        Image image = new Image("file:" + imagePath);
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(300); // Adjust width as needed
                        imageView.setPreserveRatio(true); // Maintain aspect ratio
                        imageContainer.getChildren().add(imageView);
                    } catch (Exception e) {
                        System.err.println("Error loading image: " + imagePath + ". " + e.getMessage());
                    }
                }
            }
        }
    }



    private void calculateTimeRemaining(String timestamp) {
        try {
            LocalDateTime storyTime = LocalDateTime.parse(timestamp, FORMATTER);
            LocalDateTime now = LocalDateTime.now();
            long hoursLeft = 24 - ChronoUnit.HOURS.between(storyTime, now);

            if (hoursLeft > 0) {
                timeRemainingLabel.setText(hoursLeft + " hours remaining");
            } else {
                timeRemainingLabel.setText("Story expired");
            }
        } catch (Exception e) {
            System.err.println("Error parsing timestamp: " + timestamp + ". " + e.getMessage());
            timeRemainingLabel.setText("Time unavailable");
        }
    }



}
