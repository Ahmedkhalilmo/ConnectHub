package com.example.ConnectHub;

import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class SceneTransitions {
    public static void doFadeIn(StackPane stackPane, Parent Pane1) {
        var paneToRemove = stackPane.getChildren().get(0);

        stackPane.getChildren().add(Pane1);
        var fadeInTransition = new FadeTransition(Duration.millis(600));

        fadeInTransition.setOnFinished(evt -> {
            stackPane.getChildren().remove(paneToRemove);
        });
        fadeInTransition.setNode(Pane1);
        fadeInTransition.setFromValue(0);
        fadeInTransition.setToValue(1);
        fadeInTransition.play();
    }
}
