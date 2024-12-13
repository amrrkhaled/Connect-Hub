package backend.notifications;

import org.json.JSONArray;

public interface IGroupNotifications {
    public void createNotifications(String groupName,String notificationName);
    public JSONArray getNotification();

}
