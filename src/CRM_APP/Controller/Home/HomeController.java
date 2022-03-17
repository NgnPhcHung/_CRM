package CRM_APP.Controller.Home;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

import CRM_APP.Controller.Task.Controller;
import CRM_APP.Controller.Task.FullCalendarView;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Login.LoginDB;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    private LoginDB database = new LoginDB();
    public static String userId;
    private Database mydb = new Database();

    @FXML
    void initialize() {
        HomeController.userId = getUserId();
        System.out.println("my name is " + userId);
    }

    //when user logout
    @FXML
    void closeApplication(ActionEvent event) {
        try {
            userLogout(getUserId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        //close
        Platform.exit();
        System.exit(0);
    }
    private void userLogout(String uid) throws SQLException, ClassNotFoundException {
        String deviceName = getDevice();
        String aid = generateId();
        ResultSet rs = mydb.getSomeID(aid, Const.AUTHEN_TABLE, Const.AUTHEN_AUTHENID);
        while(rs.next()){
            aid = generateId();
            rs = mydb.getSomeID(aid, Const.AUTHEN_TABLE, Const.AUTHEN_AUTHENID);
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String logTime = dtf.format(now);
        database.authen("logout", aid, uid, logTime, deviceName);
    }
    //check value is in arrat
    private boolean check(String arr[], String valueCheck){
        boolean test = false;
        for(int i  = 0; i < arr.length;i++){
            if(arr[i].equals(valueCheck)) {
                test = true;
                break;
            }
            else test = false;
        }
        return test;
    }
    //add tab to tabs
    private String[] addElement(String[] a, String e) {
        a  = Arrays.copyOf(a, a.length + 1);
        a[a.length - 1] = e;
        return a;
    }
    //show confirm when buttion survey fired
    private void showConfirmation(Tab tab) throws IOException {
        Label label = new Label();
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Select type");
//        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
//        alert.setX(bounds.getMaxX() - 1240);//vertical
//        alert.setY(bounds.getMaxY() - 900); //horizontal
        alert.getDialogPane().setStyle(
                "-fx-background-radius: 10px;"+
                "-fx-background-color: transparent;");
        alert.getDialogPane().getScene().setFill(Color.TRANSPARENT); // Used for better visual representation of the bug
        alert.setHeaderText("");
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initStyle(StageStyle.TRANSPARENT);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("/CRM_APP/Style/MyDialogs.css");
        dialogPane.getStyleClass().add("myDialog");

        ButtonType Result = new ButtonType("Result");
        ButtonType Question = new ButtonType("Question");
        ButtonType Close = new ButtonType("X");

        // remove default buttontype settings
        alert.getButtonTypes().clear();

        alert.getButtonTypes().addAll(Close, Result, Question);
        //style X button
        alert.getDialogPane().getChildren().forEach(node -> {
            if (node instanceof ButtonBar) {
                ButtonBar buttonBar = (ButtonBar) node;
                buttonBar.getButtons().forEach(possibleButtons -> {
                    if (possibleButtons instanceof Button) {
                        Button b = (Button) possibleButtons;
                        if (b.getText().equals("X")) {
                            b.setStyle("-fx-background-color: #ff5252;  -fx-text-fill: #ffffff;");
                        }
                    }
                });
            }
        });
        // option != null.
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == null) {
            label.setText("No selection!");
        } else if (option.get() == Result) {
            GridPane newPane = FXMLLoader.load(getClass().getResource(SceneHandler.getChooseFileFxml("result", "Survey")));
            tab.setContent(newPane);
            tp_homeMain.getTabs().add(tab);
        } else if (option.get() == Question) {
            GridPane newPane = FXMLLoader.load(getClass().getResource(SceneHandler.getChooseFileFxml("question", "Survey")));
            tab.setContent(newPane);
            tp_homeMain.getTabs().add(tab);
        }else if (option.get() == Close) {
           alert.close();
        } else {
            label.setText("-");
        }
    }
    //set new tab when press button
    @FXML
    void mouseClickEvent(MouseEvent event) throws IOException {
        boolean isOpen = false;

        String temp[] = new String[]{};

        Button button = (Button)event.getSource();
        String btnName = button.getText().trim();
        //if tab not in tabs, add tab
        for(Tab tabs: tp_homeMain.getTabs()){
            if(check(temp,tabs.getText())==false){
                temp = addElement(temp,tabs.getText());
            }
        }

        if(check(temp,btnName)) isOpen=false;
        else isOpen=true;

        if(isOpen){
            Tab tab = new Tab(btnName);

            tp_homeMain.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
            tp_homeMain.getStylesheets().add("/CRM_APP/Style/HomeStyle.css");
            tp_homeMain.setStyle("tab-close-button: #cfd8dc");
            tab.setStyle("-fx-text-fill: #cfd8dc;\n" +
                    "    -fx-font-weight: bold;\n" +
                    "    -fx-font-family: Calibri;\n" +
                    "    -fx-font-size: 20;");
            if (btnName.equals("Survey")){
                showConfirmation(tab);
            }else {
                if(btnName.equals("Task")){
                    tab.setContent(new FullCalendarView(YearMonth.now()).getView());
                }else{
                    GridPane newPane = FXMLLoader.load(getClass().getResource(SceneHandler.getFileFXML(btnName)));
                    tab.setContent(newPane);
                }
                tp_homeMain.getTabs().add(tab);
            }
        }
    }
    public void setUserId(String userId){
        this.userId = userId;
    }
    public String getUserId(){
        return this.userId;
    }
    //get current device name
    private String getDevice(){
        String name = "Unknown";

        try
        {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            name = addr.getHostName();
        }
        catch (UnknownHostException ex)
        {
            System.out.println("Hostname can not be resolved");
        }
        return name;
    }
    //auto generate random string - id when fire
    private String generateId(){
        int n=9;
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
