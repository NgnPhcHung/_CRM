package CRM_APP.Controller.Survey;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Survey.QuestionTypeDB;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Handler.ShakerHandler;
import CRM_APP.Model.QuestionType;
import CRM_APP.Model.SurveyType;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.apache.commons.lang3.StringUtils;

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

    @FXML
    void initialize() {
        if (questionTypeID == null) {
            btn_Delete.setVisible(false);
        } else {
            lbl_title.setText("Update Question Type");
            populateDetail();
            btn_Delete.setVisible(true);
        }
        // onclick button
        onClick();
    }

    private void onClick() {
        try {
            btn_Save.setOnAction(e -> {
                if (questionTypeID == null) {
                    save();
                } else {
                    update();
                }
            });

            btn_Back.setOnAction(e -> {
                sceneHandler = new SceneHandler();
                sceneHandler.slideScene(btn_Back, QuestionTypeCellController.cellStack, "-X", "/CRM_APP/View/Survey/questionType.fxml");
            });

            btn_Delete.setOnAction(e -> {
                try {
                    // LƯỜI CHECK XÓA, EM TỰ CHECK NHA HƯNG :))) .(CHECK XEM CÓ ĐANG SỬ DỤNG LOẠI KHẢO SÁT KHÔNG MỚI ĐƯỢC XÓA)
                    delete();
                    sceneHandler = new SceneHandler();
                    sceneHandler.slideScene(btn_Delete, QuestionTypeCellController.cellStack, "-X", "/CRM_APP/View/Survey/questionType.fxml");
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
            ResultSet row = database.getSomeID(questionTypeID, Const.QUESTION_TYPE_TABLE, Const.QUESTIONTYPE_ID);
            while (row.next()) {
                txt_Name.setText(row.getString(Const.QUESTIONTYPE_NAME));
                txt_Des.setText(row.getString(Const.QUESTIONTYPE_DES));
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    //region DATABASE PROCESS
    private void save() {
        //region INITIALIZE
        String number = OtherHandler.generateNumber();
        String nameQuesType = txt_Name.getText().trim();
        String des = txt_Des.getText().trim();
        if (nameQuesType.equals("")) {
            lbl_Noti.setVisible(true);
            lbl_Noti.setText("Invalid Input");
        } else {
            lbl_Noti.setVisible(false);
            String questionTypeID = "QT" + number;
            try {
                database = new Database();
                //Regenerate number if user's number exist
                ResultSet row = database.getSomeID(questionTypeID, Const.QUESTION_TYPE_TABLE, Const.QUESTIONTYPE_ID);
                while (row.next()) {
                    number = OtherHandler.generateNumber();
                    questionTypeID = "QT" + number;
                    row = database.getSomeID(questionTypeID, Const.QUESTION_TYPE_TABLE, Const.QUESTIONTYPE_ID);
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
            questionTypeDB = new QuestionTypeDB();
            questionType = new QuestionType();
            questionType.setqTypeID(questionTypeID);
            questionType.setqTypeName(nameQuesType);
            questionType.setDes(des);
            questionTypeDB.save(questionType);
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_Save, QuestionTypeCellController.cellStack, "-X", "/CRM_APP/View/Survey/questionType.fxml");
        }
        //endregion
    }

    private void delete() {
        database = new Database();
        database.detele(Const.QUESTION_TYPE_TABLE, Const.QUESTIONTYPE_ID, questionTypeID);
    }

    private void update() {
        sceneHandler = new SceneHandler();
        questionType = new QuestionType();
        questionTypeDB = new QuestionTypeDB();

        if (StringUtils.isEmpty(txt_Name.getText()) || StringUtils.isEmpty(txt_Des.getText())) {
            lbl_Noti.setVisible(true);
            lbl_Noti.setText("Information can not be null");
        } else {
            String name = txt_Name.getText().trim();
            String des = txt_Des.getText().trim();
            lbl_Noti.setVisible(false);
            questionType.setqTypeName(name);
            questionType.setDes(des);
            questionType.setqTypeID(questionTypeID);
            questionTypeDB.update(questionType);
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_Save, QuestionTypeCellController.cellStack, "-X", "/CRM_APP/View/Survey/questionType.fxml");
        }
    }
    //endregion

}
