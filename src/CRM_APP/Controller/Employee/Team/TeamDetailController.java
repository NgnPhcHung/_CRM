package CRM_APP.Controller.Employee.Team;

import CRM_APP.Controller.Employee.Employee.EmployeeCellController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Employee.TeamDB;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Team;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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

public class TeamDetailController {

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
    private JFXListView<Team> lv_emp;

    private Database database;
    private ResultSet row = null;
    private TeamDB teamDB;
    private Team team;
    private SceneHandler sceneHandler;
    private ObservableList<Team> teams;
    public static String teamID;
    public static StackPane backPane;

    @FXML
    void initialize() {
        populateList();
        btn_back.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_back, EmployeeCellController.cellStack, "-X", "/CRM_APP/View/Employee/Team/team.fxml");

        });
        btn_addNew.setOnAction(e->{
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_back, EmployeeCellController.cellStack, "-X", "/CRM_APP/View/Employee/Team/addMember.fxml");
        });
    }

    private void populateList(){
        sceneHandler = new SceneHandler();
        database = new Database();
        teamDB = new TeamDB();
        teams = FXCollections.observableArrayList();
        ResultSet row = null;
        try {
            row = database.getSomeID(teamID, Const.TEAM_DETAIL_TABLE, Const.TEAM_ID);
            while(row.next()){
                team = new Team();
                team.setTeamID(row.getString(Const.TEAM_ID));
                team.setEmID(row.getString(Const.EMPLOYEE_ID));
                teams.add(team);
            }
            lv_emp.setItems(teams);
            lv_emp.setCellFactory(TeamDetailCellController -> new TeamDetailCellController());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
