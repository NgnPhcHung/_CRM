package CRM_APP.Controller.Survey;

import CRM_APP.Controller.Home.HomeController;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Question;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class QuestionCellController extends JFXListCell<Question> {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox main_pane;

    @FXML
    private Label lbl_question;

    @FXML
    private Label lbl_surveyType;

    @FXML
    private Button btn_edit;

    @FXML
    private Label lbl_dateAdded;

    private FXMLLoader fxmlLoader ;
    private SceneHandler sceneHandler;
    private Question question;


    @FXML
    void initialize() {
        sceneHandler = new SceneHandler();
        question = new Question();
//        sceneHandler.newScene("/CRM_APP/View/Survey/questionDetail.fxml");

    }

    @Override
    protected void updateItem(Question item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else{
            if(fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("/CRM_APP/View/Survey/questionCell.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            lbl_question.setText(item.getQuestionName());
            lbl_dateAdded.setText(item.getQuestionName());
            lbl_surveyType.setText(item.getSurID());
            String questionId = item.getQuestionId();
            String questionText = item.getQuestionName();
            btn_edit.setOnAction(event -> {
                QuestionDetailController.questionID=questionId;
                QuestionDetailController.questionText=questionText;
                sceneHandler.newScene("/CRM_APP/View/Survey/questionDetail.fxml");

            });

            setText(null);
            setGraphic(main_pane);
        }
    }

}
