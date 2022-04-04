package CRM_APP.Controller.Project;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Project.ProjectDB;
import CRM_APP.Handler.DateTimePickerHandler;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Handler.TextfieldHandler;
import CRM_APP.Model.Project;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

import static CRM_APP.Controller.Project.ProjectCellController.cellStack;

public class ProjectDetailsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txt_name;

    @FXML
    private JFXComboBox<String> cb_customer;

    @FXML
    private JFXComboBox<String> cb_manager;

    @FXML
    private JFXDatePicker dp_start;

    @FXML
    private JFXDatePicker dp_end;

    @FXML
    private JFXTextField txt_amount;

    @FXML
    private Label lbl_noti;

    @FXML
    private JFXButton btn_save;

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_delete;

    SceneHandler sceneHandler;
    private ProjectDB projectDB;
    private Database database;
    public static String projectID = null;
    private ObservableList<String> list;

    @FXML
    void initialize() {
        //region FORMAT ON START
        dp_start.setValue(LocalDate.now());
        LocalDate dateStart = dp_start.getValue();
        dp_end.setValue(dateStart);

        populateComboBoxData();
        DateTimePickerHandler.disableDate(dp_end, dateStart);
        catchStartDateEnd();
        TextfieldHandler num = new TextfieldHandler();
        num.numberOnly(txt_amount);
        if(projectID.equals("null")){
            btn_delete.setVisible(false);
        }else{
            populateDetails();
            btn_delete.setVisible(true);
        }
        //endregion
        btn_save.setOnAction(e -> {
            if(projectID.equals("null")){
                save ();
            }else{
                update();
            }
        });
        btn_delete.setOnAction(e->{
            delete();
        });
    }

    //region WORK WITH DATABASE

    private void populateComboBoxData(){
        try{
            list = FXCollections.observableArrayList();
            database = new Database();
            ResultSet row = database.getAllTableValue(Const.CUSTOMER_TABLE);
            while(row.next()){
                String name = row.getString(Const.CUSTOMER_NAME);
                list.add(name);
            }
            cb_customer.setItems(list);
            cb_customer.getSelectionModel().select(0);
        }catch (SQLException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        try{
            list = FXCollections.observableArrayList();
            database = new Database();
            ResultSet row = database.getAllTableValue(Const.EMPLOYEE_TABLE);
            while(row.next()){
                String name = row.getString(Const.EMPLOYEE_NAME);
                list.add(name);
            }
            cb_manager.setItems(list);
            cb_manager.getSelectionModel().select(0);
        }catch (SQLException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    private void populateDetails(){
        database = new Database();
        try {
            ResultSet row = database.getSomeID(projectID, Const.PROJECT_TABLE, Const.PROJECT_ID);
            if(row.next()){
                txt_name.setText(row.getString(Const.PROJECT_NAME));
                txt_amount.setText(row.getString(Const.PROJECT_TOTAL_AMOUNT));
                LocalDate start = DateTimePickerHandler.formatDate(row.getString("BeginTime"));
                LocalDate end = DateTimePickerHandler.formatDate(row.getString("EndTime"));
                dp_start.setValue(start);
                dp_end.setValue(end);

                ResultSet rowManager = database.getSomeID(row.getString(Const.PROJECT_MANAGER), Const.EMPLOYEE_TABLE, Const.EMPLOYEE_ID);
                ResultSet rowCustomer = database.getSomeID(row.getString(Const.CUSTOMER_ID), Const.CUSTOMER_TABLE, Const.CUSTOMER_ID);

                if(rowManager.next()){
                    cb_manager.setValue(rowManager.getString(Const.EMPLOYEE_NAME));
                }
                if(rowCustomer.next()){
                    cb_customer.setValue(rowCustomer.getString(Const.CUSTOMER_NAME));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void update(){
        Project project = new Project();
        projectDB = new ProjectDB();
        database = new Database();
        String empName = cb_manager.getValue();
        String CusName = cb_customer.getValue();
        String emID = "";
        String cusID = "";
        if(txt_name.getText().equals("") && txt_amount.getText().equals("")){
            lbl_noti.setText("Invalid Information");
        }else{
            String manager = cb_manager.getValue();
            try {
                ResultSet row = database.getSomeID(manager, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_NAME);
                if(row.next()){
                    project.setManager(row.getString(Const.EMPLOYEE_ID));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


            try {
                ResultSet rowEmp = database.getSomeID(empName, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_NAME);
                ResultSet rowCus = database.getSomeID(CusName, Const.CUSTOMER_TABLE, Const.CUSTOMER_NAME);

                if(rowEmp.next()){
                    emID = rowEmp.getString(Const.EMPLOYEE_ID);
                    project.setManager(emID);
                }
                if(rowCus.next()){
                    cusID = rowCus.getString(Const.CUSTOMER_ID);
                    project.setCusId(cusID);
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            project.setId(projectID);
            project.setName(txt_name.getText());
            project.setBeginTime(dp_start.getValue()+"");
            project.setEndTime(dp_end.getValue()+"");
            project.setAmount(Integer.parseInt(txt_amount.getText()));
            projectDB.updateProject(project);
            sceneHandler =  new SceneHandler();
            sceneHandler.slideScene(btn_back, ProjectCellController.cellStack, "-X", "/CRM_APP/View/Project/project.fxml");
        }
    }
    private void save (){
        projectDB = new ProjectDB();
        database = new Database();
        sceneHandler = new SceneHandler();
        Project project = new Project();
        String cusID = "";
        String emID = "";
        if(txt_name.getText().equals("") && txt_amount.getText().equals("")){
            lbl_noti.setText("Invalid Information");
        }
        //region CREATE CHECK ID EXIST
        String newProjectID ="";
        try {
            newProjectID = OtherHandler.generateId();
            ResultSet check = database.getSomeID(newProjectID, Const.PROJECT_TABLE, Const.PROJECT_ID);
            while(check.next()){
                newProjectID = OtherHandler.generateId();
                check = database.getSomeID(newProjectID, Const.PROJECT_TABLE, Const.PROJECT_ID);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //endregion
        //region CONVERT CUSTOMER NAME  TO ID
        try {
            String customerNameToId = cb_customer.getValue();
            ResultSet check = database.getSomeID(customerNameToId, Const.CUSTOMER_TABLE, Const.CUSTOMER_NAME);
            if(check.next()){
                cusID = check.getString(Const.CUSTOMER_ID);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //endregion
        //region CONVERT EMPLOYEE NAME  TO ID
        try {
            String emName = cb_manager.getValue();
            ResultSet check = database.getSomeID(emName, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_NAME);
            if(check.next()){
                emID = check.getString(Const.EMPLOYEE_ID);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //endregion
        //region PACKAGE AND SEND TO DB HANDLE
        project.setId(newProjectID);
        project.setName(txt_name.getText());
        project.setCusId(cusID);
        project.setManager(emID);
        project.setBeginTime(dp_start.getValue().toString());
        project.setEndTime(dp_end.getValue().toString());
        //endregion
        projectDB.insertProject(project);
        sceneHandler.slideScene(btn_back, ProjectCellController.cellStack, "X", "/CRM_APP/View/Project/project.fxml");
    }
    private void delete(){
        database = new Database();
        sceneHandler = new SceneHandler();
        database.detele(Const.PROJECT_TABLE, Const.PROJECT_ID, projectID);
        sceneHandler.slideScene(btn_back, ProjectCellController.cellStack, "-Y", "/CRM_APP/View/Project/project.fxml");
    }

    //endregion

    //region WORKING WITH UI
    private void catchStartDateEnd(){
        dp_start.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                DateTimePickerHandler.disableDate(dp_end, dp_start.getValue());
            }
        });
    }
    @FXML
    void changeScene(ActionEvent event) {
        sceneHandler =  new SceneHandler();
        sceneHandler.slideScene(btn_back, ProjectCellController.cellStack, "X", "/CRM_APP/View/Project/project.fxml");
        txt_name.setText("");
        cb_customer.getSelectionModel().clearSelection();
        cb_manager.getSelectionModel().clearSelection();
        dp_start.getEditor().clear();
        dp_end.getEditor().clear();
        lbl_noti.setText("");
    }
    //endregion
}
