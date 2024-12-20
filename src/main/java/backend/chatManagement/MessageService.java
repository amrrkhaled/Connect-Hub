package backend.chatManagement;

import backend.user.User;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MessageService implements IMessageService {

    private final IMessageRepository messageRepository;

    public MessageService(IMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void saveMessage(String sender, String receiver, String messageContent) throws IOException {
        if (sender == null || receiver == null || messageContent == null || messageContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Sender, receiver, and message content must not be empty.");
        }
        Message message = new Message(sender, receiver, messageContent);
        List<Message> messageList = messageRepository.load();
        messageList.add(message);
        messageRepository.save(messageList);
    }

    @Override
    public List<Message> searchMessages(String contactId) throws IOException {
        if (contactId == null || contactId.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact ID must not be empty.");
        }

        // Load all messages
        List<Message> messages = messageRepository.load();

        // Filter messages to include all messages between the current user and the contact
        String currentUser = User.getUserId();
        return messages.stream()
                .filter(m -> (m.getSender().equals(currentUser) && m.getReceiver().equals(contactId)) ||
                        (m.getSender().equals(contactId) && m.getReceiver().equals(currentUser)))
                .collect(Collectors.toList());
    }

}