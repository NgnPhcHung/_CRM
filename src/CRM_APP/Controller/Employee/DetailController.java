package CRM_APP.Controller.Employee;

import CRM_APP.Controller.Employee.Employee.AddEmployeeController;
import CRM_APP.Controller.Employee.Employee.EmployeeCellController;
import CRM_APP.Controller.Employee.Team.CreateTeamController;
import CRM_APP.Controller.Home.HomeController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Employee.EmployeeDB;
import CRM_APP.Database.Employee.TeamDB;
import CRM_APP.Handler.NotificationHandler;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Employee;
import CRM_APP.Model.Question;
import CRM_APP.Model.Team;
import com.jfoenix.controls.*;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class DetailController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_back;

    @FXML
    private JFXButton btn_Save;

    @FXML
    private JFXButton btn_Skip;

    @FXML
    private JFXTextField txt_Name;

    @FXML
    private TableView tableView;

    @FXML
    private JFXCheckBox check_SelectAll;

    @FXML
    private Label lbl_Title;


    private Database database;
    private ResultSet row = null;
    private TeamDB teamDB;
    private Team team;
    private Employee employee;
    private SceneHandler sceneHandler;

    private ObservableList<Team> teams;
    private ObservableList<Employee> employees;
    private NotificationHandler notificationHandler;

    public static StackPane backPane;
    public static String condition = "";


    TableColumn col_Name ;
    TableColumn col_EmployeeID;
    TableColumn col_Action;

    @FXML
    void initialize() {
        selectAll();
        btn_back.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_back, backPane, "-X", "/CRM_APP/View/Employee/Team/team.fxml");
        });

        if(condition.equals("newEm")){
            lbl_Title.setText("Add Member");
            btn_Skip.setVisible(true);
            btn_Save.setOnAction(e -> {
                saveEmployeeDetail();
            });
            populateTeam();
        }else if(condition.equals("newTeam")){
            lbl_Title.setText("Add Team");
            btn_Skip.setVisible(false);
            btn_Save.setOnAction(e -> {
                saveTeamDetail();
            });
            populateEmployee();
        }
    }

    private void populateTeam(){
        database = new Database();
        teams = FXCollections.observableArrayList();
        styleCol();
        tableView.getColumns().addAll(col_Name, col_Action);

        try {
            ResultSet row = database.getAllTableValue(Const.TEAM_TABLE);
            while(row.next()){
                team = new Team();
                String teamName = row.getString(Const.TEAM_NAME);
                team.setTeamName(teamName);
                col_Name.setCellValueFactory(new PropertyValueFactory<Team, String>("teamName"));
                col_Action.setCellValueFactory(new PropertyValueFactory<Team,String>("remark"));
                teams.add(team);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        tableView.setItems(teams);
    }
    private void populateEmployee(){
        database = new Database();
        employees = FXCollections.observableArrayList();
        employee = new Employee();
        styleCol();
        tableView.getColumns().addAll(col_Name, col_EmployeeID, col_Action);
        try {
            ResultSet row = database.getAllTableValue(Const.EMPLOYEE_TABLE);
            while(row.next()){
                employee = new Employee();
                String emName = row.getString(Const.EMPLOYEE_NAME);
                employee.setName(emName);
                employee.setId(row.getString(Const.EMPLOYEE_ID));
                col_Name.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
                col_EmployeeID.setCellValueFactory(new PropertyValueFactory<Employee, String>("id"));
                col_Action.setCellValueFactory(new PropertyValueFactory<Employee,String>("remark"));
                employees.add(employee);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        tableView.setItems(employees);
    }

    private void saveTeamDetail(){
        CreateTeamController createTeam =  new CreateTeamController();
        TeamDB teamDB = new TeamDB();
        team = new Team();
        notificationHandler = new NotificationHandler();
        sceneHandler = new SceneHandler();
        ObservableList<Employee> listEm = FXCollections.observableArrayList();
        for (Employee emp: employees){
            if(emp.getRemark().isSelected()){
                listEm.add(emp);
            }
        }
        if(listEm.size()<=0){
            notificationHandler.popup(notificationHandler.error, "Team must have at least 1 Employee");
        }else{
            String teamID = CreateTeamController.getTeamHM().get("id");
            String teamName = CreateTeamController.getTeamHM().get("name");
            team.setTeamID(teamID);
            team.setTeamName(teamName);

            teamDB.save(team);
            for(Employee em : listEm){
                String emID = em.getId();
                team = new Team();
                team.setTeamID(teamID);
                team.setEmID(emID);
                teamDB.addMember(team);
            }
            notificationHandler.popup(notificationHandler.success, "Team " + teamName + " created");
            sceneHandler.slideScene(btn_back, backPane, "-X", "/CRM_APP/View/Employee/Team/team.fxml");
        }
    }
    private void saveEmployeeDetail(){
        EmployeeDB employeeDB = new EmployeeDB();
        employee = new Employee();
        sceneHandler = new SceneHandler();
        notificationHandler = new NotificationHandler();
        String id = AddEmployeeController.getEmployeeHM().get("ID");
        String name = AddEmployeeController.getEmployeeHM().get("name");
        String address = AddEmployeeController.getEmployeeHM().get("address");
        String phone = AddEmployeeController.getEmployeeHM().get("phone");
        String position = AddEmployeeController.getEmployeeHM().get("position");
        String password = AddEmployeeController.getEmployeeHM().get("password");
        employee.setId(id);
        employee.setName(name);
        employee.setAddress(address);
        employee.setPhone(phone);
        employee.setPosition(position);
        employee.setPassword(password);
        employeeDB.saveEmp(employee);
        notificationHandler.popup(notificationHandler.success, "Employee " + name + " created \nWith ID: " + id);
        sceneHandler.slideScene(btn_back, backPane, "-X", "/CRM_APP/View/Employee/employee.fxml");
    }
    private void selectAll(){
        tableView.getItems().removeAll(tableView.getSelectionModel().getSelectedItem());
        check_SelectAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(condition.equals("newEm")){
                    teams = tableView.getItems();
                    for(Team item: teams){
                        if(check_SelectAll.isSelected()){
                            item.getRemark().setSelected(true);
                            check_SelectAll.setText("Deselect All");
                        }else{
                            item.getRemark().setSelected(false);
                            check_SelectAll.setText("Select All");
                        }
                    }
                }else if(condition.equals("newTeam")){
                    employees = tableView.getItems();
                    for(Employee item: employees){
                        if(check_SelectAll.isSelected()){
                            item.getRemark().setSelected(true);
                            check_SelectAll.setText("Deselect All");
                        }else{
                            item.getRemark().setSelected(false);
                            check_SelectAll.setText("Select All");
                        }
                    }
                }
            }
        });
    }
    private void styleCol(){
        tableView.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );

        col_Name = new TableColumn("Name");
        col_EmployeeID = new TableColumn("Employee ID");
        col_Action = new TableColumn("Action");

        col_Name.setMaxWidth( 1f * Integer.MAX_VALUE * 80 ); // 80% width
        col_EmployeeID.setMaxWidth( 1f * Integer.MAX_VALUE * 15 ); // 10% width
        col_Action.setMaxWidth( 1f * Integer.MAX_VALUE * 5 ); // 10% width

        tableView.getStylesheets().add(HomeController.styleSheet);

        col_Name.getStyleClass().addAll("h4", "text");
        col_EmployeeID.getStyleClass().addAll("h4", "text");
        col_Action.getStyleClass().addAll("h4", "text","custom-align");
    }
}
