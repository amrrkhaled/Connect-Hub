package backend;

import backend.profile.ILoadProfiles;
import backend.profile.LoadProfiles;
import backend.user.*;

public class jsonExample {
    public static void main(String[] args) {
        String username = "ahmedddd";
        String password = "1234556w7";
        String dof = "2004-10-12";
        String email = "a.samehmepsn@ssgfmx.com";
        ILoadUsers loadUsers = new LoadUsers();
        ILoadProfiles loadProfiles = new LoadProfiles();
        IAddUser user = new AddUser(loadUsers);
        Validation valid = new UserValidator(loadUsers);
        IUpdateUser updateUser = new UpdateUser();
        UserManager manager = new UserManager(user, loadUsers, valid, updateUser);
        String msg = manager.signup(username, password,email,dof);
        msg = manager.login(username, password);
        System.out.println(msg);
        String newPassword = "12345567";
//        String username1 = valid.findUsernameByUserId("U1");
//        System.out.println(username1);
//        PasswordUtils passwordManager = new PasswordUtils(loadUsers, updateUser);
//        passwordManager.updatePasswordHashForUser(username, newPassword);
//        IAddProfile addProfile = new AddProfile(loadProfiles);
//        IUpdateProfile updateProfile = new UpdateProfile();

    }
}