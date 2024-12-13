package backend.Groups;

import backend.SaveImage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NormalUserController {
    ILoadGroups loadGroups;
    JSONArray groups;
    private final String postsFilePath = "data/groupsPosts.json";
    private final String membersFilePath = "data/group_members.json";
    IStorageHandler storageHandler;

    public NormalUserController(ILoadGroups loadGroups, IStorageHandler storageHandler) {
        this.loadGroups = loadGroups;
        groups = loadGroups.loadGroups();
        this.storageHandler = storageHandler;
    }

    // Add a post to a group
    public void addPost(String groupName, String authorId, String content, String timestamp, List<String> images) {
        JSONObject newPost = new JSONObject();
        JSONArray posts = storageHandler.loadDataAsArray(postsFilePath);
        newPost.put("authorId", authorId);
        newPost.put("contentId", "P" + (posts.length() + 1));  // Generate a unique post ID
        newPost.put("content", content);
        newPost.put("timestamp", timestamp);
        newPost.put("groupName", groupName);

        List<String> newImages = new ArrayList<>();
        if (images != null) {
            for (String imagePath : images) {
                String newPath = null;
                try {
                    newPath = SaveImage.saveImageToFolder(imagePath);  // Save the image and get the new path
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                newImages.add(newPath);  // Add the image path to the list
            }
            newPost.put("images", newImages);  // Add images to the post
        }

        posts.put(posts.length(), newPost);  // Add the new post to the posts array
        storageHandler.saveDataAsArray(posts, postsFilePath);  // Save the updated posts
    }

    // Allow a user to leave a group
    public void leaveGroup(String name, String userId) {
        JSONArray groupsMembers = storageHandler.loadDataAsArray(membersFilePath);
        JSONArray members = null;
        int groupIndex = -1;

        // Find the group by name
        for (int i = 0; i < groupsMembers.length(); i++) {
            JSONObject groupMemberObject = groupsMembers.getJSONObject(i);
            if (groupMemberObject.getString("groupName").equals(name)) {
                members = groupMemberObject.getJSONArray("members");
                groupIndex = i;
                break;  // Exit loop once the group is found
            }
        }

        // If the group is found, remove the user from the members list
        if (members != null && groupIndex != -1) {
            for (int i = 0; i < members.length(); i++) {
                if (members.getString(i).equals(userId)) {
                    groupsMembers.getJSONObject(groupIndex).getJSONArray("members").remove(i);  // Remove user
                    break;
                }
            }

            // Save the updated members list
            storageHandler.saveDataAsArray(groupsMembers, membersFilePath);
            System.out.println("User has left the group and the group has been updated successfully.");
        } else {
            System.out.println("Group or user not found.");
        }
    }
    // Get the list of group names a user is a member of
    public List<String> getGroupsForUser(String userId) {
        JSONArray groupsMembers = storageHandler.loadDataAsArray(membersFilePath);
        List<String> userGroups = new ArrayList<>();

        // Iterate through all groups to find the groups the user is a member of
        for (int i = 0; i < groupsMembers.length(); i++) {
            JSONObject groupMemberObject = groupsMembers.getJSONObject(i);
            String groupName = groupMemberObject.getString("groupName");
            JSONArray members = groupMemberObject.getJSONArray("members");

            // Check if the userId exists in the members list
            for (int j = 0; j < members.length(); j++) {
                if (members.getString(j).equals(userId)) {
                    userGroups.add(groupName);  // Add the group name to the list
                    break;  // Exit the inner loop once the user is found
                }
            }
        }

        return userGroups;  // Return the list of group names
    }
}
