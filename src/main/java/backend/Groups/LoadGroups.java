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

public class LoadGroups implements ILoadGroups {
    private static final String GROUPS_FILE_PATH = "data/groups.json";
    private static final String POSTS_FILE_PATH = "data/groupsPosts.json";
    private static final String MEMBERS_FILE_PATH = "data/group_members.json";

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
}
