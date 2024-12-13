package backend.search;

import backend.user.UserRepository;
import backend.user.IUserRepository;
import backend.user.User;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchManager {

    private final IUserRepository userRepository;

    public SearchManager(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> performUserSearch(String query) {
        List<User> matchedUsers = new ArrayList<>();

        for (int i = 0; i < userRepository.getUsersArray().length(); i++) {
            JSONObject userJson = userRepository.getUsersArray().getJSONObject(i);
            String username = userJson.getString("username");

            if (username.toLowerCase().contains(query.toLowerCase())) {
                User user = new User(username, userJson.getString("userId"));
                matchedUsers.add(user);
            }
        }
        return matchedUsers;
    }

    public IUserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
