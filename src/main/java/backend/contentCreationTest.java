package backend;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class contentCreationTest {
    public static void main(String[] args) {
        String filePath = "posts/test.json";
        String authorId = "authorId123";
        String contentId = "contentId123";
        String content = "This is a new post";
        String timestamp = "2024-12-03T12:00:00Z";
        List<String> images = List.of("image1.jpg", "image2.jpg");
        IContentCreation contentCreation = new Post(new ContentFiles());
        contentCreation.createContent(authorId, content, timestamp, null);
    }
}
