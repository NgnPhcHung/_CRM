package CRM_APP.Controller.Project.Project;

import CRM_APP.Controller.Home.HomeController;
import CRM_APP.Controller.Project.Project.ProjectCellController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Project.ProjectDB;
import CRM_APP.Handler.DateTimePickerHandler;
import CRM_APP.Handler.OtherHandler;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Handler.TextFieldHandler;
import CRM_APP.Model.Project;
import CRM_APP.Model.Question;
import CRM_APP.Model.Team;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.mysql.cj.util.StringUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private JFXButton btn_AddTeam;

    @FXML
    private Button btn_back;

    @FXML
    private JFXButton btn_delete;

    @FXML
    private TableView tableView;

    SceneHandler sceneHandler;
    private ProjectDB projectDB;
    private Database database;
    public static String projectID = null;
    private ObservableList<String> list;
    private ObservableList<Project> projects;
    private Team team;
    private Project project;
    TableColumn col_Team;
    TableColumn col_Action;
    private String cusID = "";
    private String emID = "";
    private String newProjectID = "";
    @FXML
    void initialize() {
        //region FORMAT ON START
        dp_start.setValue(LocalDate.now());
        LocalDate dateStart = dp_start.getValue();
        dp_end.setValue(dateStart);

        populateComboBoxData();
        DateTimePickerHandler.disableDate(dp_end, dateStart);
        catchStartDateEnd();
        TextFieldHandler num = new TextFieldHandler();
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
        fillTable();
        btn_AddTeam.setOnAction(e -> {
            saveTeamDetail();
        });
    }

    //region WORK WITH DATABASE

    private void fillTable(){
        tableView.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        col_Team = new TableColumn("Team");
        col_Action = new TableColumn("Action");

        tableView.getColumns().addAll(col_Team, col_Action);
        tableView.getStylesheets().add(HomeController.styleSheet);

        col_Team.setMaxWidth( 1f * Integer.MAX_VALUE * 80 ); // 20% width
        col_Action.setMaxWidth( 1f * Integer.MAX_VALUE * 10 ); // 10% width

        col_Team.getStyleClass().addAll("h4", "text");
        col_Action.getStyleClass().addAll("h4", "text","custom-align");

        try {
            project = new Project();
            database = new Database();
            projectDB = new ProjectDB();
            project.setId(projectID);
            projects = FXCollections.observableArrayList();
            ResultSet resultSet = database.getAllTableValue(Const.TEAM_TABLE);
            ResultSet rowTeam = projectDB.getProjectTeam(project);
            ObservableList<String> teamList = FXCollections.observableArrayList();

            while(resultSet.next()){
                project = new Project();
                String teamID = resultSet.getString(Const.TEAM_ID);
                String teamName="";
                ResultSet rowName = database.getSomeID(teamID, Const.TEAM_TABLE, Const.TEAM_ID);

                if(rowName.next()){
                    teamName = rowName.getString(Const.TEAM_NAME);
                    project.setProjectTeamName(teamName);
                }

                while(rowTeam.next()){
                    teamList.add(rowTeam.getString(Const.TEAM_ID));
                }

                for(String s: teamList){
                    if(s.equals(teamID)){
                        project.getRemark().setSelected(true);
                    }
                }

                col_Team.setCellValueFactory(new PropertyValueFactory<Project, String>("projectTeamName"));
                col_Action.setCellValueFactory(new PropertyValueFactory<Question,String>("remark"));
                projects.add(project);
            }
            tableView.setItems(projects);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

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
        emID = "";
        cusID = "";
        if(txt_name.getText().equals("") && txt_amount.getText().equals("")){
            lbl_noti.setText("Invalid Information");
            lbl_noti.setVisible(true);
        }else{
            lbl_noti.setVisible(false);
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

    private void saveTeamDetail(){
        database = new Database();
        projectDB = new ProjectDB();
        ObservableList<Project> listRemove = FXCollections.observableArrayList();
        for (Project project: projects){
            if(project.getRemark().isSelected()){
                listRemove.add(project);
            }
        }
        database.detele(Const.PROJECT_TEAM_DETAIL, Const.PROJECT_ID, projectID);
        for (Project item: listRemove){
            String teamName = item.getProjectTeamName();
            project = new Project();
            try {
                ResultSet rowTeam = database.getSomeID(teamName, Const.TEAM_TABLE, Const.TEAM_NAME);
                while(rowTeam.next()){
                    project.setProjectTeamID(rowTeam.getString(Const.TEAM_ID));
                }
                project.setId(projectID);
                projectDB.insertProjectTeam(project);
                lbl_noti.setText("Save Success");
                lbl_noti.setVisible(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private void save (){
        projectDB = new ProjectDB();
        database = new Database();
        sceneHandler = new SceneHandler();
        Project project = new Project();

        if(StringUtils.isNullOrEmpty(txt_name.getText()) && StringUtils.isNullOrEmpty(txt_amount.getText())){
            lbl_noti.setText("Invalid Information");
        }else{
            try {
                String newProjectID = createProjectID();
                String employeeID = convertToEmID();
                String customerID = convertToCusID();
                String projectName = txt_name.getText();
                String start = dp_start.getValue().toString();
                String end = dp_end.getValue().toString();
                if(isProjectExist(projectName)) {
                    project.setId(newProjectID);
                    project.setName(projectName);
                    project.setCusId(customerID);
                    project.setManager(employeeID);
                    project.setBeginTime(start);
                    project.setEndTime(end);
                    projectDB.insertProject(project);

                    sceneHandler = new SceneHandler();
                    sceneHandler.slideScene(btn_back, ProjectCellController.cellStack, "X", "/CRM_APP/View/Project/project.fxml");
                }else {
                    lbl_noti.setVisible(true);
                    lbl_noti.setText("This project already in list");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }



        //endregion

    }
    private void delete(){
        database = new Database();
        sceneHandler = new SceneHandler();
        try {
            if(!OtherHandler.checkExist(Const.MODULE_TABLE, Const.MODULE_PROJECT_ID, projectID)){
                database.detele(Const.PROJECT_TABLE, Const.PROJECT_ID, projectID);
                lbl_noti.setVisible(false);
                sceneHandler.slideScene(btn_back, ProjectCellController.cellStack, "-Y", "/CRM_APP/View/Project/project.fxml");
            }else{
                lbl_noti.setVisible(true);
                lbl_noti.setText("Project have Module inside, can not delete!");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private boolean isProjectExist(String name) throws SQLException, ClassNotFoundException {
        boolean boo;
            ResultSet checkNameExist = database.getSomeID(name, Const.PROJECT_TABLE, Const.PROJECT_NAME);
            if(!checkNameExist.next()){
                boo = true;
            }else boo = false;

        return boo;
    }
    private String convertToEmID(){
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
        return emID;
    }
    private String convertToCusID(){
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
        return cusID;
    }

    private String createProjectID (){
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
        return newProjectID;
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
