package CRM_APP.Controller.Employee.Team;

import CRM_APP.Controller.Employee.Employee.EmployeeCellController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Employee.TeamDB;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Team;
import com.jfoenix.controls.JFXListCell;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class TeamCellController extends JFXListCell<Team> {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox main_pane;

    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_num;

    @FXML
    private Button btn_edit;

    @FXML
    private Button btn_details;

    private FXMLLoader fxmlLoader;
    private Database database;
    private SceneHandler sceneHandler;
    private TeamDB teamDB;
    private Team team;
    public static StackPane cellStack;

    @FXML
    void initialize() {

    }

    @Override
    protected void updateItem(Team item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/CRM_APP/View/Employee/Team/teamCell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            lbl_name.setText(item.getTeamName());
            populateDetail(item.getTeamID());
//            btn_edit.setOnAction(e -> {
//                sceneHandler = new SceneHandler();
//                TeamDetailController.teamID = item.getTeamID();
//                TeamDetailController.backPane = cellStack;
//
//                sceneHandler.slideScene(btn_edit, cellStack, "-X", "/CRM_APP/View/Employee/Team/detail.fxml");
//            });
            btn_details.setOnAction(e -> {
                sceneHandler = new SceneHandler();
                CreateTeamController.teamID = item.getTeamID();
                CreateTeamController.backPane = cellStack;
                sceneHandler.slideScene(btn_edit, EmployeeCellController.cellStack, "-X", "/CRM_APP/View/Employee/Team/createTeam.fxml");
            });
        }
        setText(null);
        setGraphic(main_pane);
    }
    private void populateDetail(String id){
        teamDB = new TeamDB();
        team = new Team();
        team.setTeamID(id);
        try {
            ResultSet row = teamDB.countMember(team);
            while(row.next()){
                lbl_num.setText(row.getString("MEM"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
