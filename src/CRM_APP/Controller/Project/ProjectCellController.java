package CRM_APP.Controller.Project;

import CRM_APP.Controller.Project.Module.ModuleController;
import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Customer;
import CRM_APP.Model.Module;
import CRM_APP.Model.Project;
import com.jfoenix.controls.JFXListCell;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;

public class ProjectCellController extends JFXListCell<Project> {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox main_pane;

    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_customer;

    @FXML
    private Label lbl_manager;

    @FXML
    private Button btn_edit;

    @FXML
    private Button btn_details;

    @FXML
    private Label lbl_beginTime;

    @FXML
    private Label lbl_endTime;


    private FXMLLoader fxmlLoader;
    private Database database;
    private SceneHandler sceneHandler;
    public static StackPane cellStack;
    @FXML
    void initialize() {
        database = new Database();
    }

    @FXML
    void editEvent(ActionEvent event) {

    }

    @Override
    protected void updateItem(Project item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/CRM_APP/View/Project/projectCell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            //handle customer
            ResultSet crow = null;
            try {
                crow = database.getSomeID(item.getCusId(), "customer", "CusID");
                if (crow.next()) {
                    String cusName = crow.getString("CusName");
                    lbl_customer.setText(cusName);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            lbl_name.setText(item.getName());
            lbl_beginTime.setText(item.getBeginTime());
            lbl_endTime.setText(item.getEndTime());

            try {
                ResultSet manager = database.getSomeID(item.getManager(), Const.EMPLOYEE_TABLE, "EmpID");
                if(manager.next()){
                    lbl_manager.setText(manager.getString("EmpName"));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


            String projectID = item.getId();


            btn_details.setOnAction(e ->{
                sceneHandler =  new SceneHandler();
                ModuleController.projectID = projectID;
                sceneHandler.slideScene(btn_edit, cellStack, "Y","/CRM_APP/View/Module/module.fxml");
            });

            btn_edit.setOnAction(e -> {
                sceneHandler =  new SceneHandler();
                sceneHandler.slideScene(btn_edit, cellStack, "X","/CRM_APP/View/Project/projectDetail.fxml");
            });

            setText(null);
            setGraphic(main_pane);
        }
    }
}
