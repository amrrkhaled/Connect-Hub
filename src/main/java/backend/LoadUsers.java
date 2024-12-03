package backend;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
public class LoadUsers implements ILoadUsers {
    private final String filePath = "data/users.json";
    public JSONArray loadUsers() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new JSONArray();
        }
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            return new JSONArray(content);
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }
}
