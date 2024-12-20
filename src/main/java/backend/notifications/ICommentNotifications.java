package backend.notifications;

import org.json.JSONArray;

public interface ICommentNotifications {
    public void createNotifications(String authorId, String contentId,String timestamp);
    public JSONArray getNotification();
}
