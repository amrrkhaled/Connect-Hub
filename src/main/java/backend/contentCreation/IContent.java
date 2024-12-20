package backend.contentCreation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public interface IContent {
    void createContent(String authorId, String content, String timestamp, List<String> images);

    JSONArray getUserContent(String userId);

    JSONArray getNewsFeedContent(String userId);

    JSONObject getContentById(String id);

    JSONObject getContentIdByName(String content);

    public void addComment(String postId, String comment);

    public JSONArray getCommentsById(String postId);


}




