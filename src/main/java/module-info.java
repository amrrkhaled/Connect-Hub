module com.example.connecthub {
    requires javafx.controls;
    requires javafx.fxml;

    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.json;

    opens frontend to javafx.fxml;
    exports frontend;
}