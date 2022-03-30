package CRM_APP.Controller.Task;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Task;
import com.jfoenix.controls.JFXListCell;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class TaskCellController extends JFXListCell<Task> {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox main_pane;

    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_module;

    @FXML
    private Label lbl_emp;

    @FXML
    private Button btn_edit;

    @FXML
    private Label lbl_status;

    private FXMLLoader fxmlLoader;
    private Database database;
    private SceneHandler sceneHandler;
    public
    @FXML
    void editEvent(ActionEvent event) {

    }

    @FXML
    void initialize() {
        sceneHandler = new SceneHandler();
        database = new Database();
    }
    @Override
    protected void updateItem(Task item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/CRM_APP/View/Task/taskCell.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            database = new Database();
            try {
                ResultSet row = database.getSomeID(item.getModID(), Const.MODULE_TABLE, Const.MODULE_ID);
                if(row.next()){
                    lbl_module.setText(row.getString(Const.MODULE_NAME));
                }
                lbl_name.setText(item.getTaskName());
                lbl_emp.setText(item.getEmpID());
                handleStatus(item.getStatus());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            setText(null);
            setGraphic(main_pane);
        }
    }
    private void handleStatus(String stt){
        lbl_status.getStylesheets().add("/CRM_APP/Style/DetailStyle.css");
        switch (stt) {
            case "0":
                lbl_status.setText("Pending");
                lbl_status.getStyleClass().add("pending");
                break;
            case "1":
                lbl_status.setText("Assign");
                lbl_status.getStyleClass().add("assigned");
                break;
            case "2":
                lbl_status.setText("Working");
                lbl_status.getStyleClass().add("working");
                break;
            case "3":
                lbl_status.setText("Review");
                lbl_status.getStyleClass().add("review");
                break;
            case "4":
                lbl_status.setText("Done");
                lbl_status.getStyleClass().add("done");
                break;
            default: break;
        }
    }
}
