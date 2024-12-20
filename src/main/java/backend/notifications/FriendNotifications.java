package backend.notifications;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FriendNotifications implements IFriendNotifications {
    private final ILoadNotifications loadNotifications;
    private final String FILEPATH = "data/FriendNotifications.json";
    private int usersNotificationsNumber = 1;

    public FriendNotifications(ILoadNotifications loadNotifications) {

        this.loadNotifications = loadNotifications;
    }

    @Override
    public JSONArray getNotificationsForUser(String userId) {  /////wil return JSON array
        // load all notifications
        JSONArray notifications = loadNotifications.LoadNotification(FILEPATH);
        JSONArray userNotifications = new JSONArray();
        //check the ones for this user only
        if (notifications == null) {
            System.err.println("Error: Notifications file could not be loaded.");
            return null; // Return empty array if no content
        }
        for (int i = 0; i < notifications.length(); i++) {
            try {

                JSONObject notification = notifications.getJSONObject(i);
                if (notification.getString("receiverId").equals(userId)) {
                    userNotifications.put(notification);
                }

            } catch (Exception e) {
                System.err.println("Error processing Notification at index " + i + ": " + e.getMessage());
            }
        }
        return userNotifications;
    }

    public String getNotificationId(String userId, String senderId) {
        loadNotifications.LoadNotification(FILEPATH);
        JSONArray notifications = getNotificationsForUser(userId);
        if (notifications == null) {
            System.err.println("Error: Notifications file could not be loaded.");
            return null;
        }
        for (int i = 0; i < notifications.length(); i++) {
            if (notifications.getJSONObject(i).getString("receiverId").equals(userId) &&
                    notifications.getJSONObject(i).getString("senderId").equals(senderId)) {
                return notifications.getJSONObject(i).getString("notificationId");
            }
        }
        return null;
    }

    public void removeNotification(String userId, String senderId) {
        // Load the notifications for the given user
        JSONArray notifications = getNotificationsForUser(userId);
        if (notifications == null) {
            System.err.println("Error: Notifications file could not be loaded.");
            return;
        }

        // Iterate backwards to safely remove items without affecting the indices
        for (int i = notifications.length() - 1; i >= 0; i--) {
            JSONObject notification = notifications.getJSONObject(i);

            // Check if the current notification matches the userId and senderId
            if (notification.getString("receiverId").equals(userId) && notification.getString("senderId").equals(senderId)) {
                System.out.println("Removing notification with ID: " + notification.getString("notificationId"));
                // Remove the notification
                notifications.remove(i);

                // Save the updated notifications
                loadNotifications.saveNotification(notifications, FILEPATH);
                break; // Exit after the first matching notification is removed
            }
        }
    }


    @Override
    public void createNotifications(String sender, String receiver, String timestamp) {
        JSONObject newNotification = new JSONObject();
        JSONArray notifications = loadNotifications.LoadNotification(FILEPATH);
        newNotification.put("notificationId", "NF" + (notifications.length() + 1));
        newNotification.put("senderId", sender);
        newNotification.put("receiverId", receiver);
        newNotification.put("timestamp", timestamp);
        notifications.put(newNotification);
        loadNotifications.saveNotification(notifications, FILEPATH);
    }
}
