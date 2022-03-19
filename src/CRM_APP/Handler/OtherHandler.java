package CRM_APP.Handler;

import CRM_APP.Database.Const;
import CRM_APP.Database.Database;
import CRM_APP.Database.Login.LoginDB;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OtherHandler {
    public static void userLogin(String uid, Database mydb, LoginDB database) throws SQLException, ClassNotFoundException {
        String deviceName = getDevice();
        String aid = generateId();
        ResultSet rs = mydb.getSomeID(aid, Const.AUTHEN_TABLE, Const.AUTHEN_AUTHENID);
        while(rs.next()){
            aid = generateId();
            rs = mydb.getSomeID(aid, Const.AUTHEN_TABLE, Const.AUTHEN_AUTHENID);
        }
        String logTime = curentDateTime();
        database.authen("login", aid, uid, logTime, deviceName);
    }
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
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
