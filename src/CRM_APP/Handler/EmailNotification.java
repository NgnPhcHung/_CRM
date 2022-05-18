package CRM_APP.Handler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import javax.print.DocFlavor;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class EmailNotification {
    TrayNotification tray = new TrayNotification();

    public void popup(String from, String message){
        AnimationType animationType = AnimationType.POPUP;
        tray.setAnimationType(animationType);
        tray.setTitle(from);
        tray.setMessage(message);
        tray.setNotificationType(NotificationType.INFORMATION);
        tray.showAndDismiss(Duration.millis(15000));

    }
}
