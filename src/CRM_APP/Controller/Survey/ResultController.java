package CRM_APP.Controller.Survey;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Survey.SurveyDB;
import CRM_APP.Handler.NotificationHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Question;
import CRM_APP.Model.SurveyDetail;
import com.jfoenix.controls.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import javax.imageio.ImageIO;

public class ResultController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btn_Export;


    @FXML
    private JFXButton btn_Back;

    @FXML
    private ScrollPane sc_container;

    private VBox contentContainer = new VBox();

    public static String surveyID;
    private Database database;
    private SurveyDB surveyDB;
    private SurveyDetail surveyDetail;
    private String oldQuestion="";
    private Question  question;
    private SceneHandler sceneHandler;
    private  boolean isSameQuestion;
    private String surveyName = "";
    private NotificationHandler notification;
    VBox questionContainer = new VBox();
    VBox answerContainer = new VBox();
    ToggleGroup group = new ToggleGroup();

    @FXML
    void initialize() {
        addDetail();
        sc_container.setContent(contentContainer);
        sc_container.setFitToWidth(true);
        btn_Export.setOnAction(e -> {
            export();
        });
        btn_Back.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_Back, SurveyCellController.cellStack, "-X", "/CRM_APP/View/Survey/survey.fxml");
        });
    }

    private void addDetail(){
        surveyDB = new SurveyDB();
        surveyDetail = new SurveyDetail();
        surveyDetail.setSurveyID(surveyID);
        String currentQuestion ="";
        title();
        database = new Database();
        try {
            surveyDetail.setSurveyID(surveyID);
            ResultSet row = surveyDB.getNormalQuestion(surveyDetail);
            while(row.next()){
                currentQuestion = row.getString(Const.QUESTION_ID);
                questionContainer = new VBox();
                Label questionText = new Label(row.getString(Const.QUESTION_NAME));

                String type =row.getString(Const.QUESTIONTYPE_ID);
                String ans ="";
                isSameQuestion= currentQuestion.equals(oldQuestion);
                if(isSameQuestion){
                    if(!type.equals("QT01")){
                        ResultSet rs = database.getSomeID(currentQuestion, Const.QUESTION_DETAIL_TABLE, Const.QUESTION_ID);
                        while (rs.next()) {
                            ans = rs.getString(Const.QUESTIONDETAIL_ANSWER);
                            createQuestion(type, ans, answerContainer);
                        }
                    }
                    createQuestion(type, ans, answerContainer);
                }else {
                    group = new ToggleGroup();
                    answerContainer = new VBox();
                    if(!type.equals("QT01")){
                        ResultSet rs = database.getSomeID(currentQuestion, Const.QUESTION_DETAIL_TABLE, Const.QUESTION_ID);
                        while (rs.next()) {
                            ans = rs.getString(Const.QUESTIONDETAIL_ANSWER);
                            createQuestion(type, ans, answerContainer);
                        }
                    }else createQuestion(type, ans, answerContainer);

                    if (type.equals("QT02") || type.equals("QT03")){

                    }
                    questionContainer.getChildren().addAll(questionText, answerContainer);

                    oldQuestion = currentQuestion;
                    contentContainer.getChildren().addAll(questionContainer);
                    questionContainer = new VBox();
                }

                contentContainer.setStyle("-fx-padding: 0px 0px 10px 15px; -fx-spacing: 10px");
                questionContainer.setStyle( "-fx-padding: 25px;" +
                                            "-fx-border-insets: 25px ;" +
                                            "-fx-background-insets: 25px;");
                answerContainer.setStyle("-fx-spacing: 10px");
                questionText.getStyleClass().addAll("text", "h4", "hBold");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createQuestion(String type, String ans, VBox ansContain){
        switch (type){
            case "QT01":
                JFXTextArea textArea = new JFXTextArea();
                textArea.setPromptText(". . . ");
                textArea.setPrefHeight(100);
                textArea.getStyleClass().addAll("text", "textInput", "h4");
                ansContain.getChildren().add(textArea);

                break;
            case "QT02" :
            case "QT03" :
                    JFXRadioButton radioButton = new JFXRadioButton(ans);
                    radioButton.getStyleClass().addAll("text", "h4");
                    radioButton.setToggleGroup(group);
                    ansContain.getChildren().add(radioButton);
                break;
            case "QT04":
                JFXCheckBox checkbox = new JFXCheckBox(ans);
                checkbox.getStyleClass().addAll("text", "h4");
                ansContain.getChildren().add(checkbox);
                break;
        }
    }

    private void title(){
        surveyDB = new SurveyDB();
        VBox vBoxTitle = new VBox();
        vBoxTitle.setAlignment(Pos.CENTER);
        ResultSet row = surveyDB.getCustomer(surveyID);
        try {
            if(row.next()){
                surveyName = row.getString(Const.SURVEY_NAME);
                Label bigHeader = new Label(surveyName);
                bigHeader.getStyleClass().addAll("h1", "hBold", "text");
                Label customer = new Label("Customer: "+row.getString(Const.CUSTOMER_NAME));
                customer.getStyleClass().addAll("h2", "hBold", "hItalic", "text");
                Label employee = new Label("Employee: " +row.getString(Const.EMPLOYEE_NAME));
                employee.getStyleClass().addAll("h4", "hBold", "text");
                vBoxTitle.getChildren().addAll(bigHeader, customer, employee);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        contentContainer.getChildren().add(vBoxTitle);
    }

    private void export(){
        String fileName = surveyName ;
        notification = new NotificationHandler();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(btn_Back.getScene().getWindow());
        WritableImage image = sc_container.snapshot(new SnapshotParameters(), null);
        File file = new File(selectedDirectory + "\\"+ fileName + ".png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            notification.popup(notification.success, "Your file saved at: " + file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
