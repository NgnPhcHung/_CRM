package CRM_APP.Controller.Survey;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Survey;
import CRM_APP.Model.SurveyType;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class SurveyController {
    @FXML
    public StackPane main_stack;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btn_add;

    @FXML
    private JFXTextField txt_search;

    @FXML
    private JFXListView<Survey> lv_product;

    @FXML
    public Button btn_back;

    private ObservableList<Survey> surveys;
    private SceneHandler sceneHandler;
    private Database database;
    private Survey survey;

    @FXML
    void initialize() {
        populateList();
        btn_add.setOnAction(e -> {
            SurveyTypeCreateController.surID = null;
            SurveyCreateController.backPane = main_stack;

            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_add, main_stack, "X", "/CRM_APP/View/Survey/surveyCreate.fxml");
        });

        btn_back.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_back, main_stack, "-X", "/CRM_APP/View/Survey/surveyMenu.fxml");
        });
    }

    private void populateList() {
        sceneHandler = new SceneHandler();
        database = new Database();
        surveys = FXCollections.observableArrayList();
        ResultSet row = null;
        survey = new Survey();
        try {
            row = database.getAllTableValue(Const.SURVEY_TABLE);
            while (row.next()) {
                survey = new Survey();
                survey.setSurveyID(row.getString(Const.SURVEY_ID));
                survey.setCusID(row.getString(Const.CUSTOMER_ID));
                survey.setSurveyName(row.getString(Const.SURVEY_NAME));
                SurveyCellController.cellStack = main_stack;
                surveys.add(survey);
            }
            lv_product.setItems(surveys);
            lv_product.setCellFactory(SurveyCellController -> new SurveyCellController());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
}
