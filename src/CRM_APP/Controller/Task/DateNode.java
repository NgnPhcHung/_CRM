package CRM_APP.Controller.Task;

import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Task;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

/**
 * Create an VBox pane that can store additional data.
 */
public class DateNode extends VBox {
    private Task task;
    // Date associated with this pane
    private LocalDate date;
    SceneHandler sceneHandler = new SceneHandler();

    public DateNode(Node... children) {
        super(children);
        // Add action handler for mouse clicked
        this.setOnMouseClicked(e -> {
            System.out.println(date);
            if(!this.getStyleClass().contains("notMonth")){
                //send data
                task = new Task();
                task.setStartDate(date);
                sceneHandler = new SceneHandler();
//                sceneHandler.newScene("/CRM_APP/View/Task/taskDetail.fxml");

            }
        });
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
