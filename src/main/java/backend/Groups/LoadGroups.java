package backend.Groups;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadGroups implements ILoadGroups {
    private static final String GROUPS_FILE_PATH = "data/groups.json";
    private static final String POSTS_FILE_PATH = "data/groupsPosts.json";
    private static final String MEMBERS_FILE_PATH = "data/group_members.json";
    private static final String REQUESTS_FILE_PATH = "data/groups_join_requests.json";
    private static volatile LoadGroups instance;
    private IStorageHandler storageHandler;
    private LoadGroups(IStorageHandler storageHandler) {
        this.storageHandler = storageHandler;
    }

    public static synchronized LoadGroups getInstance(IStorageHandler storageHandler) {
        if (instance == null) {
            instance = new LoadGroups(storageHandler);
        }
        return instance;
    }

    @Override
    public JSONArray loadGroups() {
        File file = new File(GROUPS_FILE_PATH);
        // Initialize the file with an empty array if it doesn't exist or is empty
        if (!file.exists() || file.length() == 0) {
            try (FileWriter fileWriter = new FileWriter(GROUPS_FILE_PATH)) {
                fileWriter.write("[]");
                System.out.println("File initialized with an empty array.");
            } catch (IOException e) {
                System.err.println("Failed to initialize file: " + GROUPS_FILE_PATH);
                e.printStackTrace();
            }
        }

        // Load the JSON array from the file
        try (FileReader reader = new FileReader(file)) {
            return new JSONArray(new JSONTokener(reader));
        } catch (JSONException e) {
            System.err.println("Invalid JSON in file: " + GROUPS_FILE_PATH);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading file: " + GROUPS_FILE_PATH);
            e.printStackTrace();
        }
        return new JSONArray();
    }

    @Override
    public JSONObject loadGroupByName(String name) {
        JSONArray groups = loadGroups();
        for (int i = 0; i < groups.length(); i++) {
            JSONObject group = groups.getJSONObject(i);
            if (name.equalsIgnoreCase(group.optString("groupName", ""))) {
                return group;
            }
        }
        return null;
    }

    @Override
    public JSONArray loadPosts(String groupId) {
        File file = new File(POSTS_FILE_PATH);
        // Initialize the file with an empty array if it doesn't exist or is empty
        if (!file.exists() || file.length() == 0) {
            try (FileWriter fileWriter = new FileWriter(POSTS_FILE_PATH)) {
                fileWriter.write("[]");
                System.out.println("File initialized with an empty array.");
            } catch (IOException e) {
                System.err.println("Failed to initialize file: " + POSTS_FILE_PATH);
                e.printStackTrace();
            }
        }

        // Load the JSON array from the file
        try (FileReader reader = new FileReader(file)) {
            return new JSONArray(new JSONTokener(reader));
        } catch (JSONException e) {
            System.err.println("Invalid JSON in file: " + POSTS_FILE_PATH);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading file: " + POSTS_FILE_PATH);
            e.printStackTrace();
        }
        return new JSONArray();
    }

    @Override
    public JSONArray loadGroupsbyUserId(String userId) {
        JSONArray groups = loadGroups();  // Load all groups
        JSONArray userGroups = new JSONArray();  // This will store the groups that the user is associated with
        JSONArray groupsMembers = storageHandler.loadDataAsArray(MEMBERS_FILE_PATH);  // Load group members

        // First loop to check for primary admin and admins
        for (int i = 0; i < groups.length(); i++) {
            JSONObject group = groups.getJSONObject(i);
            JSONArray admins = group.optJSONArray("admins");

            // Check if user is the primary admin of the group
            if (group.get("primaryAdminId").equals(userId)) {
                userGroups.put(group);
            }
            // Check if user is in the list of admins
            else if (admins != null && admins.length() > 0) {
                for (int j = 0; j < admins.length(); j++) {
                    String admin = admins.optString(j);
                    if (admin.equals(userId)) {
                        userGroups.put(group);
                        break;  // Exit once we find the user as an admin
                    }
                }
            }
        }

        // Second loop to check for regular group membership
        for (int i = 0; i < groupsMembers.length(); i++) {
            JSONObject group = groupsMembers.getJSONObject(i);
            JSONArray members = group.optJSONArray("members");

            // Check if user is a member of the group and not already in userGroups
            if (members != null) {
                for (int j = 0; j < members.length(); j++) {
                    String member = members.optString(j);
                    if (member.equals(userId) && !containsGroup(userGroups, group)) {
                        userGroups.put(group);
                        break;  // Exit once we find the user as a member
                    }
                }
            }
        }

        return userGroups;  // Return the list of groups the user is associated with
    }

    // Helper method to check if a group is already in the userGroups array
    private boolean containsGroup(JSONArray userGroups, JSONObject group) {
        for (int i = 0; i < userGroups.length(); i++) {
            if (userGroups.getJSONObject(i).getString("groupName").equals(group.getString("groupName"))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> loadGroupSuggestions(String userId) {
        List<String> suggestions = new ArrayList<>();
        // Load necessary data arrays

        JSONArray groupsMembers = storageHandler.loadDataAsArray(MEMBERS_FILE_PATH);
        JSONArray requests = storageHandler.loadDataAsArray(REQUESTS_FILE_PATH);
        JSONArray groups = storageHandler.loadDataAsArray(GROUPS_FILE_PATH);

        // Check if arrays are empty or null
        if (groupsMembers == null || requests == null || groups == null) {
            System.err.println("Error: One or more JSON arrays failed to load.");
            return suggestions; // Return empty suggestions if data couldn't be loaded
        }

        // Iterate through all the groups to check if the user is a member or has requested to join
        for (int i = 0; i < groups.length(); i++) {
            JSONObject group = groups.optJSONObject(i);

            if (group == null) {
                continue; // Skip null or invalid group data
            }
            String groupName = group.optString("groupName", "Unknown Group");

            // Check if the user is already a member or primary admin
            if (isUserMemberOrAdmin(userId, groupName, group, groupsMembers)) {
                continue;  // Skip if user is already a member or primary admin
            }

            // Check if the user has already sent a request to join the group
            if (hasUserSentRequest(userId, groupName, requests)) {
                continue;  // Skip if user has already sent a request
            }

            // If neither, add the group to the suggestions
            suggestions.add(groupName);
        }

        return suggestions;
    }

    private boolean isUserMemberOrAdmin(String userId, String groupName, JSONObject group, JSONArray groupsMembers) {
        // Check if user is a primary admin of the group


            if (group.optString("groupName").equals(groupName)) {
                String primaryAdminId = group.optString("primaryAdminId", "");
                if (primaryAdminId.equals(userId)) {
                    return true;  // User is the primary admin
                }
            }


        // Check if user is a member of the group by passing groupsMembers
        JSONArray members = getGroupMembers(groupName, groupsMembers);
        for (int j = 0; j < members.length(); j++) {
            if (members.optString(j).equals(userId)) {
                return true;  // User is a member of the group
            }
        }

        return false;  // User is neither a member nor a primary admin
    }

    private boolean hasUserSentRequest(String userId, String groupName, JSONArray requests) {
        // Check if user has already sent a request to join the group
        for (int i = 0; i < requests.length(); i++) {
            JSONObject requestGroup = requests.optJSONObject(i);
            if (requestGroup == null) {
                continue; // Skip invalid or null request group data
            }
            if (requestGroup.optString("groupName").equals(groupName)) {
                JSONArray groupRequests = requestGroup.optJSONArray("requests");
                if (groupRequests != null) {
                    for (int j = 0; j < groupRequests.length(); j++) {
                        JSONObject request = groupRequests.optJSONObject(j);
                        if (request == null) {
                            continue; // Skip invalid or null request data
                        }
                        String requestUserId = request.optString("userId", "");
                        if (requestUserId.equals(userId)) {
                            return true;  // User has already sent a request
                        }
                    }
                }
            }
        }
        return false;  // User hasn't sent a request to join this group
    }

    private JSONArray getGroupMembers(String groupName, JSONArray groupsMembers) {
        // This method will fetch and return the members of a given group
        // Iterate through the groupsMembers to find the group by name
        for (int i = 0; i < groupsMembers.length(); i++) {
            JSONObject group = groupsMembers.optJSONObject(i);
            if (group == null) {
                continue; // Skip invalid or null group data
            }
            if (group.optString("groupName").equals(groupName)) {
                return group.optJSONArray("members");
            }
        }
        return new JSONArray();  // Return an empty array if no members are found
    }



}











