package backend.contentCreation;
import org.json.JSONArray;

public interface IContentFiles {
     JSONArray loadContent(String filePath);
     void saveContent(JSONArray content , String filePath);
}
