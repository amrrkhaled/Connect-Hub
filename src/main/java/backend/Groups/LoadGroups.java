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
        JSONArray groups = loadGroups();
        JSONArray userGroups = new JSONArray();
        JSONArray groupsMembers = storageHandler.loadDataAsArray(MEMBERS_FILE_PATH);
        for (int i = 0; i < groups.length(); i++) {
            JSONObject group = groups.getJSONObject(i);
            JSONArray admins = group.optJSONArray("admins");
            if (group.get("primaryAdminId").equals(userId)) {
               userGroups.put(group);
            }
            else if (admins.length() > 0) {
                for (int j = 0; j < admins.length(); j++) {
                    String admin = admins.optString(j);
                    if(admin.equals(userId)){
                        userGroups.put(group);

                    }
                }
            }
        }
        for (int i = 0; i < groupsMembers.length(); i++) {
            JSONObject group = groupsMembers.getJSONObject(i);
            JSONArray memebrs = group.optJSONArray("members");
            for (int j = 0; j < memebrs.length(); j++) {
                String member = memebrs.optString(j);
                if(member.equals(userId)){
                    userGroups.put(group);
                    break;
                }
            }
        }


    return userGroups;

    }


    @Override
    public List<String> loadGroupSuggestions(String userId) {
        List<String> suggestions = new ArrayList<>();
        JSONArray groupsMembers = storageHandler.loadDataAsArray(MEMBERS_FILE_PATH);
        JSONArray requests = storageHandler.loadDataAsArray(REQUESTS_FILE_PATH);

        // Iterate through all the groups in the groupsMembers array
        for (int i = 0; i < groupsMembers.length(); i++) {
            JSONObject group = groupsMembers.getJSONObject(i);
            JSONArray members = group.optJSONArray("members");

            boolean isUserMember = false;

            // Check if the current user is already a member of the group
            for (int j = 0; j < members.length(); j++) {
                String member = members.optString(j);
                if (member.equals(userId)) {
                    isUserMember = true;
                    break; // User is already a member, no need to add this group to suggestions
                }
            }

            // If the user is not a member, we proceed to check if the user has already sent a request
            if (!isUserMember) {

                // Check if the user has already sent a request for this group
                boolean hasUserSentRequest = false;
                for (int j = 0; j < requests.length(); j++) {
                    JSONObject requestGroup = requests.getJSONObject(j);

                    // Check if this group has requests and if the groupName matches
                    if (requestGroup.getString("groupName").equals(group.getString("groupName"))) {
                        JSONArray groupRequests = requestGroup.getJSONArray("requests");

                        // Loop through the requests for this group to check if the current user has already requested to join
                        for (int k = 0; k < groupRequests.length(); k++) {
                            JSONObject request = groupRequests.getJSONObject(k);
                            String requestUserId = request.getString("userId");

                            // If the user has already sent a request, we mark it as true
                            if (requestUserId.equals(userId)) {
                                hasUserSentRequest = true;
                                break; // No need to continue looping through requests for this group
                            }
                        }
                    }

                    if (hasUserSentRequest) {
                        break; // No need to continue looping through requests, as we found the user's request
                    }
                }

                // If the user hasn't sent a request, we add the group to suggestions
                if (!hasUserSentRequest) {
                    suggestions.add(group.optString("groupName", "Unknown Group"));
                }
            }
        }

        return suggestions;
    }



}











