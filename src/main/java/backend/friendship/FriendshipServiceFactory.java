package backend.friendship;

import backend.user.*;

public class FriendshipServiceFactory {
    private static FriendshipServiceFactory instance;

    private FriendshipServiceFactory() {}

    // Singleton instance getter
    public static synchronized FriendshipServiceFactory getInstance() {
        if (instance == null) {
            instance = new FriendshipServiceFactory();
        }
        return instance;
    }

    // Factory method to create a FriendshipService instance
    public FriendshipService createFriendshipService() {
        ILoadUsers loadUsers = LoadUsers.getInstance();
        IUserRepository userRepository = UserRepository.getInstance(loadUsers);
        ILoadFriendShips loadFriendShips = LoadFriendShips.getInstance();
        return FriendshipService.getInstance(userRepository, loadFriendShips);
    }
}
