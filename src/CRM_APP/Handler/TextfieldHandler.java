package CRM_APP.Handler;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

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
}
