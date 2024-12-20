package frontend.notifications;


public class notificationsThread extends Thread {
    @Override
    public void run() {
        NotificationController notificationController = new NotificationController();
        System.out.println("Notification thread started");
        while (true) {
            notificationController.updateNotifications();
            System.out.println("Notification thread started");
        }


    }
}
