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
        IFriendShipManager manager = FriendShipManager.getInstance(loadFriendShips, userRepository, loadUsers); // Singleton instance
        IFriendShipValidation validation = FriendShipValidation.getInstance();
        return new FriendShip(userRepository, loadFriendShips, validation, manager, loadUsers);
    }
}
