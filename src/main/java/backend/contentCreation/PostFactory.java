package backend.contentCreation;

import backend.friendship.*;
// import backend.friendship.FriendShipManager;


public class PostFactory {

    private static PostFactory instance;

    private PostFactory() {
    }

    public static synchronized PostFactory getInstance() {
        if (instance == null) {
            instance = new PostFactory();
        }
        return instance;
    }

    public Post createPost() {
        IContentFiles contentFiles = ContentFiles.getInstance();
        FriendShip friendShip = FriendShipFactory.createFriendShip();
        FriendRequestServiceFactory factory = FriendRequestServiceFactory.getInstance();
        FriendRequestService service = factory.createFriendRequestService();
        return Post.getInstance(contentFiles,service.getFriendshipService(),service);
    }
}
