package CRM_APP.Controller.Employee.Team;

import CRM_APP.Controller.Employee.DetailController;
import CRM_APP.Controller.Employee.Employee.EmployeeCellController;
import CRM_APP.Controller.Employee.Employee.EmployeeController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Employee.TeamDB;
import CRM_APP.Handler.NotificationHandler;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Project;
import CRM_APP.Model.Team;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.mysql.cj.util.StringUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

public class CreateTeamController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label lbl_noti;

    @FXML
    private JFXTextField txt_teamName;

    @FXML
    private JFXButton btn_save;

    @FXML
    private JFXButton btn_Delete;

    @FXML
    private JFXButton btn_back;

    private SceneHandler sceneHandler;
    private Database database;
    private ObservableList<Team> teams;
    private Team team;
    private TeamDB teamDB;
    private NotificationHandler notificationHandler;
    public static String teamID = "";
    public static StackPane backPane;
    private static HashMap<String, String> teamHM = new HashMap<String, String>();

    @FXML
    void initialize() {
            btn_back.setOnAction(e->{
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_save, backPane,"Y","/CRM_APP/View/Employee/Team/team.fxml");
        });
        if(!teamID.equals("")){
            detail();
            btn_save.setOnAction(e -> {
                updateTeam();
            });
            btn_Delete.setOnAction(e -> {
                delete();
                btn_back.fire();
            });
        }else{
            btn_Delete.setVisible(false);
            btn_save.setOnAction(ev->{
                createTeam();
            });
        }
    }


    private void createTeam(){
        teamDB = new TeamDB();
        database = new Database();
        sceneHandler = new SceneHandler();
        notificationHandler = new NotificationHandler();
        if(StringUtils.isNullOrEmpty(txt_teamName.getText())){
            lbl_noti.setVisible(true);
            lbl_noti.setText("Invalid Name");
        }else{
            String name = txt_teamName.getText();
            String id = OtherHandler.generateId();
            //check is team name exist

            try {
                ResultSet row = database.getSomeID(name, Const.TEAM_TABLE, Const.TEAM_NAME);
                if (row.next()){
                    lbl_noti.setVisible(true);
                    lbl_noti.setText("Team Already Exist");
                    notificationHandler.popup(notificationHandler.error, "Team Already Exist");
                    txt_teamName.clear();
                }else{
                    lbl_noti.setVisible(false);
                    ResultSet rowID = database.getSomeID(id, Const.TEAM_TABLE, Const.TEAM_ID);
                    while(rowID.next()){
                        id = OtherHandler.generateId();
                        rowID = database.getSomeID(id, Const.TEAM_TABLE, Const.TEAM_ID);
                    }
                    teamHM.put("id", id);
                    teamHM.put("name", name);
                    setTeamHM(teamHM);
                    DetailController.condition = "newTeam";
                    DetailController.backPane = backPane;

                    sceneHandler.slideScene(btn_save, backPane,"Y","/CRM_APP/View/Employee/detail.fxml");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void updateTeam(){
        database = new Database();
        teamDB = new TeamDB();
        team = new Team();
        notificationHandler = new NotificationHandler();
        if(StringUtils.isNullOrEmpty(txt_teamName.getText())){
            notificationHandler.popup(notificationHandler.error, "Team name cannot be blank");
        }else{
            String teamName = txt_teamName.getText();
            team.setTeamID(teamID);
            team.setTeamName(teamName);
            teamDB.updateTeam(team);
            notificationHandler.popup(notificationHandler.success, "Team " + teamName + " update success");
            btn_back.fire();
        }
    }

    private void detail(){
        try {
            database = new Database();
            teamDB = new TeamDB();
            team = new Team();
            ResultSet row = database.getSomeID(teamID, Const.TEAM_TABLE, Const.TEAM_ID);
            while(row.next()){
                txt_teamName.setText(row.getString(Const.TEAM_NAME));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void delete(){
        database = new Database();
        database.detele(Const.TEAM_DETAIL_TABLE, Const.TEAM_ID, teamID);
        database.detele(Const.TEAM_TABLE, Const.TEAM_ID, teamID);
    }

    public static HashMap<String, String> getTeamHM() {
        return teamHM;
    }

    public void setTeamHM(HashMap<String, String> teamHM) {
        this.teamHM = teamHM;
    }
}
