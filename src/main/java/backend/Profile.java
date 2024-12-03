package backend;

public class Profile {
    private String profilePhoto;
    private String coverPhoto;
    private String bio;
    private String userId;

    public Profile(String profilePhoto, String coverPhoto, String bio, String userId) {
        this.setProfilePhoto(profilePhoto);
        this.setCoverPhoto(coverPhoto);
        this.setBio(bio);
        this.setUserId(userId);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }
}
