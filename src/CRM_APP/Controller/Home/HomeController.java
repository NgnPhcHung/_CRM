package CRM_APP.Controller.Home;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import CRM_APP.Controller.Employee.Employee.EmployeeProfileController;
import CRM_APP.Controller.Login.LoginController;
import CRM_APP.Controller.Task.FullCalendarView;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Home.HomeDB;
import CRM_APP.Database.Login.LoginDB;
import CRM_APP.Handler.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.*;

public class HomeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TabPane tp_homeMain;

    @FXML
    private VBox tab_home;

    @FXML
    private Button btn_document;

    @FXML
    private Button btn_customer;

    @FXML
    private Button btn_task;

    @FXML
    private Button btn_project;

    @FXML
    private Button btn_bill;

    @FXML
    private Button btn_setting;

    @FXML
    private Button btn_exit;

    @FXML
    private Button btn_Min;

    @FXML
    private Button btn_Max;

    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_position;

    @FXML
    private Label lbl_Export;

    @FXML
    private JFXToggleButton tog_Style;

    @FXML
    private Button btn_Logout;

    @FXML
    private Button btn_History;

    //setting style
    private String light = "/CRM_APP/Style/LightStyle.css";
    private String dark = "/CRM_APP/Style/NightStyle.css";
    private LoginDB database = new LoginDB();
    private Database mydb = new Database();
    private HomeDB homeDB;
    private NotificationHandler notification;
    private boolean isFullScreen = false;

    public static String userId;
    public static Stage primStage;
    public static String styleSheet;
    public static String emName = "";
    @FXML
    void initialize() {
        styleSheet = light;
        tp_homeMain.getStylesheets().add(styleSheet);
        getInfo();
        if (!userId.contains("AD")) {
            btn_project.setVisible(false);
            btn_History.setVisible(false);
        }
        tog_Style.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            tp_homeMain.getStylesheets().remove(light);
            if(!newValue){
                styleSheet = light;
                tp_homeMain.getStylesheets().add(light);
                tp_homeMain.getStylesheets().remove(dark);
                tog_Style.setText("Light mode");
            }else{
                styleSheet = dark;
                tp_homeMain.getStylesheets().add(dark);
                tp_homeMain.getStylesheets().remove(light);
                tog_Style.setText("Dark mode");
            }
        }));
        btn_Min.setOnAction(event -> {
            LoginController.getPrimaryStage().setIconified(true);
        });
        btn_Max.setOnAction(e ->{
            resizeScreen();
        });
        btn_exit.setOnAction(e -> {
            closeApplication();
        });
        if(userId.equals("SAD")){
            btn_History.setVisible(true);
        }
        btn_Logout.setOnAction(e -> {
            userLogout();
        });
    }

    //region LOGOUT
    private void closeApplication() {
        closeMethod();
        //close
        Platform.exit();
        System.exit(0);
    }
    private void userLogout(String uid){
        String deviceName = OtherHandler.getDevice();
        String aid = OtherHandler.generateId();
        database = new LoginDB();
        mydb = new Database();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String logTime = dtf.format(now);
        database.authen("logout", aid, uid, logTime, deviceName);
    }
    private void closeMethod(){
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                userLogout(getUserId());
            }
        }, "Shutdown-thread"));
    }
    //endregion

    //check check tab name exist
    private boolean check(String arr[], String valueCheck) {
        boolean test = false;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(valueCheck)) {
                test = true;
                break;
            } else test = false;
        }
        return test;
    }

    //add tab to tabs
    private String[] addElement(String[] a, String e) {
        a = Arrays.copyOf(a, a.length + 1);
        a[a.length - 1] = e;
        return a;
    }

    //this will get all opened tab
    private int tabManage(String btn) {
        int isOpen = 0;

        String temp[] = new String[]{};
        String btnName = btn;

        //if tab not in tabs, add tab
        for (Tab tabs : tp_homeMain.getTabs()) {
            if (check(temp, tabs.getText()) == false) {
                temp = addElement(temp, tabs.getText());
            }
        }

        if (check(temp, btnName)) return isOpen = 1;
        else return isOpen = 0;
    }

    //set new tab when press button
    @FXML
    void mouseClickEvent(MouseEvent event) throws IOException {
        Button button = (Button) event.getSource();
        String btnName = button.getText().trim();
        Tab tab = new Tab(btnName);
        if (tabManage(btnName) == 0) {
            tp_homeMain.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
            tp_homeMain.setStyle("tab-close-button: #cfd8dc");

            if (btnName.equals("Survey")) {
                StackPane newPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/CRM_APP/View/Survey/surveyMenu.fxml")));
                tab.setContent(newPane);
            } else if (btnName.equals("Task")) {
                FullCalendarView.openFromHome = true;
                tab.setContent(new FullCalendarView(YearMonth.now()).getView());
            } else if (btnName.equals("Employee") && !userId.contains("AD")) {
                EmployeeProfileController.emID = userId;
                EmployeeProfileController.condition = "home";
                StackPane newPane = FXMLLoader.load(getClass().getResource("/CRM_APP/View/Employee/employeeProfile.fxml"));
                tab.setContent(newPane);
            } else {
                StackPane newPane = FXMLLoader.load(getClass().getResource(SceneHandler.getFileFXML(btnName)));
                tab.setContent(newPane);
            }
            tp_homeMain.getSelectionModel().select(tab);

            tab.setStyle("-fx-text-fill: #cfd8dc;\n" +
                    "    -fx-font-weight: bold;\n" +
                    "    -fx-font-family: Calibri;\n" +
                    "    -fx-font-size: 20;");

            tp_homeMain.getTabs().add(tab);
        }else if (tabManage(btnName) == 1){
//            tp_homeMain.getSelectionModel().select(tab);
            SelectionModel<Tab> selectionModel = tp_homeMain.getSelectionModel();

            for (Tab tabs : tp_homeMain.getTabs()) {
                if(tabs.getText() == btnName){
                    selectionModel.select(tabs);
                }
            }
        }
    }

    private void getInfo() {
        Database db = new Database();
        ResultSet row = null;
        try {
            row = db.getSomeID(userId, Const.EMPLOYEE_TABLE, Const.EMPLOYEE_ID);
            if (row.next()) {
                emName =  row.getString(Const.EMPLOYEE_NAME);
                lbl_name.setText("Welcome back, " + emName);
                lbl_position.setText(row.getString("Position"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    //get set
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    private void resizeScreen(){
        SceneHandler sceneHandler= new SceneHandler();
        isFullScreen = !isFullScreen;
        if(isFullScreen){
            LoginController.getPrimaryStage().setMaximized(isFullScreen);
            LoginController.getPrimaryStage().setWidth(sceneHandler.getScreen()[1]);
            LoginController.getPrimaryStage().setHeight(sceneHandler.getScreen()[0]*0.95);
        }else{
            LoginController.getPrimaryStage().setMaximized(isFullScreen);
            LoginController.getPrimaryStage().setHeight(sceneHandler.getScreen()[0]*0.7);
            LoginController.getPrimaryStage().setWidth(sceneHandler.getScreen()[1]*0.7);
        }
    }
    private void userLogout(){
        try {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/CRM_APP/View/Login/login.fxml"));

        loader.load();
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.UNDECORATED);
        userLogout(getUserId());
        btn_Logout.getScene().getWindow().hide();
        stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
