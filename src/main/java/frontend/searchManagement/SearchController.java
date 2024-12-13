package frontend.searchManagement;

import backend.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class SearchController {
    @FXML
    private TextField queryField;

    @FXML
    private VBox userResults;

    @FXML
    private VBox groupResults;

    static String keyword ;
    public static void setKeyword(String word) {
        keyword = word;
    }


    public void initialize() {
        displayUserResults();
        displayGroupResults();
    }

    @FXML
//    private void onSearch() {
//        String query = queryField.getText().trim();
//        if (query.isEmpty()) {
//            showError("Search query cannot be empty.");
//            return;
//        }
//
//        displayUserResults(searchManagement.searchUsersByUsername(query));
//        displayGroupResults(searchManagement.searchGroupsByName(List.of("Group A", "Group B", "Example Group"), query));
//    }

    private void displayUserResults() {
        userResults.getChildren().clear();
        if (users.isEmpty()) {
            userResults.getChildren().add(new Label("No users found."));
        } else {
            for ( User user : users) {
                Label userLabel = new Label("Username: " + user.getUsername());
                userResults.getChildren().add(userLabel);
            }
        }
    }

    private void displayGroupResults(List<String> groups) {
        groupResults.getChildren().clear();
        if (groups.isEmpty()) {
            groupResults.getChildren().add(new Label("No groups found."));
        } else {
            for (String group : groups) {
                Label groupLabel = new Label("Group: " + group);
                groupResults.getChildren().add(groupLabel);
            }
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
}
