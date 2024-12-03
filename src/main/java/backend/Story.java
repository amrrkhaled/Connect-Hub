package backend;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class Story implements IContentCreation {
    private final IContentFiles contentFiles;
    private final String FILEPATH = "data/stories.json";

    public Story(IContentFiles contentFiles) {
        this.contentFiles = contentFiles;
    }

    @Override
    public void createContent(String authorId,  String content, String timestamp, List<String> images) {
        JSONObject newStory = new JSONObject();
        JSONArray stories = contentFiles.loadContent(FILEPATH);
        newStory.put("authorId", authorId);
        newStory.put("contentId", "S" + (stories.length()+1));
        newStory.put("content", content);
        newStory.put("timestamp", timestamp);
        newStory.put("images", images);
        stories.put(stories.length(),newStory);
        contentFiles.saveContent(stories,FILEPATH);

    }


}