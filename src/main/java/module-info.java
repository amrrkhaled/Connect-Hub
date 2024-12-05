module com.example.connecthub {
    requires javafx.controls;
    requires javafx.fxml;

    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.json;

    opens frontend to javafx.fxml;
    exports frontend;
    exports frontend.friendshipManagement;
    opens frontend.friendshipManagement to javafx.fxml;
    exports frontend.profileManagement;
    opens frontend.profileManagement to javafx.fxml;
}