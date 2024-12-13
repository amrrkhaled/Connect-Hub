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

    public List<String> getNotificationMessages(String userId) {
        JSONArray notifications = loadNotifications.LoadNotification(FILEPATH);
        IStorageHandler storageHandler = new StorageHandler();
        ILoadGroups loadGroups = LoadGroups.getInstance(storageHandler);
        NormalUserController user = new NormalUserController(loadGroups, storageHandler);

        List<String> groupNamesForUser = user.getGroupsForUser(userId);
        System.out.println("groups for the user amr " + groupNamesForUser.toString());
        JSONArray postsForUser = new JSONArray();
        // Collect posts from all groups the user belongs to
        for (String groupName : groupNamesForUser) {
            JSONArray posts = loadGroups.loadGroupPostsByName(groupName);
            for (int i = 0; i < posts.length(); i++) {
                postsForUser.put(posts.getJSONObject(i));
            }
        }
        System.out.println("posts for the user amr " + postsForUser.toString());
        // Create a set of content IDs from notifications
        Set<String> notificationContentIds = new HashSet<>();
        for (int i = 0; i < notifications.length(); i++) {
            notificationContentIds.add(notifications.getJSONObject(i).getString("contentId"));
        }
        System.out.println("notificationContentIds for the user amr " + notificationContentIds.toString());
        // Generate new notifications for unseen posts
        List<String> messages = new ArrayList<>();
        for (int i = 0; i < postsForUser.length(); i++) {
            JSONObject post = postsForUser.getJSONObject(i);
            String contentId = post.getString("contentId").trim();
            System.out.println("myId to test " + contentId);
            System.out.println(!notificationContentIds.contains(contentId));
            if (!notificationContentIds.contains(contentId)) {
                System.out.println("I entered with " + contentId);
                // Create a new notification message
                String authorId = post.getString("authorId");
                String timestamp = post.getString("timestamp");
                ILoadUsers loadUsers = LoadUsers.getInstance();
                IUserRepository userRepository =  UserRepository.getInstance(loadUsers);
                String authorName = userRepository.getUsernameByUserId(authorId);
                String message = String.format("%s posted @: %s", authorName, timestamp);

                // Add to messages and optionally to notifications (if needed for persistence)
                messages.add(message);
            }
        }
        System.out.println("messages for the user amr " + messages.toString());
        // Add existing notifications for the user
        for (int i = 0; i < notifications.length(); i++) {
            JSONObject notification = notifications.getJSONObject(i);
            messages.add(notification.getString("notification"));

        }
        System.out.println("messages for the user amr returned" + messages.toString());
        return messages;
    }


    @Override
    public void createNotifications(String notification, String authorId, String contentId, String timestamp) {
        JSONObject newNotification = new JSONObject();
        JSONArray notifications = loadNotifications.LoadNotification(FILEPATH);
        newNotification.put("notification", notification);
        newNotification.put("authorId", authorId);
        newNotification.put("contentId", contentId);
        newNotification.put("timestamp", timestamp);
        notifications.put(newNotification);
        loadNotifications.saveNotification(notifications, FILEPATH);

    }
}
