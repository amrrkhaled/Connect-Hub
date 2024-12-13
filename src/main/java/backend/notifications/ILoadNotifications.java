package backend.notifications;

import org.json.JSONArray;

public interface ILoadNotifications {
   public  JSONArray LoadNotification(String filepath);
   public void saveNotification(JSONArray content , String filePath);
}
