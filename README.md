# **Connect Hub - README**

## **Overview**
Connect Hub is a social media platform that allows users to connect with friends, share posts, and manage their profiles. It is built with Java for the backend and Swing for the user interface. The platform supports features such as user account management, content creation, and real-time status updates.

---

## **Features**

### 1. **User Account Management**
- **Signup**: Create a new account with email, username, password, and date of birth.
- **Login/Logout**: Secure authentication and status updates.
- **Validation**: Ensures proper input formats and secure password storage using hashing (SHA-256).

### 2. **Profile Management**
- Update profile details (bio, profile photo, and cover photo).
- Change password with secure validation.
- View personal posts and manage friends.

### 3. **Content Creation**
- **Posts**: Create posts with text and optional images.
- **Stories**: Create ephemeral stories that expire after 24 hours.

### 4. **Friend Management**
- Send, accept, and decline friend requests.
- Block or remove friends.
- View friend suggestions.

### 5. **Newsfeed**
- Display friends’ posts and stories.
- Show real-time online/offline status of friends.
- Include friend suggestions for potential connections.

### 6. **Security**
- Secure password storage using hashing.
- Input validation to prevent security vulnerabilities.
- Error handling for safe and user-friendly operations.

---

## **Installation**

1. **Prerequisites**:
   - Java Development Kit (JDK) 8 or higher.
   - A development environment such as IntelliJ IDEA or Eclipse.

2. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/connect-hub.git
   ```

3. **Build the Project**:
   - Open the project in your preferred IDE.
   - Ensure all dependencies are configured.

4. **Run the Application**:
   - Run the `Main.java` file to start the application.

---

## **Project Structure**

```plaintext
connect-hub/
├── src/
│   ├── auth/
│   │   ├── Login.java
│   │   ├── Signup.java
│   ├── profile/
│   │   ├── ProfileManager.java
│   │   ├── BioEditor.java
│   ├── content/
│   │   ├── PostManager.java
│   │   ├── StoryManager.java
│   ├── friend/
│   │   ├── FriendManager.java
│   │   ├── FriendSuggestions.java
│   ├── newsfeed/
│   │   ├── Newsfeed.java
│   ├── utils/
│   │   ├── Database.java
│   │   ├── Security.java
│   └── Main.java
├── data/
│   ├── users.json
│   ├── posts.json
│   ├── friendships.json
├── README.md
└── LICENSE
```

---

## **Usage**

### **1. User Account Management**
- Run the application and navigate to the signup page to create an account.
- Use the login page to access your account and update your profile.

### **2. Profile Management**
- Navigate to the profile page to edit your bio, upload photos, or change your password.

### **3. Create Content**
- Use the "Create Post" and "Create Story" buttons to share updates.
- Posts appear in the newsfeed, and stories expire after 24 hours.

### **4. Manage Friends**
- Use the friend request section to send and manage requests.
- View suggestions and block/remove friends as needed.

### **5. Newsfeed**
- See the latest updates from friends in the newsfeed.
- Refresh to view the latest posts and statuses.

---

## **Database Schema (JSON)**

### **Users**
```json
{
  "userId": "unique-id",
  "email": "user@example.com",
  "username": "JohnDoe",
  "password": "hashed-password",
  "dateOfBirth": "2000-01-01",
  "status": "offline"
}
```

### **Posts**
```json
{
  "contentId": "unique-id",
  "authorId": "user-id",
  "content": "Hello, world!",
  "imagePath": "path/to/image.jpg",
  "timestamp": "2024-11-30T15:00:00Z",
  "type": "post"
}
```

### **Friendships**
```json
{
  "userId": "unique-id",
  "friendId": "friend-id",
  "status": "Accepted"
}
```

---

## **Contributing**

1. Fork the repository.
2. Create a new branch for your feature or bug fix:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes:
   ```bash
   git commit -m "Description of your changes"
   ```
4. Push to your branch:
   ```bash
   git push origin feature-name
   ```
5. Open a pull request.

---
