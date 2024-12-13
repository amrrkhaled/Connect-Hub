package backend.search;

import backend.user.User;

import java.util.List;

public interface ISearchUsers {
    List<User> searchUsers(String query);
}
