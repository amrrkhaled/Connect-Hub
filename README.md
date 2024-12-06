# Connect Hub

**Connect Hub** is a social networking platform developed in Java with the backend powered by core Java and the frontend using Java Swing. The platform allows users to manage accounts, create posts, manage friendships, and update profiles. It utilizes a file-based database (JSON format) for data management. The system emphasizes simplicity, usability, and security.

## Objective
The primary goal of **Connect Hub** is to create a social networking platform that supports:
- User management (sign up, login, logout, and profile management)
- Content creation (posts and stories)
- Friend management (sending requests, accepting, removing, blocking)
- A newsfeed that shows content from friends and suggests new friends

## Features
- **User Account Management**: 
  - Users can sign up, log in, and log out.
  - Passwords are securely hashed before storing.
  - Users' online/offline status is tracked.
  
- **Profile Management**: 
  - Users can update profile photos, cover photos, bio, and passwords.
  - Users can view their posts and manage their friend list.

- **Content Creation**:
  - **Posts**: Users can create posts (permanent content).
  - **Stories**: Users can create stories that disappear after 24 hours.
  
- **Friend Management**:
  - Send friend requests, accept/reject, block or remove friends.
  - Display online/offline status of friends in the newsfeed.

- **Newsfeed**:
  - Display posts and stories from friends.
  - Suggest potential new friends.
  - Provide a form for creating new posts or stories.

## Requirements
### 1. **User Account Management**
  - **Backend**:
    - Implement user signup, login, and logout functionalities.
    - Validate email format and ensure all required fields are provided.
    - Use hashed passwords (e.g., SHA-256).
    - Track user status (online/offline).
    
  - **Frontend**:
    - Implement forms for signup and login with error handling for invalid credentials.
    - Display the online/offline status on the user's profile and newsfeed.

### 2. **Profile Management**
  - **Backend**:
    - Allow users to update their profile (photo, bio, and password).
    - Fetch and display the user’s posts and friends list, along with their statuses (online/offline).
    
  - **Frontend**:
    - Create a profile page where users can update their information and view their posts.

### 3. **Content Creation (Posts and Stories)**
  - **Backend**:
    - Allow users to create posts and stories with text and optional images.
    - Automatically remove expired stories from the system.

  - **Frontend**:
    - Design a content creation section in the newsfeed for creating posts and stories.

### 4. **Friend Management**
  - **Backend**:
    - Implement friend requests (pending, accepted, declined).
    - Allow users to block or remove friends.
    - Display online/offline status of friends.
    
  - **Frontend**:
    - Create a friend management interface to handle friend requests and manage friends.

### 5. **Newsfeed**
  - **Backend**:
    - Provide support for fetching posts, stories, and friend suggestions.
    - Serve updated user statuses and new content.
    
  - **Frontend**:
    - Design a newsfeed page that displays recent posts and active stories, with sections for friend suggestions and content creation.

## File Structure


## **Project Structure**

```plaintext
/project-root
├── /src
│   ├── /main
│   │   ├── /java
│   │   │   ├── /backend            # Backend logic for user accounts, posts, etc.
│   │   │   │   ├── User.java
│   │   │   │   ├── Post.java
│   │   │   │   ├── Friend.java
│   │   │   │   └── Profile.java
│   │   │   ├── /frontend           # Controllers for each frontend page
│   │   │   │   ├── LoginController.java
│   │   │   │   ├── ProfileController.java
│   │   │   │   ├── NewsfeedController.java
│   │   │   │   ├── PostController.java
│   │   │   │   └── FriendController.java
│   │   │   └── Main.java            # Main entry point of the application
│   │   ├── /resources
│   │   │   └── /fxml                # FXML layout files for GUI
│   │   │       ├── login.fxml
│   │   │       ├── profile.fxml
│   │   │       ├── newsfeed.fxml
│   │   │       └── post.fxml
│   │   └── /data                    # File-based data storage
│   │   |    └── profile.json         # Stores user profile data
│   │   │    ├── posts.json
│   │   │    ├── stories.json
│   │   │    ├── users.json
│   │   │    └── friendships.josn
│   └── /test
│       └── /backend                # Unit tests for backend logic
│           └── jsonExample`.java
│           └── UserTest.java
│       └── /frontend               # Unit tests for frontend controllers
│           └── contentCreationTest.java
│           └── FeedTest.java
│           └── NewsfeedControllerTest.java
├── /lib                             # External libraries if used
├── README.md                        # Project description and guidelines
└── .gitignore                       # Git ignore file for version control
                        

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
## Setup and Installation

### Prerequisites
- **JDK 17+**: Ensure you have Java Development Kit installed.
- **JavaFX SDK**: Required for Swing components and UI development.

### Steps to Install
1. **Clone the Repository**:
    ```bash
    git clone https://github.com/amrrkhaled/Connect-Hub.git
    cd Connect-Hub
    ```

2. **Set Up JavaFX**:
    - Download and configure the **JavaFX SDK**.
    - In your IDE, configure the JavaFX library.

3. **Run the Application**:
    - Open `Main.java` and run the project.
    - If you face any issues with JavaFX setup, make sure the correct path is configured in your IDE settings.

## Backend Functionality
- **User Management**: User signup, login, logout are handled with input validation. Passwords are securely stored using SHA-256.
- **Profile Management**: Users can edit their profiles. The backend stores updated data in a JSON file.
- **Content Creation**: Posts are stored with unique identifiers, content, and timestamps. Stories automatically expire after 24 hours.
- **Friend Management**: Users can send, accept, or reject friend requests. They can also block or remove friends, and see friends' statuses.

## Frontend Functionality
- **Login/Signup Form**: Provides forms for logging in and signing up users.
- **Profile Management**: Users can view and update their details, including photos and bio.
- **Newsfeed**: Displays posts and stories, and suggests new friends based on user activity.

## Security
- **Password Hashing**: Implement SHA-256 for secure password hashing.
- **Input Validation**: Ensure proper validation for user input fields like email, passwords, etc.
- **Error Handling**: Ensure error handling for failed login attempts.

## Database Management
- **JSON Files**: Data is stored in JSON format, which is easily readable and lightweight for small-scale projects.
- **File Structure**: Each data type (users, posts, stories) has its own JSON file.
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
