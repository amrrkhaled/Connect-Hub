package backend.user;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserRepository implements IUserRepository {
    ILoadUsers loadUsers;
    private static UserRepository instance;

    private UserRepository(ILoadUsers loadUsers) {
        // Private constructor to prevent direct instantiation
        this.loadUsers = loadUsers;
    }

    public static synchronized UserRepository getInstance(ILoadUsers loadUsers) {
        if (instance == null) {
            instance = new UserRepository(loadUsers);
        }
        return instance;
    }
    public JSONArray getUsersArray() {
        return loadUsers.loadUsers();
    }

    public String getUsernameByUserId(String userId) {
        JSONArray usersArray = getUsersArray();
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            try {
                if (user.getString("userId").equals(userId)) {
                    return user.getString("username");
                }
            } catch (Exception e) {
                System.out.println("Error retrieving data for user at index " + i + ": " + e.getMessage());
            }
        }
        return null;
    }

    public String getStatusByUserId(String userId) {
        JSONArray usersArray = getUsersArray();
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            try {
                if (user.getString("userId").equals(userId)) {
                    return user.getString("status");
                }
            } catch (Exception e) {
                System.out.println("Error retrieving data for user at index " + i + ": " + e.getMessage());
            }
        }
        return null;
    }

    public JSONObject findUserByUsername(String username) {
        JSONArray usersArray = getUsersArray();
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            try {
                if (user.getString("username").equals(username)) {
                    return user;
                }
            } catch (Exception e) {
                System.out.println("Error retrieving data for user at index " + i + ": " + e.getMessage());
            }
        }
        return null;
    }
    public String findUserIdByUsername(String username) {
        JSONArray usersArray = getUsersArray();
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            try {
                if (user.getString("username").equals(username)) {
                    return user.getString("userId");
                }
            } catch (Exception e) {
                System.out.println("Error retrieving data for user at index " + i + ": " + e.getMessage());
            }
        }
        return null;
    }
}
