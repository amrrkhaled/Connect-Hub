module com.example.connecthub {
    requires javafx.controls;
    requires javafx.fxml;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.json;
    requires java.desktop;
    requires java.base;
    opens frontend to javafx.fxml;
    exports frontend;
    exports backend.chatManagement;
    exports backend.user;



    exports frontend.notifications to javafx.fxml; // Export to JavaFX FXML module
    opens frontend.notifications to javafx.fxml;

    exports frontend.friendshipManagement;
    opens frontend.friendshipManagement to javafx.fxml;

    exports frontend.chatManagement;
    opens frontend.chatManagement to javafx.fxml;

    exports frontend.groupManagement;
    opens frontend.groupManagement to javafx.fxml;

    exports frontend.searchManagement;
    opens frontend.searchManagement to javafx.fxml;

    exports frontend.contentCreation;
    opens frontend.contentCreation to javafx.fxml;


    exports frontend.profileManagement;
    opens frontend.profileManagement to javafx.fxml;

    exports frontend.userManagement;
    opens frontend.userManagement to javafx.fxml;

    exports frontend.newsFeed;
    opens frontend.newsFeed to javafx.fxml;

    exports frontend.controllersTest;
    opens frontend.controllersTest to javafx.fxml;


}



