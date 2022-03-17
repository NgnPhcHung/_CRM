package CRM_APP.Controller.Billing;
import CRM_APP.Model.Import;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class BillController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GridPane main_pane;

    @FXML
    private JFXListView<Import> lv_detail;

    ObservableList<Import> imports;

    @FXML
    void initialize() {
        Import imp = new Import();
        imp.setName("");
        imports = FXCollections.observableArrayList(imp);
        imports.addAll(imp);
        lv_detail.setItems(imports);
        lv_detail.setCellFactory(ImportCellController -> new BillCellController());
    }

}

