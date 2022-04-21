package CRM_APP.Controller.Survey;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Survey.SurveyDB;
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
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.apache.commons.lang3.StringUtils;

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

    @FXML
    void initialize() {
        populateComboBox();
        btn_Back.setOnAction(e ->{
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_Back, SurveyCellController.cellStack, "Y", "/CRM_APP/View/Survey/survey.fxml");
        });

        btn_Save.setOnAction(e -> {
            save();
            btn_Back.fire();
        });
    }

    private void populateComboBox(){
        OtherHandler.populateComboBox(cb_Customer, Const.CUSTOMER_TABLE, Const.CUSTOMER_NAME);
    }
    private void save(){
        if(!StringUtils.isEmpty(txt_Name.getText())){
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
                }else if (rowCus.next()){
                    surveyDB = new SurveyDB();
                    survey = new Survey();
                    survey.setSurveyName(name);
                    survey.setSurveyID(id);
                    survey.setCusID(rowCus.getString(Const.CUSTOMER_ID));
                    surveyDB.save(survey);
                    lbl_Noti.setVisible(false);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }else{
             lbl_Noti.setVisible(true);
             lbl_Noti.setText("Invalid Input");
        }
    }
    private void deleteSurvey(){
        database = new Database();

    }
}
