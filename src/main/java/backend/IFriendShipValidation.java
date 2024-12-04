package backend;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IFriendShipValidation {
    public boolean checkDuplicates(String userId1, String userId2, JSONArray friendships);
}
