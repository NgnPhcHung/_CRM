package CRM_APP.Controller.Project.Module;

import CRM_APP.Controller.Project.Project.ProjectCellController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Project.ModuleDB;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Handler.TextFieldHandler;
import CRM_APP.Model.Module;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ModuleController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_addNew;

    @FXML
    private Button btn_back;

    @FXML
    private JFXTextField txt_module;

    @FXML
    private JFXButton btn_pending;

    @FXML
    private Label lbl_pending;

    @FXML
    private JFXButton btn_total;

    @FXML
    private JFXButton btn_working;

    @FXML
    private Label lbl_working;

    @FXML
    private JFXButton btn_revewing;

    @FXML
    private Label lbl_reviewing;

    @FXML
    private JFXButton btn_done;

    @FXML
    private Label lbl_done;

    @FXML
    private JFXButton btn_Total;

    @FXML
    private Label lbl_Total;

    @FXML
    private JFXListView<Module> lv_modules;

    private SceneHandler sceneHandler;
    private Database database;
    private ModuleDB moduleDB;
    public static String projectID;
    private ObservableList<Module> modules;
    private Module module;
    @FXML
    void initialize() {
        filterCell();
        populateList("nor");
        try {
            fillCard();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        btn_back.setOnAction(e -> {
            sceneHandler.slideScene(btn_back, ProjectCellController.cellStack, "X", "/CRM_APP/View/Project/project.fxml");
        });
        btn_addNew.setOnAction(e -> {
            ModuleDetailController.modID=null;
            ModuleDetailController.projectID=projectID;
            sceneHandler.slideScene(btn_back, ProjectCellController.cellStack, "X", "/CRM_APP/View/Module/moduleDetail.fxml");
        });
    }

    private void fillCard() throws SQLException, ClassNotFoundException {
        moduleDB = new ModuleDB();
        ResultSet row = moduleDB.getCountStatus(projectID);
        ResultSet row2 = moduleDB.countAll(projectID);
        String pending = "";
        String reviewing = "";
        String working = "";
        String done = "";
        String total  = "";
        if(row.next() && row2.next()){
            pending = row.getString("Pending");
            working = row.getString("Working");
            reviewing = row.getString("Reviewing");
            done = row.getString("Done");
            total = row2.getString(1);
        }

        lbl_pending.setText(pending);
        lbl_working.setText(working);
        lbl_reviewing.setText(reviewing);
        lbl_done.setText(done);
        lbl_Total.setText(total);
    }

    private void populateList(String status) {
        sceneHandler= new SceneHandler();
        database = new Database();
        ModuleDB moduleDB;
        modules = FXCollections.observableArrayList();
        ResultSet row = null;

        if(status.equals("nor")){
            try {
                row = database.getSomeID(projectID, Const.MODULE_TABLE, Const.MODULE_PROJECT_ID);
                while(row.next()){
                    Module module = new Module();
                    module.setProjectID(projectID);
                    module.setModuleID(row.getString("ModID"));
                    module.setModName(row.getString("ModName"));
                    module.setStatus(row.getString("Status"));

                    modules.add(module);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            moduleDB = new ModuleDB();
            try {
                module = new Module();
                module.setStatus(status);
                module.setProjectID(projectID);
                row = moduleDB.getStatus(module);
                while(row.next()){
                    module = new Module();
                    module.setProjectID(projectID);
                    module.setModuleID(row.getString("ModID"));
                    module.setModName(row.getString("ModName"));
                    module.setStatus(row.getString("Status"));

                    modules.add(module);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        lv_modules.setItems(modules);
        lv_modules.setCellFactory(ModuleCellController -> new ModuleCellController());
    }

    @FXML
    void filterEvent(ActionEvent event) {
        Button button = (Button) event.getSource();
        String btnName = button.getText().trim();

        switch (btnName){
            case "Pending":
                populateList("0");
                break;
            case "Working":
                populateList("1");
                break;
            case "Reviewing":
                populateList("2");
                break;
            case "Done":
                populateList("3");
                break;
            case "Total":
                populateList("nor");
                break;
            default:break;
        }
    }

    private void filterCell(){
        txt_module.textProperty().addListener(((observable, oldValue, newValue) -> {
            sceneHandler= new SceneHandler();
            database = new Database();
            modules = FXCollections.observableArrayList();
            moduleDB = new ModuleDB();
            ResultSet row = null;

            try {
                row = database.filterDataInput(Const.MODULE_TABLE, Const.MODULE_NAME, newValue);
                while(row.next()){
                    module = new Module();
                    module.setProjectID(projectID);
                    module.setModuleID(row.getString("ModID"));
                    module.setModName(row.getString("ModName"));
                    module.setStatus(row.getString("Status"));

                    modules.add(module);
                }
                lv_modules.setItems(modules);
                lv_modules.setCellFactory(ModuleCellController -> new ModuleCellController());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }));
    }
}

