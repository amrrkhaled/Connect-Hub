package backend.user;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserManager {
    private final IAddUser addUser;
    private final ILoadUsers loadUsers;
    private final Validation validation;
    private final IUpdateUser updateUser;
    private final IUserRepository userRepository;
    public UserManager(IAddUser addUser, ILoadUsers loadUsers, Validation validation , IUpdateUser updateUser, IUserRepository userRepository) {
        this.addUser = addUser;
        this.loadUsers = loadUsers;
        this.validation = validation;
        this.updateUser = updateUser;
        this.userRepository = userRepository;
    }

    public IAddUser getAddUser() {
        return addUser;
    }

    public ILoadUsers getLoadUsers() {
        return loadUsers;
    }
    public IUpdateUser getUpdateUser() {
        return updateUser;
    }
    public IUserRepository getUserRepository() {
        return userRepository;
    }
    public Validation getValidation() {
        return validation;
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
            String storedPasswordHash = IPasswordUtils.getStoredPasswordHashForUser(username, usersArray);
            if (validation.isPasswordValid(password, storedPasswordHash)) {
                JSONObject user = validation.findUserByUsername(username);
                user.put("status", "online");
                updateUser.updateUser(username,usersArray,user);
                updateUser.saveUsers(usersArray);
                return user.getString("userId");
            } else {
                return "Incorrect password";
            }
        } else {
            return "Username not found";
        }
    }



    public void logout(String userId) {

        String username = userRepository.getUsernameByUserId(userId);
        JSONObject user = userRepository.findUserByUsername(username);
        JSONArray usersArray = loadUsers.loadUsers();
        user.put("status", "offline");
        updateUser.updateUser(username,usersArray,user);
        updateUser.saveUsers(usersArray);
    }
}
