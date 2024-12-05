package backend.user;

import org.json.JSONObject;

public interface IUserRepository {
    public String getUsernameByUserId(String userId);
    public String getStatusByUserId(String userId);
    public JSONObject findUserByUsername(String username);
    public String findUserIdByUsername(String username);
}
