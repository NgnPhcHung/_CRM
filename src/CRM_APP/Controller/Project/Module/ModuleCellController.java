package CRM_APP.Controller.Project.Module;

import CRM_APP.Model.Module;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ModuleCellController extends JFXListCell<Module> {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox main_pane;

    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_project;

    @FXML
    private Button btn_edit;

    @FXML
    private Label lbl_status;

    private FXMLLoader fxmlLoader;
    private Module module;
    @FXML
    void editEvent(ActionEvent event) {

    }

    @FXML
    void initialize() {

    }


    @Override
    protected void updateItem(Module item, boolean empty) {
        super.updateItem(item, empty);
        module = new Module();
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/CRM_APP/View/Module/moduleCell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            lbl_name.setText(item.getModName());
            lbl_project.setText(item.getProjectID());
            handleStatus(item.getStatus());

            setText(null);
            setGraphic(main_pane);
        }
    }

    private void handleStatus(String stt){
        lbl_status.getStylesheets().add("/CRM_APP/Style/ModuleStyle.css");
        switch (stt) {
            case "0":
                lbl_status.setText("Pending");
                lbl_status.getStyleClass().add("pending");
                break;
            case "1":
                lbl_status.setText("Assigned");
                lbl_status.getStyleClass().add("assigned");
                break;
            case "2":
                lbl_status.setText("Working");
                lbl_status.getStyleClass().add("working");
                break;
            case "3":
                lbl_status.setText("Reviewing");
                lbl_status.getStyleClass().add("review");
                break;
            case "4":
                lbl_status.setText("Done");
                lbl_status.getStyleClass().add("done");
                break;
            default: break;
        }

    }
}
