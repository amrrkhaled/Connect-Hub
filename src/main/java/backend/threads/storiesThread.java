package backend.threads;

import backend.contentCreation.ContentFiles;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class storiesThread extends Thread {
    private final String FILEPATH = "data/stories.json";
    private ContentFiles contentFiles = new ContentFiles();

    @Override
    public void run() {
        // Load stories
        while (true) {
            try {
                // Simulate work in the main thread (e.g., UI updates)


                JSONArray stories = contentFiles.loadContent(FILEPATH);
                JSONArray activeStories = new JSONArray();

                LocalDateTime now = LocalDateTime.now(); // Current local time
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Time format

                // Loop through all the stories
                for (int i = 0; i < stories.length(); i++) {
                    try {
                        JSONObject story = stories.getJSONObject(i);
                        String storyTimestampStr = story.getString("timestamp"); // Assuming "timestamp" is a string

                        LocalDateTime storyTime = LocalDateTime.parse(storyTimestampStr, formatter);

                        // Calculate hours since the story was posted
                        long hoursSincePosted = Duration.between(storyTime, now).toHours();

                        // Check if story is within the last 24 hours
                        if (hoursSincePosted < 24) {
                            activeStories.put(story); // Add active story
                        }

                    } catch (Exception e) {
                        System.err.println("Error processing story at index " + i + ": " + e.getMessage());
                    }
                }

                // Save filtered active stories back to the file
                contentFiles.saveContent(activeStories, FILEPATH);
                LocalDateTime noww = LocalDateTime.now();
                DateTimeFormatter formatterr = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                System.out.println(noww.format(formatterr));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}