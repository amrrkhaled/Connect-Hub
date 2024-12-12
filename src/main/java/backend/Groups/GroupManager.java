package backend.Groups;

import backend.SaveImage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class GroupManager {
    ILoadGroups loadGroups;
    private int numberOfGroups;
    private final String filePath = "data/groups.json";
    JSONArray groups;
    public GroupManager(ILoadGroups loadGroups) {
        this.loadGroups = loadGroups;
        groups = loadGroups.loadGroups();
        this.numberOfGroups = groups.length() + 1;
    }
    public void createGroup(String userId,String groupName , String groupDescription , String groupImage) {
        // Check if the groupId already exists
        for (int i = 0; i < groups.length(); i++) {
            JSONObject group = groups.getJSONObject(i);
            if (group.getString("groupName").equals(groupName)) {
                System.out.println("Group with Name " + groupName + " already exists.");
                return; // Do not create the group, as it already exists
            }
        }
        // If groupId doesn't exist, create the new group
        JSONObject group = new JSONObject();
        group.put("groupName", groupName);
        group.put("description", groupDescription);
        group.put("primaryAdminId", userId);
        group.put("groupId","G" +numberOfGroups);
        String gImage = null;
        try {
            gImage = SaveImage.saveImageToFolder(groupImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        group.put("groupImage",gImage);
        groups.put(group);
        // Write the updated array back to the file
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(groups.toString(4));
            System.out.println("Successfully written to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void deleteGroup(String groupId , String userId) {
        for (int i = 0; i < groups.length(); i++) {
            JSONObject group = groups.getJSONObject(i);
            if (group.getString("groupId").equals(groupId) && group.getString("PrimaryAdminId").equals(userId)) {
                groups.remove(i);
            }
        }
    }
}
