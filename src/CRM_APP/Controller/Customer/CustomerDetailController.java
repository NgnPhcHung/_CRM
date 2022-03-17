package CRM_APP.Controller.Customer;

import CRM_APP.Handler.SceneHandler;
import CRM_APP.Handler.TextfieldHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CustomerDetailController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txt_name;

    @FXML
    private JFXTextField txt_email;

    @FXML
    private JFXTextField txt_address;

    @FXML
    private JFXButton btn_edit;

    @FXML
    private Label lbl_numProject;

    @FXML
    private JFXButton btn_project;

    @FXML
    private JFXButton btn_contact;

    @FXML
    private Label lbl_noti;

    @FXML
    private Label lbl_projectName;


    SceneHandler sceneHandler;
    TextfieldHandler textfieldHandler;

    @FXML
    void saveEvent(ActionEvent event) {
        if(!textfieldHandler.validateEmail(txt_email)){
            lbl_noti.setText("Please input valid email");
        }else
            lbl_noti.setText("");
    }

    @FXML
    void showProjectEvent(ActionEvent event) {
        sceneHandler = new SceneHandler();
        sceneHandler.newScene("/CRM_APP/View/Project/project.fxml");
    }

    @FXML
    void showContactEvent(ActionEvent event) {
        sceneHandler = new SceneHandler();
        sceneHandler.newScene("/CRM_APP/View/Customer/contact.fxml");
    }

    @FXML
    void initialize() {
        textfieldHandler = new TextfieldHandler();

    }
}
