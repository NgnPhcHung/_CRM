package CRM_APP.Controller.Employee.Team;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Employee.TeamDB;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Team;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXMLLoader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class TeamDetailCellController extends JFXListCell<Team> {
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

    private FXMLLoader fxmlLoader;
    private Database database;
    private SceneHandler sceneHandler;
    private TeamDB teamDB;
    private Team team;

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
                fxmlLoader = new FXMLLoader(getClass().getResource("/CRM_APP/View/Employee/Team/teamDetailCell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            database = new Database();
            sceneHandler = new SceneHandler();

            team = new Team();
            teamDB = new TeamDB();

            try {
                team.setEmID(item.getEmID());
                team.setTeamID(item.getTeamID());
                ResultSet rowEm = database.getSomeID(item.getEmID(), Const.EMPLOYEE_TABLE, Const.EMPLOYEE_ID);
                ResultSet rowTask = teamDB.countTask(team);
                while(rowEm.next()){
                    lbl_name.setText(rowEm.getString(Const.EMPLOYEE_NAME));
                }
                while (rowTask.next()){
                    lbl_num.setText(rowTask.getString("countTask"));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        setText(null);
        setGraphic(main_pane);
    }
}
