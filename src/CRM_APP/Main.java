package CRM_APP;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        setPrimaryStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("View/Login/login.fxml"));
        primaryStage.setTitle("NphCRM");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void setPrimaryStage(Stage stage) {
        Main.primaryStage = stage;
    }

    static public Stage getPrimaryStage() {
        return Main.primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
