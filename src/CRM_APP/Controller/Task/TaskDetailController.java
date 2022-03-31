package CRM_APP.Controller.Task;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.ResourceBundle;

import CRM_APP.Controller.Home.HomeController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Task.TaskDB;
import CRM_APP.Handler.DateTimePickerHandler;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Model.Employee;
import CRM_APP.Model.Task;
import com.jfoenix.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
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
    private JFXComboBox<String> cb_employ;

    @FXML
    private ToggleButton tog_pend;

    @FXML
    private ToggleButton tog_assign;

    @FXML
    private ToggleButton tog_work;

    @FXML
    private ToggleButton tog_review;

    @FXML
    private ToggleButton tog_done;

    @FXML
    private JFXTextArea txt_des;

    @FXML
    private JFXColorPicker colorPicker_taskColor;

    @FXML
    private JFXButton btn_save;

    private TaskDB taskDB = new TaskDB();
    private Database database = new Database();
    public static LocalDate dates;
    public static String taskId;
    public static boolean isAdmin = false;
    private String status;

    @FXML
    void initialize() {

        //if not admin logged in
        if(!isAdmin){
            cb_employ.setDisable(true);
            tog_pend.setDisable(true);
            txt_name.setDisable(true);
            datePick_taskDate.setDisable(true);
            datePick_taskEnd.setDisable(true);
            populateDetail();
            manageToggle();

            btn_save.setOnAction(e ->{
                empSave();
                btn_save.getScene().getWindow().hide();
            });
        }

        //region SET TOGGLE DATA & HANDLE EVENT VALUE CHANGE
        ToggleGroup group = new ToggleGroup();
        tog_pend.setUserData(0);
        tog_assign.setUserData(1);
        tog_work.setUserData(2);
        tog_review.setUserData(3);
        tog_done.setUserData(4);

        tog_pend.setToggleGroup(group);
        tog_assign.setToggleGroup(group);
        tog_work.setToggleGroup(group);
        tog_done.setToggleGroup(group);
        tog_review.setToggleGroup(group);
        status=group.getSelectedToggle().getUserData()+"";
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    status=group.getSelectedToggle().getUserData()+"";
                }
            }
        });
        //endregion
    }
    //employee save
    void empSave(){
        Task task = new Task();
        Color color = colorPicker_taskColor.getValue();
        String des = txt_des.getText();
        task.setTaskID(taskId);
        task.setColor(color+"");
        task.setDes(des);
        task.setStatus(status);
        taskDB.empUpdateTask(task);
    }
    private void manageToggle(){
        try {
            ResultSet row = database.getSomeID(taskId, Const.TASK_TABLE, Const.TASK_ID);
            if(row.next()){
                String s = row.getString("Status");

                switch (s){
                    case "0":
                        tog_pend.setSelected(true);
                        break;
                    case "1":
                        tog_assign.setSelected(true);
                        break;
                    case "2":
                        tog_work.setSelected(true);
                        break;
                    case "3":
                        tog_review.setSelected(true);
                        break;
                    case "4":
                        tog_done.setSelected(true);
                        break;
                    default:break;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //Fill data
    private void populateDetail(){
        String emID = "";
        try {
            ResultSet row = database.getSomeID(taskId, Const.TASK_TABLE, Const.TASK_ID);
            while(row.next()){
                txt_name.setText(row.getString(Const.TASK_NAME));
                datePick_taskDate.setValue(DateTimePickerHandler.formatDate(row.getString(Const.TASK_START)));
                datePick_taskEnd.setValue(DateTimePickerHandler.formatDate(row.getString(Const.TASK_END)));
                txt_des.setText(row.getString(Const.TASK_DES));
                emID = row.getString(Const.TASK_EMP_ID);
                String color = row.getString("Color");
                String taskColor = OtherHandler.toRGBCode(Color.web(color));
                colorPicker_taskColor.setValue(Color.valueOf(taskColor));
            }
            ResultSet row2 = database.getSomeID(emID, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_ID);
            while(row2.next()){

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
