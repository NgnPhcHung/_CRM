package CRM_APP.Controller.Survey;

import CRM_APP.Controller.Home.HomeController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Survey.SurveyDB;
import CRM_APP.Handler.NotificationHandler;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Question;
import CRM_APP.Model.Survey;
import CRM_APP.Model.SurveyDetail;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.mysql.cj.util.StringUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;


public class SurveyDetailController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_Back;

    @FXML
    private JFXButton btn_Detete;

    @FXML
    private JFXButton btn_Save;

    @FXML
    private JFXTextField txt_Find;

    @FXML
    private JFXCheckBox check_SelectAll;

    @FXML
    private TableView tableView;

    @FXML
    private Label lbl_Header;

    @FXML
    private Label lbl_Noti;

    @FXML
    private JFXComboBox<String> cb_Employee;


    private Database database;
    private SceneHandler sceneHandler;
    private ObservableList<Question> questions;
    private ObservableList<Question> items;
    private SurveyDetail surveyDetail;
    private Question question;
    TableColumn col_Question ;
    TableColumn col_Action;
    private Survey survey;
    private SurveyDB surveyDB;
    private boolean testBoolean = false;
    private NotificationHandler notification;
    public static String surveyID;
    public static String surveyName;

    @FXML
    void initialize() {
        fillDetail();
        filterData();
        cellClickEvent();
        manageCheckBoxEvent();
        btn_Save.setOnAction(e -> {
            save();
        });
        back();
        lbl_Header.setText(surveyName + " Details");
    }

    private void manageCheckBoxEvent(){
        tableView.getItems().removeAll(tableView.getSelectionModel().getSelectedItem());
        check_SelectAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                items = tableView.getItems();
                for(Question item: items){
                    if(check_SelectAll.isSelected()){
                        item.getRemark().setSelected(true);
                        check_SelectAll.setText("Deselect All");
                    }else{
                        item.getRemark().setSelected(false);
                        check_SelectAll.setText("Select All");
                    }
                }
            }
        });
    }

    private void back(){
        btn_Back.setOnAction(e -> {
            sceneHandler = new SceneHandler();
            sceneHandler.slideScene(btn_Back, SurveyCellController.cellStack, "-X", "/CRM_APP/View/Survey/survey.fxml");
        });
    }

    private void save(){
        notification = new NotificationHandler();
        if(StringUtils.isNullOrEmpty(cb_Employee.getValue())){
            notification.popup(notification.error, "Employee is not selected");
        }else{
            database = new Database();
            ObservableList<Question> listRemove = FXCollections.observableArrayList();
            for (Question question: questions){
                if(question.getRemark().isSelected()){
                    listRemove.add(question);
                }
            }
            database.detele(Const.SURVEY_DETAIL_TABLE, Const.SURVEY_DETAIL_ID, surveyID);
            for(Question item: listRemove){
                String quesName = item.getQuestionName();
                String empName = cb_Employee.getValue();
                try {
                    surveyDetail = new SurveyDetail();
                    ResultSet rowQuestion = database.getSomeID(quesName, Const.QUESTION_TABLE, Const.QUESTION_NAME);
                    ResultSet rowEmp = database.getSomeID(empName, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_NAME);
                    while(rowQuestion.next()){
                        surveyDetail.setQuestionID(rowQuestion.getString(Const.QUESTION_ID));
                    }while(rowEmp.next()){
                        surveyDetail.setEmpID(rowEmp.getString(Const.EMPLOYEE_ID));
                    }
                    surveyDetail.setSurveyID(surveyID);
                    ResultSet rowQuestionExist = surveyDB.checkQuestion(surveyDetail);
                    if(rowQuestionExist.next()){
                        continue;
                    }

                    surveyDB = new SurveyDB();

                    surveyDB.saveDetail(surveyDetail);

                    notification.popup(notification.success, "Survey " +surveyName + " saved" );
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
            }
        }
    }

    private void filterData(){
        question = new Question();
        items = tableView.getItems();
        FilteredList<Question> filteredData = new FilteredList<>(items, b  -> true);
        txt_Find.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filteredData.setPredicate(question -> {
                    if (newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if(question.getQuestionName().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else {
                        return false;
                    }
                });
            }
        });
        SortedList<Question> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }

    private void fillDetail(){
        populateComboBox();
        database = new Database();
        questions = FXCollections.observableArrayList();
        //region SETTING SOME STYLE
            tableView.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );

            col_Question = new TableColumn("Question");
            col_Action = new TableColumn("Action");

            col_Question.setMaxWidth( 1f * Integer.MAX_VALUE * 80 ); // 20% width
            col_Action.setMaxWidth( 1f * Integer.MAX_VALUE * 10 ); // 10% width

            tableView.getColumns().addAll(col_Question, col_Action);
            tableView.getStylesheets().add(HomeController.styleSheet);

            col_Question.getStyleClass().addAll("h4", "text");
            col_Action.getStyleClass().addAll("h4", "text","custom-align");

        //endregion
        try {
            surveyDB = new SurveyDB();
            surveyDetail = new SurveyDetail();
            surveyDetail.setSurveyID(surveyID);
            ResultSet rowQuestion = database.getAllTableValue(Const.QUESTION_TABLE);
            ResultSet row = surveyDB.getQuestion(surveyDetail);

            ObservableList<String> questionList = FXCollections.observableArrayList();
            while(rowQuestion.next()){
                question = new Question();
                String questionName = rowQuestion.getString(Const.QUESTION_NAME);

                question.setQuestionName(questionName);
                while(row.next()){
                    questionList.add(row.getString(Const.QUESTION_NAME));
                }
                for(String q: questionList){
                    if(q.equals(questionName)){
                        question.getRemark().setSelected(true);
                    }
                }

                col_Question.setCellValueFactory(new PropertyValueFactory<Question, String>("questionName"));
                col_Action.setCellValueFactory(new PropertyValueFactory<Question,String>("remark"));
                questions.add(question);
            }
            tableView.setItems(questions);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void populateComboBox(){
        OtherHandler.populateComboBox(cb_Employee, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_NAME);
    }
    private void cellClickEvent(){
        tableView.setRowFactory( tv -> {
            TableRow<Question> row = new TableRow<>();
            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.setOnMouseClicked(e-> {

            });
            row.setOnMouseClicked(event -> {
                testBoolean= !testBoolean;
                if (testBoolean){
                    row.getStyleClass().add("cellText");
                }else{
                    row.getStyleClass().removeIf(style -> style.equals("cellText"));
                }
            });

            return row ;
        });
    }

}
