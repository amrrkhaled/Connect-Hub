package frontend.searchManagement;

import backend.search.SearchManager;
import backend.user.UserRepository;
import backend.user.ILoadUsers;
import backend.user.LoadUsers;
import backend.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import java.util.List;

public class SearchController {

    private final SearchManager searchManager;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> resultsListView;

    public SearchController() {
        ILoadUsers loadUsers = LoadUsers.getInstance();
        UserRepository userRepository = UserRepository.getInstance(loadUsers);
        this.searchManager = new SearchManager(userRepository);
    }

    @FXML
    private void initialize() {
        resultsListView.getItems().clear();
    }

    @FXML
    private void handleSearch(KeyEvent event) {
        String query = searchField.getText().trim();

        if (!query.isEmpty()) {
            List<User> results = searchManager.performUserSearch(query);
            updateSearchResults(results);
        }
    }

    private void updateSearchResults(List<User> results) {
        resultsListView.getItems().clear();

        if (results.isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Search Results");
            alert.setHeaderText("No users found");
            alert.setContentText("No users matched your search");
            alert.showAndWait();
            return;
        }

        for (User user : results) {
            String username = searchManager.getUserRepository().getUsernameByUserId(user.getUserId());
            if (username != null) {
                resultsListView.getItems().add(username);
            }
        }
    }
}
