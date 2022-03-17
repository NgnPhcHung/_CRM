package CRM_APP.Controller.Employee;

import CRM_APP.Handler.SceneHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;

public class EmployeeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btn_manage;

    @FXML
    private JFXListView<?> lv_employee;

    SceneHandler sceneHandler;

    @FXML
    void initialize() {
        sceneHandler= new SceneHandler();
        btn_manage.setOnAction(event -> sceneHandler.newScene("/CRM_APP/View/Employee/addEmployee.fxml"));
    }
}
