package backend.search;

import backend.user.User;

import java.util.List;

public class SearchUsers implements ISearchUsers {
    private final ISearchRepository searchRepository;

    public SearchUsers(ISearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @Override
    public List<User> searchUsers(String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }
        return searchRepository.findUsersByQuery(query);
    }
}
