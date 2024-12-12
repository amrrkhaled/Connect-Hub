package backend.notifications;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.print.DocFlavor;

public class Notification implements ISM {
    private final ILoadNotifications loadNotifications;
    private final String FILEPATH = "data/notifications.json";

    public Notification(ILoadNotifications loadNotifications) {
        this.loadNotifications = loadNotifications;
    }

    @Override
    public JSONArray getNotification(String author){  /////wil return JSON array

        JSONArray notifications = loadNotifications.LoadNotification();

        JSONArray userNotifications = new JSONArray();
        if (notifications == null) {
            System.err.println("Error: Notifications file could not be loaded.");
            return userNotifications; // Return empty array if no content
        }

        for (int i = 0; i < notifications.length(); i++) {
            try {
                JSONObject notification = notifications.getJSONObject(i);
                if (notification.getString("authorId").equals(author)) {
                    userNotifications.put(notification);
                }
            } catch (Exception e) {
                System.err.println("Error processing Notification at index " + i + ": " + e.getMessage());
            }
        }

        return userNotifications;
    }
    @Override
    public void createNotifications(String author, String receiver){
        JSONObject newNotification = new JSONObject();
        JSONArray notifications = loadNotifications.LoadNotification();
        newNotification.put("id1", author);
        newNotification.put("id2", receiver);
        notifications.put(newNotification);
        loadNotifications.saveContent(notifications,FILEPATH);
    }
}
