package CRM_APP.Controller.Employee.Employee;
import CRM_APP.Controller.Home.HomeController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Employee.EmployeeDB;
import CRM_APP.Handler.NotificationHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Handler.ShakerHandler;
import CRM_APP.Handler.TextFieldHandler;
import CRM_APP.Model.Employee;
import CRM_APP.Model.Team;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.mysql.cj.util.StringUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class EmployeeProfileController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private GridPane grid_Main;

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
    private JFXPasswordField txt_RePassword;

    @FXML
    private JFXListView<Team> lv_JoinTeam;

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
    private ObservableList<Team> teams;
    private NotificationHandler notificationHandler;
    private TextFieldHandler textfieldHandler;
    @FXML
    void initialize() {
        textfieldHandler = new TextFieldHandler();
        sceneHandler = new SceneHandler();
        grid_Main.getStylesheets().add(HomeController.styleSheet);
        populateDetail();
        taskCounter();
        populateList();
        if(HomeController.userId.contains("EM")){
            btn_Edit.setVisible(false);
        }else {
            txt_Password.setVisible(false);
            txt_RePassword.setVisible(false);
            btn_Save.setVisible(false);
        }

        if(condition.equals("home")){
            btn_Back.setVisible(false);
        }
        textfieldHandler.limitInput(txt_Password, 6);
        textfieldHandler.limitInput(txt_RePassword, 6);
        //press button back and check where it come from
        btn_Back.setOnAction(e -> {
            if(condition.equals("EmployeeList")){
                sceneHandler.slideScene(btn_Back, EmployeeCellController.cellStack, "-Y", "/CRM_APP/View/Employee/employee.fxml");
            }else if(condition.equals("TeamList")){
                sceneHandler.slideScene(btn_Back, EmployeeCellController.cellStack, "-Y", "/CRM_APP/View/Employee/detail.fxml");
            }
        });
        btn_Edit.setOnAction(e -> {
            AddEmployeeController.emID = emID;
            AddEmployeeController.backPane =  EmployeeCellController.cellStack;
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
        }
    }
    private void changePassword(){
        notificationHandler = new NotificationHandler();
        employeeDB = new EmployeeDB();
        employee = new Employee();
        database = new Database();

        String password = txt_Password.getText();
        String rePassword = txt_RePassword.getText();

        if(!StringUtils.isNullOrEmpty(password) && !StringUtils.isNullOrEmpty(rePassword)
            && password.equals(rePassword)){
            employee.setPassword(password);
            employee.setId(emID);
            employeeDB.updateEmp(employee);
            notificationHandler.popup(notificationHandler.success, "Change password success!");
            txt_Password.clear();
        }else if(!password.equals(rePassword)){
            shakerHandler = new ShakerHandler(txt_Password, 2, 50);
            shakerHandler.shake();
            notificationHandler.popup(notificationHandler.success, "Re-Password and Password does not match");
        }
    }
    public void taskCounter(){
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
            ResultSet row = employeeDB.countTaskExpired(employee, LocalDate.now()+"");
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
    private void populateList(){
        sceneHandler = new SceneHandler();
        database = new Database();
        teams = FXCollections.observableArrayList();
        try {

            ResultSet row = database.getSomeID(emID, Const.TEAM_DETAIL_TABLE, Const.TEAM_EM_ID);
            while (row.next()) {
                database = new Database();
                ResultSet rs = database.getSomeID(row.getString(Const.TEAM_ID), Const.TEAM_TABLE, Const.TEAM_ID);
                if(rs.next()){
                    Team team = new Team();
                    team.setTeamID(rs.getString(Const.TEAM_ID));
                    team.setTeamName(rs.getString(Const.TEAM_NAME));

                    teams.add(team);
                }
            }
            lv_JoinTeam.setItems(teams);
            lv_JoinTeam.setCellFactory(EmployeeProfileCellController -> new EmployeeProfileCellController());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
