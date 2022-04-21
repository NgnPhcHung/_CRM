package CRM_APP.Controller.Survey;

import CRM_APP.Controller.Home.HomeController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Survey.SurveyDB;
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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.StringUtils;


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
    public static String surveyID;
    private SurveyDB surveyDB;

    @FXML
    void initialize() {
        fillDetail();
        manageCheckBoxEvent();
        save();
        filterData();
        back();
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
        col_Action.setMaxWidth( 1f * Integer.MAX_VALUE * 5 ); // 10% width
        tableView.getColumns().addAll(col_Question, col_Action);
        tableView.getStylesheets().add(HomeController.styleSheet);
        col_Question.getStyleClass().addAll("h4", "text");
        col_Action.getStyleClass().addAll("h4", "text","custom-align");
        //endregion
        try {
            ResultSet row = database.getAllTableValue(Const.QUESTION_TABLE);
            while(row.next()){
                surveyDetail = new SurveyDetail();
                question = new Question();

                question.setQuestionName(row.getString(Const.QUESTION_NAME));
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
        btn_Save.setOnAction(e -> {
            ObservableList<Question> listRemove = FXCollections.observableArrayList();
            for (Question question: questions){
                if(question.getRemark().isSelected()){
                    listRemove.add(question);
                }
            }
            //check question exist
            database = new Database();
            for(Question item: listRemove){
                database = new Database();
                String quesName = item.getQuestionName();
                String empName = cb_Employee.getValue();
                try {
                    ResultSet rowQuestion = database.getSomeID(quesName, Const.QUESTION_TABLE, Const.QUESTION_NAME);
                    ResultSet rowEmp = database.getSomeID(empName, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_NAME);
                    if(rowQuestion.next() && rowEmp.next()){
                        surveyDetail = new SurveyDetail();
                        surveyDetail.setSurveyID(surveyID);
                        surveyDetail.setEmpID(rowEmp.getString(Const.EMPLOYEE_ID));
                        surveyDetail.setQuestionID(rowQuestion.getString(Const.QUESTION_ID));
                        surveyDB = new SurveyDB();
                        surveyDB.saveDetail(surveyDetail);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
            }

            questions.removeAll(listRemove);
        });
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

    private void populateDetail(){
        if(!StringUtils.isEmpty(surveyID) && !surveyID.equals("")){

        }else{
            fillDetail();
        }
    }
    private void populateComboBox(){
        OtherHandler.populateComboBox(cb_Employee, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_NAME);
    }
}
