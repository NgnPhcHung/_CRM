package CRM_APP.Handler;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class SceneHandler {
    FXMLLoader loader ;
    //handle button change scene
    public void newScene(String url){
        loader = new FXMLLoader();
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

    public void slideScene(Control btn, StackPane stack_parentContainer, String dir, String url) {
        Parent root = null;
        try {
            if(dir.equals("Y")){
                root = FXMLLoader.load(getClass().getResource(url));
                Scene scene = btn.getScene();
                root.translateYProperty().set(scene.getHeight());
                stack_parentContainer.getChildren().add(root);

                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame(Duration.seconds(0.6), kv);
                timeline.getKeyFrames().add(kf);
                timeline.play();
            }else if(dir.equals("X")){
                root = FXMLLoader.load(getClass().getResource(url));
                Scene scene = btn.getScene();
                root.translateXProperty().set(scene.getWidth());
                stack_parentContainer.getChildren().add(root);

                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame(Duration.seconds(0.6), kv);
                timeline.getKeyFrames().add(kf);
                timeline.play();
            }else if(dir.equals("-Y")){
                root = FXMLLoader.load(getClass().getResource(url));
                Scene scene = btn.getScene();
                root.translateYProperty().set(scene.getHeight()*-1);
                stack_parentContainer.getChildren().add(root);

                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame(Duration.seconds(0.6), kv);
                timeline.getKeyFrames().add(kf);
                timeline.play();
            }else if(dir.equals("-X")){
                root = FXMLLoader.load(getClass().getResource(url));
                Scene scene = btn.getScene();
                root.translateXProperty().set(scene.getWidth()*-1);
                stack_parentContainer.getChildren().add(root);

                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame(Duration.seconds(0.6), kv);
                timeline.getKeyFrames().add(kf);
                timeline.play();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void slideSceneForTask(VBox btn, StackPane stack_parentContainer, String dir, String url) {
        Parent root = null;
        try {
            if(dir.equals("Y")){
                root = FXMLLoader.load(getClass().getResource(url));
                Scene scene = btn.getScene();
                root.translateYProperty().set(scene.getHeight());
                stack_parentContainer.getChildren().add(root);

                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame(Duration.seconds(0.6), kv);
                timeline.getKeyFrames().add(kf);
                timeline.play();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void handleScene(GridPane main_pane, String url) throws IOException {
        GridPane pane = new FXMLLoader().load(getClass().getResource(url));
        pane.prefWidthProperty().bind(main_pane.widthProperty());
        pane.prefHeightProperty().bind(main_pane.heightProperty());
//        main_pane.setSc(pane);
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
