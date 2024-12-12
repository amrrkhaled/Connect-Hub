package backend.Groups;

import org.json.JSONArray;
import org.json.JSONObject;

public class GeneralAdminController {
    private final ILoadGroups loadGroups;
    private final IStorageHandler storageHandler;
    private final JSONArray groups;
    private final String membersFilePath = "data/group_members.json";
    private final String joinRequestsFilePath = "data/groups_join_requests.json";

    public GeneralAdminController(ILoadGroups loadGroups, IStorageHandler storageHandler) {
        this.loadGroups = loadGroups;
        this.storageHandler = storageHandler;
        this.groups = loadGroups.loadGroups();
    }

    public void acceptMember(String groupId, String userId) {
        // Load members
        JSONArray groupsMembers = storageHandler.loadDataAsArray(membersFilePath);
        JSONArray joinRequestsData = storageHandler.loadDataAsArray(joinRequestsFilePath);

        // Find the group in the join requests
        JSONObject groupReq = null;
        for (int i = 0; i < joinRequestsData.length(); i++) {
            JSONObject groupRequestObject = joinRequestsData.getJSONObject(i);
            if (groupRequestObject.getString("groupId").equals(groupId)) {
                groupReq = groupRequestObject;
                break;
            }
        }

        if (groupReq == null) {
            System.out.println("Group " + groupId + " not found");
            return;
        }
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

        // Find the user in the join requests for the group
        JSONArray requests = groupReq.getJSONArray("requests");
        boolean userFound = false;
        for (int i = 0; i < requests.length(); i++) {
            JSONObject requestObject = requests.getJSONObject(i);
            if (requestObject.getString("userId").equals(userId)) {
                // User found, remove from join requests
                requests.remove(i);
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            System.out.println("User " + userId + " not found in join requests for group " + groupId);
            return;
        }

        // Add user to the group members
        boolean groupFound = false;
        for (int j = 0; j < groupsMembers.length(); j++) {
            JSONObject groupMember = groupsMembers.getJSONObject(j);
            if (groupMember.getString("groupId").equals(groupId)) {
                JSONArray members = groupMember.getJSONArray("members");
                members.put(userId);
                groupFound = true;
                break;
            }
        }

        if (!groupFound) {
            // Create a new entry for the group in group_members.json
            JSONObject newGroupMember = new JSONObject();
            newGroupMember.put("groupId", groupId);
            newGroupMember.put("members", new JSONArray().put(userId));
            groupsMembers.put(newGroupMember);
        }

        // Save changes
        storageHandler.saveDataAsArray(joinRequestsData, joinRequestsFilePath);
        storageHandler.saveDataAsArray(groupsMembers, membersFilePath);

        System.out.println("User " + userId + " successfully added to group " + groupId);
    }

    public void rejectMember(String groupId, String userId) {
        JSONArray groupsMembers = storageHandler.loadDataAsArray(membersFilePath);
        JSONArray joinRequestsData = storageHandler.loadDataAsArray(joinRequestsFilePath);
        for (int i = 0; i < groupsMembers.length(); i++) {
            JSONObject groupMemberObject = groupsMembers.getJSONObject(i);
            if (groupMemberObject.getString("groupId").equals(groupId)) {
                JSONArray members = groupMemberObject.getJSONArray("members");
                for (int j = 0; j < members.length(); j++) {
                    String memberObject = members.optString(j);
                    if (memberObject.equals(userId)) {
                        System.out.println("Member " + memberObject.toString() + " already exists");
                        return;
                    }
                }
            }
        }
        for (int i = 0; i < joinRequestsData.length(); i++) {
            System.out.println("Join request " + joinRequestsData.getJSONObject(i).getString("groupId"));
            System.out.println(groupId);
            JSONObject joinRequestObject = joinRequestsData.getJSONObject(i);
            if (joinRequestObject.getString("groupId").equals(groupId)) {
                System.out.println("i entered here");
                JSONArray requests = joinRequestObject.getJSONArray("requests");
                for (int j = 0; j < requests.length(); j++) {
                    String memberRequest = requests.getJSONObject(j).getString("userId");
                    if (memberRequest.equals(userId)) {
                        requests.remove(i);
                        System.out.println("Member " + memberRequest + " successfully removed to join requests for group " + groupId);
                    }
                }
            }
        }
        storageHandler.saveDataAsArray(joinRequestsData, joinRequestsFilePath);
        storageHandler.saveDataAsArray(groupsMembers, membersFilePath);
    }
    public void removeMember(String groupId, String userId){
        JSONArray groupsMembers = storageHandler.loadDataAsArray(membersFilePath);
        for (int i = 0; i < groupsMembers.length(); i++) {
            JSONObject groupMemberObject = groupsMembers.getJSONObject(i);
            if (groupMemberObject.getString("groupId").equals(groupId)) {
                JSONArray members = groupMemberObject.getJSONArray("members");
                for (int j = 0; j < members.length(); j++) {
                    String memberObject = members.optString(j);
                    System.out.println("Member " + memberObject.toString());
                    if (memberObject.equals(userId)) {
                        System.out.println("Members " + members.toString() );
                        members.remove(j);
                        storageHandler.saveDataAsArray(groupsMembers, membersFilePath);
                        System.out.println("Members " + members.toString() );
                        System.out.println("Member " + memberObject.toString() + " successfully removed from the group " + groupId);
                        return;
                    }
                }
            }
        }
    }

}
