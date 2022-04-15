package CRM_APP.Controller.Survey;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import CRM_APP.Database.Database;
import CRM_APP.Database.Survey.SurveyDB;
import CRM_APP.Database.Survey.SurveyTypeDB;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Survey;
import CRM_APP.Model.SurveyType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import com.jfoenix.controls.JFXListCell;
import javafx.scene.layout.StackPane;

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
    private Label lbl_customer;

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

    public static StackPane cellStack;
    private FXMLLoader fxmlLoader;
    private Database database;
    private SceneHandler sceneHandler;
    private SurveyDB surveyDB;
    private Survey survey;


    @Override
    protected void updateItem(Survey item, boolean empty) {
        try {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                if (fxmlLoader == null) {
                    fxmlLoader = new FXMLLoader(getClass().getResource("/CRM_APP/View/Survey/surveyCell.fxml"));
                    fxmlLoader.setController(this);
                    try {
                        fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                lbl_name.setText(item.getSurveyName());
                lbl_customer.setText(item.getCusID());

                btn_edit.setOnAction(e -> {
                    //sceneHandler = new SceneHandler();
                    //SurveyTypeCreateController.surID = item.getSurID();
                    //sceneHandler.slideScene(btn_edit, cellStack, "-X", "/CRM_APP/View/Survey/surveyTypeCreate.fxml");
                });
                setText(null);
                setGraphic(main_pane);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
