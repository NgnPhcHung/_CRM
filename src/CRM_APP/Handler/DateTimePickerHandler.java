package CRM_APP.Handler;

import com.jfoenix.controls.JFXDatePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DateCell;

import java.time.LocalDate;

public class DateTimePickerHandler {
    public static void disableDate(JFXDatePicker pdate, LocalDate toCheckValue){
        pdate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = toCheckValue;

                setDisable(empty || date.compareTo(today) < 0 );
            }
        });
    }
    public static LocalDate formatDate(String i){
        String start =i;
        String[] d = start.split(" ");
        LocalDate date = LocalDate.parse(d[0]);
        return date;
    }
}
