package backend.notifications;

import org.json.JSONArray;
import org.json.JSONObject;

public class LikeNotifications implements ILikeNotifications{
    private final ILoadNotifications loadNotifications;
    private final String FILEPATH = "data/likeNotifications.json";
    public LikeNotifications(ILoadNotifications loadNotifications) {
        this.loadNotifications = loadNotifications;

    }
    @Override
    public JSONArray getNotification() {  /////wil return JSON array
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
    public void createNotifications(String userId, String contentId,String timestamp){
        JSONObject newNotification = new JSONObject();
        JSONArray notifications = loadNotifications.LoadNotification(FILEPATH);
        newNotification.put("notificationId", "NL" + (notifications.length() + 1));
        newNotification.put("authorId", userId);
        newNotification.put("contentId", contentId);
        newNotification.put("timestamp", timestamp);
        notifications.put(newNotification);
        System.out.println("new notification "+newNotification);
        loadNotifications.saveNotification(notifications, FILEPATH);
    }
}
