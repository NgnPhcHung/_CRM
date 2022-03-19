package CRM_APP.Controller.Task;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ResourceBundle;

import CRM_APP.Controller.Home.HomeController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Task.TaskDB;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Model.Employee;
import CRM_APP.Model.Task;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Window;

public class TaskDetailController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txt_name;

    @FXML
    private JFXDatePicker datePick_taskDate;

    @FXML
    private JFXDatePicker datePick_taskEnd;

    @FXML
    private JFXTextField txt_emp;

    @FXML
    private JFXToggleButton btn_status;

    @FXML
    private JFXTextArea txt_des;

    @FXML
    private JFXColorPicker colorPicker_taskColor;

    @FXML
    private JFXButton btn_save;

    private TaskDB db = new TaskDB();
    private Database database = new Database();
    private Employee em = new Employee();
    @FXML
    void saveEvent(ActionEvent event) throws SQLException, ClassNotFoundException {
        String taskID = OtherHandler.generateId();
        ResultSet taskRow = database.getSomeID(taskID, Const.TASK_TABLE, Const.TASK_ID);
        while (taskRow.next()){
            taskID = OtherHandler.generateId();
            taskRow = database.getSomeID(taskID, Const.TASK_TABLE, Const.TASK_ID);
        }
        String stt = btn_status.isSelected()? "1": "0";
        db.createTask(taskID, HomeController.userId, txt_name.getText(), stt, colorPicker_taskColor.getValue().toString()
                ,datePick_taskDate.getValue().toString(), datePick_taskEnd.getValue().toString());

    }

    @FXML
    void initialize() {
        Task task = new Task();
        txt_name.setText(task.getTask());
        datePick_taskDate.setValue(task.getDate());
        txt_emp.setText(HomeController.userId);
        Iterator<Window> itr = Window.impl_getWindows();
        System.out.println(itr);
    }
}
