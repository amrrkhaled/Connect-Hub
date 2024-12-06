package backend.friendship;

import backend.user.*;

public class FriendRequestServiceFactory {
    private static FriendRequestServiceFactory instance;

    private FriendRequestServiceFactory() {}

    // Singleton instance getter
    public static synchronized FriendRequestServiceFactory getInstance() {
        if (instance == null) {
            instance = new FriendRequestServiceFactory();
        }
        return instance;
    }

    // Factory method to create a FriendRequestService instance
    public FriendRequestService createFriendRequestService() {
        ILoadUsers loadUsers = LoadUsers.getInstance();
        IUserRepository userRepository = UserRepository.getInstance(loadUsers);
        ILoadFriendShips loadFriendShips = LoadFriendShips.getInstance();
        IFriendshipService friendshipService = FriendshipService.getInstance(userRepository,loadFriendShips); // Assuming the FriendshipService also follows Singleton
        return new FriendRequestService(userRepository, friendshipService, loadFriendShips, loadUsers);
    }

}
