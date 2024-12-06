package backend.contentCreation;

import backend.friendship.FriendShip;
import backend.friendship.FriendShipFactory;
import backend.friendship.IFriendShipManager;

public class StoryFactory {

    private static StoryFactory instance;

    private StoryFactory() {
    }

    public static synchronized StoryFactory getInstance() {
        if (instance == null) {
            instance = new StoryFactory();
        }
        return instance;
    }

    public Story createStory() {
        IContentFiles contentFiles = ContentFiles.getInstance();  // Assuming ContentFiles is the correct class
        FriendShip friendShip = FriendShipFactory.createFriendShip();  // Create the FriendShip instance
        IFriendShipManager friendShipManager = friendShip.getManager();  // Get the FriendShipManager
        return Story.getInstance(contentFiles, friendShipManager);  // Return the Story instance
    }
}