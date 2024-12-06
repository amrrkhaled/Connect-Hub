package backend.profile;

public class ProfileManagerFactory {
    private static ProfileManagerFactory instance;

    private ProfileManagerFactory() {}

    public static synchronized ProfileManagerFactory getInstance() {
        if (instance == null) {
            instance = new ProfileManagerFactory();
        }
        return instance;
    }

    public ProfileManager createProfileManager(String userId) {
        ILoadProfiles loadProfiles = LoadProfiles.getInstance();
        IUpdateProfile updateProfile = UpdateProfile.getInstance();
        IProfileRepository profileRepository = ProfileRepository.getInstance(loadProfiles); // Singleton
        return new ProfileManager(loadProfiles, userId,updateProfile,profileRepository);
    }
}