package backend.Groups;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LoadGroups implements ILoadGroups {
    private final String filePath = "data/groups.json";

    private static LoadGroups instance;

    private LoadGroups() {
    }

    public static synchronized LoadGroups getInstance() {
        if (instance == null) {
            instance = new LoadGroups();
        }
        return instance;
    }

    @Override
    public JSONArray loadGroups() {
        File file = new File(filePath);
        // Initialize the file with an empty array if it doesn't exist or is empty
        if (!file.exists() || file.length() == 0) {
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write("[]");
                System.out.println("File initialized with an empty array.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Load the JSON array from the file
        try (FileReader reader = new FileReader(file)) {
            return new JSONArray(new JSONTokener(reader));
        } catch (JSONException | IOException e) {
            System.err.println("Invalid JSON in file: " + filePath);
            e.printStackTrace();
            return new JSONArray();
        }
    }
}
