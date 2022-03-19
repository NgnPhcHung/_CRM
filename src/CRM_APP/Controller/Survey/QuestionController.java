package CRM_APP.Controller.Survey;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Answer;
import CRM_APP.Model.Question;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class QuestionController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btn_manage;

    @FXML
    private JFXTextField txt_search;

    @FXML
    private JFXListView<Question> lv_questionlist;

    private ObservableList<Question> questions;
    private SceneHandler sceneHandler;
    private Database database;

    private Thread listViewThread;
    private boolean isRunning = true;
    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        listViewThread= new Thread(this::handleThread);
        listViewThread.start();

        populateQuestions();
        btn_manage.setOnAction(event -> sceneHandler.newScene("/CRM_APP/View/Survey/questionDetail.fxml"));
    }

    //populate question list in database to list view
    private void populateQuestions() throws SQLException, ClassNotFoundException {
        sceneHandler= new SceneHandler();
        database = new Database();
        questions = FXCollections.observableArrayList();

        ResultSet row = database.getAllTableValue(Const.QUESTION_TABLE);
        while(row.next()){
            Question question = new Question();

            question.setQuestionId(row.getString("QuestionID"));
            question.setQuestionName(row.getString("QuestionName"));
            question.setDateAdd(row.getString("DateAdd"));
            question.setSurID(row.getString("SurID"));

            questions.addAll(question);
        }
        lv_questionlist.setItems(questions);
        lv_questionlist.setCellFactory(QuestionCellController -> new QuestionCellController());
    }

    //refresh listview
    private void handleThread(){
        while(isRunning){
            Platform.runLater(() ->{
                try {
                    populateQuestions();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            try{

                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
