package CRM_APP.Controller.Task;

import CRM_APP.Controller.Project.ProjectCellController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.DateTimePickerHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Task;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TaskListController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_addNew;

    @FXML
    private JFXTextField txt_task;

    @FXML
    private JFXButton btn_pending;

    @FXML
    private Label lbl_pending;

    @FXML
    private JFXButton btn_assign;

    @FXML
    private Label lbl_assign;

    @FXML
    private JFXButton btn_working;

    @FXML
    private Label lbl_working;

    @FXML
    private JFXButton btn_reviewing;

    @FXML
    private Label lbl_reviewing;

    @FXML
    private JFXButton btn_done;

    @FXML
    private Label lbl_done;

    @FXML
    private JFXListView<Task> lv_tasks;
    private ObservableList<Task> tasks;
    private SceneHandler sceneHandler;
    public static String modID;
    private Database database;

    @FXML
    void filterEvent(ActionEvent event) {

    }

    @FXML
    void initialize() {
        sceneHandler = new SceneHandler();
        btn_back.setOnAction(e -> {
            sceneHandler.slideScene(btn_back, ProjectCellController.cellStack, "-Y", "/CRM_APP/View/Module/module.fxml");
        });
        populateTask();
        btn_addNew.setOnAction(e->{
            sceneHandler.slideScene(btn_back, ProjectCellController.cellStack, "-Y", "/CRM_APP/View/Task/taskDetail.fxml");
        });
    }

    private void populateTask(){
        sceneHandler= new SceneHandler();
        database = new Database();
        tasks = FXCollections.observableArrayList();
        ResultSet row = null;
        try {
            row = database.getSomeID(modID, Const.TASK_TABLE, "ModID");
            while(row.next()){
                Task task = new Task();


                task.setTaskID(row.getString(Const.TASK_ID));
                task.setTaskName(row.getString(Const.TASK_NAME));
                task.setEmpID(row.getString(Const.TASK_EMP_ID));
                task.setModID(row.getString(Const.TASK_MOD_ID));
                LocalDate start = DateTimePickerHandler.formatDate(row.getString("StartDate"));
                task.setStartDate(start);
                LocalDate end = DateTimePickerHandler.formatDate(row.getString("EndDate"));
                task.setEndDate(end);
                task.setStatus(row.getString(Const.TASK_STATUS));
                task.setColor(row.getString(Const.TASK_COLOR));

                tasks.add(task);
            }
            lv_tasks.setItems(tasks);
            lv_tasks.setCellFactory(TaskCellController -> new TaskCellController());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
