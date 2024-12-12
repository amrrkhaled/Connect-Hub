package backend.backendTests;

import backend.contentCreation.ContentFiles;
import backend.contentCreation.IContentFiles;
import backend.contentCreation.Post;
import backend.contentCreation.PostFactory;
import backend.friendship.*;
import backend.notifications.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class jsonExample {
    public static void main(String[] args) {
//      String username1 = "asser";
//      String password1 = "123454678";
//      String dof1 = "2004-10-12";
//      String email1 = "a.freepsn@gmail.com";
//    ILoadUsers loadUsers = new LoadUsers();
//      IAddUser user = new AddUser(loadUsers);
//      Validation valid = new UserValidator(loadUsers);
//      IUpdateUser updateUser = new UpdateUser();
//      UserManager manager = new UserManager(user, loadUsers, valid, updateUser);
//      manager.signup(username1, password1, email1, dof1);
//      String username2 = "alo";
//      String password2 = "123454678";
//      String dof2 = "2004-10-12";
//      String email2 = "a.fdfsfs@mil.com";
//      UserManager manager2 = new UserManager(user, loadUsers, valid, updateUser);
//     manager2.signup(username2, password2 , email2 , dof2);
//       String username3 = "yamal";
//       String password3 ="123454678";
//       String dof3 ="2004-10-12";
//       String email3 ="mohamddded@mss.com";
//       UserManager manager3 = new UserManager(user, loadUsers, valid , updateUser);
//       manager3.signup(username3, password3 , email3 , dof3);
//       String username4 = "eid";
//       String password4 ="123454678";
//       String dof4 ="2004-10-12";
//       String email4 ="mohamed@mere.com";
//       UserManager manager4 = new UserManager(user, loadUsers, valid , updateUser);
//     //  manager4.signup(username4, password4, email4 , dof4);
//       ILoadFriendShips loadFriendShips = new LoadFriendShips();
//       IUserRepository userRepository = new UserRepository(loadUsers);
//       IFriendShipManager manager5 = new FriendShipManager(loadFriendShips, userRepository,loadUsers);
//       IFriendShipValidation validation = new FriendShipValidation();
//       FriendShip friendShip = new FriendShip(userRepository , loadFriendShips,validation,manager5);
//      friendShip.addFriend("U8","ahmed");
//       friendShip.acceptFriend("U8","ahmed");
//       friendShip.acceptFriend("U6","eid");
//       friendShip.addFriend("U1","yamal");
//       friendShip.addFriend("U5","ahmed");

//       friendShip.addFriend("U1","omar");
//       friendShip.addFriend("U1","mohamed");
//       friendShip.addFriend("U1","eid");
//       friendShip.addFriend("U2","mohamed");
//       friendShip.addFriend("U2","eid");
//       friendShip.addFriend("U3","eid");
//       System.out.println("Pending friends for ahmed: ");
//      List<String> friends = manager5.getPendingFriends("U1");
//       for (String friend : friends) {
//          System.out.println("Pending friend: " + friend);
//       }
//       System.out.println("Pending friends for mohamed: ");
//       List<String> friends2 = manager5.getPendingFriends("U2");
//       for (String friend : friends2) {
//          System.out.println("Pending friend: " + friend);
//       }
//       List<String> friends3 = manager5.getFriendRequests("U4");
//       for (String friend : friends3) {
//          System.out.println("Friend request: " + friend);
//       }
//       List<String> friends4 = manager5.getFriendRequests("U2");
//       for (String friend : friends4) {
//          System.out.println("Friend request: " + friend);
//       }
//       List<String> friends5 =manager5.getFriends("U1");
//       for (String friend : friends5) {
//          System.out.println("Friend request: " + friend);
//       }
//       friendShip.acceptFriend("U2","ahmed");
//        friends5 =manager5.getFriends("U1");
//        for (String friend : friends5) {
//            System.out.println("Friend request: " + friend);
//        }
//        manager2.login(username2, password2);
//        friends5 =manager5.getFriends("U1");
//        for (String friend : friends5) {
//            System.out.println("Friend request: " + friend);
//        }
//        friendShip.acceptFriend("U3","ahmed");
////        friendShip.acceptFriend("U4","ahmed");
//        List<String> friends6 =manager5.getFriendSuggestions("U2");
//        for (String friend : friends6) {
//            System.out.println (friend);
//        }
        // Initialize dependencies
//          ILoadFriendShips loadFriendShips = new LoadFriendShips();
//          ILoadUsers loadUsers = new LoadUsers();
//          IUserRepository userRepository = new UserRepository(loadUsers);
//          IFriendShipManager manager = new FriendShipManager(loadFriendShips, userRepository);
//
//          // Fetch data
//          List<String> friends = manager.getFriendsWithStatus("U1");
//          List<String> friendRequests = manager.getFriendRequests("U1");
//          List<String> pendingFriendRequests = manager.getPendingFriends("U1");
//          List<String> friendSuggestions = manager.getFriendSuggestions("U1");
//
//          // Print data
//          System.out.println("Friends with Status:");
//          friends.forEach(System.out::println);
//
//          System.out.println("\nFriend Requests:");
//          friendRequests.forEach(System.out::println);
//
//          System.out.println("\nPending Friend Requests:");
//          pendingFriendRequests.forEach(System.out::println);
//
//          System.out.println("\nFriend Suggestions:");
//          friendSuggestions.forEach(System.out::println);
//       }
//       ILoadFriendShips loadFriendShips = new LoadFriendShips();
//       ILoadUsers loadUsers = new LoadUsers();
//       IUserRepository userRepository = new UserRepository(loadUsers);
//       IFriendShipManager manager = new FriendShipManager(loadFriendShips, userRepository);
//       List<String> friends = manager.getFriendsWithStatus("U1");
//       List<String> friendRequests = manager.getFriendRequests("U1");
//       List<String> pendingFriendRequests = manager.getPendingFriends("U1");
//       List<String> friendSuggestions = manager.getFriendSuggestions("U1");
        PostFactory postFactory = PostFactory.getInstance();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = now.format(formatter);
        List<String> imagePaths = new ArrayList<>();
        imagePaths.add("/home/ahmed-sameh/Pictures/Screenshots/client1.jpg");
        postFactory.createPost().createContent("P1","i am a big man",formattedTimestamp,imagePaths);
        ILoadNotifications loadNotifications = new LoadNotifications();
        PostNotification postNotification = new PostNotification(loadNotifications);
        postNotification.createNotifications("U1","P1",formattedTimestamp);
        FriendShip friendShip = FriendShipFactory.createFriendShip();
        friendShip.addFriend("U1","sameh");
        FriendNotifications friendNotifications = new FriendNotifications(loadNotifications);
        friendNotifications.createNotifications("U1","U2" , formattedTimestamp);


    }


}
