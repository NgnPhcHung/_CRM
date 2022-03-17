package CRM_APP.Controller.Survey;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

public class SurveyDetailController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txt_docName;

    @FXML
    private JFXTextField txt_projectName;

    @FXML
    private JFXTextField txt_des;

    @FXML
    private Label lbl_fileName;

    @FXML
    private JFXButton btn_filePicker;

    List<String> lstFile;

    @FXML
    void initialize() {
        lstFile =new ArrayList<>();
        lstFile.add("*.doc");
        lstFile.add("*.docx");
        lstFile.add("*.DOC");
        lstFile.add("*.DOCX");
        lstFile.add("*.pdf");
        lstFile.add("*.PDF");

        btn_filePicker.setOnAction(event -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Document File", lstFile));
            File f = fc.showOpenDialog(null);

            if (f != null) {
                lbl_fileName.setText(f.getName());
                txt_docName.setText(f.getName());
            }
        });
    }
}
