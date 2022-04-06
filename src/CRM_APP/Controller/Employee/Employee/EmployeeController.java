package CRM_APP.Controller.Employee.Employee;

import CRM_APP.Controller.Employee.Employee.EmployeeCellController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Employee;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

import static CRM_APP.Controller.Employee.Employee.EmployeeCellController.cellStack;

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

    @FXML
    void initialize() {
        populateList();
        btn_add.setOnAction(e -> {

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
        try {
            row = database.getAllTableValue(Const.EMPLOYEE_TABLE);
            while(row.next()){
                Employee employee = new Employee();
                employee.setId(row.getString(Const.EMPLOYEE_ID));
                employee.setName(row.getString(Const.EMPLOYEE_NAME));
                employee.setAddress(row.getString(Const.EMPLOYEE_ADDRESS));
                employee.setPhone(row.getString(Const.EMPLOYEE_PHONE));
                employee.setPosition(row.getString(Const.EMPLOYEE_POSITION));
                cellStack = main_stack;
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

}
