package CRM_APP.Controller.Employee.Employee;

import CRM_APP.Controller.Employee.Employee.EmployeeCellController;
import CRM_APP.Controller.Task.TaskCellController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.DateTimePickerHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Employee;
import CRM_APP.Model.Task;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;


public class EmployeeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    public StackPane main_stack;

    @FXML
    private JFXButton btn_add;

    @FXML
    private JFXButton btn_team;

    @FXML
    private JFXTextField txt_find;

    @FXML
    private JFXListView<Employee> lv_employee;

    private SceneHandler sceneHandler;
    private Database database;
    private ObservableList<Employee> employees;
    private Employee employee;
    @FXML
    void initialize() {
        filterCell();
        populateList();
        btn_add.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_add, main_stack, "-X", "/CRM_APP/View/Employee/addEmployee.fxml");
        });
        btn_team.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_add, main_stack, "-X", "/CRM_APP/View/Employee/Team/team.fxml");
        });
    }
    private void populateList(){
        sceneHandler = new SceneHandler();
        database = new Database();
        employees = FXCollections.observableArrayList();
        ResultSet row = null;
        employee = new Employee();
        try {
            row = database.getAllTableValue(Const.EMPLOYEE_TABLE);
            while(row.next()){
                employee = new Employee();
                employee.setId(row.getString(Const.EMPLOYEE_ID));
                employee.setName(row.getString(Const.EMPLOYEE_NAME));
                employee.setAddress(row.getString(Const.EMPLOYEE_ADDRESS));
                employee.setPhone(row.getString(Const.EMPLOYEE_PHONE));
                employee.setPosition(row.getString(Const.EMPLOYEE_POSITION));
                EmployeeCellController.cellStack = main_stack;
                employees.add(employee);
            }
            lv_employee.setItems(employees);
            lv_employee.setCellFactory(EmployeeCellController -> new EmployeeCellController());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void filterCell(){
        txt_find.textProperty().addListener(((observable, oldValue, newValue) -> {
            sceneHandler = new SceneHandler();
            database = new Database();
            ObservableList<Employee> employs = FXCollections.observableArrayList();
            ResultSet row = null;
            try {
                row = database.filterDataInput(Const.EMPLOYEE_TABLE, Const.EMPLOYEE_NAME, newValue);
                while(row.next()){
                    Employee employee = new Employee();
                    employee.setId(row.getString(Const.EMPLOYEE_ID));
                    employee.setName(row.getString(Const.EMPLOYEE_NAME));
                    employee.setAddress(row.getString(Const.EMPLOYEE_ADDRESS));
                    employee.setPhone(row.getString(Const.EMPLOYEE_PHONE));
                    employee.setPosition(row.getString(Const.EMPLOYEE_POSITION));

                    employs.add(employee);
                }
                lv_employee.setItems(employs);
                lv_employee.setCellFactory(EmployeeCellController -> new EmployeeCellController());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }));
    }
}
