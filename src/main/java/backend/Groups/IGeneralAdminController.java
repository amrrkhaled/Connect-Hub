package backend.Groups;

public interface IGeneralAdminController {
    public void rejectMember(String groupId, String userId);
    public void acceptMember(String groupId, String userId);
}
