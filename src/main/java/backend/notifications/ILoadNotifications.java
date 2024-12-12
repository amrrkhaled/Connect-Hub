package backend.notifications;

import org.json.JSONArray;

public interface ILoadNotifications {
   public  JSONArray LoadNotification();
   public void saveContent(JSONArray content , String filePath);
}
