package CRM_APP.Controller.Survey;

import CRM_APP.Database.Database;
import CRM_APP.Database.Survey.QuestionTypeDB;
import CRM_APP.Database.Survey.SurveyTypeDB;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.QuestionType;
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

public class QuestionTypeCellController extends JFXListCell<QuestionType> {
    @FXML
    private HBox main_pane;

    @FXML
    private Label lbl_question_type;

    @FXML
    private Button btn_edit;

    public static StackPane cellStack;
    private FXMLLoader fxmlLoader;
    private Database database;
    private SceneHandler sceneHandler;
    private QuestionTypeDB questionTypeDB;
    private QuestionType questionType;

    @Override
    protected void updateItem(QuestionType item, boolean empty) {
        try {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                if (fxmlLoader == null) {
                    fxmlLoader = new FXMLLoader(getClass().getResource("/CRM_APP/View/Survey/questionTypeCell.fxml"));
                    fxmlLoader.setController(this);
                    try {
                        fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                lbl_question_type.setText(item.getqTypeName());

                btn_edit.setOnAction(e -> {
                    sceneHandler = new SceneHandler();
                    QuestionTypeCreateController.questionTypeID = item.getqTypeID();
                    sceneHandler.slideScene(btn_edit, cellStack, "-X", "/CRM_APP/View/Survey/questionTypeCreate.fxml");
                });
                setText(null);
                setGraphic(main_pane);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
