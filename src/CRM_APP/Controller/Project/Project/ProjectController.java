package CRM_APP.Controller.Project.Project;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import CRM_APP.Controller.Project.Project.ProjectCellController;
import CRM_APP.Controller.Project.Project.ProjectDetailsController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Project.ProjectDB;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Project;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;

import static CRM_APP.Controller.Project.Project.ProjectCellController.cellStack;

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
        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                populateList();
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        filterCell();
        btn_new.setOnAction(e->{
            ProjectDetailsController.projectID = "null";
            ProjectDetailsController.backPane = main_stack;
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_new, main_stack, "-X", "/CRM_APP/View/Project/projectDetail.fxml");
        });
    }

    private void populateList(){
        sceneHandler= new SceneHandler();
        database = new Database();
        projects = FXCollections.observableArrayList();
        ResultSet row = null;
        try {
            row = database.getAllTableValue(Const.PROJECT_TABLE);
            while(row.next()){
                Project project = new Project();
                project.setId(row.getString("ProjectID"));
                project.setName(row.getString("ProjectName"));
                project.setBeginTime(row.getString("BeginTime"));
                project.setEndTime(row.getString("EndTime"));
                project.setManager(row.getString("Manager"));
                project.setCusId(row.getString("CusID"));
                ProjectCellController.cellStack = main_stack;
                projects.add(project);
            }
            lv_project.setItems(projects);
            lv_project.setCellFactory(ProjectCellController -> new ProjectCellController());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void newProjectEvent(ActionEvent event) {
//
    }
    //filter cell when user input find project
    void filterCell() {
        txt_findName.textProperty().addListener(((observable, oldValue, newValue) -> {
            sceneHandler= new SceneHandler();
            database = new Database();
            projects = FXCollections.observableArrayList();
            ResultSet row = null;
            try {
                row = database.filterDataInput(Const.PROJECT_TABLE, Const.PROJECT_NAME, newValue);
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
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }));
    }
}
