package CRM_APP.Controller.Employee.Employee;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Employee.EmployeeDB;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Handler.ShakerHandler;
import CRM_APP.Handler.TextfieldHandler;
import CRM_APP.Model.Employee;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class EmployeeProfileController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label lbl_TotalTask;

    @FXML
    private Label lbl_ExpiredTask;

    @FXML
    private Label lbl_EmpID;

    @FXML
    private Label lbl_Address;

    @FXML
    private Label lbl_Phone;

    @FXML
    private Label lbl_Position;

    @FXML
    private JFXPasswordField txt_Password;

    @FXML
    private JFXListView<?> lv_JoinTeam;

    @FXML
    private JFXButton btn_Save;

    @FXML
    private Label lbl_EmpName;

    @FXML
    private Label lbl_Noti;

    @FXML
    private Label lbl_HeaderPosition;

    @FXML
    private Button btn_Back;

    @FXML
    private Button btn_Edit;

    public static String emID;
    private Database database;
    public static String condition;
    private SceneHandler sceneHandler;
    private EmployeeDB employeeDB;
    private Employee employee;
    private ShakerHandler shakerHandler;
    @FXML
    void initialize() {
        populateDetail();
        taskCounter();
        if(emID.contains("EM")){
            btn_Edit.setVisible(false);
        }
        if(condition.equals("home")){
            btn_Back.setVisible(false);
        }
        TextfieldHandler textfieldHandler = new TextfieldHandler();
        textfieldHandler.limitInput(txt_Password, 6);
        //press button back and check where it come from
        btn_Back.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            if(condition.equals("EmployeeList")){
                sceneHandler.slideScene(btn_Back, EmployeeCellController.cellStack, "-Y", "/CRM_APP/View/Employee/employee.fxml");
            }else if(condition.equals("TeamList")){
                sceneHandler.slideScene(btn_Back, EmployeeCellController.cellStack, "-Y", "/CRM_APP/View/Employee/Team/teamDetail.fxml");
            }
        });
        btn_Edit.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            AddEmployeeController.emID = emID;
            sceneHandler.slideScene(btn_Back, EmployeeCellController.cellStack, "-Y", "/CRM_APP/View/Employee/addEmployee.fxml");
        });
        btn_Save.setOnAction(e -> {
            changePassword();
        });
    }
    private void populateDetail(){
        try {
            database = new Database();
            ResultSet row = database.getSomeID(emID, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_ID);
            while(row.next()){
                lbl_EmpID.setText(emID);
                lbl_EmpName.setText(row.getString(Const.EMPLOYEE_NAME));
                lbl_Address.setText(row.getString(Const.EMPLOYEE_ADDRESS));
                lbl_Position.setText(row.getString(Const.EMPLOYEE_POSITION));
                lbl_HeaderPosition.setText(row.getString(Const.EMPLOYEE_POSITION));
                lbl_Phone.setText(row.getString(Const.EMPLOYEE_PHONE));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void changePassword(){
        employeeDB = new EmployeeDB();
        employee = new Employee();
        String password = txt_Password.getText();
        if(!password.equals("")){
            employee.setPassword(password);
            employee.setId(emID);
            employeeDB.updateEmp(employee);
            lbl_Noti.setVisible(true);
            lbl_Noti.setText("Change password success!");
            txt_Password.clear();
        }else{
            shakerHandler = new ShakerHandler(txt_Password, 2, 50);
            shakerHandler.shake();
            lbl_Noti.setVisible(false);
        }
    }

    private void taskCounter(){
        //region TOTAL TASK
        try {
            employeeDB = new EmployeeDB();
            employee = new Employee();
            employee.setId(emID);
            ResultSet row = employeeDB.countTask1(employee);
            while(row.next()){
                lbl_TotalTask.setText(row.getString("TotalTask"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //endregion
        //region EXPIRED TASK
        try {
            employeeDB = new EmployeeDB();
            employee = new Employee();
            employee.setId(emID);
            ResultSet row = employeeDB.countTask2(employee, LocalDate.now()+"");
            while(row.next()){
                lbl_ExpiredTask.setText(row.getString(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //endregion
    }
}
