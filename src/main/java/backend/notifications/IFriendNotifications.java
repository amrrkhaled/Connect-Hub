package backend.notifications;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public interface IFriendNotifications {
    public JSONArray getNotificationsForUser(String userId);

    public void createNotifications(String sender, String receiver, String timestamp);

    public void removeNotification(String userId, String senderId);

    public String getNotificationId(String userId, String senderId);
}