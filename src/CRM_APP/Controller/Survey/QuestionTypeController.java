package CRM_APP.Controller.Survey;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.QuestionType;
import CRM_APP.Model.SurveyType;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionTypeController {
    @FXML
    public StackPane main_stack;

    @FXML
    public Button btn_back;

    @FXML
    private JFXButton btn_add;


    @FXML
    private JFXListView<QuestionType> lv;

    private SceneHandler sceneHandler;
    private Database database;
    private ObservableList<QuestionType> questionTypes;
    private QuestionType questionType;

    @FXML
    void initialize() {
        populateList();

        btn_add.setOnAction(e -> {
            QuestionTypeCreateController.questionTypeID = null; // Thêm dòng này để xóa id nếu đã bấm edit trước đó.
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_add, main_stack, "-X", "/CRM_APP/View/Survey/questionTypeCreate.fxml");
        });

        btn_back.setOnAction(e -> {
            //sceneHandler = new SceneHandler();
            //sceneHandler.slideScene(btn_back, main_stack, "-Y", "/CRM_APP/View/Survey/surveyMenu.fxml");
        });
    }

    private void populateList() {
        sceneHandler = new SceneHandler();
        database = new Database();
        questionTypes = FXCollections.observableArrayList();
        ResultSet row = null;
        questionType = new QuestionType();
        try {
            row = database.getAllTableValue(Const.QUESTION_TYPE_TABLE);
            while (row.next()) {
                questionType = new QuestionType();
                questionType.setqTypeID(row.getString(Const.QUESTIONTYPE_ID));
                questionType.setqTypeName(row.getString(Const.QUESTIONTYPE_NAME));
                QuestionTypeCellController.cellStack = main_stack;
                questionTypes.add(questionType);
            }
            lv.setItems(questionTypes);
            lv.setCellFactory(QuestionTypeCellController -> new QuestionTypeCellController());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
}
