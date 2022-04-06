package CRM_APP.Controller.Employee.Employee;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import CRM_APP.Database.Database;
import CRM_APP.Handler.SceneHandler;
import CRM_APP.Model.Employee;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class EmployeeCellController extends JFXListCell<Employee> {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox main_pane;

    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_position;

    @FXML
    private Label lbl_address;

    @FXML
    private Label lbl_phone;

    @FXML
    private Label lbl_team;

    @FXML
    private Button btn_edit;

    public static StackPane cellStack;
    private FXMLLoader fxmlLoader;
    private Database database;
    private SceneHandler sceneHandler;

    @FXML
    void initialize() {

    }

    @Override
    protected void updateItem(Employee item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/CRM_APP/View/Employee/employeeCell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            lbl_name.setText(item.getName());
            lbl_phone.setText(item.getPhone());
            lbl_position.setText(item.getPosition());
            lbl_address.setText(item.getAddress());
        }
        setText(null);
        setGraphic(main_pane);
    }
}
