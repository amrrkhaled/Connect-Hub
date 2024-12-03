package backend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ContentFiles implements IContentFiles{


    @Override
    public JSONArray loadContent(String filePath){

        File file = new File(filePath);

        try (FileReader reader = new FileReader(file)) {
            return new JSONArray(new JSONTokener(reader));
        } catch (JSONException | IOException e) {
            System.err.println("Invalid JSON in file: " + filePath);
            e.printStackTrace();
            return new JSONArray(); // Return an empty array to prevent crashes
        }
    }
    @Override
    public void saveContent(JSONArray content , String filePath){

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(content.toString(4));
            System.out.println("Successfully written to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
