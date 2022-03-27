package CRM_APP.Handler;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class ShakerHandler {
    TranslateTransition translateTransition;

    //by passing the node, node can be anything(button, textField, textView, ...)
    public ShakerHandler(Node node, int count, int dur) {
        translateTransition = new TranslateTransition(Duration.millis(dur), node);//times and the object which animating
        translateTransition.setFromX(0f);
        translateTransition.setByX(10f);
        translateTransition.setCycleCount(count);
        translateTransition.setAutoReverse(true);

    }

    public void shake(){
        translateTransition.playFromStart();
    }
}
