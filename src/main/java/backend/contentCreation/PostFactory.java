package backend.contentCreation;

import backend.friendship.FriendShip;
import backend.friendship.FriendShipFactory;
import backend.friendship.FriendShipManager;
import backend.friendship.IFriendShipManager;

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
        FriendShip friendShip= FriendShipFactory.createFriendShip();
        IFriendShipManager friendShipManager =  friendShip.getManager();
        return Post.getInstance(contentFiles, friendShipManager);
    }
}
