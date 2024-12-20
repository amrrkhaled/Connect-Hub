package backend.notifications;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LoadNotifications implements ILoadNotifications {
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
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(newContent.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
