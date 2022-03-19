package CRM_APP.Controller.Customer;

import CRM_APP.Handler.TextfieldHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;

public class ContactController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField txt_firstname;

    @FXML
    private JFXTextField txt_lastname;

    @FXML
    private JFXRadioButton radio_male;

    @FXML
    private JFXRadioButton radio_female;

    @FXML
    private JFXTextField txt_phone;

    @FXML
    private JFXTextField txt_email;

    @FXML
    private JFXTextField txt_address;

    @FXML
    private JFXButton btn_save;

    private boolean isMale = true;
    TextfieldHandler textfieldHandler;

    @FXML
    void initialize() {
        textfieldHandler = new TextfieldHandler();
        textfieldHandler.numberOnly(txt_phone);
    }



}
