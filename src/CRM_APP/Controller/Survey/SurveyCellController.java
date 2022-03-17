package CRM_APP.Controller.Survey;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Survey;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import com.jfoenix.controls.JFXListCell;

public class SurveyCellController extends JFXListCell<Survey> {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox main_pane;

    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_staff;

    @FXML
    private Label lbl_project;

    @FXML
    private Label lbl_version;

    @FXML
    private Button btn_view;

    @FXML
    private Button btn_edit;

    @FXML
    private Label lbl_dateAdded;

    FXMLLoader fxmlLoader;
    SceneHandler sceneHandler = new SceneHandler();

    @FXML
    void editEvent(ActionEvent event) {

        sceneHandler.newScene("/CRM_APP/View/Survey/surveyDetail.fxml");
    }

    //Button view for view document?????

    @FXML
    void initialize() {

    }

    @Override
    protected void updateItem(Survey item, boolean empty) {
        super.updateItem(item, empty);
        String tag;
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else{
            if(fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("/CRM_APP/View/Survey/surveyCell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            lbl_name.setText(item.getName());
            lbl_staff.setText(item.getStaff());
            lbl_version.setText(item.getVersion());

            setText(null);
            setGraphic(main_pane);
        }
    }
}
