package backend;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;

public class jsonExample {
    public static void main(String[] args) {
       String username = "amr khaled";
       String password ="123454678";
       String dof ="2004-10-12";
       String email ="a.samehmepsn@gfm.com";
       ILoadUsers loadUsers = new LoadUsers();
       IAddUser user = new AddUser(loadUsers);
       Validation valid = new UserValidator(loadUsers);
       UserManager manager = new UserManager(user , loadUsers , valid);
       String msg=manager.login(username,password);
       System.out.println(msg);
    }
}
