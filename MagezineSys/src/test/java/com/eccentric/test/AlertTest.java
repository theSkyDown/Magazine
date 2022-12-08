/*
 * author:eccentric
 * time:2022/11/10
 */
package com.eccentric.test;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class AlertTest extends Application {


    public void start(Stage primaryStage) throws Exception {
        HBox hBox = new HBox();
        Button btn = new Button();

        btn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
            public void handle(Event event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "注册成功");
                alert.setHeaderText(null);
                alert.show();
            }
        });


        hBox.getChildren().add(btn);
        primaryStage.setScene(new Scene(hBox,300,300));
        primaryStage.show();
    }
}
