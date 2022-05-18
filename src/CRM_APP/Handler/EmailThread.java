package CRM_APP.Handler;

public class EmailThread extends Thread{
    @Override
    public void run() {
        super.run();
        while (true){
            new EmailReader();
        }
    }
}
