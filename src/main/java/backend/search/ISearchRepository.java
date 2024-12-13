package backend.search;

import backend.user.User;

import java.util.List;

public interface ISearchRepository {
    List<User> findUsersByQuery(String query);
}
