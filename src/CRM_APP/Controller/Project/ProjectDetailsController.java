package CRM_APP.Controller.Project;

import CRM_APP.Handler.SceneHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import static CRM_APP.Controller.Project.ProjectCellController.cellStack;

public class ProjectDetailsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txt_name;

    @FXML
    private JFXComboBox<?> cb_customer;

    @FXML
    private JFXComboBox<?> cb_manager;

    @FXML
    private JFXDatePicker dp_start;

    @FXML
    private JFXDatePicker dp_end;

    @FXML
    private JFXTextField txt_amount;

    @FXML
    private Label lbl_noti;

    @FXML
    private JFXButton btn_save;

    @FXML
    private Button btn_back;

    SceneHandler sceneHandler;

    @FXML
    void initialize() {

    }

    @FXML
    void changeScene(ActionEvent event) {
        sceneHandler =  new SceneHandler();
        sceneHandler.slideScene(btn_back, ProjectCellController.cellStack, "-X", "/CRM_APP/View/Project/project.fxml");
        txt_amount.setText("");
        txt_name.setText("");
        cb_customer.getSelectionModel().clearSelection();
        cb_manager.getSelectionModel().clearSelection();
        dp_start.getEditor().clear();
        dp_end.getEditor().clear();
        lbl_noti.setText("");
    }

}
