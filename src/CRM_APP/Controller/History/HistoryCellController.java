package CRM_APP.Controller.History;

import CRM_APP.Controller.Project.Module.ModuleDetailController;
import CRM_APP.Controller.Task.TaskListController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.History;
import CRM_APP.Model.Module;
import com.jfoenix.controls.JFXListCell;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static CRM_APP.Controller.Project.Project.ProjectCellController.cellStack;

public class HistoryCellController extends JFXListCell<History> {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox main_pane;

    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_ID;

    @FXML
    private Label lbl_Log;

    @FXML
    private Label lbl_Out;

    @FXML
    private Label lbl_Device;

    private FXMLLoader fxmlLoader;
    private Database database;
    History history;

    @FXML
    void initialize() {
        database = new Database();

    }


    @Override
    protected void updateItem(History item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/CRM_APP/View/History/historyCell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            lbl_ID.setText(item.getId());
            lbl_name.setText(item.getName());
            lbl_Log.setText(item.getStartDate());
            lbl_Out.setText(item.getEndDate());
            lbl_Device.setText(item.getDevice());
            setText(null);
            setGraphic(main_pane);
        }
    }
}
