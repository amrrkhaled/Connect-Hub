package backend.search;

import backend.user.LoadUsers;

public class SearchManagerFactory {
    public static SearchManager createSearchManager() {
        ISearchRepository searchRepository = new SearchRepository(LoadUsers.getInstance());
        ISearchUsers searchUsers = new SearchUsers(searchRepository);
        return new SearchManager(searchUsers);
    }
}
