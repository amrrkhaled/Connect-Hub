package backend.notifications;

import org.json.JSONArray;

public interface IFriendNotifications {
    public void createNotifications(String sender, String receiver);
    public JSONArray getNotification();
}
