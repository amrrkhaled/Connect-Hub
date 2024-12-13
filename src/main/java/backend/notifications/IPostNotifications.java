package backend.notifications;

import org.json.JSONArray;

import java.util.List;

public interface IPostNotifications {
    public JSONArray getNotification();
    public void createNotifications(String authorId, String contentId,String timestamp);
//    public List<String> getNotificationMessages(String userId);
}
