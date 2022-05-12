package CRM_APP.Controller.Survey;

import CRM_APP.Controller.Employee.Employee.EmployeeCellController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Employee.EmployeeDB;
import CRM_APP.Database.Survey.SurveyTypeDB;
import CRM_APP.Handler.*;
import CRM_APP.Model.Employee;
import CRM_APP.Model.Survey;
import CRM_APP.Model.SurveyType;
import CRM_APP.Model.Team;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.mysql.cj.util.StringUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SurveyTypeCreateController {
    @FXML
    private JFXTextField txt_Name;

    @FXML
    private JFXTextField txt_Des;

    @FXML
    private JFXButton btn_Save;

    @FXML
    private Label lbl_Noti;

    @FXML
    private Label lbl_title;

    @FXML
    private Button btn_Back;

    @FXML
    private Button btn_Delete;

    public static String surID = null;
    private Database database;
    private SceneHandler sceneHandler;
    private SurveyTypeDB surveyTypeDB;
    private SurveyType surveyType;
    private ShakerHandler shakerHandler;
    private TextFieldHandler textfieldHandler;
    private SurveyTypeController surveyTypeController;
    private NotificationHandler notificationHandler;
    public static StackPane backPane;

    @FXML
    void initialize() {
        if (surID == null) {
            btn_Delete.setVisible(false);
        } else {
            lbl_title.setText("Update Survey Type");
            populateDetail();
            btn_Delete.setVisible(true);
        }
        // onclick button
        btn_Back.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_Save, backPane, "-X", "/CRM_APP/View/Survey/surveyType.fxml");
        });
        onClick();
    }

    private void onClick() {
        try {
            btn_Save.setOnAction(e -> {
                if (surID == null) {
                    save();
                } else {
                    update();
                }
            });

            btn_Delete.setOnAction(e -> {
                try {
                    delete();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateDetail() {
        try {
            database = new Database();
            ResultSet row = database.getSomeID(surID, Const.SURVEY_TYPE_TABLE, Const.SURVEYTYPE_ID);
            while (row.next()) {
                txt_Name.setText(row.getString(Const.SURVEYTYPE_NAME));
                txt_Des.setText(row.getString(Const.SURVEYTYPE_DES));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void save() {
        notificationHandler = new NotificationHandler();
        surveyTypeController = new SurveyTypeController();
        database = new Database();
        surveyTypeDB = new SurveyTypeDB();
        sceneHandler = new SceneHandler();

        if (StringUtils.isNullOrEmpty( txt_Name.getText())) {
            notificationHandler.popup(notificationHandler.error, "Invalid Input");
        } else {
            String nameSurType = txt_Name.getText();
            ResultSet check = database.getSomeID(nameSurType, Const.SURVEY_TYPE_TABLE, Const.SURVEYTYPE_NAME);
            try {
                if(check.next()){
                    notificationHandler.popup(notificationHandler.warning, "This type Existed");
                }else{
                    String number = OtherHandler.generateNumber();
                    String des = txt_Des.getText();
                    String surTypeID = "S" + number;

                    //Regenerate number if user's number exist
                    ResultSet row = database.getSomeID(surID, Const.SURVEY_TYPE_TABLE, Const.SURVEYTYPE_ID);
                    while (row.next()) {
                        number = OtherHandler.generateNumber();
                        surTypeID = "S" + number;
                        row = database.getSomeID(surTypeID, Const.SURVEY_TYPE_TABLE, Const.SURVEYTYPE_ID);
                    }

                    surveyType = new SurveyType();
                    surveyType.setSurID(surTypeID);
                    surveyType.setSurName(nameSurType);
                    surveyType.setDes(des);
                    surveyTypeDB.saveSurveyType(surveyType);
                    notificationHandler.popup(notificationHandler.success, "Survey Type " + nameSurType + " created successful");
                    btn_Back.fire();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void delete() {
        database = new Database();
        notificationHandler = new NotificationHandler();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("Do you want to delete this Survey Type?");
        alert.setContentText("Please check again!");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                database.detele(Const.SURVEY_TYPE_TABLE, Const.SURVEYTYPE_ID, surID);
                notificationHandler.popup(notificationHandler.success, "Survey deleted");
                btn_Back.fire();
            }
        });
    }

    private void update() {
        sceneHandler = new SceneHandler();
        surveyType = new SurveyType();
        surveyTypeDB = new SurveyTypeDB();
        sceneHandler = new SceneHandler();
        notificationHandler = new NotificationHandler();
        if (StringUtils.isNullOrEmpty(txt_Name.getText()) ) {
            notificationHandler.popup(notificationHandler.error, "Invalid Information");
        } else {
            String name = txt_Name.getText().trim();
            String des = txt_Des.getText().trim();
            lbl_Noti.setVisible(false);
            surveyType.setSurName(name);
            surveyType.setDes(des);
            surveyType.setSurID(surID);
            surveyTypeDB.update(surveyType);
            notificationHandler.popup(notificationHandler.success, "Survey Type " + name + " created successful");
        }
    }

}
