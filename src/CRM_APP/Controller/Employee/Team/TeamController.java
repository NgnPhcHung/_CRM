package CRM_APP.Controller.Employee.Team;

import CRM_APP.Controller.Employee.Employee.EmployeeCellController;
import CRM_APP.Controller.Employee.Employee.EmployeeController;
import CRM_APP.Controller.Home.HomeController;
import CRM_APP.Controller.Project.Project.ProjectCellController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Employee.TeamDB;
import CRM_APP.Database.Project.ProjectDB;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class TeamController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_addNew;

    @FXML
    private JFXTextField txt_Find;


    @FXML
    private JFXListView<Team> lv_team;

    @FXML
    private TableView tableView;

    private SceneHandler sceneHandler;
    private Database database;
    private ObservableList<Team> teams;
    private Team team;
    private TeamDB teamDB;
    private Employee employee;
    private ObservableList<Employee> employees;
    private ObservableList<Employee> items;

    public static StackPane backPane;

    TableColumn col_Employee;
    TableColumn col_Action;


    @FXML
    void initialize() {
        populateList();
        btn_addNew.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            CreateTeamController.backPane =backPane;
            CreateTeamController.teamID = "";
            sceneHandler.slideScene(btn_addNew, backPane,"Y","/CRM_APP/View/Employee/Team/createTeam.fxml");
        });
        btn_back.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_addNew, backPane,"Y","/CRM_APP/View/Employee/employee.fxml");
        });

        lv_team.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Team>() {
            @Override
            public void changed(ObservableValue<? extends Team> observable, Team oldValue, Team newValue) {
                tableView.getColumns().clear();
                fillTable(newValue.getTeamID());
            }
        });
    }

    private void fillTable(String teamID){
        tableView.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        col_Employee = new TableColumn("Employee");
        col_Action = new TableColumn("Action");

        tableView.getColumns().addAll(col_Employee, col_Action);
        tableView.getStylesheets().add(HomeController.styleSheet);

        col_Employee.setMaxWidth( 1f * Integer.MAX_VALUE * 80 ); // 20% width
        col_Action.setMaxWidth( 1f * Integer.MAX_VALUE * 10 ); // 10% width

        col_Employee.getStyleClass().addAll("h4", "text");
        col_Action.getStyleClass().addAll("h4", "text","custom-align");
        ObservableList<String> employeeList = FXCollections.observableArrayList();
        try {
            employee = new Employee();
            database = new Database();
            team = new Team();
            teamDB = new TeamDB();
            team.setTeamID(teamID);
            employees= FXCollections.observableArrayList();
            ResultSet row = null;
            ResultSet existEmployee = null;
            row = database.getSomeID(teamID, Const.TEAM_DETAIL_TABLE, Const.TEAM_ID);

            while(row.next()){
                team = new Team();
                employee = new Employee();
                String teamId =row.getString(Const.TEAM_ID);
                String emId = row.getString(Const.EMPLOYEE_ID);

                team.setTeamID(teamId);
                team.setEmID(emId);
                employee.setName(employName(emId));
                existEmployee = teamDB.getTeamMember(team);
                while(existEmployee.next()){
                    employeeList.add(existEmployee.getString(Const.EMPLOYEE_ID));
                }

                for(String id: employeeList){
                    if(id.equals(emId)){
                        employee.getRemark().setSelected(true);
                    }
                }

                col_Employee.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
                col_Action.setCellValueFactory(new PropertyValueFactory<Employee, String>("remark"));
                employees.add(employee);
            }
            tableView.setItems(employees);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private String employName(String emId){
        database = new Database();
        ResultSet row = database.getSomeID(emId, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_ID);
        String emName = "";
        try {
            if(row.next()){
                emName = row.getString(Const.EMPLOYEE_NAME);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return emName;
    }

    private void populateList() {
        sceneHandler = new SceneHandler();
        database = new Database();
        teams = FXCollections.observableArrayList();
        ResultSet row = null;
        try {
            row = database.getAllTableValue(Const.TEAM_TABLE);
            while (row.next()) {
                Team team = new Team();
                team.setTeamID(row.getString(Const.TEAM_ID));
                team.setTeamName(row.getString(Const.TEAM_NAME));

                teams.add(team);
            }
            TeamCellController.cellStack = backPane;
            lv_team.setItems(teams);
            lv_team.setCellFactory(TeamCellController -> new TeamCellController());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

