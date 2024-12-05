package backend.friendship;

import org.json.JSONArray;

public interface IFriendShipValidation {
    public boolean checkDuplicates(String userId1, String userId2, JSONArray friendships);
}
