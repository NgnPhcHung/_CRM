package CRM_APP.Controller.Survey;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.SurveyType;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SurveyTypeController {
    @FXML
    public StackPane main_stack;

    @FXML
    public Button btn_back;

    @FXML
    private JFXButton btn_add;


    @FXML
    private JFXListView<SurveyType> lv_survey_type;

    private SceneHandler sceneHandler;
    private Database database;
    private ObservableList<SurveyType> surveyTypes;
    private SurveyType surveyType;

    @FXML
    void initialize() {
        populateList();

        btn_add.setOnAction(e -> {
            SurveyTypeCreateController.surID = null;
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_add, main_stack, "-X", "/CRM_APP/View/Survey/surveyTypeCreate.fxml");
        });

        btn_back.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_back, SurveyTypeCellController.cellStack, "-X", "/CRM_APP/View/Survey/surveyMenu.fxml");
        });
    }

    private void populateList() {
        sceneHandler = new SceneHandler();
        database = new Database();
        surveyTypes = FXCollections.observableArrayList();
        ResultSet row = null;
        surveyType = new SurveyType();
        try {
            row = database.getAllTableValue(Const.SURVEY_TYPE_TABLE);
            while (row.next()) {
                surveyType = new SurveyType();
                surveyType.setSurID(row.getString(Const.SURVEYTYPE_ID));
                surveyType.setSurName(row.getString(Const.SURVEYTYPE_NAME));
                SurveyTypeCellController.cellStack = main_stack;
                surveyTypes.add(surveyType);
            }
            lv_survey_type.setItems(surveyTypes);
            lv_survey_type.setCellFactory(SurveyTypeCellController -> new SurveyTypeCellController());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
}
