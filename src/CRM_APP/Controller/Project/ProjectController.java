package CRM_APP.Controller.Project;

import java.net.URL;
import java.util.ResourceBundle;

import CRM_APP.Model.Project;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class ProjectController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ScrollPane lst_detail;

    @FXML
    private JFXListView lv_project;

    ObservableList<Project> projects;

    @FXML
    void initialize() {
        Project project = new Project();
        project.setName("NPH Entertainment");
        project.setTag("first");
        project.setEndDate("Mar 25, 2022");
        project.setStartDate("Nov 1, 2021");
        project.setProcess(0.7f);
        projects = FXCollections.observableArrayList();
        projects.addAll(project);
        lv_project.setItems(projects);
        lv_project.setCellFactory(ProjectCellController -> new ProjectCellController());
    }
}
