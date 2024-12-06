package backend.friendship;

import java.util.List;

public interface IFriendRequestService {
    public List<String> getFriendSuggestions(String userId);
    public List<String> getFriendRequests(String userId);
    public String extractUsername(String friendUsernameWithStatus);
}
