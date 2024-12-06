package backend.user;

public class UserFactory implements IUserFactory {
    private static UserFactory instance;

    private UserFactory() {
        // Private constructor to enforce singleton pattern
    }

    public static synchronized UserFactory getInstance() {
        if (instance == null) {
            instance = new UserFactory();
        }
        return instance;
    }
    @Override
    public UserManager createUserManager() {
        // Create necessary singleton dependencies
        ILoadUsers loadUsers = LoadUsers.getInstance();
        IAddUser addUser = new AddUser(loadUsers);
        IUpdateUser updateUser = UpdateUser.getInstance();
        IPasswordUtils passwordUtils = PasswordUtils.getInstance(loadUsers, updateUser);
        Validation validation = UserValidator.getInstance(loadUsers,passwordUtils);
        IUserRepository userRepository = UserRepository.getInstance(loadUsers);
        return new UserManager(addUser, loadUsers, validation, updateUser, userRepository);
    }
}
