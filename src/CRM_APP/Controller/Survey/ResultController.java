package CRM_APP.Controller.Survey;

import CRM_APP.Database.Database;
import CRM_APP.Database.Survey.SurveyDB;
import CRM_APP.Model.SurveyDetail;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ResultController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btn_manage;

    @FXML
    private JFXTextField txt_Find;

    @FXML
    private ScrollPane lv_container;

    private VBox contentContainer = new VBox();

    public static String surveyID;
    private Database database;
    private SurveyDB surveyDB;
    private SurveyDetail surveyDetail;

    @FXML
    void initialize() {

    }

    private void addDetail(){
        surveyDB = new SurveyDB();
        surveyDetail = new SurveyDetail();
        surveyDetail.setSurveyID(surveyID);
        try {
            ResultSet row = surveyDB.getAnswerBySurvey(surveyDetail);
            while(row.next()){

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String constraintQuestionType(String typeID){
        String type = "";
        switch (typeID){
            case "QT01":
                break;
            case "QT02":
                break;
            case "QT03":
                break;
            case "QT04":
                break;
        }
        return type;
    }
}
