package backend.notifications;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class FriendNotifications implements IFriendNotifications {
    private final ILoadNotifications loadNotifications;
    private final String FILEPATH = "data/FriendNotifications.json";


    public FriendNotifications(ILoadNotifications loadNotifications) {
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
    public void createNotifications(String sender, String receiver){
        JSONObject newNotification = new JSONObject();
        JSONArray notifications = loadNotifications.LoadNotification(FILEPATH);
        newNotification.put("sender", sender);
        newNotification.put("receiver", receiver);
        notifications.put(newNotification);
        loadNotifications.saveNotification(notifications,FILEPATH);
    }
}
