package CRM_APP.Controller.Project;

import CRM_APP.Model.Project;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProjectCellController extends JFXListCell<Project> {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label lbl_projectName;

    @FXML
    private AnchorPane pane_tag;

    @FXML
    private Label lbl_startDate;

    @FXML
    private Label lbl_endDate;

    @FXML
    private ProgressBar pgr_process;

    @FXML
    private HBox main_pane;

    FXMLLoader fxmlLoader ;

    @FXML
    void initialize() {

    }

    @Override
    protected void updateItem(Project item, boolean empty) {
        super.updateItem(item, empty);
        String tag;
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        }else{
            if(fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("/CRM_APP/View/Project/projectCell.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            lbl_projectName.setText(item.getName());
            lbl_endDate.setText(item.getEndDate());
            lbl_startDate.setText(item.getStartDate());
            pgr_process.setProgress((double) item.getProcess());
            tag = item.getTag();
            if(tag == "first"){
                pane_tag.setStyle("-fx-background-color: #ffca28");
            }

            setText(null);
            setGraphic(main_pane);
        }
    }
}
