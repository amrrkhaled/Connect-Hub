package backend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Post implements IContent {

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

    @Override
    public JSONArray getUserContent(String userId) {
        JSONArray posts = contentFiles.loadContent(FILEPATH);
        JSONArray userPosts = new JSONArray();
        if (posts == null) {
            System.err.println("Error: Content file could not be loaded.");
            return userPosts; // Return empty array if no content
        }

        for (int i = 0; i < posts.length(); i++) {
            try {
                JSONObject post = posts.getJSONObject(i);
                if (post.getString("userId").equals(userId)) {
                    userPosts.put(post);
                }
            } catch (Exception e) {
                System.err.println("Error processing post at index " + i + ": " + e.getMessage());
            }
        }

        return userPosts;
    }

    @Override
    public JSONArray getNewsFeedContent(String userId) {
        List<String> friendsIDs = List.of("friend1", "friend2", "friend3");  // Get the list of friend IDs
        JSONArray feedPosts = new JSONArray();

        if (friendsIDs == null || friendsIDs.isEmpty()) {
            System.err.println("Error: No friends found for userId " + userId);
            return feedPosts; // Return empty feed if no friends
        }

        for (String friendId : friendsIDs) {
            try {
                JSONArray friendPosts = getUserContent(friendId); // Get content for each friend
                for (int i = 0; i < friendPosts.length(); i++) {
                    feedPosts.put(friendPosts.getJSONObject(i)); // Add each post to the feed
                }
            } catch (Exception e) {
                System.err.println("Error fetching posts for friendId " + friendId + ": " + e.getMessage());
            }
        }

        return feedPosts;
    }

}