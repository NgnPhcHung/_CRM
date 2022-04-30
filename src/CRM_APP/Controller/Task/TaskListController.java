package CRM_APP.Controller.Task;

import CRM_APP.Controller.Project.Project.ProjectCellController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Task.TaskDB;
import CRM_APP.Handler.DateTimePickerHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Module;
import CRM_APP.Model.Task;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
    private JFXButton btn_Total;

    @FXML
    private Label lbl_Total;

    @FXML
    private JFXListView<Task> lv_tasks;

    private ObservableList<Task> tasks;
    private SceneHandler sceneHandler;
    public static String modID;
    private Database database;
    private TaskDB taskDB ;
    private Task task;
    @FXML
    void filterEvent(ActionEvent event) {
        Button button = (Button) event.getSource();
        String btnName = button.getText().trim();

        switch (btnName){
            case "Pending":
                populateTask("0");
                break;
            case "Working":
                populateTask("1");
                break;
            case "Reviewing":
                populateTask("2");
                break;
            case "Done":
                populateTask("3");
                break;
            case "Total":
                populateTask("nor");
                break;
            default:break;
        }
    }

    @FXML
    void initialize() {
        filterCell();
        try {
            fillCard();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        btn_back.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_back, ProjectCellController.cellStack, "-Y", "/CRM_APP/View/Module/module.fxml");
        });
        populateTask("nor");
        btn_addNew.setOnAction(e->{
            sceneHandler = new SceneHandler();
            TaskDetailController.isAdmin = true;
            TaskDetailController.taskId= "";
            TaskDetailController.modID = modID;
            sceneHandler.slideScene(btn_back, ProjectCellController.cellStack, "-Y", "/CRM_APP/View/Task/taskDetail.fxml");
        });
    }

    private void populateTask(String status){
        sceneHandler= new SceneHandler();
        database = new Database();
        tasks = FXCollections.observableArrayList();
        ResultSet row = null;
        if(status.equals("nor")){
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
        }else{
            try {
                taskDB = new TaskDB();
                task = new Task();
                task.setStatus(status);
                task.setModID(modID);
                row = taskDB.getStatus(task);
                while(row.next()){
                    task = new Task();
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
            }
        }
    }

    private void filterCell(){
        txt_task.textProperty().addListener(((observable, oldValue, newValue) -> {
            sceneHandler= new SceneHandler();
            database = new Database();
            tasks = FXCollections.observableArrayList();
            ResultSet row = null;
            try {
                row = database.filterDataInput(Const.TASK_TABLE, Const.TASK_NAME, newValue);
                while(row.next()){
                    task = new Task();
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
            }
        }));
    }
    private void fillCard() throws SQLException, ClassNotFoundException {
        taskDB = new TaskDB();
        ResultSet row = taskDB.getCountStatus(modID);
        ResultSet row2 = taskDB.countAll(modID);
        String pending = "";
        String reviewing = "";
        String working = "";
        String done = "";
        String total = "";
        if(row.next() && row2.next()){
            pending = row.getString("Pending");
            working = row.getString("Working");
            reviewing = row.getString("Reviewing");
            done = row.getString("Done");
            total = row2.getString(1);
        }
        lbl_pending.setText(pending);
        lbl_working.setText(working);
        lbl_reviewing.setText(reviewing);
        lbl_done.setText(done);
        lbl_Total.setText(total);
    }
}
