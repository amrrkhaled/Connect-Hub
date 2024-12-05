package backend;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserRepository {
    ILoadUsers loadUsers;
    public UserRepository(ILoadUsers loadUsers){
        loadUsers = loadUsers;
    }
    public String getUsernameByUserId(String userId) {
        JSONArray usersArray = loadUsers.loadUsers();
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            if (user.getString("userId").equals(userId)) {
                return user.getString("username");
            }
        }
        return null;
    }
    public String getStatusByUserId(String userId){
        JSONArray usersArray = loadUsers.loadUsers();
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            if (user.getString("userId").equals(userId)) {
                return user.getString("status");
            }
        }
        return null;
    }

}
