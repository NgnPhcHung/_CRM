package CRM_APP.Handler;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;


public class FadeTransitionHandler {
    public static void applyTransition(Node node, Duration duration, EventHandler<ActionEvent> event){
        FadeTransition fadeIn = new FadeTransition(duration, node);
        fadeIn.setCycleCount(1);
        fadeIn.setFromValue(0.2);
        fadeIn.setToValue(1);
        fadeIn.setAutoReverse(true);
        fadeIn.setOnFinished(event);

        FadeTransition fadeOut = new FadeTransition(duration, node);
        fadeIn.setCycleCount(1);
        fadeIn.setFromValue(1);
        fadeIn.setToValue(0.2);
        fadeOut.setAutoReverse(true);

        fadeOut.play();
        fadeOut.setOnFinished(e -> {
            fadeIn.play();
        });
    }
}
