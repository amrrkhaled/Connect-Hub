package backend.user;

import org.json.JSONObject;

public interface Validation {
    boolean doesUsernameExist(String username);

    public boolean doesEmailExist(String email);

    public boolean isPasswordValid(String password, String storedPasswordHash);

    public JSONObject findUserByUsername(String username);

}

