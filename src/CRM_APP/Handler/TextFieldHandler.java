package CRM_APP.Handler;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFieldHandler {
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

    //validate email type
    public boolean validEmail(final JFXTextField field){
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

    public boolean checkPhone(String str){
        boolean flag;
        String phonePattern = "(\\d{3})?\\d{2}\\d{7}";
        flag = str.matches(phonePattern);
        return flag;
    }
}
