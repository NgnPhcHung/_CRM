package CRM_APP.Controller.Survey;

import CRM_APP.Controller.Employee.Employee.EmployeeCellController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Employee.EmployeeDB;
import CRM_APP.Database.Survey.SurveyTypeDB;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Handler.ShakerHandler;
import CRM_APP.Handler.TextfieldHandler;
import CRM_APP.Model.Employee;
import CRM_APP.Model.Survey;
import CRM_APP.Model.SurveyType;
import CRM_APP.Model.Team;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.apache.commons.lang3.StringUtils;

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
    private TextfieldHandler textfieldHandler;

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

            btn_Back.setOnAction(e -> {
                sceneHandler = new SceneHandler();
                sceneHandler.slideScene(btn_Back, SurveyTypeCellController.cellStack, "-X", "/CRM_APP/View/Survey/surveyType.fxml");
            });

            btn_Delete.setOnAction(e -> {
                try {
                    // LƯỜI CHECK XÓA, EM TỰ CHECK NHA HƯNG :))) .(CHECK XEM CÓ ĐANG SỬ DỤNG LOẠI KHẢO SÁT KHÔNG MỚI ĐƯỢC XÓA)
                    delete();
                    sceneHandler = new SceneHandler();
                    sceneHandler.slideScene(btn_Delete, SurveyTypeCellController.cellStack, "-X", "/CRM_APP/View/Survey/surveyType.fxml");
                    lbl_Noti.setVisible(false);
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
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    //region DATABASE PROCESS
    private void save() {
        //region INITIALIZE
        String number = OtherHandler.generateNumber();
        String nameSurType = txt_Name.getText().trim();
        String des = txt_Des.getText().trim();
        if (nameSurType.equals("")) {
            lbl_Noti.setVisible(true);
            lbl_Noti.setText("Invalid Input");
        } else {
            lbl_Noti.setVisible(false);
            String surTypeID = "S" + number;
            try {
                database = new Database();
                //Regenerate number if user's number exist
                ResultSet row = database.getSomeID(surID, Const.SURVEY_TYPE_TABLE, Const.SURVEYTYPE_ID);
                while (row.next()) {
                    number = OtherHandler.generateNumber();
                    surTypeID = "S" + number;
                    row = database.getSomeID(surTypeID, Const.SURVEY_TYPE_TABLE, Const.SURVEYTYPE_ID);
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
            surveyTypeDB = new SurveyTypeDB();
            surveyType = new SurveyType();
            surveyType.setSurID(surTypeID);
            surveyType.setSurName(nameSurType);
            surveyType.setDes(des);
            surveyTypeDB.saveSurveyType(surveyType);
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_Save, SurveyTypeCellController.cellStack, "-X", "/CRM_APP/View/Survey/surveyType.fxml");
        }
        //endregion
    }

    private void delete() {
        database = new Database();
        database.detele(Const.SURVEY_TYPE_TABLE, Const.SURVEYTYPE_ID, surID);
    }

    private void update() {
        sceneHandler = new SceneHandler();
        surveyType = new SurveyType();
        surveyTypeDB = new SurveyTypeDB();


        if (StringUtils.isEmpty(txt_Name.getText()) || StringUtils.isEmpty(txt_Des.getText())) {
            lbl_Noti.setVisible(true);
            lbl_Noti.setText("Information can not be null");
        } else {
            String name = txt_Name.getText().trim();
            String des = txt_Des.getText().trim();
            lbl_Noti.setVisible(false);
            surveyType.setSurName(name);
            surveyType.setDes(des);
            surveyType.setSurID(surID);
            surveyTypeDB.update(surveyType);
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_Save, SurveyTypeCellController.cellStack, "-X", "/CRM_APP/View/Survey/surveyType.fxml");
        }
    }
    //endregion

}
