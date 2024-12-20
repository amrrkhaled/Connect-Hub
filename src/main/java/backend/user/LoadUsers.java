package backend.user;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LoadUsers implements ILoadUsers {
    private final String filePath = "data/users.json";

    private static LoadUsers instance;

    public LoadUsers() {

    }

    public static synchronized LoadUsers getInstance() {
        if (instance == null) {
            instance = new LoadUsers();
        }
        return instance;
    }

    @Override
    public JSONArray loadUsers() {
        File file = new File(filePath);

        // Initialize file with an empty array if it doesn't exist

            if (file.length() == 0 || !file.exists()) {

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
}