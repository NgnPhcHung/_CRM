package CRM_APP.Handler;

import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class NotificationHandler {
    TrayNotification tray = new TrayNotification();
    public NotificationType success = NotificationType.SUCCESS;
    public NotificationType error = NotificationType.ERROR;
    public NotificationType warning = NotificationType.WARNING;

    public void popup(NotificationType type, String message){
        AnimationType animationType = AnimationType.POPUP;
        tray.setAnimationType(animationType);
        tray.setTitle(message);
        tray.setNotificationType(type);
        tray.showAndDismiss(Duration.millis(5000));
    }
}
