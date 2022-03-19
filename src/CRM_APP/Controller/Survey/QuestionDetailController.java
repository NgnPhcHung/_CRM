package CRM_APP.Controller.Survey;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Survey.QuestionDB;
import CRM_APP.Database.Survey.SurveyDB;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
    private HBox hbox_noAnswer;

    @FXML
    private JFXButton btn_unhideAddAnswer;

    @FXML
    private JFXTextField txt_answer;

    @FXML
    private JFXButton btn_addAnswer;

    @FXML
    private JFXListView<Answer> lv_answerList;

    @FXML
    private JFXButton btn_save;

    @FXML
    private JFXButton btn_close;

    @FXML
    private VBox vbox_createAnswer;

    private SurveyDB surveyDB = new SurveyDB();
    private QuestionDB db = new QuestionDB();
    private Database database = new Database();
    private QuestionDB questionDB= new QuestionDB();

    private Survey survey = new Survey();
    private ObservableList<String> list = FXCollections.observableArrayList();
    private ObservableList<Answer> answers;
    private SceneHandler sceneHandler;
//    private ObservableList<Answer> answers;

    private boolean answerExist=false;
    public static String questionID;
    public static String questionText;
    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        comboBoxHandler();
        answerExist = checkAnswerExist();
        populateQuestions();

            toggleAnswer();;

        txt_question.setText(questionText);
    }
    //work with answerCell.fxml
    @FXML
    void addEvent(ActionEvent event) throws SQLException, ClassNotFoundException {
        String ans = txt_answer.getText().trim();
        String ansId = OtherHandler.generateId();

        if(!ans.equals("")){
            ResultSet taskRow = database.getSomeID(ansId, Const.QUESTION_DETAIL_TABLE, Const.QUESTIONDETAIL_ID);
            while (taskRow.next()){
                ansId = OtherHandler.generateId();
                taskRow = database.getSomeID(ansId, Const.QUESTION_DETAIL_TABLE, Const.QUESTIONDETAIL_ID);
            }

            questionDB.createQuestionDetail(ansId, questionID, ans);
            try {
                populateQuestions();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            txt_answer.setText("");
        }
    }

    @FXML
    void closeEvent(ActionEvent event) {
        System.exit(0);
    }

    //save question
    @FXML
    void saveQuestionEvent(ActionEvent event) throws SQLException, ClassNotFoundException {
        String question = txt_question.getText().trim();
        String date = OtherHandler.curentDateTime();
        if(question.equals("") && cb_surveyType.getValue().equals("")){

        }else{
            String quesID = OtherHandler.generateId();
            String surID = "";
            for (String s : cb_surveyType.getValue().split(" ")) {
                surID+=s.charAt(0);
            }
            ResultSet taskRow = database.getSomeID(quesID, Const.QUESTION_TABLE, Const.QUESTION_ID);
            while (taskRow.next()){
                quesID = OtherHandler.generateId();
                taskRow = database.getSomeID(quesID, Const.QUESTION_TABLE, Const.QUESTION_ID);
            }
            db.createQuestion(quesID, surID, question, date);
        }
        //refresh listview in Question Controller

    }

    @FXML
    void unhideEvent(ActionEvent event)  {
        answerExist=  true;
        toggleAnswer();
    }

    //populate list view
    private void populateQuestions() throws SQLException, ClassNotFoundException {
        sceneHandler= new SceneHandler();
        database = new Database();
        answers = FXCollections.observableArrayList();

        ResultSet row = questionDB.getValidAnswer(questionID);
        while(row.next()){
            Answer answer = new Answer();

            answer.setId(row.getString("ChoiceID"));
            answer.setAnswer(row.getString("Choice"));

            answers.addAll(answer);
        }
        lv_answerList.setItems(answers);
        lv_answerList.setCellFactory(AnswerCellController -> new AnswerCellController());
    }

    //look at checkAnswerExist() :')
    private void toggleAnswer(){

            hbox_noAnswer.setVisible(!answerExist);
            vbox_createAnswer.setVisible(answerExist);

    }

    //check if answer exist? vbox_createAnswer.visible? !
    //check if answer exist? hbox_noAnswer.visible? !
    private boolean checkAnswerExist()throws SQLException, ClassNotFoundException{
        ResultSet row = questionDB.getValidAnswer(questionID);
        if(row.next()){
            return true;
        }else return false;
    }

    //set items for combo box - survey_type
    private void comboBoxHandler() {
        try{
            ResultSet surveyDetail = surveyDB.getSurveyType();
            String sur ="";
            while (surveyDetail.next()){
                sur = surveyDetail.getString("SurName");
                list.add(sur);
            }
            cb_surveyType.setItems(list);
            cb_surveyType.getSelectionModel().select(0);
        }catch (SQLException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
