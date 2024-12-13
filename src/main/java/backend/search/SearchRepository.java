package backend.search;

import backend.user.ILoadUsers;
import backend.user.LoadUsers;
import backend.user.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchRepository implements ISearchRepository {
    private final ILoadUsers userLoader;

    public SearchRepository(ILoadUsers userLoader) {
        this.userLoader = userLoader;
    }

    @Override
    public List<User> findUsersByQuery(String query) {
        List<User> matchingUsers = new ArrayList<>();
        JSONArray usersArray = userLoader.loadUsers();

        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject userObject = usersArray.getJSONObject(i);
            String username = userObject.getString("username");

            if (username.toLowerCase().contains(query.toLowerCase())) {
                matchingUsers.add(new User(
                        userObject.getInt("id"),
                        username,
                        userObject.getString("password"),
                        userObject.getString("email"),
                        userObject.getString("name")
                ));
            }
        }

        return matchingUsers;
    }
}
