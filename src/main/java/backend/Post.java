package backend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Post implements IContentCreation {

    private final IContentFiles contentFiles;
    private final String FILEPATH = "data/posts.json";

    public Post(IContentFiles contentFiles) {
        this.contentFiles = contentFiles;
    }
    @Override
    public void createContent(String authorId, String content, String timestamp, List<String> images) {
        JSONObject newPost = new JSONObject();
        JSONArray posts = contentFiles.loadContent(FILEPATH);
        newPost.put("authorId", authorId);
        newPost.put("contentId", "P" + (posts.length()+1));
        newPost.put("content", content);
        newPost.put("timestamp", timestamp);
        List<String> newImages= new ArrayList<>();
        for(String imagePath : images) {
            String newPath = null;
            try {
                newPath = SaveImage.saveImageToFolder(imagePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            newImages.add(newPath);
        }
        newPost.put("images", newImages);
        posts.put(posts.length(),newPost);
        contentFiles.saveContent(posts,FILEPATH);

    }


}