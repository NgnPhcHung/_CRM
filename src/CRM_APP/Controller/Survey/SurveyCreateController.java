package CRM_APP.Controller.Survey;

import CRM_APP.Controller.Home.HomeController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Survey.SurveyDB;
import CRM_APP.Handler.NotificationHandler;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Survey;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.mysql.cj.util.StringUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;


public class SurveyCreateController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label lbl_title;

    @FXML
    private JFXTextField txt_Name;

    @FXML
    private JFXComboBox<String> cb_Customer;

    @FXML
    private Label lbl_Noti;

    @FXML
    private JFXButton btn_Save;

    @FXML
    private Button btn_Back;

    @FXML
    private JFXButton btn_Delete;

    private Database database;
    private SurveyDB surveyDB;
    private Survey survey;
    private SceneHandler sceneHandler;
    private NotificationHandler notification;
    private NotificationHandler notfication;

    public static StackPane backPane;
    public static String surveyID;

    @FXML
    void initialize() {
        populateComboBox();
        btn_Back.setOnAction(e ->{
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_Back, backPane, "Y", "/CRM_APP/View/Survey/survey.fxml");
        });

        btn_Save.setOnAction(e -> {
            save();
        });
        btn_Delete.setVisible(true);
        btn_Delete.setOnAction(e -> {
            deleteSurvey();
        });
        if(!StringUtils.isNullOrEmpty(surveyID)){
            populateSurvey();
        }
    }

    private void populateComboBox(){
        OtherHandler.populateComboBox(cb_Customer, Const.CUSTOMER_TABLE, Const.CUSTOMER_NAME);
    }
    private void save(){
        notfication = new NotificationHandler();
        if(!StringUtils.isNullOrEmpty(txt_Name.getText())){
            String name = txt_Name.getText();
            database = new Database();
            try {
                String id = OtherHandler.generateId();
                String cusName = cb_Customer.getValue();
                ResultSet rowName = database.getSomeID(name, Const.SURVEY_TABLE, Const.SURVEY_NAME);
                ResultSet rowId = database.getSomeID(id, Const.SURVEY_TABLE, Const.SURVEY_NAME);
                ResultSet rowCus = database.getSomeID(cusName, Const.CUSTOMER_TABLE, Const.CUSTOMER_NAME);
                while(rowId.next()){
                    id = OtherHandler.generateId();
                    rowId = database.getSomeID(id, Const.SURVEY_TABLE, Const.SURVEY_NAME);
                }
                if(rowName.next()){
                    lbl_Noti.setVisible(true);
                    lbl_Noti.setText("This Survey Already Exist");
                    notfication.popup(notfication.warning, "This Survey Already Exist");
                }else if (rowCus.next()){
                    String cusID = rowCus.getString(Const.CUSTOMER_ID);
                    
                    if(StringUtils.isNullOrEmpty(surveyID)){
                        createSurvey(id, name, cusID);
                    }else{
                        updateSurvey(name, cusID);
                    }
                    btn_Back.fire();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }else{
             lbl_Noti.setVisible(true);
             lbl_Noti.setText("Invalid Input");
             notfication.popup(notfication.error, "Invalid Input");
        }
    }

    private void populateSurvey(){
        database = new Database();
        try {
            ResultSet resultSet = database.getSomeID(surveyID, Const.SURVEY_TABLE, Const.SURVEY_ID);
            if(resultSet.next()){
                ResultSet rs = database.getSomeID(resultSet.getNString(Const.CUSTOMER_ID), Const.CUSTOMER_TABLE, Const.CUSTOMER_ID);
                if(rs.next()){
                    txt_Name.setText(resultSet.getString(Const.SURVEY_NAME));
                    cb_Customer.setValue(rs.getString(Const.CUSTOMER_NAME));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void updateSurvey(String name, String cusID){
        surveyDB = new SurveyDB();
        survey = new Survey();
        survey.setSurveyName(name);
        survey.setSurveyID(surveyID);
        survey.setCusID(cusID);
        surveyDB.update(survey);
        notfication.popup(notfication.success, "Survey name "+ name + " updated");
    }

    private void createSurvey(String id, String name, String cusID){
        surveyDB = new SurveyDB();
        survey = new Survey();
        survey.setSurveyName(name);
        survey.setSurveyID(id);
        survey.setCusID(cusID);
        surveyDB.save(survey);
        notfication.popup(notfication.success, "Survey name "+ name + " created");
        lbl_Noti.setVisible(false);
    }

    private void deleteSurvey(){
        database = new Database();

    }
}
