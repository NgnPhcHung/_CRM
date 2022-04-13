package CRM_APP.Handler;

import CRM_APP.Database.Database;
import CRM_APP.Model.Project;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextfieldHandler {
    //setting phone field receive number only
    public void numberOnly(final JFXTextField field){
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    //money format
    public void money(JFXTextField money){
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(money);
        money.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (newValue.matches("\\d*")) {
                    DecimalFormat formatter = new DecimalFormat("#,###");
                    String newValueStr = formatter.format(Double.parseDouble(newValue));
                    money.setText(newValueStr);
                }
            }
        });
    }

    //validate email type
    public boolean validateEmail(final JFXTextField field){
        Pattern p =  Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
        Matcher m = p.matcher(field.getText());
        if(m.find() && m.group().equals(field.getText())){
            return true;
        }else{
            return false;
        }
    }
    public void limitTextField(JFXTextField textField, int LIMIT){
        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    // Check if the new character is greater than LIMIT
                    if (textField.getText().length() >= LIMIT) {

                        // if it's 11th character then just setText to previous
                        // one
                        textField.setText(textField.getText().substring(0, LIMIT));
                    }
                }
            }
        });
    }

    public void limitInput(JFXPasswordField textField, int LIMIT){
        textField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    // Check if the new character is greater than LIMIT
                    if (textField.getText().length() >= LIMIT) {

                        // if it's 11th character then just setText to previous
                        // one
                        textField.setText(textField.getText().substring(0, LIMIT));
                    }
                }
            }
        });
    }
}
