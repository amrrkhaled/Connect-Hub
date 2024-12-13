package backend.notifications;

import org.json.JSONArray;

import java.util.List;

public interface IFriendNotifications {
    public JSONArray getNotification();
    public void createNotifications(String notification,String sender, String receiver , String timestamp);
    public List<String> getNotificationMessages();
}
