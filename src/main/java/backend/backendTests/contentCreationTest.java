package backend.backendTests;
import backend.contentCreation.ContentFiles;
import backend.contentCreation.IContent;
import backend.contentCreation.Post;
import backend.contentCreation.PostFactory;

import java.util.List;

public class contentCreationTest {
    public static void main(String[] args) {
        String filePath = "posts/test.json";
        String authorId = "authorId123";
        String contentId = "contentId123";
        String content = "This is a new post";
        String timestamp = "2024-12-03T12:00:00Z";
        List<String> images = List.of("image3.jpg", "image4.jpg");
        PostFactory postFactory = PostFactory.getInstance();
        IContent contentCreation = postFactory.createPost();
        contentCreation.createContent(authorId, content, timestamp, null);
    }
}
