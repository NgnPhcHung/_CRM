package CRM_APP.Controller.Employee.Team;

import CRM_APP.Controller.Employee.Employee.EmployeeCellController;
import CRM_APP.Controller.Employee.Employee.EmployeeController;
import CRM_APP.Controller.Project.Project.ProjectCellController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Employee.TeamDB;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Module;
import CRM_APP.Model.Project;
import CRM_APP.Model.Team;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private JFXTextField txt_module;


    @FXML
    private JFXListView<Team> lv_team;

    private SceneHandler sceneHandler;
    private Database database;
    private ObservableList<Team> teams;
    private Team team;
    private TeamDB teamDB;

    @FXML
    void initialize() {
        populateList();
        btn_addNew.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_addNew, EmployeeCellController.cellStack,"Y","/CRM_APP/View/Employee/Team/createTeam.fxml");
        });
        btn_back.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_addNew, EmployeeCellController.cellStack,"Y","/CRM_APP/View/Employee/employee.fxml");
        });
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
            lv_team.setItems(teams);
            lv_team.setCellFactory(TeamCellController -> new TeamCellController());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

