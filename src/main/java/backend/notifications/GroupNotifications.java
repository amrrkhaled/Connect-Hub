package backend.notifications;

import backend.Groups.ILoadGroups;
import org.json.JSONArray;
import org.json.JSONObject;

public class GroupNotifications implements IGroupNotifications{
    ILoadNotifications loadNotifications;
    private final String FILEPATH = "data/groupNotifications.json";
    public GroupNotifications(ILoadNotifications loadNotifications) {
        this.loadNotifications = loadNotifications;
    }
    public void createNotifications(String groupName, String notificationName){
        JSONObject newNotification = new JSONObject();
        JSONArray notifications = loadNotifications.LoadNotification(FILEPATH);
        newNotification.put("groupName", groupName);
        newNotification.put("notification", notificationName);

        notifications.put(newNotification);
        loadNotifications.saveNotification(notifications,FILEPATH);
    }
    public JSONArray getNotification(){
        JSONArray notifications = loadNotifications.LoadNotification(FILEPATH);
        JSONArray groupNotifications = new JSONArray();
        if (notifications == null) {
            System.err.println("Error: Notifications file could not be loaded.");
            return groupNotifications;
        }
        for(int i = 0; i < notifications.length(); i++){
            try{
                JSONObject notification = notifications.getJSONObject(i);
                groupNotifications.put(notification);
            }catch (Exception e) {
                System.err.println("Error processing Notification at index " + i + ": " + e.getMessage());
            }
        }
        return groupNotifications;
    }
}
