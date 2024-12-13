package backend.Groups;

import javafx.scene.image.Image;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class PrimaryAdmin extends GeneralAdminController {

    private final String groupsFilePath = "data/groups.json";
    private final String postsFilePath = "data/groupsPosts.json";
    private final String membersFile = "data/group_members.json";

    public PrimaryAdmin(ILoadGroups loadGroups, IStorageHandler storageHandler) {
        super(loadGroups, storageHandler);
    }

    public void addAdminToGroup(String name, String userId) {
        JSONObject group = loadGroups.loadGroupByName(name);
        if (group != null) {
            // Initialize "admins" if not already present
            if (!group.has("admins")) {
                group.put("admins", new JSONArray());
            }

            JSONArray admins = group.getJSONArray("admins");
            admins.put(userId); // Add user as an admin

            // Update the group in the groups array
            JSONArray groups = loadGroups.loadGroups();
            for (int i = 0; i < groups.length(); i++) {
                if (groups.getJSONObject(i).getString("groupName").equals(name)) {
                    groups.put(i, group);
                    break;
                }
            }

            storageHandler.saveDataAsArray(groups, groupsFilePath);
            System.out.println("Admin added successfully.");
        } else {
            System.out.println("Group not found.");
        }
    }

    public void updateDescription(String groupName , String description){
        JSONArray Groups = storageHandler.loadDataAsArray(groupsFilePath);  // Load the JSON array
        for (int i = 0; i < Groups.length(); i++) {
            JSONObject group = Groups.getJSONObject(i);

            // Check if the group name matches the one passed as a parameter
            if (group.getString("groupName").equals(groupName)) {
                group.put("description",description);
               Groups.put(i, group);
                break;
            }
        }
        storageHandler.saveDataAsArray(Groups, groupsFilePath);
    }

    public void removeAdmin(String name, String userId) {
        JSONObject group = loadGroups.loadGroupByName(name);
        if (group != null) {
            if (!group.has("admins")) {
                group.put("admins", new JSONArray());
            }

            JSONArray admins = group.getJSONArray("admins");
            boolean adminFound = false;

            for (int i = 0; i < admins.length(); i++) {
                if (admins.getString(i).equals(userId)) {
                    admins.remove(i);
                    adminFound = true;
                    break;
                }
            }

            if (adminFound) {
                // Update group with modified admins list
                group.put("admins", admins);
                JSONArray groups = loadGroups.loadGroups();
                for (int i = 0; i < groups.length(); i++) {
                    if (groups.getJSONObject(i).getString("groupName").equals(name)) {
                        groups.put(i, group);
                        break;
                    }
                }

                storageHandler.saveDataAsArray(groups, groupsFilePath);
                System.out.println("Admin removed successfully.");
            } else {
                System.out.println("Admin not found in the group.");
            }
        } else {
            System.out.println("Group not found.");
        }
    }



    public void deleteGroup(String name) {
        JSONArray groups = loadGroups.loadGroups();
        for (int i = 0; i < groups.length(); i++) {
            if (groups.getJSONObject(i).getString("groupName").equals(name)) {
                groups.remove(i);
                storageHandler.saveDataAsArray(groups, groupsFilePath);
                System.out.println("Group deleted successfully.");
                break;
            }
        }
        JSONArray groupMembers = storageHandler.loadDataAsArray(membersFile);  // Load the JSON array
for(int i = 0; i < groupMembers.length(); i++) {
    if (groupMembers.getJSONObject(i).getString("groupName").equals(name)) {
        groupMembers.remove(i);
        storageHandler.saveDataAsArray(groupMembers, groupsFilePath);
        System.out.println("Group deleted successfully.");
        break;
    }
}
    }
}
