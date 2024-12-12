package backend.notifications;

import org.json.JSONArray;

public interface IPostNotifications {
    public JSONArray getNotification();
    public void createNotifications(String author, String receiver,String timestamp);
}
