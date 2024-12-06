package backend.contentCreation;

import backend.friendship.*;

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
        FriendRequestServiceFactory factory = FriendRequestServiceFactory.getInstance();
        FriendRequestService service = factory.createFriendRequestService();
        return Story.getInstance(contentFiles,service.getFriendshipService(),service);  // Return the Story instance
    }
}