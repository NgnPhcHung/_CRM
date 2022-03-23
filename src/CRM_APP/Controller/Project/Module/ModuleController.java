package CRM_APP.Controller.Project.Module;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Project.ModuleDB;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Module;
import CRM_APP.Model.Project;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javax.xml.crypto.Data;

public class ModuleController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_addNew;

    @FXML
    private JFXTextField txt_module;

    @FXML
    private JFXButton btn_pending;

    @FXML
    private Label lbl_pending;

    @FXML
    private JFXButton btn_total;

    @FXML
    private Label lbl_assigned;

    @FXML
    private JFXButton btn_working;

    @FXML
    private Label lbl_working;

    @FXML
    private JFXButton btn_revewing;

    @FXML
    private Label lbl_reviewing;

    @FXML
    private JFXButton btn_revewing1;

    @FXML
    private Label lbl_done;


    @FXML
    private JFXListView<Module> lv_modules;

    private SceneHandler sceneHandler;
    private Database database;
    private ModuleDB db = new ModuleDB();

    public static String projectID;
    private ObservableList<Module> modules;

    @FXML
    void filterEvent(ActionEvent event) {

    }

    @FXML
    void initialize() {
        try {
            fillCard();
            populateList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void fillCard() throws SQLException, ClassNotFoundException {
        ResultSet row = db.getCountStatus(projectID);
        String pending = "";
        String reviewing = "";
        String working = "";
        String assigned = "";
        String done = "";

        if(row.next()){
            pending = row.getString("Pending");
            working = row.getString("Working");
            reviewing = row.getString("Reviewing");
            assigned = row.getString("Assigned");
            done = row.getString("Done");
        }
        lbl_pending.setText(pending);
        lbl_working.setText(working);
        lbl_reviewing.setText(reviewing);
        lbl_assigned.setText(assigned);
        lbl_done.setText(done);
    }

    private void populateList() throws SQLException, ClassNotFoundException {
        sceneHandler= new SceneHandler();
        database = new Database();
        modules = FXCollections.observableArrayList();
        ResultSet row = database.getSomeID(projectID, Const.MODULE_TABLE, Const.MODULE_PROJECT_ID);
        while(row.next()){
            Module module = new Module();
            module.setProjectID(projectID);
            module.setModuleID(OtherHandler.generateId());
            module.setModName(row.getString("ModName"));
            module.setStatus(row.getString("Status"));

            modules.add(module);
        }
        lv_modules.setItems(modules);
        lv_modules.setCellFactory(ModuleCellController -> new ModuleCellController());
    }
}
