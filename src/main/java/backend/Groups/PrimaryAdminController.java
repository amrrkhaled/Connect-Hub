package backend.Groups;

public class PrimaryAdminController extends GeneralAdminController {
    public PrimaryAdminController(ILoadGroups loadGroups, IStorageHandler storageHandler) {
        super(loadGroups, storageHandler);
    }

    public void promoteToAdmin(String userId, String groupId) {
        // promote the user to be an admin
        // save it in the a database called data/admins.json
        // in this form
        // "groupId": "G1",
        //    "admins": ["U4","U3"]
        //check my code for members it will help
    }

    public void removeAdmin(String userId, String groupId) {

    }

    //make the parameters in AddPost according to your imagination
    public void AddPost() {
    }
    public void RemovePost() {

    }
    public void editPost() {}
    public void deleteGroup(String groupId) {}
}
