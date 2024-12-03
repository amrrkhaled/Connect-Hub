package backend;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;



public class Post implements IContentCreation {

    private final IContentFiles contentFiles;
    private final String FILEPATH = "data/posts.json";

    public Post(IContentFiles contentFiles) {
        this.contentFiles = contentFiles;
    }
    @Override
    public void createContent(String authorId, String contentId, String content, String timestamp, List<String> images) {
        JSONObject newPost = new JSONObject();
        JSONArray posts = contentFiles.loadContent(FILEPATH);
        newPost.put("authorId", authorId);
        newPost.put("contentId", posts.length()+1);
        newPost.put("content", content);
        newPost.put("timestamp", timestamp);
        newPost.put("images", images);
        posts.put(posts.length(),newPost);
        contentFiles.saveContent(posts,FILEPATH);

    }


}