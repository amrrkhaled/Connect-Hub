package backend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class UpdateUser implements IUpdateUser{
    public void updateUser(String username , JSONArray usersArray ,JSONObject user) {
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject existingUser = usersArray.getJSONObject(i);
            if (existingUser.getString("username").equals(username)) {
                usersArray.put(i, user);
                break;
            }
        }
    }

    public void saveUsers(JSONArray usersArray) {
        try (FileWriter file = new FileWriter("data/users.json")) {
            file.write(usersArray.toString(4)); // Indented for readability
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
