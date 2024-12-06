package backend.contentCreation;

import backend.SaveImage;
import backend.friendship.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;

public class Story implements IContent , IContentRepository{
    private final IContentFiles contentFiles;
    private final IFriendshipService friendShipService ;
    private  final FriendRequestService friendRequestService;
    private final String FILEPATH = "data/stories.json";
    private static Story instance;
    private Story(IContentFiles contentFiles, IFriendshipService friendShipService , FriendRequestService friendRequestService)
    {
        this.contentFiles = contentFiles;
        this.friendShipService = friendShipService;
        this.friendRequestService = friendRequestService;
    }
    public static synchronized Story getInstance(IContentFiles contentFiles, IFriendshipService friendShipService , FriendRequestService friendRequestService) {
        if (instance == null) {
            instance = new Story(contentFiles, friendShipService , friendRequestService);
        }
        return instance;
    }
    @Override
    public void createContent(String authorId,  String content, String timestamp, List<String> images) {
        JSONObject newStory = new JSONObject();
        JSONArray stories = contentFiles.loadContent(FILEPATH);
        newStory.put("authorId", authorId);
        newStory.put("contentId", "S" + (stories.length()+1));
        newStory.put("content", content);
        newStory.put("timestamp", timestamp);
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
        newStory.put("images", newImages);

        stories.put(stories.length(),newStory);
        contentFiles.saveContent(stories,FILEPATH);

    }

    @Override
    public JSONArray getUserContent(String userId) {
        JSONArray stories = contentFiles.loadContent(FILEPATH); // Load stories
        JSONArray userStories = new JSONArray();

        if (stories == null) {
            System.err.println("Error: Content file could not be loaded.");
            return userStories; // Return empty array if no content
        }

        LocalDateTime now = LocalDateTime.now(); // Current local time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Define time format

        for (int i = 0; i < stories.length(); i++) {
            try {
                JSONObject story = stories.getJSONObject(i);
                if (story.getString("authorId").equals(userId)) {
                    String storyTimestampStr = story.getString("timestamp"); // Assuming "timestamp" is in the formatted string
                    LocalDateTime storyTime = LocalDateTime.parse(storyTimestampStr, formatter);

                    // Calculate hours since story was posted
                    long hoursSincePosted = Duration.between(storyTime, now).toHours();
                    if (hoursSincePosted <= 24) { // Check if story is within the last 24 hours
                        userStories.put(story);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error processing story at index " + i + ": " + e.getMessage());
            }
        }

        return userStories;
    }

    @Override
    public JSONArray getNewsFeedContent(String userId) {
        List<String> friendsIDs = friendShipService.getFriends(userId);  // Get the list of friend IDs
        JSONArray feedStories = new JSONArray();

        if (friendsIDs == null || friendsIDs.isEmpty()) {
            System.err.println("Error: No friends found for userId " + userId);
            return feedStories; // Return empty feed if no friends
        }

        LocalDateTime now = LocalDateTime.now(); // Current local time

        for (String friendId : friendsIDs) {
            try {
                JSONArray friendStories = getUserContent(friendId); // Get stories for each friend
                for (int i = 0; i < friendStories.length(); i++) {
                    JSONObject story = friendStories.getJSONObject(i);
                        feedStories.put(story);

                }
            } catch (Exception e) {
                System.err.println("Error fetching stories for friendId " + friendId + ": " + e.getMessage());
            }
        }

        return feedStories;
    }



}