package frontend.chatManagement;

import backend.chatManagement.IMessageService;
import backend.chatManagement.Message;
import backend.chatManagement.MessageRepository;
import backend.chatManagement.MessageService;
import backend.friendship.ILoadFriendShips;
import backend.friendship.LoadFriendShips;
import backend.user.ILoadUsers;
import backend.user.LoadUsers;
import backend.user.User;
import backend.user.UserRepository;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MessagesController {

    @FXML
    private ListView<String> contactsListView;

    @FXML
    private ListView<String> messagesListView;

    @FXML
    private TextField messageInputField;

    @FXML
    private TextField searchMessagesField;

    @FXML
    private Button refreshButton;

    @FXML
    private Button searchButton;

    private IMessageService messageService;
    private ILoadUsers loadUsers;
    private ILoadFriendShips loadFriendships;
    private String currentUser;
    private String selectedContact;
    private Timer timer;

    public MessagesController() {
        this.messageService = new MessageService(new MessageRepository());
        this.loadUsers = new LoadUsers();
        this.loadFriendships = LoadFriendShips.getInstance();
    }

    public void setMessageService(IMessageService messageService) {
        this.messageService = messageService;
    }

    public void setLoadUsers(ILoadUsers loadUsers) {
        this.loadUsers = loadUsers;
    }

    @FXML
    private void initialize() {
        currentUser = User.getUserId();
        loadContacts();

        contactsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedContact = newValue;
                displayMessagesForContact(selectedContact);
                startChatUpdater();
            }
        });

        contactsListView.getSelectionModel().clearSelection();
        refreshButton.setOnAction(event -> refreshChat());
        searchButton.setOnAction(event -> searchMessages());
    }

    private void loadContacts() {
        try {
            JSONArray users = loadUsers.loadUsers();
            JSONArray friendships = loadFriendships.loadFriendships();
            Set<String> friends = getFriendsList(friendships, currentUser);

            contactsListView.getItems().clear();

            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                String username = user.optString("username", "Unknown");
                String userId = user.optString("userId", "");
                if (friends.contains(userId)) {
                    contactsListView.getItems().add(username);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Set<String> getFriendsList(JSONArray friendships, String currentUserId) {
        Set<String> friends = new HashSet<>();
        for (int i = 0; i < friendships.length(); i++) {
            JSONObject friendship = friendships.getJSONObject(i);
            String userId1 = friendship.optString("userId1", "");
            String userId2 = friendship.optString("userId2", "");
            String status = friendship.optString("status", "");

            if ("accepted".equalsIgnoreCase(status)) {
                if (userId1.equals(currentUserId)) {
                    friends.add(userId2);
                } else if (userId2.equals(currentUserId)) {
                    friends.add(userId1);
                }
            }
        }
        return friends;
    }

    private void displayMessagesForContact(String contact) {
        try {
            List<Message> messages = messageService.searchMessages(contact);
            messagesListView.getItems().clear();

            for (Message message : messages) {
                String senderName = message.getSender().equals(currentUser) ? "You" : UserRepository.getInstance(loadUsers).getUsernameByUserId(message.getSender());
                String receiverName = message.getReceiver().equals(currentUser) ? "You" : UserRepository.getInstance(loadUsers).getUsernameByUserId(message.getReceiver());

                if ((message.getSender().equals(currentUser) && message.getReceiver().equals(contact)) ||
                        (message.getSender().equals(contact) && message.getReceiver().equals(currentUser))) {
                    String displayMessage = senderName + ": " + message.getContent();
                    messagesListView.getItems().add(displayMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startChatUpdater() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (selectedContact != null) {
                    Platform.runLater(() -> displayMessagesForContact(selectedContact));
                }
            }
        }, 0, 3000); // Update every 3 seconds
    }

    @FXML
    private void refreshChat() {
        if (selectedContact != null) {
            displayMessagesForContact(selectedContact);
        }
    }

    @FXML
    private void goBackToFeed() {
        try {
            if (timer != null) {
                timer.cancel();
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/NewsFeed.fxml"));
            Scene newsFeedScene = new Scene(loader.load());
            Stage stage = (Stage) contactsListView.getScene().getWindow();
            stage.setScene(newsFeedScene);
            stage.setTitle("News Feed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sendMessage() {
        String message = messageInputField.getText().trim();
        if (message.isEmpty()) {
            System.out.println("Cannot send an empty message.");
            return;
        }

        if (selectedContact == null) {
            System.out.println("No contact selected.");
            return;
        }

        try {
            messageService.saveMessage(currentUser, selectedContact, message);
            messagesListView.getItems().add(currentUser + ": " + message);
            messageInputField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void searchMessages() {
        String searchText = searchMessagesField.getText().trim();
        if (searchText.isEmpty() || selectedContact == null) {
            return;
        }

        try {
            List<Message> messages = messageService.searchMessages(selectedContact);
            messagesListView.getItems().clear();

            for (Message message : messages) {
                if (message.getSender().equals(currentUser) || message.getReceiver().equals(currentUser)) {
                    if (message.getContent().contains(searchText)) {
                        messagesListView.getItems().add(message.toString());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
