package CRM_APP.Controller.Project;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import CRM_APP.Controller.Home.HomeController;
import CRM_APP.Controller.Project.Module.ModuleController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Project;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static CRM_APP.Controller.Project.ProjectCellController.cellStack;

public class ProjectController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ScrollPane lst_detail;

    @FXML
    private Button btn_new;

    @FXML
    private JFXListView lv_project;

    @FXML
    private StackPane main_stack;

    @FXML
    private JFXTextField txt_findName;

    private SceneHandler sceneHandler;
    private ObservableList<Project> projects;
    private Database database;

    @FXML
    void initialize() {
        try {
            populateList();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void populateList() throws SQLException, ClassNotFoundException {
        sceneHandler= new SceneHandler();
        database = new Database();
        projects = FXCollections.observableArrayList();

        ResultSet row = database.getAllTableValue(Const.PROJECT_TABLE);

        while(row.next()){
            Project project = new Project();
            project.setId(row.getString("ProjectID"));
            project.setName(row.getString("ProjectName"));
            project.setBeginTime(row.getString("BeginTime"));
            project.setEndTime(row.getString("EndTime"));
            project.setManager(row.getString("Manager"));
            project.setCusId(row.getString("CusID"));
            cellStack = main_stack;
            projects.add(project);
        }
        lv_project.setItems(projects);
        lv_project.setCellFactory(ProjectCellController -> new ProjectCellController());
    }
    @FXML
    void newProjectEvent(ActionEvent event) {
        sceneHandler.slideScene(btn_new, cellStack, "-Y","/CRM_APP/View/Project/projectDetail.fxml");
    }
}
