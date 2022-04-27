package CRM_APP.Handler;

import java.sql.ResultSet;

public class ThreadHandler implements Runnable{
    private Thread t;
    private String threadName;
    private volatile ResultSet rs;

    public ThreadHandler() {
        this.rs = rs;
    }

    public ResultSet getRs() {
        return rs;
    }

    public void setRs(ResultSet rs) {
        this.rs = rs;
    }

    @Override
    public void run() {

    }

}