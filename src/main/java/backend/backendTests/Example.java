package backend.backendTests;

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

        System.out.println(sameh);

        JSONArray samehArray = sameh.getJSONArray("numbers");
        samehArray.remove(1);

        System.out.println(sameh);

    }
}
