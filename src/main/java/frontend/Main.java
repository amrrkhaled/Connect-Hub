package frontend;

//import backend.threads.storiesThread;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;



public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("signup.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        primaryStage.setTitle("Signup");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Program is closing, shutting down...");
            System.exit(0);
        });

        primaryStage.show();
    }

    public static void main(String[] args) {

//        storiesThread storiesThread = new storiesThread();
//        storiesThread.start();  // This runs the thread in the background

        // Main program continues executing concurrently
        System.out.println("Main program is running while the thread is also running.");


        launch(args);
        // Keep the main program running if necessary (for example, using a loop)




    }
}
