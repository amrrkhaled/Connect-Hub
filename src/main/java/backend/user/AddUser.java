package backend.user;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class AddUser implements IAddUser {
    private int usersNumber = 1;
    private final String filePath = "data/users.json";
    private final ILoadUsers userLoader;

    public AddUser(ILoadUsers userLoader) {

        this.userLoader = userLoader;
        JSONArray usersArray = userLoader.loadUsers();
        this.usersNumber = usersArray.length() + 1;
    }

    public void addUser(String username, String password, String email, String dob) {
        // Create a new user JSON object
        JSONObject newUser = new JSONObject();
        newUser.put("userId", "U" + usersNumber);
        newUser.put("username", username);
        newUser.put("password", PasswordUtils.hashPassword(password));
        newUser.put("email", email);
        newUser.put("dateOfBirth", dob);
        newUser.put("status", "offline");
        JSONArray usersArray = userLoader.loadUsers();
        usersArray.put(newUser);
        // Write the updated array back to the file
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(usersArray.toString(4));
            System.out.println("Successfully written to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
