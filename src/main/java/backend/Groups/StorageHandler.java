package backend.Groups;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StorageHandler implements IStorageHandler {
    public JSONObject loadData(String filePath) {
        File file = new File(filePath);
        // Create the file if it doesn't exist and initialize with an empty object
        if (!file.exists() || file.length() == 0) {
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write("{}");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileReader reader = new FileReader(file)) {
            return new JSONObject(new JSONTokener(reader));
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject(); // Return an empty object on error
        }
    }
    public JSONArray loadDataAsArray(String filePath) {
        File file = new File(filePath);
        // Create the file if it doesn't exist and initialize with an empty array
        if (!file.exists() || file.length() == 0) {
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write("[]");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileReader reader = new FileReader(file)) {
            return new JSONArray(new JSONTokener(reader));
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray(); // Return an empty array on error
        }
    }


    public void saveData(JSONObject joinRequests , String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(joinRequests.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveDataAsArray(JSONArray data, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
