package backend.notifications;

import backend.Groups.*;
import backend.user.ILoadUsers;
import backend.user.IUserRepository;
import backend.user.LoadUsers;
import backend.user.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PostNotification implements IPostNotifications {
    private final ILoadNotifications loadNotifications;
    private final String FILEPATH = "data/PostNotifications.json";

    public PostNotification(ILoadNotifications loadNotifications) {
        this.loadNotifications = loadNotifications;
        ;
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
    public void createNotifications(String authorId, String contentId, String timestamp) {
        JSONObject newNotification = new JSONObject();
        JSONArray notifications = loadNotifications.LoadNotification(FILEPATH);
        newNotification.put("authorId", authorId);
        newNotification.put("contentId", contentId);
        newNotification.put("timestamp", timestamp);
        notifications.put(newNotification);
        System.out.println("new notification "+newNotification);
        loadNotifications.saveNotification(notifications, FILEPATH);

    }
}
