package backend.chatManagement;

import java.io.IOException;
import java.util.List;

public interface IMessageService {
    void saveMessage(String sender, String receiver, String messageContent) throws IOException;
    List<Message> searchMessages(String query) throws IOException;
}
