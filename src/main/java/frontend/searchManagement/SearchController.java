package frontend.groupManagement;

import backend.search.SearchManager;
import backend.user.User;
import backend.Groups.Group;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class SearchController {

    @FXML
    private TextField searchBar;

    @FXML
    private ListView<String> searchResults;

    private final SearchManager searchManager = new SearchManager();

    @FXML
    private void handleSearch() {
        String keyword = searchBar.getText().trim();
        if (!keyword.isEmpty()) {
            // Clear previous results
            searchResults.getItems().clear();

            // Search users and groups
            List<User> users = searchManager.searchUsers(keyword);
            List<Group> groups = searchManager.searchGroups(keyword);
            users.forEach(user -> searchResults.getItems().add("User: " + user.getUsername()));
            groups.forEach(group -> searchResults.getItems().add("Group: " + group.getName()));
        }
    }
}
