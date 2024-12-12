package backend.backendTests;

import backend.Groups.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class GroupManagementTest {
        public static void main(String[] args) {
                // Mocking the required interfaces
                IStorageHandler storageHandler = new StorageHandler();
                ILoadGroups loadGroups = LoadGroups.getInstance(storageHandler);

                // Creating instances of controllers
                GroupManager groupManager = new GroupManager(loadGroups);
                NormalUserController normalUserController = new NormalUserController(loadGroups, storageHandler);
                PrimaryAdmin primaryAdminController = new PrimaryAdmin(loadGroups, storageHandler);
                Request requestController = new Request(loadGroups, storageHandler);

                // Simulating group creation
                System.out.println("Creating group...");
                groupManager.createGroup("admin1", "Group 1", "This is a test group", "C:\\Users\\amrkh\\Downloads\\WhatsApp Image 2024-06-06 at 21.59.04_ed24978c.jpg");

                // Simulating sending join requests
                System.out.println("Sending join requests...");
                requestController.sendJoinRequest("Group 1", "user1", "I want to join this group");
                requestController.sendJoinRequest("Group 1", "user2", "I'd love to be part of this group!");
                requestController.sendJoinRequest("Group 1", "user3", "Can I join as well?");
                requestController.sendJoinRequest("Group 1", "user4", "I'm interested in joining!");

                // Simulating adding posts by members
                System.out.println("Adding posts...");
                List<String> postImages = new ArrayList<>();
                postImages.add("C:\\Users\\amrkh\\Downloads\\WhatsApp Image 2024-06-06 at 21.59.04_ed24978c.jpg");
                normalUserController.addPost("Group 1", "user1", "This is a test post from user1", Instant.now().toString(), postImages);
                normalUserController.addPost("Group 1", "user2", "This is another post from user2", Instant.now().toString(), postImages);

                // Simulating adding admins
                System.out.println("Adding admins...");
                primaryAdminController.addAdminToGroup("Group 1", "user2");
                primaryAdminController.addAdminToGroup("Group 1", "user3");

                // Simulating removing an admin (user3)
                System.out.println("Removing admin (user3)...");
                primaryAdminController.removeAdmin("Group 1", "user3");

                // Simulating accepting members into the group
                System.out.println("Accepting members...");
                primaryAdminController.acceptMember("Group 1", "user1");
                primaryAdminController.acceptMember("Group 1", "user2");
                primaryAdminController.acceptMember("Group 1", "user3");
                primaryAdminController.acceptMember("Group 1", "user4");

                // Simulating removing a member (user1)
                System.out.println("Removing member (user1)...");
                primaryAdminController.removeMember("Group 1", "user1");

                // Simulating editing a post's content
                System.out.println("Editing post content...");
                primaryAdminController.editPostContent("P1", "This is the updated content of the post");

                // Simulating editing a post's images
                System.out.println("Editing post images...");
                List<String> updatedImages = new ArrayList<>();
                updatedImages.add("C:\\Users\\amrkh\\Downloads\\WhatsApp Image 2024-06-06 at 21.59.04_ed24978c.jpg");
                primaryAdminController.editPostImages("P1", updatedImages);

                // Simulating leaving a group (user2)
                System.out.println("User leaving group (user2)...");
                normalUserController.leaveGroup("Group 1", "user2");

                // Simulating re-adding admin (user1)
                System.out.println("Re-adding admin (user1)...");
                primaryAdminController.addAdminToGroup("Group 1", "user1");

                // Simulating sending another join request (user5)
                System.out.println("Sending another join request (user5)...");
                requestController.sendJoinRequest("Group 1", "user5", "Can I join again?");
        }
}
