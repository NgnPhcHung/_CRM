package CRM_APP.Controller.Survey;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Survey.QuestionTypeDB;
import CRM_APP.Handler.NotificationHandler;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Handler.ShakerHandler;
import CRM_APP.Model.QuestionType;
import CRM_APP.Model.SurveyType;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.mysql.cj.util.StringUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionTypeCreateController {
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
    private JFXButton btn_Delete;

    public static String questionTypeID = null;
    private Database database;
    private SceneHandler sceneHandler;
    private QuestionTypeDB questionTypeDB;
    private QuestionType questionType;
    private ShakerHandler shakerHandler;
    private NotificationHandler notification;
    @FXML
    void initialize() {
        populateDetail();
        // onclick button
        onClick();
    }

    private void onClick() {
        try {
            btn_Save.setOnAction(e -> {
                update();
            });

            btn_Back.setOnAction(e -> {
                sceneHandler = new SceneHandler();
                sceneHandler.slideScene(btn_Back, QuestionTypeCellController.cellStack, "-X", "/CRM_APP/View/Survey/questionType.fxml");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateDetail() {
        try {
            database = new Database();
            ResultSet row = database.getSomeID(questionTypeID, Const.QUESTION_TYPE_TABLE, Const.QUESTIONTYPE_ID);
            while (row.next()) {
                txt_Name.setText(row.getString(Const.QUESTIONTYPE_NAME));
                txt_Des.setText(row.getString(Const.QUESTIONTYPE_DES));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void update() {
        sceneHandler = new SceneHandler();
        questionType = new QuestionType();
        questionTypeDB = new QuestionTypeDB();
        notification = new NotificationHandler();
        if (StringUtils.isNullOrEmpty(txt_Name.getText()) || StringUtils.isNullOrEmpty(txt_Des.getText())) {
            notification.popup(notification.warning, "Information can not be null");
        } else {
            String name = txt_Name.getText().trim();
            String des = txt_Des.getText().trim();
            questionType.setqTypeName(name);
            questionType.setDes(des);
            questionType.setqTypeID(questionTypeID);
            questionTypeDB.update(questionType);
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_Save, QuestionTypeCellController.cellStack, "-X", "/CRM_APP/View/Survey/questionType.fxml");
            notification.popup(notification.success, "Question updated");
        }
    }

}
