package backend;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IUserRepository {
    public String getUsernameByUserId(String userId);
    public String getStatusByUserId(String userId);
}
