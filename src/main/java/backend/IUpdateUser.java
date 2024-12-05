package backend;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IUpdateUser {
    public void updateUser(String username, JSONArray usersArray, JSONObject user);

    public void saveUsers(JSONArray usersArray);
}
