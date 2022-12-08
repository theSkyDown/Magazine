/*
 * author:eccentric
 * time:2022/11/11
 */
package com.eccentric.test;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jdk.nashorn.internal.parser.DateParser;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocaleDateTimeTest extends Application {
    @Test
    public void test(){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox vBox = new VBox();

        DatePicker date = new DatePicker();
        Button btn = new Button("点击");
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(date.getValue());
            }
        });
        vBox.getChildren().setAll(date,btn);


        primaryStage.setScene(new Scene(vBox,200,200));
        primaryStage.show();
    }
}
