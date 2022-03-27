package CRM_APP.Controller.Project.Module;

import CRM_APP.Controller.Project.ProjectCellController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Project.ModuleDB;
import CRM_APP.Database.Project.ProjectDB;
import CRM_APP.Database.Survey.SurveyDB;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Module;
import com.gluonhq.charm.glisten.control.ToggleButtonGroup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ModuleDetailController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txt_name;

    @FXML
    private JFXComboBox<String> cb_project;

    @FXML
    private ToggleButton tog_pend;

    @FXML
    private ToggleButton tog_work;

    @FXML
    private ToggleButton tog_review;

    @FXML
    private ToggleButton tog_done;

    @FXML
    private Label lbl_noti;

    @FXML
    private JFXButton btn_save;

    @FXML
    private Button btn_back;

    private String status;
    private ToggleGroup group;
    private ModuleDB moduleDB;
    public static String modID;
    SceneHandler sceneHandler;
    Database database ;
    public static String projectID;
    private ObservableList<String> list = FXCollections.observableArrayList();
    @FXML
    void changeScene(ActionEvent event) {
        sceneHandler = new SceneHandler();
        sceneHandler.slideScene(btn_back, ProjectCellController.cellStack, "X", "/CRM_APP/View/Module/module.fxml");
    }

    @FXML
    void initialize() {
        comboBoxHandler();
        database = new Database();
        if(modID==null){
            cb_project.setDisable(false);
        }else{
            cb_project.setDisable(true);
            manageToggle();
            try {
                ResultSet row = database.getSomeID(projectID, Const.PROJECT_TABLE, "ProjectID");
                if(row.next()) {
                    cb_project.setValue(row.getString("ProjectName"));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        group = new ToggleGroup();
        tog_pend.setUserData(0);
        tog_work.setUserData(1);
        tog_review.setUserData(2);
        tog_done.setUserData(3);
        tog_done.setToggleGroup(group);
        tog_pend.setToggleGroup(group);
        tog_review.setToggleGroup(group);
        tog_work.setToggleGroup(group);
        status=group.getSelectedToggle().getUserData()+"";
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                   status=group.getSelectedToggle().getUserData()+"";
                }
            }
        });

        btn_save.setOnAction(e -> {
            if(modID.equals(null)){
               if(txt_name.getText().equals("")) {
                   lbl_noti.setText("Invalid Name");
                }else {
                    save();
               }
            }else{
                update();
            }
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_back, ProjectCellController.cellStack, "X", "/CRM_APP/View/Module/module.fxml");
        });

        populateDetail();
    }

    private void save(){
        moduleDB = new ModuleDB();
        Module module = new Module();
        database = new Database();

        String proId ="";
        String projectName = cb_project.getSelectionModel().getSelectedItem();
        try {
            ResultSet row = database.getSomeID(projectName, Const.PROJECT_TABLE, "ProjectName");
            if (row.next()){
                proId= row.getString("ProjectID");
                module.setModuleID(OtherHandler.generateId());
                module.setModName(txt_name.getText().trim());
                module.setStatus(status);
                module.setProjectID(proId);
                moduleDB.insertModule(module);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void manageToggle(){
        try {
            ResultSet row = database.getSomeID(modID, Const.MODULE_TABLE, "ModID");
            if(row.next()){
                String s = row.getString("Status");
                if(s.equals("0")){
                    tog_pend.setSelected(true);
                }else if(s.equals("1")){
                    tog_work.setSelected(true);
                }else if(s.equals("2")){
                    tog_review.setSelected(true);
                }else if(s.equals("3")){
                    tog_done.setSelected(true);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void populateDetail(){
        Database database = new Database();
        try {
            ResultSet row = database.getSomeID(modID, Const.MODULE_TABLE, Const.MODULE_ID);
            while (row.next()){
                txt_name.setText(row.getString("ModName"));
//                cb_project.setPromptText();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    private void comboBoxHandler() {
        try{
            ProjectDB projectDB=  new ProjectDB();
            ResultSet row = projectDB.getProjectList();
            String name ="";
            while (row.next()){
                name = row.getString("ProjectName");
                list.add(name);
            }
            cb_project.setItems(list);
            cb_project.getSelectionModel().select(0);
        }catch (SQLException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    private void update(){
        Module module = new Module();
        moduleDB = new ModuleDB();
        if(txt_name.getText().equals("")){
            lbl_noti.setText("Invalid Name");
        }else{
            module.setModuleID(modID);
            module.setModName(txt_name.getText());
            module.setStatus(status);

            moduleDB.updateModule(module);
        }
    }
}