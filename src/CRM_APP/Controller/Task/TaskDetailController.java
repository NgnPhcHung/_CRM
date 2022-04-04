package CRM_APP.Controller.Task;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.ResourceBundle;

import CRM_APP.Controller.Home.HomeController;
import CRM_APP.Controller.Project.Module.ModuleController;
import CRM_APP.Controller.Project.ProjectCellController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Task.TaskDB;
import CRM_APP.Handler.DateTimePickerHandler;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Employee;
import CRM_APP.Model.Task;
import com.jfoenix.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private JFXComboBox<String> cb_module;

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

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_delete;

    private TaskDB taskDB = new TaskDB();
    private Database database = new Database();
    public static LocalDate dates;
    public static String taskId="";
    public static boolean isAdmin = false;
    private String status;
    private SceneHandler sceneHandler;
    private ObservableList<String> list;
    private Task task;
    @FXML
    void initialize() {
        //if not admin logged in

        if(!isAdmin){
            cb_employ.setDisable(true);
            cb_module.setDisable(true);
            tog_pend.setDisable(true);
            txt_name.setDisable(true);
            btn_back.setVisible(false);
            btn_delete.setVisible(false);
            datePick_taskDate.setDisable(true);
            datePick_taskEnd.setDisable(true);
            populateDetail();
            populateComboBox();
            manageToggle();
            manageTogglePopulate();
            btn_save.setOnAction(e ->{
                empSave();
                btn_save.getScene().getWindow().hide();
            });
        }else{
            //region ADMIN
            manageTogglePopulate();
            if(taskId.equals("")){
                newTask();
                datePick_taskDate.setValue(LocalDate.now());
            }
            populateComboBox();
            populateDetail();
            LocalDate dateStart = datePick_taskDate.getValue();
            DateTimePickerHandler.disableDate(datePick_taskEnd, dateStart);
            datePick_taskEnd.setValue(dateStart);
            catchStartDateEnd();

            btn_back.setOnAction(e -> {
                sceneHandler = new SceneHandler();
                sceneHandler.slideScene(btn_back, ProjectCellController.cellStack, "-Y", "/CRM_APP/View/Task/taskList.fxml");
            });
            btn_delete.setOnAction(e -> {
                delete();
            });
            btn_save.setOnAction(e -> {
                if(taskId == ""){
                    save();
                }else {
                    update();
                }
                sceneHandler = new SceneHandler();
                sceneHandler.slideScene(btn_back, ProjectCellController.cellStack, "-Y", "/CRM_APP/View/Task/taskList.fxml");
            });
            //endregion
        }



        //region SET TOGGLE DATA & HANDLE EVENT VALUE CHANGE
        ToggleGroup group = new ToggleGroup();
        tog_pend.setUserData(0);
        tog_work.setUserData(1);
        tog_review.setUserData(2);
        tog_done.setUserData(3);

        tog_pend.setToggleGroup(group);
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
    
    private void newTask(){
        database = new Database();
        try {
            ResultSet resultSet= database.getSomeID(TaskListController.modID, Const.MODULE_TABLE, Const.MODULE_ID);
            while(resultSet.next()){
                cb_module.setValue(resultSet.getString(Const.MODULE_NAME));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

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
                        tog_work.setSelected(true);
                        break;
                    case "2":
                        tog_review.setSelected(true);
                        break;
                    case "3":
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
    private void catchStartDateEnd(){
        datePick_taskDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                DateTimePickerHandler.disableDate(datePick_taskEnd, datePick_taskDate.getValue());
            }
        });
    }
    private void populateComboBox(){
        try {
            database = new Database();
            ResultSet row = null;
            list = FXCollections.observableArrayList();
            row = database.getAllTableValue(Const.EMPLOYEE_TABLE);
            while(row.next()){
                String name = row.getString(Const.EMPLOYEE_NAME);
                list.add(name);
            }
            cb_employ.setItems(list);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            database = new Database();
            ResultSet row = null;
            list = FXCollections.observableArrayList();
            row = database.getAllTableValue(Const.MODULE_TABLE);
            while(row.next()){
                String name = row.getString(Const.MODULE_NAME);
                list.add(name);
            }
            cb_module.setItems(list);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

       if(isAdmin){
           cb_employ.getSelectionModel().select(0);
           cb_module.getSelectionModel().select(0);
       }
    }
    private void populateDetail(){
        try {
            String emID;
            String modID;
            ResultSet row = database.getSomeID(taskId, Const.TASK_TABLE, Const.TASK_ID);
            while(row.next()){
                txt_name.setText(row.getString(Const.TASK_NAME));
                datePick_taskDate.setValue(DateTimePickerHandler.formatDate(row.getString(Const.TASK_START)));
                datePick_taskEnd.setValue(DateTimePickerHandler.formatDate(row.getString(Const.TASK_END)));
                txt_des.setText(row.getString(Const.TASK_DES));
                emID = row.getString(Const.TASK_EMP_ID);
                modID = row.getString(Const.TASK_MOD_ID);
                String color = row.getString("Color");
                String taskColor = OtherHandler.toRGBCode(Color.web(color));
                colorPicker_taskColor.setValue(Color.valueOf(taskColor));
                ResultSet row2 = database.getSomeID(emID, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_ID);
                ResultSet row3 = database.getSomeID(modID, Const.MODULE_TABLE, Const.MODULE_ID);
                while(row2.next()){
                    cb_employ.setValue(row2.getString(Const.EMPLOYEE_NAME));
                }
                while(row3.next()){
                    System.out.println(row3.getString(Const.MODULE_NAME));
                    cb_module.setValue(row3.getString(Const.MODULE_NAME));
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //employee action
    void empSave(){
        task = new Task();
        Color color = colorPicker_taskColor.getValue();
        String des = txt_des.getText();
        task.setTaskID(taskId);
        task.setColor(color+"");
        task.setDes(des);
        task.setStatus(status);
        taskDB.empUpdateTask(task);
    }

    //admin action
    private void save(){
        database = new Database();
        task= new Task();
        taskDB = new TaskDB();
        String id = OtherHandler.generateId();
        String name = txt_name.getText();
        LocalDate start = datePick_taskDate.getValue();
        LocalDate end = datePick_taskEnd.getValue();
        String em = cb_employ.getValue();
        String mod = cb_module.getValue();
        String des = txt_des.getText();
        String color = colorPicker_taskColor.getValue()+"";
        String emID;
        String modID;
        ResultSet row = null;
        ResultSet rowID = null;
        ResultSet rowMod = null;

        try {
            row = database.getSomeID(em, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_NAME);
            rowID = database.getSomeID(id,  Const.TASK_TABLE, Const.TASK_ID);
            rowMod = database.getSomeID(mod,  Const.MODULE_TABLE, Const.MODULE_NAME);
            while (rowID.next()){
                id = OtherHandler.generateId();
                rowID = database.getSomeID(id,  Const.TASK_TABLE, Const.TASK_ID);
            }
            if(row.next()){
                emID = row.getString(Const.EMPLOYEE_ID);
                task.setEmpID(emID);
            }
            if(rowMod.next()){
                modID = rowMod.getString(Const.MODULE_ID);
                task.setModID(modID);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        task.setTaskID(id);
        task.setTaskName(name);
        task.setStartDate(start);
        task.setEndDate(end);
        task.setDes(des);
        task.setColor(color);
        task.setStatus(status);
        taskDB.save(task);
    }
    private void delete(){
        database = new Database();
        database.detele(Const.TASK_TABLE, Const.TASK_ID, taskId);
        sceneHandler = new SceneHandler();
        sceneHandler.slideScene(btn_back, ProjectCellController.cellStack, "-Y", "/CRM_APP/View/Task/taskList.fxml");
    }
    private void update(){
        database = new Database();
        task= new Task();
        taskDB = new TaskDB();

        String name = txt_name.getText();
        LocalDate start = datePick_taskDate.getValue();
        LocalDate end = datePick_taskEnd.getValue();
        String em = cb_employ.getValue();
        String mod = cb_module.getValue();
        String des = txt_des.getText();
        String color = colorPicker_taskColor.getValue()+"";
        String emID;
        String modID;
        ResultSet rowEm = null;
        ResultSet rowMod = null;

        try {
            rowEm = database.getSomeID(em, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_NAME);
            rowMod = database.getSomeID(mod,  Const.MODULE_TABLE, Const.MODULE_NAME);
            if(rowMod.next()){
                modID = rowMod.getString(Const.MODULE_ID);
                task.setModID(modID);
            }
            if(rowEm.next()){
                emID = rowEm.getString(Const.EMPLOYEE_ID);
                task.setEmpID(emID);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        task.setTaskID(taskId);
        task.setTaskName(name);
        task.setStartDate(start);
        task.setEndDate(end);
        task.setStatus(status);
        task.setDes(des);
        task.setColor(color);
        taskDB.update(task);
    }
    private void manageTogglePopulate(){
        try {
            ResultSet row = database.getSomeID(taskId, Const.TASK_TABLE, Const.TASK_ID);
            if(row.next()){
                String s = row.getString("Status");
                if(s.equals("0")){
                    tog_pend.setSelected(true);
                }else if(s.equals("1")){
                    tog_work.setSelected(true);
                }else if(s.equals("2")){
                    tog_review.setSelected(true);
                }else if(s.equals("3")){
                    tog_done.setSelected(true);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
