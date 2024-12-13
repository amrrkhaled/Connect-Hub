package backend.notifications;

import org.json.JSONArray;

public interface IGroupNotifications {
    public void createNotifications(String groupId, String postId, String timestamp);
    public JSONArray getNotification();

}
