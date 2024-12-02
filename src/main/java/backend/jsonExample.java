package backend;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

public class jsonExample {
    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "John");
        jsonObject.put("age", 30);
        jsonObject.put("city", "New York");

        String filePath = "data/test.json";

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(jsonObject.toString(4)); // The '4' specifies indentation level
            System.out.println("Successfully written to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
