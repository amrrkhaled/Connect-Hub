package backend.notifications;

import org.json.JSONArray;
import org.json.JSONObject;

public interface ISM {
    public JSONArray getNotification(String author);
    void createNotifications(String id1, String id2);
}
