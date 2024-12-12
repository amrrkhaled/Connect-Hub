package backend.Groups;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IStorageHandler {
    JSONObject loadData(String filePath);

    void saveData(JSONObject joinRequests, String filePath);

    JSONArray loadDataAsArray(String filePath);
    public void saveDataAsArray(JSONArray data, String filePath);
}
