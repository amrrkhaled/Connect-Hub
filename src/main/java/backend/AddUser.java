package backend;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
public class AddUser implements IAddUser{
    private int usersNumber = 1;
    private final String filePath = "data/users.json";
    private final ILoadUsers userLoader;
    public AddUser(ILoadUsers userLoader) {
        this.userLoader = userLoader;
    }
    public void addUser(String username, String password, String email, String dob) {
        JSONObject newUser = new JSONObject();
        newUser.put("userId", "U" + usersNumber++);
        newUser.put("username", username);
        newUser.put("password", PasswordUtils.hashPassword(password));
        newUser.put("email", email);
        newUser.put("dateOfBirth", dob);
        newUser.put("status","offline");
        JSONArray usersArray = userLoader.loadUsers();
        usersArray.put(newUser);
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(newUser.toString(4));
            System.out.println("Successfully written to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
