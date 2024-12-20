package backend.notifications;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LoadNotifications implements ILoadNotifications{
    private static LoadNotifications instance;
    public LoadNotifications() {
    }
    public static synchronized LoadNotifications getInstance() {
        if (instance == null) {
            instance = new LoadNotifications();
        }
        return instance;
    }
    @Override
    public JSONArray LoadNotification(String filePath) {
        File file = new File(filePath);

        // Initialize file with an empty array if it doesn't exist
        if (!file.exists()) {
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write("[]");
                System.out.println("File initialized with an empty array.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileReader reader = new FileReader(file)) {
            return new JSONArray(new JSONTokener(reader));
        } catch (JSONException | IOException e) {
            System.err.println("Invalid JSON in file: " + filePath);
            e.printStackTrace();
            return new JSONArray(); // Return an empty array to prevent crashes
        }
    }
    @Override

    public void saveNotification(JSONArray newContent, String filePath) {
        File file = new File(filePath);

        // Read the existing notifications
        JSONArray existingContent = new JSONArray();
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                existingContent = new JSONArray(new JSONTokener(reader));
            } catch (JSONException | IOException e) {
                System.err.println("Error reading existing notifications: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Check for duplicates and merge newContent with existingContent
        JSONArray updatedContent = new JSONArray();
        for (int i = 0; i < newContent.length(); i++) {
            boolean isDuplicate = false;
            for (int j = 0; j < existingContent.length(); j++) {
                if (newContent.getJSONObject(i).getString("notificationId")
                        .equals(existingContent.getJSONObject(j).getString("notificationId"))) {
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) {
                updatedContent.put(newContent.getJSONObject(i));
            }
        }

        // Add all existing notifications
        for (int i = 0; i < existingContent.length(); i++) {
            updatedContent.put(existingContent.getJSONObject(i));
        }

        // Save the updated notifications back to the file
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(updatedContent.toString(4)); // 4 for pretty printing
            System.out.println("Notifications saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving notifications: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
