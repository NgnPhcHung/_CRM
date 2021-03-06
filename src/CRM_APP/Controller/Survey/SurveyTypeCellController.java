package CRM_APP.Controller.Survey;

import CRM_APP.Controller.Employee.Employee.EmployeeProfileController;
import CRM_APP.Database.Database;
import CRM_APP.Database.Survey.SurveyTypeDB;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Survey;
import CRM_APP.Model.SurveyType;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.sql.ResultSet;

public class SurveyTypeCellController extends JFXListCell<SurveyType> {
    @FXML
    private HBox main_pane;

    @FXML
    private Label lbl_survey_type;

    @FXML
    private Label lbl_numberQuestion;

    @FXML
    private Button btn_edit;


    public static StackPane cellStack;
    private FXMLLoader fxmlLoader;
    private Database database;
    private SceneHandler sceneHandler;
    private SurveyTypeDB surveyTypeDB;
    private Survey survey;
    private SurveyType surveyType;

    @Override
    protected void updateItem(SurveyType item, boolean empty) {
        try {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                if (fxmlLoader == null) {
                    fxmlLoader = new FXMLLoader(getClass().getResource("/CRM_APP/View/Survey/surveyTypeCell.fxml"));
                    fxmlLoader.setController(this);
                    try {
                        fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                surveyType = new SurveyType();
                lbl_survey_type.setText(item.getSurName());
                surveyType.setSurID(item.getSurID());
                surveyTypeDB = new SurveyTypeDB();
                ResultSet row = surveyTypeDB.countQuestion(surveyType);
                while(row.next()){
                    lbl_numberQuestion.setText(row.getString(1));
                }
                btn_edit.setOnAction(e -> {
                    sceneHandler = new SceneHandler();
                    SurveyTypeCreateController.surID = item.getSurID();
                    SurveyTypeCreateController.backPane = cellStack;

                    sceneHandler.slideScene(btn_edit, cellStack, "-X", "/CRM_APP/View/Survey/surveyTypeCreate.fxml");
                });
                this.setOnMouseClicked(e -> {
                    sceneHandler = new SceneHandler();
                    SurveyTypeCreateController.surID = item.getSurID();
                    SurveyTypeCreateController.backPane = cellStack;
                    sceneHandler.slideScene(btn_edit, cellStack, "-X", "/CRM_APP/View/Survey/surveyTypeCreate.fxml");
                });
                setText(null);
                setGraphic(main_pane);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
