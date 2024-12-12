package backend.Groups;

import backend.SaveImage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

import java.util.List;

public class Request implements IRequest {
    ILoadGroups loadGroups;
    JSONArray groups;
    private final String filePath = "data/groups_join_requests.json";
    private final String membersFilePath = "data/group_members.json";
    private final String postsFilePath = "data/groupsPosts.json";
    private final String groupsFilePath = "data/groupsPosts.json";

    IStorageHandler requestsHandler;
    IStorageHandler postsHandler;
    IStorageHandler  groupsHandler;
    public Request(ILoadGroups loadGroups, IStorageHandler requestsHandler) {
        this.loadGroups = loadGroups;
        groups = loadGroups.loadGroups();
        System.out.println(groups);
        this.requestsHandler = requestsHandler;
    }

    public void sendJoinRequest(String groupName, String userId, String message) {
        String name;
        boolean requestExists = false;
        JSONArray groupsMembers = requestsHandler.loadDataAsArray(membersFilePath);
        for (int i = 0; i < groupsMembers.length(); i++) {
            JSONObject groupMemberObject = groupsMembers.getJSONObject(i);
            JSONArray members = groupMemberObject.getJSONArray("members");
            for (int j = 0; j < members.length(); j++) {
                String memberObject = members.optString(j);
                System.out.println("Member " + memberObject.toString());
                if (memberObject.equals(userId)) {
                    System.out.println("Member " + memberObject.toString() + " already exists");
                    return;
                }
            }
        }
        for (int i = 0; i < groups.length(); i++) {
            JSONObject group = groups.getJSONObject(i);

            name = group.getString("groupName");
            if (name.equals(groupName)) {
                JSONObject request = new JSONObject();
                String groupId = group.getString("groupId");
                request.put("userId", userId);
                request.put("timestamp", Instant.now().toString());
                request.put("message", message);

                // Load the existing join requests (as a JSONArray) for all groups
                JSONArray joinRequests = requestsHandler.loadDataAsArray(filePath);
                // Look for the group in the joinRequests array
                boolean groupFound = false;
                for (int j = 0; j < joinRequests.length(); j++) {
                    JSONObject groupRequestObject = joinRequests.getJSONObject(j);

                    if (groupRequestObject.getString("groupId").equals(groupId)) {
                        // If the group exists, look for an existing request from the same user
                        JSONArray groupRequests = groupRequestObject.optJSONArray("requests");
                        if (groupRequests == null) {
                            groupRequests = new JSONArray();  // Create a new requests array if not present
                        }

                        // Check if the user has already sent a request for this group

                        for (int k = 0; k < groupRequests.length(); k++) {
                            JSONObject existingRequest = groupRequests.getJSONObject(k);
                            if (existingRequest.getString("userId").equals(userId)) {
                                requestExists = true;  // Request from the same user already exists
                                break;
                            }
                        }

                        // If the request doesn't already exist, add it
                        if (!requestExists) {
                            groupRequests.put(request);
                            groupRequestObject.put("requests", groupRequests);
                            groupFound = true;
                        } else {
                            System.out.println("You have already sent a request to join this group.");
                        }
                        break;
                    }
                }

                // If the group doesn't exist in the join requests, create a new entry for it
                if (!groupFound && !requestExists) {
                    JSONObject newGroupRequest = new JSONObject();
                    newGroupRequest.put("groupId", groupId);
                    newGroupRequest.put("groupName", groupName);
                    JSONArray groupRequests = new JSONArray();
                    groupRequests.put(request);
                    newGroupRequest.put("requests", groupRequests);
                    joinRequests.put(newGroupRequest);
                }

                // Save the updated join requests data back to the file
                requestsHandler.saveDataAsArray(joinRequests, filePath);

                if (groupFound) {
                    System.out.println("Join request sent successfully.");
                }

                return;
            }
        }
        System.out.println("Group not found: " + groupName);

}
}
