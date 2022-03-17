package CRM_APP.Controller.Task;

import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Create an VBox pane that can store additional data.
 */
public class DateNode extends VBox {
    private Task task;
    // Date associated with this pane
    private LocalDate date;
    SceneHandler sceneHandler = new SceneHandler();

//     Create a VBox node. Date is not assigned in the constructor.
//     children children of the anchor pane

    public DateNode(Node... children) {
        super(children);
        // Add action handler for mouse clicked
        this.setOnMouseClicked(e -> {
            System.out.println(date);
            if(!this.getStyleClass().contains("notMonth")){

                //send data
                Task.date = date;
//                task.setDate(date);
                sceneHandler.newScene("/CRM_APP/View/Task/taskDetail.fxml");
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
