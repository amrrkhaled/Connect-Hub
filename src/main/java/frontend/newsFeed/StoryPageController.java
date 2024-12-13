package frontend.newsFeed;

import backend.friendship.FriendShip;
import backend.friendship.FriendShipFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    private VBox imageContainer; // For holding all stories
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private FriendShip friendShip = FriendShipFactory.createFriendShip();
    public void setStoryContent(JSONArray stories) {
        // Clear the container to avoid duplicating images
        imageContainer.getChildren().clear();

        if (stories.length() > 0) {
            // Get the user ID from the first story and set the username once
            JSONObject firstStory = stories.optJSONObject(0);
            if (firstStory != null) {
                String userId = firstStory.optString("authorId", "Unknown User");
                String userName = friendShip.getUserRepository().getUsernameByUserId(userId);
                userIdLabel.setText("User: " + userName);
            }
        }

        // Loop through the array of stories
        for (int j = 0; j < stories.length(); j++) {
            JSONObject story = stories.optJSONObject(j);  // Get the individual story object

            if (story != null) {
                // Create a new VBox for each story
                VBox storyVBox = new VBox(10); // Spacing between elements in each story's VBox
                storyVBox.setStyle("-fx-padding: 10; -fx-background-color: #ffffff; -fx-background-radius: 10px;");

                // Extract story details
                String userId = story.optString("authorId", "Unknown User");
                String caption = story.optString("content", "");
                String timestamp = story.optString("timestamp", "");
                JSONArray images = story.optJSONArray("images");

                // Set the user ID and caption for each story
                Label captionLabel = new Label(caption);
                captionLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333333; -fx-font-weight: bold;");

                // Calculate and display remaining time for each story
                String timeRemaining = calculateTimeRemaining(timestamp);
                Label timeRemainingLabel = new Label("Time Remaining: " + timeRemaining);
                timeRemainingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #000dff; -fx-font-weight: bold;");

                // Add labels to the storyVBox
                storyVBox.getChildren().addAll(captionLabel, timeRemainingLabel);

                // Load and display images for each story
                if (images != null) {
                    for (int i = 0; i < images.length(); i++) {
                        String imagePath = images.optString(i, null); // Get image path or null
                        if (imagePath != null && !imagePath.isEmpty()) {
                            try {
                                Image image = new Image("file:" + imagePath);
                                ImageView imageView = new ImageView(image);
                                imageView.setFitWidth(300); // Adjust width as needed
                                imageView.setPreserveRatio(true); // Maintain aspect ratio
                                storyVBox.getChildren().add(imageView);
                            } catch (Exception e) {
                                System.err.println("Error loading image: " + imagePath + ". " + e.getMessage());
                            }
                        }
                    }
                }

                // Add the storyVBox to the imageContainer
                imageContainer.getChildren().add(storyVBox);
            }
        }
    }

    private String calculateTimeRemaining(String timestamp) {
        try {
            LocalDateTime storyTime = LocalDateTime.parse(timestamp, FORMATTER);
            LocalDateTime now = LocalDateTime.now();
            long hoursLeft = 24 - ChronoUnit.HOURS.between(storyTime, now);

            if (hoursLeft > 0) {
                return hoursLeft + " hours remaining";
            } else {
                return "Story expired";
            }
        } catch (Exception e) {
            System.err.println("Error parsing timestamp: " + timestamp + ". " + e.getMessage());
            return "Time unavailable";
        }
    }
}
