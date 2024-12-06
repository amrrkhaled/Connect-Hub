package backend.friendship;

import backend.user.ILoadUsers;
import backend.user.IUserRepository;
import backend.user.LoadUsers;
import backend.user.UserRepository;

public class FriendShipFactory {
    public static FriendShip createFriendShip() {
        ILoadFriendShips loadFriendShips = LoadFriendShips.getInstance();
        ILoadUsers loadUsers = LoadUsers.getInstance();
        IUserRepository userRepository = UserRepository.getInstance(loadUsers);
        IFriendshipService friendshipService = FriendshipService.getInstance(userRepository,loadFriendShips);
        IFriendRequestService friendRequestService = FriendRequestServiceFactory.getInstance().createFriendRequestService();
        IFriendShipValidation validation = FriendShipValidation.getInstance();
        return new FriendShip(userRepository, loadFriendShips, validation, friendshipService,friendRequestService, loadUsers);
    }
}
