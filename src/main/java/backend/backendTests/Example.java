import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Arrays;

public class Example {
    public static void main(String[] args) {
        JSONObject sameh = new JSONObject();

        // Convert the list into a JSONArray
        JSONArray numbersArray = new JSONArray(Arrays.asList(1, 3, 5));

        // Add the JSONArray to the JSONObject
        sameh.put("numbers", numbersArray);

        // Print the JSON object
        System.out.println(sameh.toString());
    }
}
