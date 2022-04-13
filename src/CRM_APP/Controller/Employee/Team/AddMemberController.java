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
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AddMemberController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label lbl_noti;

    @FXML
    private JFXTextField txt_employee;

    @FXML
    private JFXComboBox<String> cb_team;

    @FXML
    private JFXComboBox<String> cb_employee;

    @FXML
    private JFXButton btn_save;

    @FXML
    private JFXButton btn_back;

    private SceneHandler sceneHandler;
    private Database database;
    private ObservableList<Team> teams;
    private Team team;
    private TeamDB teamDB;

    @FXML
    void initialize() {
            populateCombobox();
        btn_save.setOnAction(ev->{
            createTeam();
                    });

        btn_back.setOnAction(e->{
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_save, EmployeeCellController.cellStack,"Y","/CRM_APP/View/Employee/Team/team.fxml");

        });
    }
    private void createTeam(){
        String name = cb_employee.getValue();
        String teamName = cb_team.getValue();


            if(name.equals("")){
                lbl_noti.setVisible(true);
                lbl_noti.setText("Invalid Name");
            }else {
                //check is team name exist
                database = new Database();
                teamDB = new TeamDB();
                team = new Team();
                boolean checkName;
                try {
                    String teamID = "";
                    String emID = "";
                    //get id from team and employee
                    ResultSet rowTeam = database.getSomeID(teamName, Const.TEAM_TABLE, Const.TEAM_NAME);
                    ResultSet rowEm = database.getSomeID(name, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_NAME);
                    while (rowTeam.next() && rowEm.next()) {
                        teamID = rowTeam.getString(Const.TEAM_ID);
                        emID = rowEm.getString(Const.EMPLOYEE_ID);
                        team.setTeamID(emID);
                        team.setTeamID(teamID);
                    }

                    ResultSet row = teamDB.getTeamMember(team);
                    if (row.next()) {
                        lbl_noti.setVisible(true);
                        lbl_noti.setText("Member Already In Team");
                    } else {
                        lbl_noti.setVisible(false);
                        teamDB = new TeamDB();
                        team = new Team();
                        team.setTeamID(teamID);
                        team.setEmID(emID);
                        teamDB.addMember(team);
                        sceneHandler = new SceneHandler();
                        sceneHandler.slideScene(btn_save, EmployeeCellController.cellStack, "Y", "/CRM_APP/View/Employee/Team/team.fxml");
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
    }

    private void populateCombobox(){
        OtherHandler.populateComboBox(cb_employee, Const.EMPLOYEE_TABLE,Const.EMPLOYEE_NAME);
        OtherHandler.populateComboBox(cb_team, Const.TEAM_TABLE,Const.TEAM_NAME);
    }
}
