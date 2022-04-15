package CRM_APP.Controller.Survey;


import CRM_APP.Handler.SceneHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;


public class SurveyMenuController {
    @FXML
    private Button btn_survey_type;

    @FXML
    public StackPane main_stack;

    private SceneHandler sceneHandler;

    // Click item menu
    @FXML
    void initialize() {
        try {
            sceneHandler = new SceneHandler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void mouseClickEvent(MouseEvent event) {
        try {
            Button button = (Button) event.getSource();
            String btnName = button.getText().trim();

            // Lười viết hàm tách, làm vậy cho lẹ :))))
            switch (btnName) {
                case "Survey Type":
                    sceneHandler.slideScene(btn_survey_type, main_stack, "-Y", "/CRM_APP/View/Survey/surveyType.fxml");
                    break;
                case "Question Type":
                    sceneHandler.slideScene(btn_survey_type, main_stack, "-Y", "/CRM_APP/View/Survey/questionType.fxml");
                    break;
                case "Question":
                    sceneHandler.slideScene(btn_survey_type, main_stack, "-Y", "/CRM_APP/View/Survey/question.fxml");
                    break;
                case "Create Survey":
                    sceneHandler.slideScene(btn_survey_type, main_stack, "-Y", "/CRM_APP/View/Survey/survey.fxml");
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
