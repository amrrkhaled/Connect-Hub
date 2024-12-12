package backend.notifications;

import org.json.JSONArray;

public interface IFriendNotifications {
    public void createNotifications(String sender, String receiver, String timestamp);
    public JSONArray getNotification();
}
