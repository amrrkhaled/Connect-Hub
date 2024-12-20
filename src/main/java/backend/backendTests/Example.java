package backend.backendTests;
import backend.friendship.FriendShip;
import backend.friendship.FriendShipFactory;
import backend.notifications.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
public class Example {
    public static void main(String[] args) {
        FriendShip friendShip = FriendShipFactory.createFriendShip();
        LoadNotifications loadNotifications = LoadNotifications.getInstance();
        FriendNotifications friendNotifications = new FriendNotifications(loadNotifications);
//        ICommentNotifications commentNotifications = new CommentNotifications(loadNotifications);
//        commentNotifications.createNotifications("U1","P4",getCurrentTimestamp());
        ILikeNotifications likeNotifications = new LikeNotifications(loadNotifications);
        likeNotifications.createNotifications("U2","P4",getCurrentTimestamp());
//        friendShip.addFriend("U1","sameh");
//        friendShip.addFriend("U3","sameh");
//        friendShip.addFriend("U4","sameh");
//
//        friendShip.acceptFriend("U2","amr");
////        List<String> actualMessages = notificationService.getNotificationMessages("U1");
//
//        // Print results
//        IPostNotifications postNotifications = new PostNotification(loadNotifications);
//        String authorId = "U1";
//
//        postNotifications.createNotifications(authorId,"P5",getCurrentTimestamp());
    }
    public static String getCurrentTimestamp() {
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        return now.format(formatter);
    }
    }
