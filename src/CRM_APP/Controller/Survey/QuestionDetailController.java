package CRM_APP.Controller.Survey;

import CRM_APP.Controller.Home.HomeController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Survey.QuestionDB;
import CRM_APP.Database.Survey.QuestionTypeDB;
import CRM_APP.Database.Survey.SurveyTypeDB;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Handler.ShakerHandler;
import CRM_APP.Model.Answer;
import CRM_APP.Model.Question;
import CRM_APP.Model.Survey;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

public class QuestionDetailController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GridPane grid_Main;

    @FXML
    private JFXTextField txt_question;

    @FXML
    private JFXComboBox<String> cb_surveyType;

    @FXML
    private JFXComboBox<String> cb_questionType;

    @FXML
    private HBox hbox_noAnswer;

    @FXML
    private JFXButton btn_unhideAddAnswer;

    @FXML
    public JFXTextField txt_answer;

    @FXML
    private JFXButton btn_addAnswer;

    @FXML
    private JFXListView<Answer> lv_answerList;

    @FXML
    private JFXButton btn_save;

    @FXML
    private Button btn_Back;

    @FXML
    private JFXButton btn_Delete;

    @FXML
    private VBox vbox_createAnswer;

    @FXML
    private Label lbl_Notification;


    private SurveyTypeDB surveyTypeDB = new SurveyTypeDB();
    private QuestionTypeDB questionTypeDB = new QuestionTypeDB();
    private QuestionDB db = new QuestionDB();
    private Database database = new Database();
    private QuestionDB questionDB = new QuestionDB();

    private Survey survey = new Survey();
    private ObservableList<String> listSurveyType = FXCollections.observableArrayList();
    private ObservableList<String> listQuestionType = FXCollections.observableArrayList();
    private ObservableList<Answer> answers;
    private SceneHandler sceneHandler;

    private boolean answerExist = false;
    public static String questionID= "";
    public static String questionText ="";

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        grid_Main.getStylesheets().add(HomeController.styleSheet);

        comboBoxHandler();
        if(com.mysql.cj.util.StringUtils.isNullOrEmpty(questionID)){
            btn_Delete.setVisible(false);
            txt_question.setText("");
        }else{
            txt_question.setText(questionText);
        }
        answerExist = checkAnswerExist();
        populateQuestions();

        toggleAnswer();

        btn_Back.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_Back, QuestionCellController.stackCell, "-X", "/CRM_APP/View/Survey/question.fxml");
        });

        btn_Delete.setOnAction(e -> {
            deleteQuestion();
            btn_Back.fire();
        });

        btn_save.setOnAction(e -> {
            saveQuestion();
        });
        btn_unhideAddAnswer.setOnAction(e -> {
            toggleAnswer();
        });
    }

    //work with questionDetailCell.fxml
    @FXML
    void addEvent(ActionEvent event) throws SQLException, ClassNotFoundException {
        String ans = txt_answer.getText().trim();
        String ansId = OtherHandler.generateId();

        if (!ans.equals("")) {
            ResultSet taskRow = database.getSomeID(ansId, Const.QUESTION_DETAIL_TABLE, Const.QUESTIONDETAIL_ID);
            while (taskRow.next()) {
                ansId = OtherHandler.generateId();
                taskRow = database.getSomeID(ansId, Const.QUESTION_DETAIL_TABLE, Const.QUESTIONDETAIL_ID);
            }

            questionDB.createQuestionDetail(ansId, questionID, ans);
            try {
                populateQuestions();
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
            txt_answer.setText("");
        }
    }

    //save question
    private void saveQuestion(){
        if (StringUtils.isEmpty(txt_question.getText()) || cb_surveyType.getValue().equals("") || cb_questionType.getValue().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Please input data");
            alert.setContentText("Question, survey type and question can't empty!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        } else {
            String questionName = txt_question.getText().trim();
            String quesID = OtherHandler.generateId();
            String surID = getIdSurveyType(cb_surveyType.getValue().trim());
            String questionTypeID = getIdQuestionType(cb_questionType.getValue().trim());

            database =  new Database();

            //region SAVE
            if(StringUtils.isEmpty(questionID)){
                try {
                    ResultSet rsCheckName = database.getSomeID(questionName, Const.QUESTION_TABLE, Const.QUESTION_NAME);
                    if(rsCheckName.next()) {
                        ShakerHandler shakerHandler = new ShakerHandler(txt_question, 2, 50);
                        shakerHandler.shake();
                        lbl_Notification.setVisible(true);
                        lbl_Notification.setText("This question already exist");
                        txt_question.setText("");
                    }else{
                        ResultSet taskRow = null;
                        taskRow = database.getSomeID(quesID, Const.QUESTION_TABLE, Const.QUESTION_ID);
                        while (taskRow.next()) {
                            quesID = OtherHandler.generateId();
                            taskRow = database.getSomeID(quesID, Const.QUESTION_TABLE, Const.QUESTION_ID);
                        }
                        db.createQuestion(quesID, surID, questionName, questionTypeID);
                        questionID = quesID;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            //endregion

            //region UPDATE
            else{
                String quesUpdateID = questionID;
                Question question = new Question();
                question.setQuestionId(quesUpdateID);
                question.setQuestionName(questionName);
                question.setTypeID(questionTypeID);
                question.setSurID(surID);
                questionDB = new QuestionDB();
                questionDB.updateQuestion(question);
            }
            //endregion
            if(!questionTypeID.equals("QT01")) {
                btn_unhideAddAnswer.setVisible(true);
            }
        }
    }

    //populate list view
    private void populateQuestions() throws SQLException, ClassNotFoundException {
        sceneHandler = new SceneHandler();
        database = new Database();
        answers = FXCollections.observableArrayList();
        Question question = new Question();
        question.setQuestionId(questionID);
        ResultSet row = questionDB.getValidAnswer(questionID);
        ResultSet rowSur = questionDB.getSur(question);
        while(rowSur.next()){
            cb_surveyType.setValue(rowSur.getString(Const.SURVEYTYPE_NAME));
            cb_questionType.setValue(rowSur.getString(Const.QUESTIONTYPE_NAME));
        }
        while (row.next()) {
            Answer answer = new Answer();
            answer.setId(row.getString(Const.QUESTIONDETAIL_ID));
            answer.setAnswer(row.getString(Const.QUESTIONDETAIL_ANSWER));
            answers.addAll(answer);
        }
        lv_answerList.setItems(answers);
        lv_answerList.setCellFactory(AnswerCellController -> new AnswerCellController());
    }

    //look at checkAnswerExist() :')
    private void toggleAnswer() {
        vbox_createAnswer.setVisible(false);
        database = new Database();
        try {
            ResultSet row = database.getSomeID(questionID, Const.QUESTION_TABLE, Const.QUESTION_ID);
            if(row.next()){
                if(!row.getString(Const.QUESTIONTYPE_ID).equals("QT01")){
                    vbox_createAnswer.setVisible(true);
                } else vbox_createAnswer.setVisible(false);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //check if answer exist? vbox_createAnswer.visible? !
    //check if answer exist? hbox_noAnswer.visible? !
    private boolean checkAnswerExist() throws SQLException, ClassNotFoundException {
        ResultSet row = questionDB.getValidAnswer(questionID);
        if (row.next()) {
            return true;
        } else return false;
    }

    //set items for combo box - survey_type
    private void comboBoxHandler() {
        OtherHandler.populateComboBox(cb_surveyType, Const.SURVEY_TYPE_TABLE, Const.SURVEYTYPE_NAME);
        OtherHandler.populateComboBox(cb_questionType, Const.QUESTION_TYPE_TABLE, Const.QUESTIONTYPE_NAME);

        cb_questionType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("Câu hỏi tự luận")){
                vbox_createAnswer.setVisible(false);
            }
        });
    }

    // get id from data list
    private String getIdSurveyType(String surveyType) {
        try {
            ResultSet surveyDetail = surveyTypeDB.getSurveyType();
            while (surveyDetail.next()) {
                if (surveyType.equals(surveyDetail.getString(Const.SURVEYTYPE_NAME)))
                    return surveyDetail.getString(Const.SURVEYTYPE_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getIdQuestionType(String questionType) {
        try {
            ResultSet questionDetail = questionTypeDB.getQuestionType();
            while (questionDetail.next()) {
                if (questionType.equals(questionDetail.getString(Const.QUESTIONTYPE_NAME)))
                    return questionDetail.getString(Const.QUESTIONTYPE_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void deleteQuestion(){
        database = new Database();
        database.detele(Const.QUESTION_DETAIL_TABLE, Const.QUESTION_ID, questionID);
        database = new Database();
        database.detele(Const.QUESTION_TABLE, Const.QUESTION_ID, questionID);
    }
}

