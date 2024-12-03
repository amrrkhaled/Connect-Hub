package backend;
import java.util.List;

public interface IContentCreation {
    void createContent(String authorId, String contentId, String content, String timestamp, List<String> images);
    }




