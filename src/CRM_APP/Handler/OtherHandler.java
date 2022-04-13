package CRM_APP.Handler;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Login.LoginDB;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OtherHandler {

    public static String curentDateTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String logTime = dtf.format(now);
        return logTime;
    }
    //get current device name
    public static String getDevice(){
        String name = "Unknown";

        try
        {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            name = addr.getHostName();
        }
        catch (UnknownHostException ex)
        {
            System.out.println("Hostname can not be resolved");
        }
        return name;
    }
    //auto generate random string - id when fire
    public static String generateId(){
        int n=9;
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int)(AlphaNumericString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString .charAt(index));
        }

        return sb.toString();
    }

    public static String generateNumber(){
        int n  = 7;
        // chose a Character random from this String
        String numberString = "0123456789";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index= (int)(numberString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(numberString.charAt(index));
        }

        return sb.toString();
    }
    //to color hex code
    public static String toRGBCode( Color color ) {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }
    //return date list from start to end
    public static List<LocalDate> dateList(LocalDate dateStart, LocalDate dateEnd){
        List<LocalDate> dates = Stream.iterate(dateStart, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(dateStart, dateEnd)+1)
                .collect(Collectors.toList());
        if(dates.size()>0);
        return dates;
    }

    //fill data to combo box
    public static void populateComboBox(JFXComboBox cb, String table, String col){
        ObservableList<String> list;
        Database database ;
        try {
            database = new Database();
            ResultSet row = null;
            list = FXCollections.observableArrayList();
            row = database.getAllTableValue(table);
            while(row.next()){
                String name = row.getString(col);
                list.add(name);
            }
            cb.setItems(list);
            cb.getSelectionModel().select(0);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}