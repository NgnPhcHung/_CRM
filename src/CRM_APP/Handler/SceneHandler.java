package CRM_APP.Handler;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneHandler {
    //handle button change scene
    public void newScene(String url){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(url));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();
    }

    protected void handleScene(BorderPane main_pane, String url) throws IOException {
        GridPane pane = new FXMLLoader().load(getClass().getResource(url));
        pane.prefWidthProperty().bind(main_pane.widthProperty());
        pane.prefHeightProperty().bind(main_pane.heightProperty());
        main_pane.setCenter(pane);
    }

    public static String getFileFXML(String name){
        String url ;
        String packageName = name.substring(0, 1).toUpperCase() + name.substring(1);;
        String fxmlName = name.substring(0, 1).toLowerCase() + name.substring(1);;
        url =  "/CRM_APP/View/"+packageName + "/"+fxmlName+".fxml";
        return url;
    }

    public static String getChooseFileFxml(String name, String folder){
        String url ;
        String fxmlName = name.toLowerCase();
        url =  "/CRM_APP/View/"+folder+"/"+fxmlName+".fxml";
        return url;
    }

    public double[] getScreen(){
        double[] coordinates = new double[2];

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        coordinates[0] = screenBounds.getHeight();
        coordinates[1] = screenBounds.getWidth();
        return coordinates;
    }
}
