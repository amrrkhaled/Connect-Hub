package backend.chatManagement;

import java.io.IOException;
import java.util.List;

public interface IMessageRepository {
    List<Message> load() throws IOException;

    void save(List<Message> messages) throws IOException;
}
