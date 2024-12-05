package backend.friendship;

import backend.user.ILoadUsers;
import backend.user.IUserRepository;
import backend.user.LoadUsers;
import backend.user.UserRepository;

public class FriendShipFactory {
    public static FriendShip createFriendShip() {
        ILoadFriendShips loadFriendShips = new LoadFriendShips();
        ILoadUsers loadUsers = LoadUsers.getInstance();
        IUserRepository userRepository = UserRepository.getInstance(loadUsers);
        IFriendShipManager manager = FriendShipManager.getInstance(loadFriendShips, userRepository, loadUsers); // Singleton instance
        IFriendShipValidation validation = new FriendShipValidation();
        IFriendShipValidation friendShipValidation = new FriendShipValidation();
        return new FriendShip(userRepository, loadFriendShips, friendShipValidation,manager, loadUsers);
    }
}
