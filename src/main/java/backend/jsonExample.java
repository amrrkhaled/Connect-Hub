package backend;

public class jsonExample {
    public static void main(String[] args) {
//       String username = "ahmed";
//       String password ="12345467";
//       String dof ="2004-10-12";
//       String email ="a.samehmepsn@gfmx.com";
       ILoadUsers loadUsers = new LoadUsers();
//       IAddUser user = new AddUser(loadUsers);
       Validation valid = new UserValidator(loadUsers);
//       IUpdateUser updateUser = new UpdateUser();
//       UserManager manager = new UserManager(user , loadUsers , valid , updateUser);
//       manager.logout(username);
//       String msg = manager.login(username, password);
//       System.out.println(msg);
       String userId1 = "U7";
       IFriendShipValidation fsv = new FriendShipValidation();
       String username1 = "amr khaled";
       ILoadFriendShips loadFriendShips = new LoadFriendShips();
       IFriendShipManager manager = new FriendShipManager();
       FriendShip manager1 = new FriendShip(valid ,loadFriendShips,fsv,manager);
//       manager1.addFriend(userId1,username1);
    //manager1.acceptFriend("U6","ahmed");
    manager1.BlockFriendship(userId1,username1);

    }
}
