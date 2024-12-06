package backend.contentCreation;

import org.json.JSONArray;

public interface IContentRepository {
    public JSONArray getNewsFeedContent(String userId);
    public JSONArray getUserContent(String userId);
}
