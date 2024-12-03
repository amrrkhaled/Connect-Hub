package backend;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserManager {
    private final IAddUser addUser;
    private final ILoadUsers loadUsers;
    private final Validation validation;

    public UserManager(IAddUser addUser, ILoadUsers loadUsers, Validation validation) {
        this.addUser = addUser;
        this.loadUsers = loadUsers;
        this.validation = validation;
    }

    public String signup(String username, String password, String email, String dob) {
        if (validation.findUserByUsername(username) != null) {
            return "Username already exists";
        }
        if (validation.doesEmailExist(email)) {
            return "Email already exists";
        }
        addUser.addUser(username, password, email, dob);
        return "User created";
    }

    public String login(String username, String password) {
        if (validation.doesUsernameExist(username)) {
            JSONArray usersArray = loadUsers.loadUsers();
            String storedPasswordHash = PasswordUtils.getStoredPasswordHashForUser(username, usersArray);
            if (validation.isPasswordValid(password, storedPasswordHash)) {
                JSONObject user = validation.findUserByUsername(username);
                user.put("status", "online");
                return "Login successful!";
            } else {
                return "Incorrect password";
            }
        } else {
            return "Username not found";
        }

    }

    public void logout(String username) {
        JSONObject user = validation.findUserByUsername(username);
        user.put("status", "offline");
    }
}
