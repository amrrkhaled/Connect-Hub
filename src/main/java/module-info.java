module com.example.connecthub {
    requires javafx.controls;
    requires javafx.fxml;

    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.json;

    opens frontend to javafx.fxml;
    exports frontend;

    exports frontend.contentCreation;
    opens frontend.contentCreation to javafx.fxml;


    exports frontend.profileManagement;
    opens frontend.profileManagement to javafx.fxml;

    exports frontend.userManagement;
    opens frontend.userManagement to javafx.fxml;

}