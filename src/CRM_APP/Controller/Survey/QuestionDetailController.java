package CRM_APP.Controller.Survey;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Survey.QuestionDB;
import CRM_APP.Database.Survey.QuestionTypeDB;
import CRM_APP.Database.Survey.SurveyTypeDB;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Answer;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

public class QuestionDetailController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
//    private ObservableList<Answer> answers;

    private boolean answerExist = false;
    public static String questionID;
    public static String questionText;
    //public String questionIdCreated = null;
    private Thread listViewThread;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        hbox_noAnswer.setVisible(false);
        comboBoxHandler();
        if(StringUtils.isEmpty(questionID)){
            btn_Delete.setVisible(false);
        }
        answerExist = checkAnswerExist();
//        populateQuestions();
        listViewThread = new Thread(this::handleThread);
        listViewThread.start();
        toggleAnswer();
        txt_question.setText(questionText);

        btn_Back.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_Back, QuestionCellController.stackCell, "-X", "/CRM_APP/View/Survey/question.fxml");
        });
    }

    //work with answerCell.fxml
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

    @FXML
    void closeEvent(ActionEvent event) {

    }

    //save question
    @FXML
    void saveQuestionEvent(ActionEvent event) throws SQLException, ClassNotFoundException {

        //String date = OtherHandler.curentDateTime();
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
            String question = txt_question.getText().trim();
            String quesID = OtherHandler.generateId();
            String surID = getIdSurveyType(cb_surveyType.getValue().trim());
            String questionTypeID = getIdQuestionType(cb_questionType.getValue().trim());
            ResultSet taskRow = database.getSomeID(quesID, Const.QUESTION_TABLE, Const.QUESTION_ID);
            while (taskRow.next()) {
                quesID = OtherHandler.generateId();
                taskRow = database.getSomeID(quesID, Const.QUESTION_TABLE, Const.QUESTION_ID);
            }
            db.createQuestion(quesID, surID, question, questionTypeID);
            questionID = quesID;
            hbox_noAnswer.setVisible(true);
        }
        //refresh listview in Question Controller

    }

    @FXML
    void unhideEvent(ActionEvent event) {
        answerExist = true;
        toggleAnswer();
    }

    //populate list view
    private void populateQuestions() throws SQLException, ClassNotFoundException {
        sceneHandler = new SceneHandler();
        database = new Database();
        answers = FXCollections.observableArrayList();

        ResultSet row = questionDB.getValidAnswer(questionID);
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
        //hbox_noAnswer.setVisible(!answerExist);
        vbox_createAnswer.setVisible(answerExist);
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
        try {
            ResultSet surveyDetail = surveyTypeDB.getSurveyType();
            String sur = "";
            while (surveyDetail.next()) {
                sur = surveyDetail.getString(Const.SURVEYTYPE_NAME);
                listSurveyType.add(sur);
            }
            cb_surveyType.setItems(listSurveyType);
            cb_surveyType.getSelectionModel().select(0);

            ResultSet questionTypeDetail = questionTypeDB.getQuestionType();
            String ques = "";
            while (questionTypeDetail.next()) {
                ques = questionTypeDetail.getString(Const.QUESTIONTYPE_NAME);
                listQuestionType.add(ques);
            }
            cb_questionType.setItems(listQuestionType);
            cb_questionType.getSelectionModel().select(0);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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

    private void handleThread() {
        while (true) {
            Platform.runLater(() -> {
                try {
                    populateQuestions();
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            });
            try {

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

