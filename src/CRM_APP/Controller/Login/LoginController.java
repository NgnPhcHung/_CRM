package CRM_APP.Controller.Login;

import CRM_APP.Controller.Home.HomeController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Login.LoginDB;
import CRM_APP.Handler.*;
import CRM_APP.Main;
import CRM_APP.Model.Employee;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.mysql.cj.util.StringUtils;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class LoginController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    HBox pane_Container;

    @FXML
    private Button btn_close;

    @FXML
    private Button btn_Min;

    @FXML
    private Label lbl_invalid;

    @FXML
    private JFXTextField txt_username;

    @FXML
    private JFXPasswordField txt_password;

    @FXML
    private JFXButton btn_login;

    @FXML
    private VBox left_pane;

    @FXML
    private VBox right_pane;

    @FXML
    private ImageView imgArrow;

    private SceneHandler sceneHandler;
    private LoginDB database = new LoginDB(); // Khởi tạo database cho login
    private Database mydb = new Database(); // Khởi tạo lớp database
    private Employee employee = new Employee(); // Khai báo đối tượng nhân viên
    private String userId;
    double x;
    double y;
    private static Stage primaryStage;
    private NotificationHandler notification;

    @FXML
    void initialize() {
        ShakerHandler loginShake = new ShakerHandler(imgArrow, Animation.INDEFINITE, 500);
        loginShake.shake();
        btn_Min.setOnAction(e -> {
            Main.getPrimaryStage().setIconified(true);
        });
        btn_login.setOnAction(e ->{
            loginEvent();
        });
        pane_Container.setOnKeyPressed( e -> {
            if( e.getCode() == KeyCode.ENTER ) {
                loginEvent();
            }
        });
    }
    //close app event
    @FXML
    void closeEvent(ActionEvent event) {
        System.exit(0);
    }

    void loginEvent() {
        sceneHandler = new SceneHandler();
        employee = new Employee();
        if(StringUtils.isNullOrEmpty(txt_username.getText()) || StringUtils.isNullOrEmpty(txt_password.getText())){
            notification = new NotificationHandler();
            notification.popup(notification.warning, "Text cannot be blank");
        }else{
            String loginText = txt_username.getText().trim();
            String loginPwd = txt_password.getText().trim();

            employee.setId(loginText);
            employee.setPassword(loginPwd);

            try {
                ResultSet userRow = database.getUser(employee);
                int counter = 0;
                while(userRow.next()){
                    counter++;
                    userId=userRow.getString("EmpID");
                }
                if(counter == 1){
                    lbl_invalid.setVisible(false);
                    userLogin(userId);
                    loginSuccess();
                }else{
                    ShakerHandler userShake = new ShakerHandler(txt_username, 2,50);
                    ShakerHandler passShake = new ShakerHandler(txt_password, 2,50);
                    notification = new NotificationHandler();
                    userShake.shake();
                    passShake.shake();
                    notification.popup(notification.error, "Username/password wrong");
                    lbl_invalid.setVisible(true);
                    lbl_invalid.setText("Username/password wrong");
                    txt_password.setText("");
                    txt_username.setText("");
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    //change scene event
    private void loginSuccess() {
        notification = new NotificationHandler();
        notification.popup(notification.success, "Login Success");
        FadeTransitionHandler.applyTransition(pane_Container, Duration.seconds(0.5), e ->{
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/CRM_APP/View/Home/home.fxml"));
                HomeController.userId = userId;
                loader.load();

                Parent root = loader.getRoot();
                Stage stage = new Stage();
                setPrimaryStage(stage);
                stage.setScene(new Scene(root));
                stage.initStyle(StageStyle.UNDECORATED);

                stage.setHeight(sceneHandler.getScreen()[0]*0.7);
                stage.setWidth(sceneHandler.getScreen()[1]*0.7);
                pane_Container.getChildren().removeAll();
                btn_login.getScene().getWindow().hide();

                // Gọi màn hình home

                stage.show();
                root.setOnMousePressed(event -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged(event -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);
                    stage.setOpacity(0.5f);
                });

                root.setOnDragDone(event ->{
                    stage.setOpacity(1f);
                });
                root.setOnMouseReleased(event -> {
                    stage.setOpacity(1f);
                });

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }
    //this func will fire when login success and be4 change scene
    private void userLogin(String uid){
        String deviceName = OtherHandler.getDevice(); // Lấy tên thiết bị
        String aid = OtherHandler.generateId(); // Tạo ra id ngẫu nhiên
        ResultSet rs = null;
        try {

            rs= mydb.getSomeID(aid, Const.AUTHEN_TABLE, Const.AUTHEN_AUTHENID);
            while(rs.next()) {
                aid = OtherHandler.generateId();
                rs = mydb.getSomeID(aid, Const.AUTHEN_TABLE, Const.AUTHEN_AUTHENID);

            }
            // Lấy thời gian hiện tại hệ thống
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String logTime = dtf.format(now);

            database.authen("login", aid, uid, logTime, deviceName);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        LoginController.primaryStage = primaryStage;
    }
}
