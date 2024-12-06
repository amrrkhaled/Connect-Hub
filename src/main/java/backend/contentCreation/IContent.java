package backend.contentCreation;
import org.json.JSONArray;

import java.util.List;

public interface IContent {
    void createContent(String authorId, String content, String timestamp, List<String> images);
    JSONArray getUserContent(String userId);
    JSONArray getNewsFeedContent(String userId);
    }




