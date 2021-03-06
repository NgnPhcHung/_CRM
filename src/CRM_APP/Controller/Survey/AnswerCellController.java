package CRM_APP.Controller.Survey;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.NotificationHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Answer;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


public class AnswerCellController extends JFXListCell<Answer> {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox main_pane;

    @FXML
    private Label lbl_answer;

    @FXML
    private Button btn_edit;

    private FXMLLoader fxmlLoader;
    private SceneHandler sceneHandler = new SceneHandler();
    private Database database;
    private NotificationHandler notification;

    @FXML
    void initialize() {

    }

    @Override
    protected void updateItem(Answer item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/CRM_APP/View/Survey/questionDetailCell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            notification = new NotificationHandler();
            lbl_answer.setText(item.getAnswer());
            btn_edit.setOnAction(e ->{
               database = new Database();
               String ans = lbl_answer.getText();
               database.detele(Const.QUESTION_DETAIL_TABLE, Const.QUESTIONDETAIL_ANSWER, ans);
               notification.popup(notification.success, ans + " has been deleted");
            });
            setText(null);
            setGraphic(main_pane);
        }
    }
}
