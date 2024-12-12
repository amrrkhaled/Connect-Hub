package backend.notifications;

import org.json.JSONArray;
import org.json.JSONObject;

public class PostNotification implements IPostNotifications {
    private final ILoadNotifications loadNotifications;
    private final String FILEPATH = "data/PostNotifications.json";

    public PostNotification(ILoadNotifications loadNotifications) {
        this.loadNotifications = loadNotifications;
    }

    @Override
    public JSONArray getNotification(){  /////wil return JSON array
        JSONArray notifications = loadNotifications.LoadNotification(FILEPATH);
        JSONArray userNotifications = new JSONArray();
        if (notifications == null) {
            System.err.println("Error: Notifications file could not be loaded.");
            return userNotifications; // Return empty array if no content
        }
        for (int i = 0; i < notifications.length(); i++) {
            try {
                JSONObject notification = notifications.getJSONObject(i);
                    userNotifications.put(notification);
            } catch (Exception e) {
                System.err.println("Error processing Notification at index " + i + ": " + e.getMessage());
            }
        }

        return userNotifications;
    }
    @Override
    public void createNotifications(String author, String receiver,String timestamp){
        JSONObject newNotification = new JSONObject();
        JSONArray notifications = loadNotifications.LoadNotification(FILEPATH);
        newNotification.put("id1", author);
        newNotification.put("id2", receiver);
        newNotification.put("timestamp", timestamp);
        notifications.put(newNotification);
        loadNotifications.saveNotification(notifications,FILEPATH);

    }
}
